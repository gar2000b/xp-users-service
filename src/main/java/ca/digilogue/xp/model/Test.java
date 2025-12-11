package ca.digilogue.xp.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Simple test response message for health check endpoints")
public class Test {
    
    @Schema(description = "Test message indicating service status", example = "XP Users Service is alive!")
    private String message;

    public Test(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}