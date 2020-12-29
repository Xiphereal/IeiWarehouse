package ieiWarehousePopulator.persistence.queryStrategy;

import ieiWarehousePopulator.domain.Article;
import ieiWarehousePopulator.domain.Copy;
import ieiWarehousePopulator.domain.Magazine;
import ieiWarehousePopulator.domain.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticlesRetrieval implements QueryStrategy {
    @Override
    public List<Article> retrieveQueryResults(ResultSet queryResultSet) throws SQLException {
        Map<Integer, Article> identifiedArticles = new HashMap<>();

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

            Integer publicationId = (Integer) queryResultSet.getObject(10);

            String authorName = (String) queryResultSet.getObject(11);
            String authorSurnames = (String) queryResultSet.getObject(12);

            Article article = resolveRelationships(title,
                    year,
                    url,
                    initialPage,
                    finalPage,
                    copyVolume,
                    copyNumber,
                    copyMonth,
                    magazineName);

            Person author = getAuthor(authorName, authorSurnames);

            // If the article has been already retrieved,
            // update that precise object from the map.
            if (identifiedArticles.containsKey(publicationId)) {
                article = identifiedArticles.get(publicationId);
            }

            article.addAuthor(author);

            identifiedArticles.put(publicationId, article);
        }

        return new ArrayList<>(identifiedArticles.values());
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

    private Person getAuthor(String authorName, String authorSurnames) {
        Person author = null;

        if (authorName != null || authorSurnames != null)
            author = new Person(authorName, authorSurnames);

        return author;
    }
}
