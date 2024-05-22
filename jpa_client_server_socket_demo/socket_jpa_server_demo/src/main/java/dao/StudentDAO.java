package dao;

import entities.Student;
import jakarta.persistence.EntityManager;

public class StudentDAO {
	private EntityManager entityManager;

	public StudentDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Student save(Student student) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(student);
			entityManager.getTransaction().commit();

			return student;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Student findById(long id) {
		return entityManager.find(Student.class, id);
	}
}
