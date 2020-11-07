import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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

    public static void performQuery(String sqlQuery) {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://" + serverHostName + ":3306/" + databaseSchemaName,
                    username,
                    password
            );

            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(sqlQuery);

            for (int resultIndex = 0; queryResult.next(); resultIndex++)
                System.out.println(queryResult.getObject(resultIndex));

            connection.close();
        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }
    }
}
