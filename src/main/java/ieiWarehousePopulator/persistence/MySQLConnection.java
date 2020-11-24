package ieiWarehousePopulator.persistence;

import ieiWarehousePopulator.domain.utils.Tuple;
import ieiWarehousePopulator.persistence.queryStrategy.AuthorsRetrieval;
import ieiWarehousePopulator.persistence.queryStrategy.IdsRetrieval;
import ieiWarehousePopulator.persistence.queryStrategy.QueryStrategy;

import java.sql.*;
import java.util.List;

/**
 * This class serves as a container for the MySQL connection, encapsulating that knowledge
 * and offering the services as class methods.
 * <p>
 * Reference: https://www.javatpoint.com/example-to-connect-to-the-mysql-database
 */
public class MySQLConnection {
    private static final String serverHostName = "iei.mysql.database.azure.com";
    private static final String databaseSchemaName = "mydb";
    private static final String username = "ricardo@iei";
    private static final String password = "Valencia2020";
    private static final String connectionOptions = "?useTimezone=true&serverTimezone=UTC&useSSL=false";

    public static List<Integer> performQueryToRetrieveIds(String sqlQuery) {
        return performQuery(sqlQuery, new IdsRetrieval());
    }

    public static List<Tuple<String, String>> performQueryToRetrieveAuthors(String sqlQuery) {
        return performQuery(sqlQuery, new AuthorsRetrieval());
    }

    /**
     * @param queryStrategy A instance of {@link QueryStrategy}, which specifies the expected result for the SQL query.
     * @param <T>           The type for the expected result for the SQL query.
     */
    private static <T> List<T> performQuery(String sqlQuery, QueryStrategy queryStrategy) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://" + serverHostName + ":3306/" + databaseSchemaName + connectionOptions,
                    username,
                    password
            );

            Statement statement = connection.createStatement();
            ResultSet queryResultSet = statement.executeQuery(sqlQuery);

            List<T> queryRetrievedResults = queryStrategy.retrieveQueryResults(queryResultSet);

            connection.close();

            return queryRetrievedResults;

        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }

        return null;
    }

    private static void printQueryResults(ResultSet queryResult) throws SQLException {
        int numberOfColumns = queryResult.getMetaData().getColumnCount();

        while (queryResult.next()) {
            for (int columnIndex = 1; columnIndex <= numberOfColumns; columnIndex++) {
                Object columnValue = queryResult.getObject(columnIndex);
                System.out.println(columnValue.toString());
            }
        }
    }

    public static void performUpdate(String sqlQuery) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://" + serverHostName + ":3306/" + databaseSchemaName + connectionOptions,
                    username,
                    password
            );

            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlQuery);

            System.out.println("Update: " + sqlQuery + " was successful!");

            connection.close();
        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }
    }
}
