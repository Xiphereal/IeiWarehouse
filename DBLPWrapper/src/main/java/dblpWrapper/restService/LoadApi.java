package dblpWrapper.restService;

import dblpWrapper.JSONtoXML.JsonToXmlConverter;
import dblpWrapper.restService.requestResponses.RequestStatusResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class LoadApi {
    private final AtomicLong requestId = new AtomicLong();

    private static final String ERROR_MESSAGE = "ERROR: The given years for the extraction are invalid." +
            "The expected parameters are 'startedYear:yyyy' and 'endYear:yyyy'" +
            ", with startedYear being less than endYear." +
            "The valid year range goes from 1000 to 2999";

    private static final String OK_MESSAGE = "OK: The extraction has started as expected.";

    @GetMapping("/extract")
    public RequestStatusResponse extractData(@RequestParam(value = "startYear", defaultValue = "1000") String startYear,
                                             @RequestParam(value = "endYear", defaultValue = "2999") String endYear) throws IOException {
        return new RequestStatusResponse(requestId.incrementAndGet(), JsonToXmlConverter.convert().toString());
    }

}
