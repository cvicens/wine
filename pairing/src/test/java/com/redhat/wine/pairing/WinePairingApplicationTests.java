package com.redhat.wine.pairing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WinePairingApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void noParamGreetingShouldReturnDefaultMessage() throws Exception {

        this.mockMvc.perform(get("/greeting")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Hello, World!"));
    }

    @Test
    public void paramGreetingShouldReturnTailoredMessage() throws Exception {

        this.mockMvc.perform(get("/greeting").param("name", "Spring Community"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Hello, Spring Community!"));
    }

    
    @Test
    public void noParamPairingShouldReturnDefaultMessage() throws Exception {

        this.mockMvc.perform(get("/pairing")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(WinePairingController.ERROR))
                .andExpect(jsonPath("$.description").value(WinePairingController.UNKOWN_FOOD))
                .andExpect(jsonPath("$.wineTypes").isArray());
    }

    @Test
    public void paramPairingWithRightFoodTypeShouldReturnTailoredMessage() throws Exception {

        this.mockMvc.perform(get("/pairing").param("foodType", FoodType.FISH.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(WinePairingController.SUCCESS))
                .andExpect(jsonPath("$.description").value(WinePairingController.SUCCESS))
                .andExpect(jsonPath("$.wineTypes").isArray())
                .andExpect(jsonPath("$.wineTypes", not(containsInAnyOrder(WineType.UNKOWN))));
                
    }
}
