package com.obs.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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
    private Inventory inventory;

    @BeforeEach
    void setup() {
        item = new Item(10L, "Test", new Double(10000));
        inventory = new Inventory(10L, item, InventoryType.T, 10, LocalDateTime.now());
    }

    @Test
    void saveInv_topUp_shouldIncreaseStockAndSaveInv() {
        when(itemRepo.findById(10L)).thenReturn(Optional.of(item));
        when(itemRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(invRepo.save(inventory)).thenReturn(inventory);

        Inventory saved = invService.saveInv(inventory);

        assertEquals(25, inventory.getQuantity());
        assertSame(inventory, saved);
        verify(itemRepo).save(item);
        verify(invRepo).save(inventory);
    }

    @Test
    void saveInv_withdrawal_shouldDecreaseStock() {
        when(itemRepo.findById(10L)).thenReturn(Optional.of(item));
        when(itemRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(invRepo.save(inventory)).thenReturn(inventory);

        invService.saveInv(inventory);
        assertEquals(12, inventory.getQuantity());
    }

    @Test
    void saveInv_itemNotFound_shouldThrow() {
        when(itemRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            invService.saveInv(inventory)
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
        List<Inventory> list = List.of(inventory);
        when(invRepo.findAll(PageRequest.of(1, 2)))
            .thenReturn(new PageImpl<>(list));

        List<Inventory> res = invService.listInv(1, 2);
        assertEquals(1, res.size());
        assertEquals(3, res.get(0).getQuantity());
    }
}
