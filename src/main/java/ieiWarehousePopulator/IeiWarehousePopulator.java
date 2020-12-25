package ieiWarehousePopulator;

import ieiWarehousePopulator.persistence.MySQLConnection;
import ieiWarehousePopulator.persistence.utils.DatabasePurge;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IeiWarehousePopulator {
    public static void main(String[] args) {

        DatabasePurge.purgeAllTables();

        SpringApplication.run(IeiWarehousePopulator.class, args);
        
//        IeeeExtractor.extractDataIntoWarehouse();
//        DblpExtractor.extractDataIntoWarehouse();
//        GoogleSchoolarExtractor.extractDataIntoWarehouse();

        // Closes the SQL connection even when the VM terminates abruptly.
        // ! Doesn't work in IDE executions.
        // See: https://stackoverflow.com/questions/3366965/is-it-is-possible-to-do-something-when-the-java-program-exits-abruptly
        Runtime.getRuntime().addShutdownHook(new Thread(() -> MySQLConnection.closeConnection()));
    }
}
