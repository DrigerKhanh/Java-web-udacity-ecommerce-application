package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CartControllerTest {
    private CartController cartController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCart_happyPath() {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("testPassword");

        Cart cart = new Cart();
        user.setCart(cart);

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setUsername(user.getUsername());
        modifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        Cart cartResponse = response.getBody();

        assertNotNull(cartResponse);
        assertEquals(200, response.getStatusCode().value());
        assertEquals((item.getPrice()), cartResponse.getTotal());
    }

    @Test
    public void addToCart_butUser_notFound() {
        when(userRepository.findByUsername(any())).thenReturn(any());

        ResponseEntity<Cart> response = cartController.addTocart(new ModifyCartRequest());
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void addToCart_butItem_notFound() {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("testPassword");

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setUsername(user.getUsername());
        modifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void removeCart_happyPath() {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("testPassword");

        Cart cart = new Cart();
        user.setCart(cart);

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setUsername(user.getUsername());
        modifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        Cart cartResponse = response.getBody();

        assertNotNull(cartResponse);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(0, cartResponse.getItems().size());
    }

    @Test
    public void removeCart_butUser_notFound() {
        when(userRepository.findByUsername(any())).thenReturn(any());

        ResponseEntity<Cart> response = cartController.removeFromcart(new ModifyCartRequest());
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void removeCart_butItem_notFound() {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("testPassword");

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setUsername(user.getUsername());
        modifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertEquals(404, response.getStatusCode().value());
    }
}
