package com.redhat.wine.cellar;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.junit4.SpringRunner;

import org.mockito.InjectMocks;
import org.mockito.Mock;

@RunWith(SpringRunner.class)
public class WineCellarApplicationTests {

    @Mock
    private WineRepository wineRepository;

    @Mock
    private io.opentracing.Tracer tracer;

    @InjectMocks
    WineCellarControllerImpl controllerImpl;

    @Test
    public void paramPairingWithRightFoodTypeShouldReturnTailoredMessage() throws Exception {
        System.out.println(">>>> UNIT WineCellarApplicationTests=>paramPairingWithRightFoodTypeShouldReturnTailoredMessage");

        List<Wine> wines = new ArrayList<Wine> ();
        wines.add(new Wine(WineType.BOLD_RED, 2013, "Sierra Cantabria Cuvee 2013", "RIOJA", "Bodegas y Viñedos Sierra Cantabria", "Spain", "100% Tempranillo", "Cherry red", "Elegant, intense", "Balanced, cocoa and red fruits", "14%"));
        when(wineRepository.findByTypeAndRegion(WineType.BOLD_RED, "RIOJA")).thenReturn(wines);
        
        WineRepositoryResponse expected = new WineRepositoryResponse (1, WineCellarControllerImpl.SUCCESS, WineCellarControllerImpl.SUCCESS, (Wine[]) wines.toArray(new Wine[wines.size()]));
        WineRepositoryResponse actual = controllerImpl.wineImpl(WineType.BOLD_RED.toString(), "rioja", wineRepository, tracer);

        assertTrue(expected.getStatus() == actual.getStatus() && expected.getWines().length == actual.getWines().length);
    }
}
