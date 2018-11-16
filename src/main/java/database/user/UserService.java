package database.user;

import crypto.HashSaltUtils;
import database.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;


public class UserService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Cryptor-pu");

    public List<User> getUsers() {
        EntityManager entityManager = emf.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> q = cb.createQuery(User.class);
        Root<User> r = q.from(User.class);
        q = q.select(r);
        List<User> result = entityManager.createQuery(q).getResultList();
        entityManager.close();
        return result;
    }

    private boolean checkIfUsernameExists(String username) {
        EntityManager entityManager = emf.createEntityManager();
        Query q = entityManager.createQuery("select u from User u where u.username = :username");
        q.setParameter("username", username);
        boolean result = q.getResultList().size() == 1;
        entityManager.close();
        return result;
    }

    public User getUserByUsername(String username) {
        EntityManager entityManager = emf.createEntityManager();
        Query q = entityManager.createQuery("select u from User u where u.username = :username");
        q.setParameter("username", username);
        User result = (User) q.getSingleResult();
        entityManager.close();
        return result;
    }

    public boolean storeUser(String username, String password) {
        EntityManager entityManager = emf.createEntityManager();
        if (checkIfUsernameExists(username)) {
            return false;
        }
        try {
            User newUser = new User(username, password);
            entityManager.getTransaction().begin();
            entityManager.persist(newUser);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        entityManager.close();
        return true;
    }

    public boolean checkUserHash(String username, String password) {
        if (!checkIfUsernameExists(username)) {
            return false;
        }
        try {
            User user = getUserByUsername(username);
            byte[] userSalt = user.getSalt();
            byte[] userPasswordHash = user.getPasswordHash();

            byte[] hashToCheck = HashSaltUtils.getPasswordSaltHash(password, userSalt);

            if (Arrays.equals(userPasswordHash, hashToCheck)) {
                return true;
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return false;
    }
}
