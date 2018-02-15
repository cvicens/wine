package com.redhat.wine.pairing;

import java.util.Optional;
import io.opentracing.Span;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WinePairingController {
    @Autowired
    private io.opentracing.Tracer tracer;
    
    @Autowired
    private RestTemplate restTemplate;
    
    public static final String UNKOWN = "UNKOWN";
    public static final String UNKOWN_ERROR = "UNKOWN_ERROR";

    public static final String UNKOWN_WINE_TYPE = "UNKOWN_WINE_TYPE";
    public static final String UNKOWN_REGION = "UNKOWN_REGION";

    public static final String UNKOWN_FOOD = "UNKOWN_FOOD";
    public static final String ERROR = "ERROR";
    public static final String SUCCESS = "SUCCESS";

    private static final String template = "Hello, %s!";

    private final AtomicLong counter = new AtomicLong();
    private final AtomicLong pairingCounter = new AtomicLong();

    @RequestMapping("/health")
    public String health() {
        return "OK";
    }

    @RequestMapping("/readiness")
    public String readiness() {
        return "OK";
    }

    @RequestMapping("/pairing")
    public WinePairingResponse pairing(@RequestParam(value="foodType", defaultValue=UNKOWN_FOOD) String foodType) {
        Optional.ofNullable(tracer.activeSpan()).ifPresent(as -> as.setBaggageItem("wine-task", "pairing"));
        FoodType _foodType = null;
        ArrayList<WineType> types = new ArrayList<WineType>();

        try {
            _foodType = FoodType.valueOf(foodType);

            if (_foodType.equals(FoodType.UNKOWN_FOOD)) {
                return new WinePairingResponse (pairingCounter.incrementAndGet(), "ERROR", UNKOWN_FOOD, new WineType[0]);
            }

            Optional.ofNullable(tracer.activeSpan()).ifPresent(as -> as.setBaggageItem("food-type", foodType));

            switch (_foodType) {
                case FISH: {
                    types.add(WineType.DRY_WHITE);
                    types.add(WineType.ROSE);
                    break;
                }
                case RED_MEAT: {
                    types.add(WineType.BOLD_RED);
                    break;
                }
                case WHITE_MEAT: {
                    types.add(WineType.LIGHT_RED);
                    types.add(WineType.SPARKLING);
                    break;
                }
                default: {
                    types.add(WineType.MEDIUM_RED);
                }
            }

            System.out.println("IN: " + foodType + " OUT: " + (WineType[]) types.toArray(new WineType[types.size()]));

            return new WinePairingResponse (pairingCounter.incrementAndGet(), SUCCESS, SUCCESS, (WineType[]) types.toArray(new WineType[types.size()]));
        } catch (Throwable e) {
            return new WinePairingResponse (pairingCounter.incrementAndGet(), ERROR, UNKOWN_FOOD, new WineType[0]);
        }
    }

    /**  Tracing tests  **/

    @RequestMapping("/chaining")
    public String chaining() {
        Optional.ofNullable(tracer.activeSpan()).ifPresent(as -> as.setBaggageItem("chaining-task", "chain-value"));
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/hello", String.class);
        return "Chaining " + response.getBody();
    }
    
    @RequestMapping("/hello")
    public String hello() throws InterruptedException {
            Span parent = tracer
                .buildSpan("hello-task")
                .startManual();

            Thread.sleep(1 + (long)(Math.random()*500));
            try {
                Span child = tracer
                    .buildSpan("world-task")
                    .asChildOf(parent)
                    .startManual();
                Thread.sleep(1 + (long)(Math.random()*500));
                child.finish();
            } finally {
                parent.finish();
            }

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/greeting", String.class);
        return "Hello World! " + response.getBody();
    }

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
}