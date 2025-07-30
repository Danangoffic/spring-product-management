package com.obs.OrderManagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotNull(message = "ID item wajib diisi")
    private Long itemId;

    @NotNull(message = "Quantity order wajib diisi")
    @Min(value = 1, message = "Quantity minimal 1")
    private Integer quantity;
}