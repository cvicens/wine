package com.redhat.wine.cellar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WineCellarController {
    @Autowired
    private io.opentracing.Tracer tracer;
    
    @Autowired
    private WineRepository wineRepository;

    @Autowired
    private WineCellarControllerImpl wineCellarControllerImpl;
    
    @RequestMapping("/health")
    public String health() {
        return "OK";
    }

    @RequestMapping("/readiness")
    public String readiness() {
        return "OK";
    }

    //@PostConstruct
    @RequestMapping("/init")
    public String init () {
		return wineCellarControllerImpl.init(wineRepository, tracer);
    }

    /**
     * Wine search by wineType (DRY_WHITE, BOLD_RED) and region (RIOJA or ALBARIÑO)
     */
    @RequestMapping("/wine")
    public WineRepositoryResponse wine (@RequestParam(value="wineType", defaultValue=WineCellarControllerImpl.UNKOWN) String wineType, 
                                        @RequestParam(value="region", defaultValue=WineCellarControllerImpl.UNKOWN_REGION) String region) 
                                        throws InterruptedException {
        return wineCellarControllerImpl. wineImpl(wineType, region, wineRepository, tracer);
    }
}