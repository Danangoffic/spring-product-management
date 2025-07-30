package com.obs.OrderManagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    
    @NotBlank(message = "Nama item wajib diisi")
    private String name;

    @NotNull(message = "Stock awal wajib diisi")
    @Min(value = 0, message = "Stock tidak boleh negatif")
    private Integer stock;
}