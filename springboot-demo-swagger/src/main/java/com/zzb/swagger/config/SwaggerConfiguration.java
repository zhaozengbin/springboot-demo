package com.zzb.swagger.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

    private static final String API_NAME_ES = "es";

    private static final String API_NAME_HANLP = "hanlp";

    private static final String API_NAME_SENSITIVE = "sensitive";

    @Bean(value = API_NAME_ES + "Api")
    public Docket esApi() {
        return getDocket(API_NAME_ES);
    }

    @Bean(value = API_NAME_HANLP + "Api")
    public Docket hanlpApi() {
        return getDocket(API_NAME_HANLP);
    }

    @Bean(value = API_NAME_SENSITIVE + "Api")
    public Docket sensitiveApi() {
        return getDocket(API_NAME_SENSITIVE);
    }

    /**
     * 方法：getDocket
     * 描述：设置Docket
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param apiName :
     * @return : springfox.documentation.spring.web.plugins.Docket
     * @date: 2020年10月13日 1:49 下午
     */
    private Docket getDocket(String apiName) {

//        ParameterBuilder parameterBuilder = new ParameterBuilder();
        List<Parameter> parameters = Lists.newArrayList();
//        parameterBuilder.name("token").description("token令牌").modelRef(new ModelRef("String"))
//                .parameterType("header")
//                .required(true).build();
//        parameters.add(parameterBuilder.build());

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(apiName + " Demo RESTful APIs", apiName + " Demo RESTful APIs"))
                .groupName(apiName.toUpperCase())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zzb.examples.controller." + apiName))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(parameters)
                .securityContexts(Lists.newArrayList(securityContext())).securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey()));
        return docket;
    }

    private ApiInfo apiInfo(String title, String desc) {
        return new ApiInfoBuilder()
                .title(title)
                .description(desc)
                .termsOfServiceUrl("https://github.com/zhaozengbin/springboot-demo")
                .contact(new Contact("dabin", "http://dabin.mengxiang.show", "4415599@qq.com"))
                .version("1.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("BearerToken", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("BearerToken", authorizationScopes));
    }
}