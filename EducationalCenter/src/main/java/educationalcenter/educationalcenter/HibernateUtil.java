package educationalcenter.educationalcenter;

import javax.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import educationalcenter.educationalcenter.entity.Teacher;
import educationalcenter.educationalcenter.entity.Course;
import java.util.Properties;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private static RuntimeException initializationException;

    private static synchronized SessionFactory getOrCreateSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            return sessionFactory;
        }
        if (initializationException != null) {
            throw initializationException;
        }
        try {
            Properties settings = new Properties();

            settings.setProperty(Environment.DRIVER, "org.postgresql.Driver");
            settings.setProperty(
                    Environment.URL,
                    System.getenv().getOrDefault("EC_DB_URL", "jdbc:postgresql://localhost:5432/educationalcenter")
            );
            settings.setProperty(Environment.USER, System.getenv().getOrDefault("EC_DB_USER", "postgres"));
            settings.setProperty(Environment.PASS, System.getenv().getOrDefault("EC_DB_PASSWORD", "sjsjsqo18ha5"));

            settings.setProperty(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
            settings.setProperty(Environment.SHOW_SQL, "true");
            settings.setProperty(Environment.FORMAT_SQL, "true");
            settings.setProperty(Environment.HBM2DDL_AUTO, "update");
            settings.setProperty(Environment.POOL_SIZE, "10");

            Configuration configuration = new Configuration();
            configuration.setProperties(settings);
            configuration.addAnnotatedClass(Teacher.class);
            configuration.addAnnotatedClass(Course.class);

            sessionFactory = configuration.buildSessionFactory();
            return sessionFactory;
        } catch (Throwable ex) {
            initializationException = new IllegalStateException(
                    "Cannot connect to PostgreSQL. Set EC_DB_URL/EC_DB_USER/EC_DB_PASSWORD correctly. " +
                            "Current EC_DB_URL default is jdbc:postgresql://localhost:5432/educationalcenter",
                    ex
            );
            throw initializationException;
        }
    }

    public static EntityManager getEntityManager() {
        return getOrCreateSessionFactory().createEntityManager();
    }

    public static SessionFactory getSessionFactory() {
        return getOrCreateSessionFactory();
    }

    public static void close() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}