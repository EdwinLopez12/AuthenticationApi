package com.authentication.api;

import com.authentication.api.app.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * The Authentication api application.
 */
@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class AuthenticationApiApplication {

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApiApplication.class, args);
	}

}
