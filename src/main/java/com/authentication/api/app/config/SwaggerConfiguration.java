package com.authentication.api.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The Swagger configuration.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    /**
     * Authentication api docket.
     *
     * @return the docket
     */
    @Bean
    public Docket authenticationApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    /**
     * Define Swagger basic.
     *
     * @return Api info
     */
    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Authentication API with Jwt")
                .version("V.0.1-alpha")
                .description("API to authenticate users with Spring boot and Jwt")
                .contact(new Contact("Edwin Lopez", "http://github.com/EdwinLopez12", "edwin.lopezb.1297@email.com"))
                .license("MIT License")
                .build();
    }
}
