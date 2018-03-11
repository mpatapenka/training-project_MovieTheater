package org.maksim.training.mtapp.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Random;

@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
@ComponentScan({
        "org.maksim.training.mtapp.entity.converter",
        "org.maksim.training.mtapp.service",
        "org.maksim.training.mtapp.web",
        "org.maksim.training.mtapp.aop"
})
@Import(PersistenceConfig.class)
public class AppConfig {
    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Random random() {
        return new Random();
    }
}