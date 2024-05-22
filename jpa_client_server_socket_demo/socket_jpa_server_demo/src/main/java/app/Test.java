package app;

import java.util.List;

import dao.EntityManagerFactoryUtil;
import dao.StudentDAO;
import entities.Student;
import jakarta.persistence.EntityManager;

public class Test {
	public static void main(String[] args) {
		EntityManagerFactoryUtil mangerFactoryUtil = new EntityManagerFactoryUtil();
		EntityManager entityManager = mangerFactoryUtil.getEnManager();

		StudentDAO studentDAO = new StudentDAO(entityManager);
		
		for (int i = 1; i<=10; i++) {
			Student student = new Student("Student " + i, 20 + i, "student" + i + "@gmail.com");
			studentDAO.save(student);
		}

		mangerFactoryUtil.closeEnManager();
		mangerFactoryUtil.closeEnManagerFactory();
	}
}
