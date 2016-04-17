package org.cdrolet.cdirect;

import com.google.gson.Gson;
import org.cdrolet.cdirect.interceptor.NotificationValidationInterceptor;
import org.cdrolet.cdirect.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by c on 4/15/16.
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {


    @Autowired
    private AuthorizationService authService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
                new NotificationValidationInterceptor(authService, parser()));
    }


    @Bean
    public Gson parser() {
        return new Gson();
    }

}
