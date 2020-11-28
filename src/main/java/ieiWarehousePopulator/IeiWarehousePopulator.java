package ieiWarehousePopulator;

import ieiWarehousePopulator.extractors.DblpExtractor;
import ieiWarehousePopulator.extractors.GoogleSchoolar;
import ieiWarehousePopulator.extractors.IeeeExtractor;
import ieiWarehousePopulator.persistence.MySQLConnection;
import ieiWarehousePopulator.utils.DatabasePurge;

public class IeiWarehousePopulator {
    public static void main(String[] args) {

        DatabasePurge.purgeAllTables();

        IeeeExtractor.extractDataIntoWarehouse();
        DblpExtractor.extractDataIntoWarehouse();
        GoogleSchoolar.extractDataIntoWarehouse();

        // Closes the SQL connection even when the VM terminates abruptly.
        // ! Doesn't work in IDE executions.
        // See: https://stackoverflow.com/questions/3366965/is-it-is-possible-to-do-something-when-the-java-program-exits-abruptly
        Runtime.getRuntime().addShutdownHook(new Thread(() -> MySQLConnection.closeConnection()));
    }
}
