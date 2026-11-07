package com.orderflow.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderRequest(
    @NotNull @Positive Long id,
    @NotNull @Positive Long customerCode,
    @NotNull @DecimalMin("0.01") BigDecimal amount
) {}