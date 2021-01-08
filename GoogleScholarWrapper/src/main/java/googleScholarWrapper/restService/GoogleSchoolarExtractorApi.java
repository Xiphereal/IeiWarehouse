package googleScholarWrapper.restService;

import domainModel.utils.YearRange;
import googleScholarWrapper.bibtexToJson.BibtexToJsonParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GoogleSchoolarExtractorApi {
    private final AtomicLong requestId = new AtomicLong();

    private static final String ERROR_MESSAGE = "ERROR: The given parameters for the search are invalid. " +
            "The years must use format startYear:yyyy and endYear:yyyy";

    //TODO:Remove getting the string from file and add selenium scrapling
    @GetMapping("/extract")
    public Map<String, Object> getData(@RequestParam(value = "startYear", defaultValue = "1000") String startYear,
                                       @RequestParam(value = "endYear", defaultValue = "2999") String endYear,
                                       @RequestParam(value = "maxPublications", defaultValue = "10") String maxPublications) throws IOException {

        String string = Files.readString(Path.of("D:\\IdeaProjects\\IeiWarehouse\\GoogleScholarWrapper\\src\\main\\resources\\sample.bib"));
        //when using selenium we wont need to split the string since we will get a List<String>
        return BibtexToJsonParser.toJson(Arrays.asList(string.split("@")),
                new YearRange(Long.parseLong(startYear), Long.parseLong(endYear)),
                Integer.parseInt(maxPublications)).toMap();
    }
}