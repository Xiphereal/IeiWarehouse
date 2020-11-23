package ieiWarehousePopulator;

import ieiWarehousePopulator.utils.DatabasePurge;

public class IeiWarehousePopulator {
    public static void main(String[] args) {
        //IeiWarehousePopulator.MySQLConnection.performUpdate("INSERT INTO persona (nombre, apellidos) VALUES (\"JAVIER\", \"Vicente\")");
        //IeiWarehousePopulator.DblpExtractor.extractDataIntoWarehouse();

        DatabasePurge.purgeAllTables();

//        Magazine magazine = new Magazine("Mi Revista");
//        Copy copy = new Copy(10, 9, 8);
//        Article article = new Article("Prueba", 2000L, "url.com", 10,15);
//
//        List<Person> authors = new ArrayList<>();
//        authors.add(new Person("Ricardo", "Soler"));
//        authors.add(new Person("Alejandro", "Lozano"));
//        authors.add(new Person("Pepe", "Carsi"));
//
//        copy.setMagazinePublishBy(magazine);
//        article.setCopyPublishedBy(copy);
//        article.setAuthors(authors);
//
//        IeiWarehousePopulator.EntitiesPersistence.persist(article);
    }
}
