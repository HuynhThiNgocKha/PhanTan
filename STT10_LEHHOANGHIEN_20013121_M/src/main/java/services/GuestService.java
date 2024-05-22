package services;

import java.util.List;

import dao.GuestDAO;
import entities.Guest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class GuestService implements GuestDAO {
	private EntityManager entityManager;

	public GuestService(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<Guest> Guests() {
		Query query = entityManager.createQuery("SELECT p FROM Guest p");
		return query.getResultList();
	}

}
