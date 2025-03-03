package de.lbakker77.retracker.core.interceptor;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(operatorInterceptor());
    }

    @Bean
    public UserTimeZoneInterceptor operatorInterceptor() {
        return new UserTimeZoneInterceptor(userTimeZoneService());
    }

    @Bean
    @RequestScope
    public UserTimeZoneServiceImpl userTimeZoneService() {
        return new UserTimeZoneServiceImpl();
    }
}
