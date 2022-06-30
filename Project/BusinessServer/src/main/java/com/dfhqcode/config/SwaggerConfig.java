package com.dfhqcode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author zxd3099
 * @create 2022-06-26-20:16
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket docket(Environment environment)
    {
        Profiles of = Profiles.of("dev", "test");

        // 判断当前是否处于该环境
        // 通过 enable() 接收此参数判断是否要显示
        environment.acceptsProfiles(of);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select() // 通过.select()方法，去配置扫描接口
                .apis(RequestHandlerSelectors.basePackage("com.dfhqcode.controller")) // RequestHandlerSelectors配置如何扫描接口
                .build();

    }

    /**
     * 创建API信息
     */
    private ApiInfo apiInfo()
    {
        Contact contact = new Contact("dfhqcode", "https://www.dfhqcode.cn", "zxd3099@163.com");
        return new ApiInfo(
                "BUYess",
                "BUYess",
                "v1.0",
                "http://terms.service.url/组织链接",
                contact,
                "Apach 2.0 许可",
                "许可链接",
                new ArrayList<>()
        );
    }
}
