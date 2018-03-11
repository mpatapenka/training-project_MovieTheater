package org.maksim.training.mtapp.config;

import org.maksim.training.mtapp.repository.UserRepository;
import org.maksim.training.mtapp.repository.inmemory.ImUserRepository;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ImUserRepositoryTestConfig {
    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public UserRepository userRepository() {
        return new ImUserRepository();
    }
}