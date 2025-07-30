package com.obs.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
    @Mock
    private ItemRepository itemRepo;
    @Mock
    private InventoryRepository invRepo;
    @InjectMocks
    private InventoryService invService;

    private Item item;
    private Inventory topUpInventory;
    private Inventory withdrawalInventory;
    private LocalDateTime now;

    @BeforeEach
    void setup() {
        // Item with initial stock
        item = new Item(10L, "TestItem", 100.0);
        // Timestamp for consistency
        now = LocalDateTime.now();
        topUpInventory = new Inventory(null, item, InventoryType.T, 20, now);
        withdrawalInventory = new Inventory(null, item, InventoryType.W, 20, now);
    }

    @Test
    void saveInv_topUp_shouldSaveAndNotModifyInventory() {
        when(itemRepo.findById(10L)).thenReturn(Optional.of(item));
        when(invRepo.save(topUpInventory)).thenReturn(topUpInventory);

        Inventory saved = invService.saveInv(topUpInventory);

        assertSame(topUpInventory, saved);
        verify(itemRepo).findById(10L);
        verify(invRepo).save(topUpInventory);
    }

    @Test
    void saveInv_withdrawal_shouldSaveAndNotModifyInventory() {
        when(itemRepo.findById(10L)).thenReturn(Optional.of(item));
        when(invRepo.save(withdrawalInventory)).thenReturn(withdrawalInventory);

        Inventory saved = invService.saveInv(withdrawalInventory);

        assertSame(withdrawalInventory, saved);
        verify(itemRepo).findById(10L);
        verify(invRepo).save(withdrawalInventory);
    }

    @Test
    void saveInv_itemNotFound_shouldThrowResourceNotFound() {
        when(itemRepo.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> invService.saveInv(topUpInventory));
        assertTrue(ex.getMessage().length() > 0);
        verify(itemRepo).findById(10L);
        verifyNoInteractions(invRepo);
    }

    @Test
    void deleteInv_shouldInvokeRepository() {
        doNothing().when(invRepo).deleteById(7L);
        invService.deleteInv(7L);
        verify(invRepo).deleteById(7L);
    }

    @Test
    void listInv_shouldReturnContentList() {
        List<Inventory> list = List.of(topUpInventory);
        when(invRepo.findAll(PageRequest.of(1, 2)))
                .thenReturn(new PageImpl<>(list));

        List<Inventory> result = invService.listInv(1, 2);
        assertEquals(1, result.size());
        assertEquals(topUpInventory, result.get(0));
        verify(invRepo).findAll(PageRequest.of(1, 2));
    }

    
}