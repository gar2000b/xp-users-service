package ca.digilogue.xp.controller;

import ca.digilogue.xp.model.User;
import ca.digilogue.xp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Users", description = "User management endpoints for creating, reading, updating, and deleting user records")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieves a list of users. Can optionally filter by firstName and/or lastName. " +
                    "If no filters are provided, returns all users."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved users",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))
            )
    })
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(
            @Parameter(description = "Filter by first name (optional)", example = "John")
            @RequestParam(required = false) String firstName,
            @Parameter(description = "Filter by last name (optional)", example = "Doe")
            @RequestParam(required = false) String lastName) {
        
        if ((firstName == null || firstName.trim().isEmpty()) && 
            (lastName == null || lastName.trim().isEmpty())) {
            log.info("Received GET /users (no filters)");
            List<User> users = userService.getAllUsers();
            log.info("GET /users → {} users returned", users.size());
            return ResponseEntity.ok(users);
        }

        log.info("Received GET /users with filters - firstName: {}, lastName: {}", firstName, lastName);
        
        List<User> users = userService.getUsersByFilters(firstName, lastName);
        
        log.info("GET /users → {} users returned", users.size());
        
        return ResponseEntity.ok(users); // 200 OK with JSON array of User
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a specific user by their unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "User ID", required = true, example = "123")
            @PathVariable String id) {
        log.info("Received GET /users/{}", id);

        User user = userService.getUserById(id);

        if (user != null) {
            log.info("GET /users/{} → Found: {{\"username\":\"{}\"}}", id, user.getUsername());
            return ResponseEntity.ok(user);
        }

        log.warn("GET /users/{} → Not Found", id);
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user record. The user ID will be automatically generated if not provided."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid user data provided"
            )
    })
    @PostMapping("/users")
    public ResponseEntity<User> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User object to be created",
                    required = true,
                    content = @Content(schema = @Schema(implementation = User.class))
            )
            @RequestBody User user) {
        log.info("Received POST /users with username: {}", user.getUsername());

        User createdUser = userService.createUser(user);

        log.info("POST /users → Created user with id: {} and username: {}", 
                createdUser.getId(), createdUser.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser); // 201 Created
    }

    @Operation(
            summary = "Update an existing user",
            description = "Updates an existing user by ID. This is a full update - all fields must be provided. " +
                    "The ID in the path parameter will override any ID in the request body."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid user data provided"
            )
    })
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "User ID", required = true, example = "123")
            @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Complete user object with updated information",
                    required = true,
                    content = @Content(schema = @Schema(implementation = User.class))
            )
            @RequestBody User user) {
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

    @Operation(
            summary = "Partially update a user",
            description = "Partially updates an existing user by ID. Only the fields provided in the request body will be updated. " +
                    "This allows for selective field updates without requiring all fields."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid update data provided"
            )
    })
    @PatchMapping("/users/{id}")
    public ResponseEntity<User> partialUpdateUser(
            @Parameter(description = "User ID", required = true, example = "123")
            @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Map of fields to update (only provided fields will be updated)",
                    required = true,
                    content = @Content(schema = @Schema(example = "{\"firstName\":\"John\",\"email\":\"john@example.com\"}"))
            )
            @RequestBody Map<String, Object> updates) {
        log.info("Received PATCH /users/{} with fields: {}", id, updates.keySet());

        User updatedUser = userService.partialUpdateUser(id, updates);

        if (updatedUser != null) {
            log.info("PATCH /users/{} → Updated user with username: {}", id, updatedUser.getUsername());
            return ResponseEntity.ok(updatedUser); // 200 OK
        }

        log.warn("PATCH /users/{} → Not Found", id);
        return ResponseEntity.notFound().build(); // 404 Not Found
    }

    @Operation(
            summary = "Delete a user",
            description = "Deletes a user by their unique identifier. This operation is permanent."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", required = true, example = "123")
            @PathVariable String id) {
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
