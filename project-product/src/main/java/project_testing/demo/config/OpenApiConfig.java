package project_testing.demo.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI productApiDoc() {
        return new OpenAPI()
            .info(new Info()
        .title("Product Mamajemen API Documentations")
        .description("Test Deskripsi API nih...")
        .version("1.0.0"));
    }

}
