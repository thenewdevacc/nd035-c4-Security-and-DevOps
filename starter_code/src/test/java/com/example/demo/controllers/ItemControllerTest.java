package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItems() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setPrice(BigDecimal.valueOf(100.0));
        item.setDescription("Item Description");

        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepository.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        items = response.getBody();
        item = items.get(0);
        assertEquals("Item", item.getName());
        assertEquals("Item Description", item.getDescription());
    }

    @Test
    public void getItemById() {
        Long id = 1L;
        Item item = new Item();
        item.setId(id);
        item.setName("Item");
        item.setPrice(BigDecimal.valueOf(100.0));
        item.setDescription("Item Description");

        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(id);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Item returnedItem = response.getBody();
        assertEquals("Item", returnedItem.getName());
        assertEquals("Item Description", returnedItem.getDescription());
        assertEquals(id, returnedItem.getId());
    }

    @Test
    public void getItemsByName() {
        String name = "Item";
        Item item = new Item();
        item.setId(1L);
        item.setName(name);
        item.setPrice(BigDecimal.valueOf(100.0));
        item.setDescription("Item Description");

        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepository.findByName(name)).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItemsByName(name);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        items = response.getBody();
        item = items.get(0);
        assertEquals("Item", item.getName());
        assertEquals("Item Description", item.getDescription());
    }
}