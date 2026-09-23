package Rastoder.SchoolManagementSystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI schoolManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("School Management System REST API")
                        .description("Domain-driven enterprise backend service managing students, teachers, parents, and academic groups. " +
                                "Architectural highlights: immutability enforced through Java Records, strict layer isolation, " +
                                "and 100% MockMvc controller slice test coverage.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Omar Rastoder")
                                .url("https://github.com/https://github.com/omarastoder-create"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}