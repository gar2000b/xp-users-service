package ca.digilogue.xp.controller;

import ca.digilogue.xp.App;
import ca.digilogue.xp.model.Info;
import ca.digilogue.xp.model.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @GetMapping("/test")
    public Test getTestMessage() {
        return new Test("XP Users Service is alive!");
    }

    @GetMapping("/info")
    public Info getServiceInfo() {
        return new Info("XP Users Service", App.version);
    }


}