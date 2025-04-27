package uz.abduraxim.LearningSystem.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfiguration {

    @Value("${server.port}")
    String SERVER_PORT;

    @Bean
    public OpenAPI customOpenAPI() {
        // general info
        Info info = new Info()
                .title("Learning system")
                .version("1.0.0")
                .description("Quyida Learning system loyihasi uchun API hujjatlar tagdim qilingan.")
                .contact(new Contact()
                        .name("Abduraxim")
                        .email("abdurakhim.tursunboyev@gmail.com")
                        .url("https://abduraxim.uz")
                );
        Server server1 = new Server()
                .description("Local")
                .url("http://localhost:" + SERVER_PORT);

        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("bearerAuth");

        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setName("bearerAuth");
        securityScheme.setType(SecurityScheme.Type.HTTP);
        securityScheme.bearerFormat("JWT");
        securityScheme.setIn(SecurityScheme.In.HEADER);
        securityScheme.setScheme("bearer");

        Components components = new Components();
        components.addSecuritySchemes("bearerAuth", securityScheme);

        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(info);
        openAPI.setServers(List.of(server1));
        openAPI.setSecurity(List.of(securityRequirement));
        openAPI.components(components);

        return openAPI;
    }
}
