package ca.digilogue.xp.controller;

import ca.digilogue.xp.App;
import ca.digilogue.xp.model.Info;
import ca.digilogue.xp.model.Test;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Service Info", description = "Service information and health check endpoints")
public class InfoController {

    private static final Logger log = LoggerFactory.getLogger(InfoController.class);

    @Operation(
            summary = "Health check endpoint",
            description = "Returns a simple test message to verify the service is running and responding"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Service is alive",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Test.class))
    )
    @GetMapping("/test")
    public ResponseEntity<Test> getTestMessage() {
        Test response = new Test("XP Users Service is alive!");

        // Log JSON-style representation
        log.info("GET /test JSON Response: {{\"message\":\"{}\"}}", response.getMessage());

        return ResponseEntity.ok(response); // 200 OK
    }

    @Operation(
            summary = "Get service information",
            description = "Returns service metadata including the service name, current version, and instance identifier"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Service information retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Info.class))
    )
    @GetMapping("/info")
    public ResponseEntity<Info> getServiceInfo() {
        Info response = new Info("XP Users Service", App.version, App.instanceId);

        // Log JSON-style representation
        log.info(
                "GET /info JSON Response: {{\"appName\":\"{}\",\"version\":\"{}\",\"instanceId\":\"{}\"}}",
                response.getAppName(),
                response.getVersion(),
                response.getInstanceId()
        );

        return ResponseEntity.ok(response); // 200 OK
    }
}