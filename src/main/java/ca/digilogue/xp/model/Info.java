package ca.digilogue.xp.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Service information containing application name, version, and instance identifier")
public class Info {
    
    @Schema(description = "Name of the application", example = "XP Users Service")
    private String appName;
    
    @Schema(description = "Current version of the application", example = "0.0.23-SNAPSHOT")
    private String version;
    
    @Schema(description = "Unique identifier for this service instance", example = "xp-users-service-1")
    private String instanceId;

    public Info(String appName, String version, String instanceId) {
        this.appName = appName;
        this.version = version;
        this.instanceId = instanceId;
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

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}