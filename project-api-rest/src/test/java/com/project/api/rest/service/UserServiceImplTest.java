package com.project.api.rest.service;

import com.project.api.rest.exception.UserNotFoundException;
import com.project.api.rest.model.User;
import com.project.api.rest.repository.UserRepository;
import com.project.api.rest.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setPassword("plaintext");
        user.setEmail("test@example.com");
    }

    @Test
    void testFindAll() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void testFindById_WhenFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertNotNull(result);
        assertEquals("Test", result.getName());
        verify(userRepository).findById(1L);
    }

    @Test
    void testFindById_WhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
        verify(userRepository).findById(1L);
    }

    @Test
    void testSave_ShouldEncryptPassword() {
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.save(user);

        verify(userRepository).save(userCaptor.capture());
        String encryptedPassword = userCaptor.getValue().getPassword();

        assertNotEquals("plaintext", encryptedPassword);
        assertTrue(new BCryptPasswordEncoder().matches("plaintext", encryptedPassword));
    }

    @Test
    void testDeleteById() {
        userService.deleteById(1L);
        verify(userRepository).deleteById(1L);
    }
}
