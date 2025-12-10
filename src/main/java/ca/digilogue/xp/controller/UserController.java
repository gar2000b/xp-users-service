package ca.digilogue.xp.controller;

import ca.digilogue.xp.model.User;
import ca.digilogue.xp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Received GET /users");

        List<User> users = userService.getAllUsers();

        log.info("GET /users → {} users returned", users.size());

        return ResponseEntity.ok(users); // 200 OK with JSON array of User
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        log.info("Received GET /users/{}", id);

        User user = userService.getUserById(id);

        if (user != null) {
            log.info("GET /users/{} → Found: {{\"username\":\"{}\"}}", id, user.getUsername());
            return ResponseEntity.ok(user);
        }

        log.warn("GET /users/{} → Not Found", id);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Received POST /users with username: {}", user.getUsername());

        User createdUser = userService.createUser(user);

        log.info("POST /users → Created user with id: {} and username: {}", 
                createdUser.getId(), createdUser.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser); // 201 Created
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        log.info("Received PUT /users/{} with username: {}", id, user.getUsername());

        // Set the ID from the path parameter to ensure it matches
        user.setId(id);

        User updatedUser = userService.updateUser(user);

        if (updatedUser != null) {
            log.info("PUT /users/{} → Updated user with username: {}", id, updatedUser.getUsername());
            return ResponseEntity.ok(updatedUser); // 200 OK
        }

        log.warn("PUT /users/{} → Not Found", id);
        return ResponseEntity.notFound().build(); // 404 Not Found
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<User> partialUpdateUser(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        log.info("Received PATCH /users/{} with fields: {}", id, updates.keySet());

        User updatedUser = userService.partialUpdateUser(id, updates);

        if (updatedUser != null) {
            log.info("PATCH /users/{} → Updated user with username: {}", id, updatedUser.getUsername());
            return ResponseEntity.ok(updatedUser); // 200 OK
        }

        log.warn("PATCH /users/{} → Not Found", id);
        return ResponseEntity.notFound().build(); // 404 Not Found
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.info("Received DELETE /users/{}", id);

        boolean deleted = userService.deleteUser(id);

        if (deleted) {
            log.info("DELETE /users/{} → Deleted successfully", id);
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        log.warn("DELETE /users/{} → Not Found", id);
        return ResponseEntity.notFound().build(); // 404 Not Found
    }
}
