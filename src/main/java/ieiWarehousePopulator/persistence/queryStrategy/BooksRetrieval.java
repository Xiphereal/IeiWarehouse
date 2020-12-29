package ieiWarehousePopulator.persistence.queryStrategy;

import ieiWarehousePopulator.domain.Article;
import ieiWarehousePopulator.domain.Book;
import ieiWarehousePopulator.domain.Copy;
import ieiWarehousePopulator.domain.Magazine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BooksRetrieval implements QueryStrategy{
    @Override
    public List<Book> retrieveQueryResults(ResultSet queryResultSet) throws SQLException {
        List<Book> retrievedBooks = new ArrayList<>();

        while (queryResultSet.next()) {
            // Each parameter 'i' passed to the getObject() method indicates
            // from which column to retrieve. Columns indexes are 1 based, not 0.
            String title = (String) queryResultSet.getObject(1);
            Long year = Long.valueOf((Integer) queryResultSet.getObject(2));
            String url = (String) queryResultSet.getObject(3);
            String publisher = (String) queryResultSet.getObject((4));

            // TODO: Retrieve the list of the book's authors.

            Book book = resolveRelationships(title,
                    year,
                    url,
                    publisher);

            retrievedBooks.add(book);
        }

        return retrievedBooks;
    }

    private Book resolveRelationships(String title,
                                         Long year,
                                         String url,
                                         String publisher) {
        Book book  = new Book(title, year, url, publisher);

        return book;
    }
}
