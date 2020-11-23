package ieiWarehousePopulator.queryStrategy;

import ieiWarehousePopulator.domain.utils.Tuple;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorsRetrieval implements QueryStrategy {

    /**
     * @param queryResultSet The first element in the SELECT statement must be the Author's name.
     *                       The second, must be the Author's surnames.
     */
    @Override
    public List<Tuple<String, String>> retrieveQueryResults(ResultSet queryResultSet) throws SQLException {
        List<Tuple<String, String>> retrievedAuthors = new ArrayList<>();

        while (queryResultSet.next()) {
            // The "1" passed into the getObject() method retrieves the value of the first column.
            String name = (String) queryResultSet.getObject(1);

            // The "2" passed into the getObject() method retrieves the value of the second column.
            String surnames = (String) queryResultSet.getObject(2);

            retrievedAuthors.add(new Tuple<>(name, surnames));
        }

        return retrievedAuthors;
    }
}
