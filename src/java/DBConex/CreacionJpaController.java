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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author Xavier
 */
public class CreacionJpaController implements Serializable {

    public CreacionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Creacion creacion) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ruta idruta = creacion.getIdruta();
            if (idruta != null) {
                idruta = em.getReference(idruta.getClass(), idruta.getIdruta());
                creacion.setIdruta(idruta);
            }
            em.persist(creacion);
            if (idruta != null) {
                idruta.getCreacionCollection().add(creacion);
                idruta = em.merge(idruta);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCreacion(creacion.getIdcreacion()) != null) {
                throw new PreexistingEntityException("Creacion " + creacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Creacion creacion) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Creacion persistentCreacion = em.find(Creacion.class, creacion.getIdcreacion());
            Ruta idrutaOld = persistentCreacion.getIdruta();
            Ruta idrutaNew = creacion.getIdruta();
            if (idrutaNew != null) {
                idrutaNew = em.getReference(idrutaNew.getClass(), idrutaNew.getIdruta());
                creacion.setIdruta(idrutaNew);
            }
            creacion = em.merge(creacion);
            if (idrutaOld != null && !idrutaOld.equals(idrutaNew)) {
                idrutaOld.getCreacionCollection().remove(creacion);
                idrutaOld = em.merge(idrutaOld);
            }
            if (idrutaNew != null && !idrutaNew.equals(idrutaOld)) {
                idrutaNew.getCreacionCollection().add(creacion);
                idrutaNew = em.merge(idrutaNew);
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
                Integer id = creacion.getIdcreacion();
                if (findCreacion(id) == null) {
                    throw new NonexistentEntityException("The creacion with id " + id + " no longer exists.");
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
            Creacion creacion;
            try {
                creacion = em.getReference(Creacion.class, id);
                creacion.getIdcreacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The creacion with id " + id + " no longer exists.", enfe);
            }
            Ruta idruta = creacion.getIdruta();
            if (idruta != null) {
                idruta.getCreacionCollection().remove(creacion);
                idruta = em.merge(idruta);
            }
            em.remove(creacion);
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

    public List<Creacion> findCreacionEntities() {
        return findCreacionEntities(true, -1, -1);
    }

    public List<Creacion> findCreacionEntities(int maxResults, int firstResult) {
        return findCreacionEntities(false, maxResults, firstResult);
    }

    private List<Creacion> findCreacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Creacion.class));
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

    public Creacion findCreacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Creacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getCreacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Creacion> rt = cq.from(Creacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
