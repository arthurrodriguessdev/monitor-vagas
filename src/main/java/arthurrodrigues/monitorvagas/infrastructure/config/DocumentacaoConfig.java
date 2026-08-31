package arthurrodrigues.monitorvagas.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentacaoConfig {
    @Bean
    public OpenAPI openAPICustom(){
        return new OpenAPI()
                .info(new Info()
                        .title("Monitor de Vagas API")
                        .description("API para monitoramento e busca de vagas de emprego")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Arthur Rodrigues Ramos")
                                .url("https://github.com/arthurrodriguessdev")));
    }
}
