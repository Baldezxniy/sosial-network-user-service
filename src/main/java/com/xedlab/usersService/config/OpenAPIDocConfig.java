package com.xedlab.usersService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!prod")
@Configuration
public class OpenAPIDocConfig {

  @Value("${openapi-doc.info.title}")
  private String title;

  @Value("${openapi-doc.info.description}")
  private String description;

  @Value("${openapi-doc.info.version}")
  private String version;

  @Value("${openapi-doc.info.contact.name}")
  private String contactName;

  @Value("${openapi-doc.info.contact.email}")
  private String contactEmil;

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
            .info(
                    new Info()
                            .title(title)
                            .description(description)
                            .version(version)
                            .contact(
                                    new Contact()
                                            .name(contactName)
                                            .email(contactEmil)
                            )
            );
  }
}
