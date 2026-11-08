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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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

    @Test
    void shouldGroupOrdersAndIgnoreDuplicateIds() throws Exception {
        String json = """
            [
                {"id": 101, "customerCode": 10, "amount": 50.00},
                {"id": 102, "customerCode": 20, "amount": 80.00},
                {"id": 103, "customerCode": 10, "amount": 30.00},
                {"id": 101, "customerCode": 10, "amount": 999.00}
            ]
            """;

        mockMvc.perform(
                post("/orders/process")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON
            ))
            .andExpect(jsonPath("$.totalsByCustomer['10']").value(80.0))
            .andExpect(jsonPath("$.totalsByCustomer['20']").value(80.0))
            .andExpect(jsonPath("$.ordersByCustomer['10'].length()").value(2))
            .andExpect(jsonPath("$.ordersByCustomer['20'].length()").value(1))
            .andExpect(jsonPath("$.ordersByCustomer['10'][0].id").value(101))
            .andExpect(jsonPath("$.ordersByCustomer['10'][0].amount").value(50.0));
    }
}