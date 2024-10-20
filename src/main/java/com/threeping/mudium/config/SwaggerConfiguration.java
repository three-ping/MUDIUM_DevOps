package com.threeping.mudium.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title="MUDIUM API 명세서", description = "MUDIUM API 명세서"))
@Configuration
public class SwaggerConfiguration {

}
