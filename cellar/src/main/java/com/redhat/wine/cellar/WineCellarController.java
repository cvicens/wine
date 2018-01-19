package com.redhat.wine.cellar;

import java.util.List;
import java.util.Optional;
import io.opentracing.Span;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WineCellarController {
    @Autowired
    private io.opentracing.Tracer tracer;
    
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WineRepository wineRepository;
    
    public static final String UNKOWN = "UNKOWN";
    public static final String UNKOWN_ERROR = "UNKOWN_ERROR";

    public static final String UNKOWN_WINE_TYPE = "UNKOWN_WINE_TYPE";
    public static final String UNKOWN_REGION = "UNKOWN_REGION";

    public static final String ERROR = "ERROR";
    public static final String SUCCESS = "SUCCESS";

    private final AtomicLong wineRepositoryCounter = new AtomicLong();

    @RequestMapping("/health")
    public String health() {
        return "OK";
    }

    @RequestMapping("/readiness")
    public String readiness() {
        return "OK";
    }

    @PostConstruct
    @RequestMapping("/init")
    public String init () {
        Optional.ofNullable(tracer.activeSpan()).ifPresent(as -> as.setBaggageItem("wine-task", "init"));
        
        System.out.println("==> Executing init!");

        // Clean
        wineRepository.deleteAll();

        // save a couple of wines
		wineRepository.save(new Wine(WineType.DRY_WHITE, 2016, "Bodegas Terras Gauda 2016", "ALBARIÑO", "Bodegas Terras Gauda", "Spain", "70% Albariño, 18% Caiño y 12% Loureiro", "Golden", "Clear, expresive", "Acid, strong, fruity", "12.5%"));
		wineRepository.save(new Wine(WineType.BOLD_RED, 2013, "Sierra Cantabria Cuvee 2013", "RIOJA", "Bodegas y Viñedos Sierra Cantabria", "Spain", "100% Tempranillo", "Cherry red", "Elegant, intense", "Balanced, cocoa and red fruits", "14%"));
        
        System.out.println("==> Init executed!");
		return "The End";
    }

    /**
     * Wine search by wineType (DRY_WHITE, BOLD_RED) and region (RIOJA or ALBARIÑO)
     */
    @RequestMapping("/wine")
    public WineRepositoryResponse wine (@RequestParam(value="wineType", defaultValue=UNKOWN) String wineType, 
                                        @RequestParam(value="region", defaultValue=UNKOWN_REGION) String region) 
                                        throws InterruptedException {
        Optional.ofNullable(tracer.activeSpan()).ifPresent(as -> as.setBaggageItem("wine-task", "search"));

        Thread.sleep(1 + (long)(Math.random()*500));
        if (Math.random() > 0.8) { 
            throw new RuntimeException("Unknown freaking error!");
        }

        try {
            WineType _wineType = WineType.valueOf(wineType.toUpperCase());
            List<Wine> wines = wineRepository.findByTypeAndRegion(_wineType, region.toUpperCase());

            if (_wineType.equals(WineType.UNKOWN)) {
                return new WineRepositoryResponse (wineRepositoryCounter.incrementAndGet(), "ERROR", UNKOWN_WINE_TYPE, new Wine[0]);
            }

            Optional.ofNullable(tracer.activeSpan()).ifPresent(as -> as.setBaggageItem("wine-type", wineType));
            Optional.ofNullable(tracer.activeSpan()).ifPresent(as -> as.setBaggageItem("wine-region", region));
            System.out.println("IN: " + wineType + " OUT: " + Arrays.toString((Wine[]) wines.toArray(new Wine[wines.size()])));

            return new WineRepositoryResponse (wineRepositoryCounter.incrementAndGet(), SUCCESS, SUCCESS, (Wine[]) wines.toArray(new Wine[wines.size()]));
        } catch (Throwable e) {
            return new WineRepositoryResponse (wineRepositoryCounter.incrementAndGet(), ERROR, UNKOWN_ERROR, new Wine[0]);
        }
    }
}