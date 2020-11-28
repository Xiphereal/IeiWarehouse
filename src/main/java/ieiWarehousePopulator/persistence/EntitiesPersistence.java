package ieiWarehousePopulator.persistence;

public class EntitiesPersistence {

    public static void persist(Persistable persistable) {
        persistable.persist();
    }

}
