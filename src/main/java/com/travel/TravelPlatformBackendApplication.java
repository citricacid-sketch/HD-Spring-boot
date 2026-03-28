package com.travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class TravelPlatformBackendApplication {

	public static void main(String[] args) {
		// Get port from environment variable for cloud deployment (e.g., Render)
		String port = System.getenv("PORT");
		if (port != null) {
			System.setProperty("server.port", port);
		}
		SpringApplication.run(TravelPlatformBackendApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// CORS configuration for development and production
				// For production, set CORS_ALLOWED_ORIGINS environment variable
				// For development, allow local Vite servers
				String corsOrigins = System.getenv("CORS_ALLOWED_ORIGINS");
				if (corsOrigins == null || corsOrigins.trim().isEmpty()) {
					// Default for development
					corsOrigins = "http://localhost:5173,http://localhost:5174";
				}
				String[] allowedOrigins = corsOrigins.split(",");

				registry.addMapping("/api/**")
					.allowedOrigins(allowedOrigins)
					.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
					.allowedHeaders("*")
					.allowCredentials(true);
			}
		};
	}
}