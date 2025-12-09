package ca.digilogue.controller;

import ca.digilogue.model.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public Test getTestMessage() {
        return new Test("XP Users Service is alive!");
    }
}