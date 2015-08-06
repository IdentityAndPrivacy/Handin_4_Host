package host;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/session")
    public Session session(@RequestParam(value="name", defaultValue="World") String name) {
        return new Session(counter.incrementAndGet(),
                String.format(template, name));
    }
}