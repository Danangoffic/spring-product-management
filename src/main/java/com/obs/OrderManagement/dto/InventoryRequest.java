package com.obs.OrderManagement.dto;

import com.obs.OrderManagement.models.InventoryType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    
    @NotNull(message = "ID item wajib diisi")
    private Long itemId;

    @NotNull(message = "Tipe inventory wajib diisi")
    private InventoryType type;

    @NotNull(message = "Quantity wajib diisi")
    @Min(value = 1, message = "Quantity minimal 1")
    private Integer quantity;
}