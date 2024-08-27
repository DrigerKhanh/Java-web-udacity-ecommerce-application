package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submitOrder_successfully() {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("testPassword");

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        List<Item> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        UserOrder userOrder = response.getBody();

        assertNotNull(userOrder);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(items.size(), userOrder.getItems().size());
        assertEquals(user.getUsername(), userOrder.getUser().getUsername());
    }

    @Test
    public void submitOrder_fail() {
        when(userRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<UserOrder> response = orderController.submit(any());
        assertEquals(404, response.getStatusCode().value());

    }

    @Test
    public void getUserOrder_successfully() {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("testPassword");

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        List<Item> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        List<UserOrder> userOrderResponse = response.getBody();

        assertNotNull(userOrderResponse);
        assertEquals(200, response.getStatusCode().value());

    }

    @Test
    public void getUserOrder_fail() {
        when(userRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(any());
        assertEquals(404, response.getStatusCode().value());
    }

}
