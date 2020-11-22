import domain.Article;
import domain.Publication;

public class EntitiesPersistence {
    public static void persist(Publication publication) {

            if (publication instanceof Article) {
                String doesArticleExistSqlQuery =
                        "SELECT id FROM publicacion " +
                                "WHERE titulo =" + publication.getTitle() + "AND" +
                                "anyo =" + publication.getYear() + ";";

                MySQLConnection.performQuery(doesArticleExistSqlQuery);
            }

    }
}
