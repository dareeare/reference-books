package educationalcenter.educationalcenter.repository;

import educationalcenter.educationalcenter.HibernateUtil;
import educationalcenter.educationalcenter.entity.Teacher;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class TeacherRepository {

    public List<Teacher> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Teacher> query = em.createQuery(
                    "SELECT DISTINCT t FROM Teacher t LEFT JOIN FETCH t.courses ORDER BY t.teacherId",
                    Teacher.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<Teacher> findById(Integer id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Teacher.class, id));
        } finally {
            em.close();
        }
    }

    public Teacher save(Teacher teacher) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Integer teacherId = teacher.getTeacherId();
            if (teacherId == null || teacherId <= 0) {
                em.persist(teacher);
            } else {
                teacher = em.merge(teacher);
            }

            em.getTransaction().commit();
            return teacher;
        } finally {
            em.close();
        }
    }

    public void delete(Teacher teacher) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Teacher managedTeacher = em.find(Teacher.class, teacher.getTeacherId());
            if (managedTeacher != null) {
                em.remove(managedTeacher);
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void deleteById(Integer id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Teacher teacher = em.find(Teacher.class, id);
            if (teacher != null) {
                em.remove(teacher);
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Teacher> findBySpecialization(String specialization) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Teacher> query = em.createQuery(
                    "SELECT t FROM Teacher t WHERE LOWER(t.specialization) LIKE LOWER(:spec)",
                    Teacher.class
            );
            query.setParameter("spec", "%" + specialization + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long count() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(t) FROM Teacher t", Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}