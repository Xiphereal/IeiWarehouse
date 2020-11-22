import java.sql.*;

/**
 * This class serves as a container for the MySQL connection, encapsulating that knowledge
 * and offering the services as class methods.
 *
 * Reference: https://www.javatpoint.com/example-to-connect-to-the-mysql-database
 */
public class MySQLConnection {
    private static final String serverHostName = "iei.mysql.database.azure.com";
    private static final String databaseSchemaName = "mydb";
    private static final String username = "ricardo@iei";
    private static final String password = "Valencia2020";
    private static final String connectionOptions = "?useTimezone=true&serverTimezone=UTC&useSSL=false";

    public static Integer performQuery(String sqlQuery) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://" + serverHostName + ":3306/" + databaseSchemaName + connectionOptions,
                    username,
                    password
            );

            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(sqlQuery);

            Integer retrievedId = getIdFromQueryResult(queryResult);

            connection.close();
            return retrievedId;
        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }

        return null;
    }

    private static Integer getIdFromQueryResult(ResultSet queryResult) throws SQLException {
        // The internal pointer of queryResult is initially pointing to null, we need to call
        // .next() to update de internal pointer to the first element.
        if (!queryResult.next())
            return null;

        // The "1" passed into the getObject() method, retrieves the value of the first column.
        Integer retrievedId = (Integer) queryResult.getObject(1);
        return retrievedId;
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
