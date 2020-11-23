package queryStrategy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface QueryStrategy {
    <T> List<T> retrieveQueryResults(ResultSet queryResultSet) throws SQLException;
}
