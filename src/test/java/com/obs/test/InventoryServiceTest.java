package com.obs.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.obs.OrderManagement.exceptions.ResourceNotFoundException;
import com.obs.OrderManagement.models.Inventory;
import com.obs.OrderManagement.models.InventoryType;
import com.obs.OrderManagement.models.Item;
import com.obs.OrderManagement.repository.InventoryRepository;
import com.obs.OrderManagement.repository.ItemRepository;
import com.obs.OrderManagement.service.InventoryService;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {
    @Mock private ItemRepository itemRepo;
    @Mock private InventoryRepository invRepo;
    @InjectMocks private InventoryService invService;

    private Item item;

    @BeforeEach
    void setup() {
        item = new Item(10L, "Test", 20);
    }

    @Test
    void saveInv_topUp_shouldIncreaseStockAndSaveInv() {
        Inventory invReq = new Inventory(null, item, InventoryType.T, 5, null);
        when(itemRepo.findById(10L)).thenReturn(Optional.of(item));
        when(itemRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(invRepo.save(invReq)).thenReturn(invReq);

        Inventory saved = invService.saveInv(invReq);

        assertEquals(25, item.getStock());
        assertSame(invReq, saved);
        verify(itemRepo).save(item);
        verify(invRepo).save(invReq);
    }

    @Test
    void saveInv_withdrawal_shouldDecreaseStock() {
        Inventory invReq = new Inventory(null, item, InventoryType.W, 8, null);
        when(itemRepo.findById(10L)).thenReturn(Optional.of(item));
        when(itemRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(invRepo.save(invReq)).thenReturn(invReq);

        invService.saveInv(invReq);
        assertEquals(12, item.getStock());
    }

    @Test
    void saveInv_itemNotFound_shouldThrow() {
        Inventory invReq = new Inventory(null, new Item(99L, null, null), InventoryType.T, 1, null);
        when(itemRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            invService.saveInv(invReq)
        );
    }

    @Test
    void deleteInv_shouldInvokeRepo() {
        doNothing().when(invRepo).deleteById(7L);
        invService.deleteInv(7L);
        verify(invRepo).deleteById(7L);
    }

    @Test
    void listInv_shouldReturnPage() {
        List<Inventory> list = List.of(new Inventory(1L, item, InventoryType.T, 3, null));
        when(invRepo.findAll(PageRequest.of(1, 2)))
            .thenReturn(new PageImpl<>(list));

        List<Inventory> res = invService.listInv(1, 2);
        assertEquals(1, res.size());
        assertEquals(3, res.get(0).getQuantity());
    }
}
