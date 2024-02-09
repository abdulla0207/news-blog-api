package com.company.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

@Configuration
@ConditionalOnProperty(value = "springfox.documentation.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerConfig {
    @Value("${server.host}")
    private String url;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(url);
        devServer.setDescription("Server URL");

        Contact contact = new Contact();
        contact.setEmail("abdulla.ermatov0407@gmail.com");
        contact.setName("Abdulla");
        contact.setUrl("https://www.abuermatov.com");


        Info info = new Info()
                .title("Newsblog.uz News API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage articles.")
                .termsOfService("https://www.abuermatov.com/terms")
                .license(null);

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
