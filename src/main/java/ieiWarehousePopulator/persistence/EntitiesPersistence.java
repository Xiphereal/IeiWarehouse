package ieiWarehousePopulator.persistence;

public class EntitiesPersistence {

    public static void persist(Persistable persistable) {
        persistable.persist();
    }

    public static void insertNewPublicationHasPerson(Integer publicationId, Integer authorId) {
        String addNewPublicationHasPersonSqlUpdate =
                "INSERT INTO publicacion_has_persona (publicacion_id, persona_id) " +
                        "VALUES (" + "\"" + publicationId + "\", " +
                        "\"" + authorId + "\");";

        MySQLConnection.performUpdate(addNewPublicationHasPersonSqlUpdate);
    }

}
