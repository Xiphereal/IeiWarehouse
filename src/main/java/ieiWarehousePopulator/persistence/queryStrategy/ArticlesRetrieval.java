package ieiWarehousePopulator.persistence.queryStrategy;

import ieiWarehousePopulator.domain.Article;
import ieiWarehousePopulator.domain.Copy;
import ieiWarehousePopulator.domain.Magazine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticlesRetrieval implements QueryStrategy {
    @Override
    public List<Article> retrieveQueryResults(ResultSet queryResultSet) throws SQLException {
        List<Article> retrievedArticles = new ArrayList<>();

        while (queryResultSet.next()) {
            // Each parameter 'i' passed to the getObject() method indicates
            // from which column to retrieve. Columns indexes are 1 based, not 0.
            String title = (String) queryResultSet.getObject(1);
            Long year = Long.valueOf((Integer) queryResultSet.getObject(2));
            String url = (String) queryResultSet.getObject(3);
            Integer initialPage = (Integer) queryResultSet.getObject(4);
            Integer finalPage = (Integer) queryResultSet.getObject(5);

            Integer copyVolume = (Integer) queryResultSet.getObject(6);
            Integer copyNumber = (Integer) queryResultSet.getObject(7);
            Integer copyMonth = (Integer) queryResultSet.getObject(8);

            String magazineName = (String) queryResultSet.getObject(9);

            // TODO: Retrieve the list of the article's authors.

            Article article = resolveRelationships(title,
                    year,
                    url,
                    initialPage,
                    finalPage,
                    copyVolume,
                    copyNumber,
                    copyMonth,
                    magazineName);

            retrievedArticles.add(article);
        }

        return retrievedArticles;
    }

    private Article resolveRelationships(String title,
                                         Long year,
                                         String url,
                                         Integer initialPage,
                                         Integer finalPage,
                                         Integer copyVolume,
                                         Integer copyNumber,
                                         Integer copyMonth,
                                         String magazineName) {
        Article article = new Article(title, year, url, initialPage, finalPage);

        Copy copy = new Copy(copyVolume, copyNumber, copyMonth);
        article.setCopyPublishedBy(copy);

        Magazine magazine = new Magazine(magazineName);
        copy.setMagazinePublishBy(magazine);
        return article;
    }
}
