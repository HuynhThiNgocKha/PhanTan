package services;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

import dao.PayrollDAO;
import entities.Employee;
import entities.Payroll;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class PayrollService extends UnicastRemoteObject implements PayrollDAO {

	private static final long serialVersionUID = 1L;
	private EntityManager entityManager;

	public PayrollService(EntityManager entityManager) throws RemoteException {
		this.entityManager = entityManager;

	}

	@Override
	public int countPayroll() throws RemoteException {
		Query query = entityManager.createQuery("SELECT COUNT(p) FROM Payroll p");
		return ((Number) query.getSingleResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Payroll> findAll() throws RemoteException {
		Query query = entityManager.createQuery("SELECT p FROM Payroll p");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Payroll> findByEmployee(String employeeId) throws RemoteException {
		Query query = entityManager.createQuery("SELECT p FROM Payroll p WHERE p.employee.id = :employeeId");
		query.setParameter("employeeId", Long.parseLong(employeeId));
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Payroll> findByDate(Month month, Year year) throws RemoteException {
		LocalDate startDate = LocalDate.of(year.getValue(), month, 1);
		LocalDate endDate = startDate.plusMonths(1).minusDays(1);

		Query query = entityManager.createQuery("SELECT p FROM Payroll p WHERE p.date BETWEEN :startDate AND :endDate");
		query.setParameter("startDate", java.sql.Date.valueOf(startDate));
		query.setParameter("endDate", java.sql.Date.valueOf(endDate));
		return query.getResultList();
	}

	@Override
	public boolean updatePayroll(Payroll payroll) throws RemoteException {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(payroll);
			entityManager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deletePayrollByMonthAndYear(Month month, Year year) throws RemoteException {
		Query query = entityManager.createQuery("DELETE FROM Payroll p WHERE p.month = :month AND p.year = :year");
		query.setParameter("month", month);
		query.setParameter("year", year);

		try {
			entityManager.getTransaction().begin();
			int deletedCount = query.executeUpdate();
			entityManager.getTransaction().commit();
			System.out.println("Deleted " + deletedCount + " payroll entries for " + month + " " + year);
			return true;
		} catch (Exception e) {
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deletePayrollByEmployee(String employeeId) throws RemoteException {
		Query query = entityManager.createQuery("DELETE FROM Payroll p WHERE p.employee.id = :employeeId");
		query.setParameter("employeeId", Long.parseLong(employeeId));

		try {
			entityManager.getTransaction().begin();
			query.executeUpdate();
			entityManager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean createPayroll(Month month, Year year) throws RemoteException {
		deletePayrollByMonthAndYear(month, year);
		List<Employee> list = new EmployeeService(entityManager).findAll();
		try {
			entityManager.getTransaction().begin();
			for (Employee e : list) {
				Payroll p = new Payroll(e.getId(), month, year, 0, 0, new BigDecimal(0), new BigDecimal(0),
						new BigDecimal(0));
				entityManager.persist(p);
			}
			entityManager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}
	
//	@Override
//	public boolean createPayroll(Month month, Year year) throws RemoteException {
//		LocalDate now = LocalDate.now();
//		Month month = now.getMonth();
//		Year year = Year.of(now.getYear());
//		deletePayrollByMonthAndYear(month, year);
//		List<Employee> list = new EmployeeService(entityManager).findAll();
//		try {
//			entityManager.getTransaction().begin();
//			for (Employee e : list) {
//				Payroll p = new Payroll(e.getId(), month, year, 0, 0, new BigDecimal(0), new BigDecimal(0),
//						new BigDecimal(0));
//				entityManager.persist(p);
//			}
//			entityManager.getTransaction().commit();
//			return true;
//		} catch (Exception e) {
//			if (entityManager.getTransaction().isActive()) {
//				entityManager.getTransaction().rollback();
//			}
//			e.printStackTrace();
//			return false;
//		}
//	}
	
}
