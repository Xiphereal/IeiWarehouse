package googleScholarWrapper;

import domainModel.utils.YearRange;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GoogleScholarWrapper {

    public static void main(String[] args) {
        // TODO: Add selenium scrapling and remove getting the objects from sample.bib

        SpringApplication.run(GoogleScholarWrapper.class, args);
    }
}
