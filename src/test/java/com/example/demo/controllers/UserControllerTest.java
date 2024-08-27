package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_user_happy_path() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("ThisIsUnitTest");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(createUserRequest.getUsername(), user.getUsername());
        assertEquals("ThisIsUnitTest", user.getPassword());
    }

    @Test
    public void create_user_short_password() {
        when(bCryptPasswordEncoder.encode("test")).thenReturn("short");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("test");
        createUserRequest.setConfirmPassword("test");

        ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    public void create_user_confirm_password_not_the_same_with_password() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("ThisIsUnitTest");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("test");

        ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    public void find_by_id_successfully() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        ResponseEntity<User> createResponse = userController.createUser(createUserRequest);
        User userCreated = createResponse.getBody();
        assertNotNull(createResponse);
        assertNotNull(userCreated);
        assertEquals(200, createResponse.getStatusCode().value());

        when(userRepository.findById(userCreated.getId())).thenReturn(Optional.of(userCreated));

        ResponseEntity<User> response = userController.findById(userCreated.getId());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(userCreated.getId(), user.getId());
        assertEquals("test", user.getUsername());
    }

    @Test
    public void find_by_username_successfully() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        ResponseEntity<User> createResponse = userController.createUser(createUserRequest);
        User userCreated = createResponse.getBody();
        assertNotNull(createResponse);
        assertNotNull(userCreated);
        assertEquals(200, createResponse.getStatusCode().value());

        when(userRepository.findByUsername(userCreated.getUsername())).thenReturn(userCreated);

        ResponseEntity<User> response = userController.findByUserName(userCreated.getUsername());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(userCreated.getUsername(), user.getUsername());
        assertEquals("test", user.getUsername());
    }

    @Test
    public void find_by_username_fail() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        ResponseEntity<User> createResponse = userController.createUser(createUserRequest);
        User userCreated = createResponse.getBody();
        assertNotNull(createResponse);
        assertNotNull(userCreated);
        assertEquals(200, createResponse.getStatusCode().value());

        when(userRepository.findByUsername(any())).thenReturn(any());

        ResponseEntity<User> response = userController.findByUserName(userCreated.getUsername());
        assertEquals(404, response.getStatusCode().value());

    }

}
