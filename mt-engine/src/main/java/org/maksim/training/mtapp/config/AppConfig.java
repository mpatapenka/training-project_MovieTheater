package org.maksim.training.mtapp.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import java.util.Random;

@Configuration
@EnableAspectJAutoProxy
@PropertySource("classpath:application.properties")
@ComponentScan({
        "org.maksim.training.mtapp.repository.inmemory",
        "org.maksim.training.mtapp.service",
        "org.maksim.training.mtapp.aop"
})
public class AppConfig {
    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Random random() {
        return new Random();
    }
}