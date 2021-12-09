package com.picpay.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfiguration {
    @Bean
    open fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
        .apiInfo(getApiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.picpay.controller"))
        .paths(PathSelectors.any())
        .build()

    private fun getApiInfo(): ApiInfo {
        val contact = Contact("Igor Souza", "igorsouza.###", "igorsouza@######.com")
        return ApiInfoBuilder()
            .title("Payment Wallet")
            .description("Payment Wallet")
            .version("1.0.0")
            .contact(contact)
            .build()
    }
}
