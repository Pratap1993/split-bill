package com.chagu.splitbill.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*


@Configuration
@EnableSwagger2
class SwaggerConfiguration {

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2).select()
            .apis(RequestHandlerSelectors.basePackage("com.chagu.splitbill")).paths(PathSelectors.any()).build()
            .apiInfo(apiInformation())
    }

    private fun apiInformation(): ApiInfo? {
        return ApiInfo(
            "SplitBill Application", "Internal SplitBill Application developed by Pratap Bhanu", "1.0.0",
            "Free To User",
            Contact("Pratap Bhanu's Github", "https://github.com/Pratap1993/", "dhal.pratapbhanu@gmail.com"),
            "API Lincense", "www.chagu4developers.com", Collections.emptyList()
        )
    }

}