package ca.digilogue.xp.config;

import ca.digilogue.xp.App;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

@Configuration
public class OpenApiConfig {

    @Autowired(required = false)
    private ApplicationContext applicationContext;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("XP Users Service API")
                        .description("REST API for managing users in the XP system. " +
                                "This microservice provides endpoints for creating, reading, updating, and deleting user records.")
                        .version(getApiVersion())
                        .contact(new Contact()
                                .name("XP Development Team")
                                .email("support@digilogue.ca")
                                .url("https://github.com/gar2000b/xp-users-service"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://digilogue.ca")));
    }

    private String getApiVersion() {
        // Try to get version from BuildProperties first (when running from JAR)
        if (applicationContext != null) {
            try {
                BuildProperties buildProperties = applicationContext.getBean(BuildProperties.class);
                return buildProperties.getVersion();
            } catch (NoSuchBeanDefinitionException ignored) {
                // BuildProperties not available (e.g., running from IDE)
            }
        }

        // Fallback to App.version if set
        if (App.version != null && !App.version.isEmpty()) {
            return App.version;
        }

        // Last resort fallback
        return "0.0.23-SNAPSHOT";
    }
}

