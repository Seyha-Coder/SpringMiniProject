package org.example.springminiproject;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Expense Tracking",
        version = "1.0",
        description = "Tracking your expenses is like shining a light on your financial path; it illuminates where your money goes, guiding you towards better financial decisions."))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer"
)
public class SpringMiniProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringMiniProjectApplication.class, args);
    }
}
