public class MainClassTest {
    public static void main(String[] args) {
        //MySQLConnection.performUpdate("INSERT INTO persona (nombre, apellidos) VALUES (\"JAVIER\", \"Vicente\")");
        DblpExtractor.extractDataIntoWarehouse();
    }
}
