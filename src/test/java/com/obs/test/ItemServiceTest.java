package com.obs.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.obs.OrderManagement.models.Item;
import com.obs.OrderManagement.repository.ItemRepository;
import com.obs.OrderManagement.service.ItemService;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock private ItemRepository itemRepo;
    @InjectMocks private ItemService itemService;

    @Test
    void createItem_shouldSaveAndReturn() {
        Item in = new Item(null, "A", 5);
        Item saved = new Item(1L, "A", 5);
        when(itemRepo.save(in)).thenReturn(saved);

        Item result = itemService.saveItem(in);
        assertEquals(1L, result.getId());
        assertEquals("A", result.getName());
        verify(itemRepo).save(in);
    }

    @Test
    void getItem_found_shouldReturn() {
        Item i = new Item(1L, "X", 10);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(i));

        Optional<Item> opt = itemService.getItem(1L);
        assertTrue(opt.isPresent());
        assertEquals("X", opt.get().getName());
    }

    @Test
    void getItem_notFound_shouldReturnEmpty() {
        when(itemRepo.findById(2L)).thenReturn(Optional.empty());
        assertTrue(itemService.getItem(2L).isEmpty());
    }

    @Test
    void updateItem_existing_shouldSave() {
        Item update = new Item(null, "B", 7);
        Item merged = new Item(3L, "B", 7);
        when(itemRepo.save(argThat(i -> i.getId().equals(3L)))).thenReturn(merged);

        Item result = itemService.updateItem(3L, update);
        assertEquals(3L, result.getId());
        verify(itemRepo).save(update);
    }

    @Test
    void deleteItem_shouldInvokeRepo() {
        doNothing().when(itemRepo).deleteById(4L);
        itemService.deleteItem(4L);
        verify(itemRepo).deleteById(4L);
    }

    @Test
    void listItems_shouldReturnPageContent() {
        List<Item> data = List.of(new Item(5L, "Z", 2));
        when(itemRepo.findAll(PageRequest.of(0, 5)))
            .thenReturn(new PageImpl<>(data));

        List<Item> res = itemService.listItems(0, 5);
        assertEquals(1, res.size());
        assertEquals("Z", res.get(0).getName());
    }
}
