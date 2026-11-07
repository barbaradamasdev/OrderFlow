package com.orderflow.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRejectOrderWithNegativeAmount() throws Exception {

        String json = """
            [
                {
                    "id": 101,
                    "customerCode": 10,
                    "amount": -50.00
                }
            ]
            """;

        mockMvc.perform(
                post("/orders/process")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldProcessValidOrders() throws Exception {

        String json = """
            [
                {
                    "id": 101,
                    "customerCode": 10,
                    "amount": 50.00
                }
            ]
            """;

        mockMvc.perform(
            post("/orders/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalsByCustomer['10']").value(50.0));
    }
}