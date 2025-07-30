package com.obs.OrderManagement.exceptions;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(Long itemId, int requestedQty, int availableQty) {
        super(String.format(
            "Item dengan id=%d memiliki stock %d, tidak cukup untuk permintaan %d",
            itemId, availableQty, requestedQty));
    }
}
