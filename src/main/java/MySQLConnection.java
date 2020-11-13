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

    public static void performQuery(String sqlQuery) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://" + serverHostName + ":3306/" + databaseSchemaName + connectionOptions,
                    username,
                    password
            );

            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(sqlQuery);

            printQueryResults(queryResult);

            connection.close();
        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }
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
