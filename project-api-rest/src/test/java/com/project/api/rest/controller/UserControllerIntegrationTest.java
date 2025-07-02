package com.project.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.rest.model.User;
import com.project.api.rest.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /users - Should return list of users")
    void listUsers_ShouldReturnList() throws Exception {
        User user1 = new User(1L, "John", "Doe", "johndoe", "pass", "john@example.com", "12345", "NY", "USA");
        User user2 = new User(2L, "Jane", "Doe", "janedoe", "pass", "jane@example.com", "67890", "LA", "USA");

        when(userService.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /users/{id} - User found")
    void getUser_ShouldReturnUser_WhenFound() throws Exception {
        User user = new User(1L, "John", "Doe", "johndoe", "pass", "john@example.com", "12345", "NY", "USA");

        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /users/{id} - User not found")
    void getUser_ShouldThrow_WhenNotFound() throws Exception {
        when(userService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /users - Create user")
    void createUser_ShouldReturnCreatedUser() throws Exception {
        User user = new User(null, "New", "User", "newuser", "pass", "new@example.com", "55555", "City", "Country");
        User saved = new User(1L, user.getName(), user.getSurname(), user.getUsername(), user.getPassword(), user.getEmail(), user.getPhone(), user.getCity(), user.getCountry());

        when(userService.save(any(User.class))).thenReturn(saved);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PATCH /users/{id} - Partial update name")
    void patchUser_ShouldUpdateUserPartially() throws Exception {
        User existing = new User(1L, "johndoe", "pass",  "John", "Doe", "john@example.com", "12345", "NY", "USA");
        User update   = new User(null, null, null, "Johnny", null, null, null, null, null);
        User patched  = new User(1L, "johndoe", "pass", "Johnny", "Doe", "john@example.com", "12345", "NY", "USA");

        when(userService.findById(1L)).thenReturn(existing);
        when(userService.save(any(User.class))).thenReturn(patched);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Johnny"));
    }

    @Test
    @DisplayName("PUT /users/{id} - Full update")
    void updateUser_ShouldReplaceUser() throws Exception {
        User existing = new User(1L, "John", "Doe", "johndoe", "pass", "john@example.com", "12345", "NY", "USA");
        User update = new User(null, "Updated", "User", "updated", "newpass", "up@example.com", "00000", "City", "Country");
        User saved = new User(1L, update.getName(), update.getSurname(), update.getUsername(), update.getPassword(), update.getEmail(), update.getPhone(), update.getCity(), update.getCountry());

        when(userService.findById(1L)).thenReturn(existing);
        when(userService.save(any(User.class))).thenReturn(saved);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    @DisplayName("DELETE /users/{id} - Delete existing user")
    void deleteUser_ShouldReturnNoContent() throws Exception {
        User user = new User(1L, "John", "Doe", "johndoe", "pass", "john@example.com", "12345", "NY", "USA");

        when(userService.findById(1L)).thenReturn(user);
        Mockito.doNothing().when(userService).deleteById(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}