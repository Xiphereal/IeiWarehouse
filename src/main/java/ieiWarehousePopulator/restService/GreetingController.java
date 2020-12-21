package ieiWarehousePopulator.restService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * This is a test resource controller class. Must be removed upon tutorial completion.
 */
@RestController
public class GreetingController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    // @GetMapping ensures that HTTP GET request to '/greeting' are mapped to the
    // greeting method. Equals to @RequestMapping(method=GET).
    //
    // @RequestParam binds the value of the query string parameter 'name' into de 'name' parameter
    // of the greeting method. 'defaultValue' indicates the default value in absence of the
    // 'name' parameter.
    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}
