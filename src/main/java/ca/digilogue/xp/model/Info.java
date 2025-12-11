package ca.digilogue.xp.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Service information containing application name and version")
public class Info {
    
    @Schema(description = "Name of the application", example = "XP Users Service")
    private String appName;
    
    @Schema(description = "Current version of the application", example = "0.0.23-SNAPSHOT")
    private String version;

    public Info(String appName, String version) {
        this.appName = appName;
        this.version = version;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}