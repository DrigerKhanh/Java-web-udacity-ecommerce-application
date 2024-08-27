package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getAllItems_successfully() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Round Widget");
        item1.setPrice(BigDecimal.valueOf(2.99));
        item1.setDescription("A widget that is round");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Square Widget");
        item2.setPrice(BigDecimal.valueOf(1.99));
        item2.setDescription("A widget that is square");

        List<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item2);

        when(itemRepository.findAll()).thenReturn(itemList);

        ResponseEntity<List<Item>> response = itemController.getItems();

        List<Item> itemResponse = response.getBody();

        assertNotNull(itemResponse);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(itemList.size(), itemResponse.size());
    }

    @Test
    public void getItem_byId_successfully() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(item.getId());
        Item itemResponse = response.getBody();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(itemResponse);
        assertEquals(item.getName(), itemResponse.getName());
        assertEquals(item.getPrice(), itemResponse.getPrice());
        assertEquals(item.getDescription(), itemResponse.getDescription());
    }

    @Test
    public void getItem_byName_successfully() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        when(itemRepository.findByName(item.getName())).thenReturn(itemList);

        ResponseEntity<List<Item>> response = itemController.getItemsByName(item.getName());
        List<Item> itemResponse = response.getBody();

        assertNotNull(itemResponse);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(itemList.size(), itemResponse.size());
    }
}
