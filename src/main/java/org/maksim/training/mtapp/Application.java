package org.maksim.training.mtapp;

import org.maksim.training.mtapp.config.PersistenceConfig;
import org.maksim.training.mtapp.config.SecurityConfig;
import org.maksim.training.mtapp.config.WebConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import java.util.Random;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan({
        "org.maksim.training.mtapp.model",
        "org.maksim.training.mtapp.service",
        "org.maksim.training.mtapp.aop"
})
@Import({
        PersistenceConfig.class,
        WebConfig.class,
        SecurityConfig.class
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Random random() {
        return new Random();
    }
}