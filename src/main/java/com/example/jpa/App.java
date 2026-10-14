package com.example.jpa;

import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.List;

public class App {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    // Méthode générique pour persister une entité
    public static <T> void create(T entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Erreur lors de la création de l'entité : " + e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("gestionPU");
        em = emf.createEntityManager();

        try {
            // Création d'une salle
            create(new Salle("Salle A", 10));

            // Requêtes
            List<Salle> grandesSalles = em.createQuery(
                            "SELECT s FROM Salle s WHERE s.capacite > :minCapacite", Salle.class)
                    .setParameter("minCapacite", 20)
                    .getResultList();

            List<Utilisateur> utilisateurs = em.createQuery(
                            "SELECT u FROM Utilisateur u WHERE u.nom LIKE :pattern", Utilisateur.class)
                    .setParameter("pattern", "%Jean%")
                    .getResultList();

            grandesSalles.forEach(s -> System.out.println("Salle : " + s));
            utilisateurs.forEach(u -> System.out.println("Utilisateur : " + u));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}