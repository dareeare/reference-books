package educationalcenter.educationalcenter.repository;

import educationalcenter.educationalcenter.entity.Course;
import educationalcenter.educationalcenter.entity.Teacher;
import educationalcenter.educationalcenter.HibernateUtil;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class CourseRepository {

    public List<Course> findAllActive() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Course> query = em.createQuery(
                    "SELECT c FROM Course c LEFT JOIN FETCH c.teacher " +
                            "WHERE c.isDeleted = false OR c.isDeleted IS NULL ORDER BY c.courseId",
                    Course.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Course> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Course> query = em.createQuery(
                    "SELECT c FROM Course c LEFT JOIN FETCH c.teacher ORDER BY c.courseId",
                    Course.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<Course> findById(Integer id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Course.class, id));
        } finally {
            em.close();
        }
    }

    public Course save(Course course) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            if (course.getIsDeleted() == null) {
                course.setIsDeleted(false);
            }
            if (course.getTeacher() != null && course.getTeacher().getTeacherId() > 0) {
                Teacher managedTeacher = em.getReference(Teacher.class, course.getTeacher().getTeacherId());
                course.setTeacher(managedTeacher);
            }

            if (course.getCourseId() == null) {
                em.persist(course);
            } else {
                course = em.merge(course);
            }

            em.getTransaction().commit();
            return course;
        } finally {
            em.close();
        }
    }

    public void softDelete(Integer id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Course course = em.find(Course.class, id);
            if (course != null) {
                course.setIsDeleted(true);
                em.merge(course);
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(Course course) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Course managedCourse = em.find(Course.class, course.getCourseId());
            if (managedCourse != null) {
                em.remove(managedCourse);
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Course> findByTeacherId(Integer teacherId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Course> query = em.createQuery(
                    "SELECT c FROM Course c LEFT JOIN FETCH c.teacher " +
                            "WHERE c.teacher.teacherId = :teacherId AND (c.isDeleted = false OR c.isDeleted IS NULL)",
                    Course.class
            );
            query.setParameter("teacherId", teacherId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long countActive() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(c) FROM Course c WHERE c.isDeleted = false OR c.isDeleted IS NULL",
                    Long.class
            );
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}