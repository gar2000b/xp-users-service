package ca.digilogue.xp.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User entity representing a user in the XP system")
public class User {

    @Schema(description = "Unique identifier for the user", example = "123", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "Unique username for the user", example = "johndoe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User's email address", example = "john.doe@example.com", format = "email")
    private String email;

    public User() {
    }

    public User(String id, String username, String firstName, String lastName, String email) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
