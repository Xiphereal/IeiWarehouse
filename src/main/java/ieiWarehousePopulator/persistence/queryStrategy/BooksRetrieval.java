package ieiWarehousePopulator.persistence.queryStrategy;

import ieiWarehousePopulator.domain.Book;
import ieiWarehousePopulator.domain.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooksRetrieval implements QueryStrategy {
    @Override
    public List<Book> retrieveQueryResults(ResultSet queryResultSet) throws SQLException {
        Map<Integer, Book> identifiedBooks = new HashMap<>();

        while (queryResultSet.next()) {
            // Each parameter 'i' passed to the getObject() method indicates
            // from which column to retrieve. Columns indexes are 1 based, not 0.
            String title = (String) queryResultSet.getObject(1);
            Long year = Long.valueOf((Integer) queryResultSet.getObject(2));
            String url = (String) queryResultSet.getObject(3);
            String publisher = (String) queryResultSet.getObject((4));

            Integer publicationId = (Integer) queryResultSet.getObject(5);

            String authorName = (String) queryResultSet.getObject(6);
            String authorSurnames = (String) queryResultSet.getObject(7);

            Book book = new Book(title, year, url, publisher);

            Person author = getAuthor(authorName, authorSurnames);

            if (identifiedBooks.containsKey(publicationId)) {
                book = identifiedBooks.get(publicationId);
            }

            book.addAuthor(author);

            identifiedBooks.put(publicationId, book);
        }

        return new ArrayList<>(identifiedBooks.values());
    }

    private Person getAuthor(String authorName, String authorSurnames) {
        Person author = null;

        if (authorName != null || authorSurnames != null)
            author = new Person(authorName, authorSurnames);

        return author;
    }
}
