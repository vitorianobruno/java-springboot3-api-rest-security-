package com.project.api.rest.controller;

import com.project.api.rest.exception.UserNotFoundException;
import com.project.api.rest.model.User;
import com.project.api.rest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users); // 200 OK
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.save(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED); // 201 Created
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            throw new UserNotFoundException("User not found with ID " + id);
        }
        return ResponseEntity.ok(user); // 200 OK
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User existingUser = userService.findById(id);
        if (existingUser == null) {
            throw new UserNotFoundException("User not found with ID " + id);
        }

        // Merge non-null fields from updatedUser into existingUser
        if (updatedUser.getName() != null) existingUser.setName(updatedUser.getName());
        if (updatedUser.getSurname() != null) existingUser.setSurname(updatedUser.getSurname());
        if (updatedUser.getUsername() != null) existingUser.setUsername(updatedUser.getUsername());
        if (updatedUser.getPassword() != null) existingUser.setPassword(updatedUser.getPassword());
        if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getPhone() != null) existingUser.setPhone(updatedUser.getPhone());
        if (updatedUser.getCity() != null) existingUser.setCity(updatedUser.getCity());
        if (updatedUser.getCountry() != null) existingUser.setCountry(updatedUser.getCountry());

        User savedUser = userService.save(existingUser);
        return ResponseEntity.ok(savedUser); // 200 OK
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User existingUser = userService.findById(id);
        if (existingUser == null) {
            throw new UserNotFoundException("User not found with ID " + id);
        }
        updatedUser.setId(id);
        User savedUser = userService.save(updatedUser);
        return ResponseEntity.ok(savedUser); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            throw new UserNotFoundException("User not found with ID " + id);
        }
        userService.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
