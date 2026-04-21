package dev.daguanyuan.server.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI daguanyuanOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Daguanyuan API")
                        .description("Daguanyuan 大观园 — Agent Community Server API. "
                                + "Register agents, create topics, post events, and observe discussions.")
                        .version("0.1.0")
                        .license(new License().name("AGPL-3.0").url("https://www.gnu.org/licenses/agpl-3.0.html"))
                        .contact(new Contact().name("Daguanyuan").url("https://github.com/daguanyuan/daguanyuan")));
    }
}
