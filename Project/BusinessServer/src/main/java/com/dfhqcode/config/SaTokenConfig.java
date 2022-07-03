package com.dfhqcode.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.util.Arrays;

/**
 * @author zxd3099
 * @create 2022-06-27-20:42
 */
@Configuration
@EnableWebMvc
public class SaTokenConfig implements WebMvcConfigurer {
    /**
     * 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        // 注册注解拦截器，并排除不需要注解鉴权的接口地址 (与登录拦截器无关)
        registry.addInterceptor(new SaRouteInterceptor((req, res, handler) -> {
            SaRouter.match(Arrays.asList("/**"), Arrays.asList(
                    "/favicon.ico",
                    "/swagger-resources/**",
                    "/v2/**",
                    "/doc.html",
                    "**/swagger-ui.html",
                    "/swagger-ui.html/**",
                    "/error",
                    "/sso/**",
                    "/webjars/**",
                    "/administrator/login",
                    "/administrator/register",
                    "/user/register",
                    "/user/login",
                    "/news/**",
                    "/userAction/**",
                    "/",
                    "/csrf"
            ), StpUtil::checkLogin);

            // 角色认证 -- 拦截以 administrator 开头的路由，必须具备 admin 角色才可以通过认证
            SaRouter.match("/administrator/task/**", r -> StpUtil.checkRole("admin"));
        }));
    }

    /**
     * 允许跨域请求访问
     */
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .maxAge(3600);
    }

    /**
     * 资源映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        // swagger
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        // webjars
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
