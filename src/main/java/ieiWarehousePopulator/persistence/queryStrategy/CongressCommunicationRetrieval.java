package ieiWarehousePopulator.persistence.queryStrategy;

import ieiWarehousePopulator.domain.CongressCommunication;
import ieiWarehousePopulator.domain.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CongressCommunicationRetrieval implements QueryStrategy {
    @Override
    public List<CongressCommunication> retrieveQueryResults(ResultSet queryResultSet) throws SQLException {
        Map<Integer, CongressCommunication> identifiedCongresses = new HashMap<>();

        while (queryResultSet.next()) {
            // Each parameter 'i' passed to the getObject() method indicates
            // from which column to retrieve. Columns indexes are 1 based, not 0.
            String title = (String) queryResultSet.getObject(1);
            Long year = Long.valueOf((Integer) queryResultSet.getObject(2));
            String url = (String) queryResultSet.getObject(3);
            Integer initialPage = (Integer) queryResultSet.getObject(4);
            Integer finalPage = (Integer) queryResultSet.getObject(5);

            String congress = (String) queryResultSet.getObject(6);
            String edition = (String) queryResultSet.getObject(7);
            String place = (String) queryResultSet.getObject(8);

            Integer publicationId = (Integer) queryResultSet.getObject(9);

            String authorName = (String) queryResultSet.getObject(10);
            String authorSurnames = (String) queryResultSet.getObject(11);

            CongressCommunication congressCommunication = resolveRelationships(title,
                    year,
                    url,
                    congress,
                    edition,
                    place,
                    initialPage,
                    finalPage);

            Person author = getAuthor(authorName, authorSurnames);

            // If the congress communication has been already retrieved,
            // update that precise object from the map.
            if (identifiedCongresses.containsKey(publicationId)) {
                congressCommunication = identifiedCongresses.get(publicationId);
            }

            congressCommunication.addAuthor(author);

            identifiedCongresses.put(publicationId, congressCommunication);
        }

        return new ArrayList<>(identifiedCongresses.values());
    }

    private CongressCommunication resolveRelationships(String title,
                                                       Long year,
                                                       String url,
                                                       String congress,
                                                       String edition,
                                                       String place,
                                                       Integer initialPage,
                                                       Integer finalPage) {
        CongressCommunication congressCommunication = new CongressCommunication(title, year, url, congress, edition, place, initialPage, finalPage);

        return congressCommunication;
    }

    private Person getAuthor(String authorName, String authorSurnames) {
        Person author = null;

        if (authorName != null || authorSurnames != null)
            author = new Person(authorName, authorSurnames);

        return author;
    }
}
