import domain.Article;
import domain.Copy;
import domain.Magazine;
import domain.Publication;

public class IeiWarehousePopulator {
    public static void main(String[] args) {
        //MySQLConnection.performUpdate("INSERT INTO persona (nombre, apellidos) VALUES (\"JAVIER\", \"Vicente\")");
        //DblpExtractor.extractDataIntoWarehouse();

        Magazine magazine = new Magazine("Mi Revista");
        Copy copy = new Copy(10, 9, 8);
        Article article = new Article("Prueba", 2000L, "url.com", 10,15);

        copy.setMagazinePublishBy(magazine);
        article.setCopyPublishedBy(copy);

        EntitiesPersistence.persist(article);
    }
}
