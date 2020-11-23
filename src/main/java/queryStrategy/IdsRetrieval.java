package queryStrategy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IdsRetrieval implements QueryStrategy {
    @Override
    public List<Integer> retrieveQueryResults(ResultSet queryResultSet) throws SQLException {
        List<Integer> retrievedIds = new ArrayList<>();

        while (queryResultSet.next()) {
            // The "1" passed into the getObject() method, retrieves the value of the first column.
            retrievedIds.add((Integer) queryResultSet.getObject(1));
        }

        return retrievedIds;
    }
}
