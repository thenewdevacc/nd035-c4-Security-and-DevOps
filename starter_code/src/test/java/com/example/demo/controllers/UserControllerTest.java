package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserControllerTest {
    
    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController() ;
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void findById() {
        Long id = 1L;
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(id);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        user = response.getBody();
        assertEquals("user", user.getUsername());
        assertEquals("password", user.getPassword());
    }

    @Test
    public void findByUsername() {
        String username = "user";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setId(1L);
        when(userRepository.findByUsername(username)).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName(username);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        user = response.getBody();
        assertEquals("user", user.getUsername());
        assertEquals("password", user.getPassword());
    }

    @Test
    public void findByUsernameFail() {
        String username = "user";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setId(1L);
        when(userRepository.findByUsername(username)).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("admin");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void create_user(){
        CreateUserRequest userRequest = TestUtils.getAUserRequest();
        final ResponseEntity<User> response =  userController.createUser(userRequest);
        User user = response.getBody();
        assertNotNull(response);
        assertNotNull(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, user.getId());
        assertEquals("username", user.getUsername());
    }

    @Test
    public void createUserFail() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("small");
        createUserRequest.setConfirmPassword("big");
        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
