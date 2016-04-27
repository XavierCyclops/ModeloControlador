/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBConex;

import DBConex.exceptions.NonexistentEntityException;
import DBConex.exceptions.PreexistingEntityException;
import DBConex.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Xavier
 */
public class RutaJpaController implements Serializable {

    public RutaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ruta ruta) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (ruta.getCreacionCollection() == null) {
            ruta.setCreacionCollection(new ArrayList<Creacion>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Creacion> attachedCreacionCollection = new ArrayList<Creacion>();
            for (Creacion creacionCollectionCreacionToAttach : ruta.getCreacionCollection()) {
                creacionCollectionCreacionToAttach = em.getReference(creacionCollectionCreacionToAttach.getClass(), creacionCollectionCreacionToAttach.getIdcreacion());
                attachedCreacionCollection.add(creacionCollectionCreacionToAttach);
            }
            ruta.setCreacionCollection(attachedCreacionCollection);
            em.persist(ruta);
            for (Creacion creacionCollectionCreacion : ruta.getCreacionCollection()) {
                Ruta oldIdrutaOfCreacionCollectionCreacion = creacionCollectionCreacion.getIdruta();
                creacionCollectionCreacion.setIdruta(ruta);
                creacionCollectionCreacion = em.merge(creacionCollectionCreacion);
                if (oldIdrutaOfCreacionCollectionCreacion != null) {
                    oldIdrutaOfCreacionCollectionCreacion.getCreacionCollection().remove(creacionCollectionCreacion);
                    oldIdrutaOfCreacionCollectionCreacion = em.merge(oldIdrutaOfCreacionCollectionCreacion);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRuta(ruta.getIdruta()) != null) {
                throw new PreexistingEntityException("Ruta " + ruta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ruta ruta) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ruta persistentRuta = em.find(Ruta.class, ruta.getIdruta());
            Collection<Creacion> creacionCollectionOld = persistentRuta.getCreacionCollection();
            Collection<Creacion> creacionCollectionNew = ruta.getCreacionCollection();
            Collection<Creacion> attachedCreacionCollectionNew = new ArrayList<Creacion>();
            for (Creacion creacionCollectionNewCreacionToAttach : creacionCollectionNew) {
                creacionCollectionNewCreacionToAttach = em.getReference(creacionCollectionNewCreacionToAttach.getClass(), creacionCollectionNewCreacionToAttach.getIdcreacion());
                attachedCreacionCollectionNew.add(creacionCollectionNewCreacionToAttach);
            }
            creacionCollectionNew = attachedCreacionCollectionNew;
            ruta.setCreacionCollection(creacionCollectionNew);
            ruta = em.merge(ruta);
            for (Creacion creacionCollectionOldCreacion : creacionCollectionOld) {
                if (!creacionCollectionNew.contains(creacionCollectionOldCreacion)) {
                    creacionCollectionOldCreacion.setIdruta(null);
                    creacionCollectionOldCreacion = em.merge(creacionCollectionOldCreacion);
                }
            }
            for (Creacion creacionCollectionNewCreacion : creacionCollectionNew) {
                if (!creacionCollectionOld.contains(creacionCollectionNewCreacion)) {
                    Ruta oldIdrutaOfCreacionCollectionNewCreacion = creacionCollectionNewCreacion.getIdruta();
                    creacionCollectionNewCreacion.setIdruta(ruta);
                    creacionCollectionNewCreacion = em.merge(creacionCollectionNewCreacion);
                    if (oldIdrutaOfCreacionCollectionNewCreacion != null && !oldIdrutaOfCreacionCollectionNewCreacion.equals(ruta)) {
                        oldIdrutaOfCreacionCollectionNewCreacion.getCreacionCollection().remove(creacionCollectionNewCreacion);
                        oldIdrutaOfCreacionCollectionNewCreacion = em.merge(oldIdrutaOfCreacionCollectionNewCreacion);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ruta.getIdruta();
                if (findRuta(id) == null) {
                    throw new NonexistentEntityException("The ruta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ruta ruta;
            try {
                ruta = em.getReference(Ruta.class, id);
                ruta.getIdruta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ruta with id " + id + " no longer exists.", enfe);
            }
            Collection<Creacion> creacionCollection = ruta.getCreacionCollection();
            for (Creacion creacionCollectionCreacion : creacionCollection) {
                creacionCollectionCreacion.setIdruta(null);
                creacionCollectionCreacion = em.merge(creacionCollectionCreacion);
            }
            em.remove(ruta);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ruta> findRutaEntities() {
        return findRutaEntities(true, -1, -1);
    }

    public List<Ruta> findRutaEntities(int maxResults, int firstResult) {
        return findRutaEntities(false, maxResults, firstResult);
    }

    private List<Ruta> findRutaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ruta.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Ruta findRuta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ruta.class, id);
        } finally {
            em.close();
        }
    }

    public int getRutaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ruta> rt = cq.from(Ruta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
