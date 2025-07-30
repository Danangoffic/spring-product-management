package com.obs.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.obs.OrderManagement.exceptions.InsufficientStockException;
import com.obs.OrderManagement.exceptions.ResourceNotFoundException;
import com.obs.OrderManagement.models.Inventory;
import com.obs.OrderManagement.models.Item;
import com.obs.OrderManagement.models.Order;
import com.obs.OrderManagement.repository.InventoryRepository;
import com.obs.OrderManagement.repository.ItemRepository;
import com.obs.OrderManagement.repository.OrderRepository;
import com.obs.OrderManagement.service.OrderService;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock private ItemRepository itemRepo;
    @Mock private OrderRepository orderRepo;
    @Mock
    private InventoryRepository inventoryRepo;
    @InjectMocks private OrderService orderService;

    private Item item;
    private Inventory inventory;

    @BeforeEach
    void init() {
        item = new Item(20L, "OrdItem", new Double(15000));
    }

    @Test
    void saveOrder_success_shouldReduceStockAndSave() {
        Order req = new Order(null, "O"+System.currentTimeMillis(), item, 5, new Double(5000), LocalDateTime.now());
        when(itemRepo.findById(20L)).thenReturn(Optional.of(item));
        when(itemRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(orderRepo.save(req)).thenAnswer(i -> {
            Order o = i.getArgument(0);
            o.setId(50L);
            return o;
        });

        Order saved = orderService.saveOrder(req);
        assertEquals(50L, saved.getId());
        assertEquals(10, saved.getQuantity());
        verify(orderRepo).save(req);
    }

    @Test
    void saveOrder_itemNotFound_shouldThrow() {
        when(itemRepo.findById(30L)).thenReturn(Optional.empty());
        Order req = new Order(null,"O"+System.currentTimeMillis(), new Item(30L,null,null), 1, new Double(1000), null);

        assertThrows(ResourceNotFoundException.class, () ->
            orderService.saveOrder(req)
        );
    }

    @Test
    void saveOrder_insufficientStock_shouldThrow() {
        inventory.setQuantity(2);
        when(inventoryRepo.findById(20L)).thenReturn(Optional.of(inventory));
        Order req = new Order(null, "O"+System.currentTimeMillis(), item, 5, new Double(5000), LocalDateTime.now());

        assertThrows(InsufficientStockException.class, () ->
            orderService.saveOrder(req)
        );
    }

    @Test
    void deleteOrder_shouldInvokeRepo() {
        doNothing().when(orderRepo).deleteById(99L);
        orderService.deleteOrder(99L);
        verify(orderRepo).deleteById(99L);
    }

    @Test
    void listOrders_shouldReturnPage() {
        List<Order> list = List.of(new Order(2L, "O"+System.currentTimeMillis(), item, 3, new Double(1300), LocalDateTime.now()));
        when(orderRepo.findAll(PageRequest.of(0, 3)))
            .thenReturn(new PageImpl<>(list));

        List<Order> res = orderService.listOrders(0, 3);
        assertEquals(1, res.size());
        assertEquals(3, res.get(0).getQuantity());
    }
}
