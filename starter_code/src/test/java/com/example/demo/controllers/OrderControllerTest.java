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
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Spy
    private OrderController orderControllerSpy;

    @Before
    public void setup() {
        orderController = new OrderController();
        orderControllerSpy = Mockito.spy(orderController);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        user.setId(1L);

        Item item = new Item();
        item.setId(2L);
        item.setName("Item");
        item.setPrice(BigDecimal.ONE);
        item.setDescription("Item Description");
        List<Item> items = new ArrayList<>();
        items.add(item);
        Cart cart = new Cart();
        cart.setId(3L);
        cart.setItems(items);
        cart.setTotal(BigDecimal.ONE);
        cart.setUser(user);

        user.setCart(cart);

        UserOrder order = new UserOrder();
        order.setId(4L);
        order.setUser(user);
        order.setItems(items);
        order.setTotal(BigDecimal.ONE);

        doReturn(order).when(orderControllerSpy).createFromCartWrapper(cart);
        when(userRepository.findByUsername("user")).thenReturn(user);
        when(orderRepository.save(order)).thenReturn(order);

        ResponseEntity<UserOrder> response = orderController.submit("user");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserOrder result = response.getBody();
        assertEquals(user, result.getUser());
        assertEquals(items, result.getItems());
        assertEquals(BigDecimal.ONE, result.getTotal());
    }

    @Test
    public void getOrdersForUser() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        user.setId(1L);

        Item item = new Item();
        item.setId(2L);
        item.setName("Item");
        item.setPrice(BigDecimal.ONE);
        item.setDescription("Item Description");
        List<Item> items = new ArrayList<>();
        items.add(item);
        UserOrder order = new UserOrder();
        order.setId(4L);
        order.setUser(user);
        order.setItems(items);
        order.setTotal(BigDecimal.ONE);
        List<UserOrder> listOrders = new ArrayList<>();
        listOrders.add(order);
        when(userRepository.findByUsername("user")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(listOrders);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("user");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<UserOrder> result = response.getBody();
        UserOrder resultOrder = result.get(0);
        assertEquals(user, resultOrder.getUser());
        assertEquals(items, resultOrder.getItems());

    }
}
