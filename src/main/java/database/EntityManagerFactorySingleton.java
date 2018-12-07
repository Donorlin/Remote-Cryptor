package database;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactorySingleton {

    private static final EntityManagerFactory emfInstance =
            Persistence.createEntityManagerFactory("Cryptor-pu");

    private EntityManagerFactorySingleton() {
    }

    public static EntityManagerFactory getEMF() {
        return emfInstance;
    }
}
