package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CartControllerTest {

    private CartController cartController;
    
	private UserRepository userRepository = mock(UserRepository.class);
	
	private CartRepository cartRepository = mock(CartRepository.class);
	
    private ItemRepository itemRepository = mock(ItemRepository.class);


    @Before
    public void setUp(){
        cartController = new CartController() ;
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setUsername("user");
        modifyCartRequest.setQuantity(1);
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
        when(userRepository.findByUsername("user")).thenReturn(user);
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Cart result = response.getBody();
        assertEquals(items, result.getItems());
        assertEquals(BigDecimal.valueOf(2), result.getTotal());
    }

    @Test
    public void removeToCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setUsername("user");
        modifyCartRequest.setQuantity(1);
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        user.setId(1L);

        Item item = new Item();
        item.setId(2L);
        item.setName("Item");
        item.setPrice(BigDecimal.ONE);
        item.setDescription("Iten Description");
        List<Item> listItems = new ArrayList<>();
        listItems.add(item);
        Cart cart = new Cart();
        cart.setId(3L);
        cart.setItems(listItems);
        cart.setTotal(BigDecimal.ONE);
        cart.setUser(user);
        user.setCart(cart);
        when(userRepository.findByUsername("user")).thenReturn(user);
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Cart result = response.getBody();
        assertEquals(listItems, result.getItems());
        assertEquals(BigDecimal.valueOf(0), result.getTotal());
    }

  
}
