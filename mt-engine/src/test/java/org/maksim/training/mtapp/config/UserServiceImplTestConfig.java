package org.maksim.training.mtapp.config;

import org.maksim.training.mtapp.repository.UserRepository;
import org.maksim.training.mtapp.service.UserService;
import org.maksim.training.mtapp.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

@Configuration
@Import(ImUserRepositoryTestConfig.class)
public class UserServiceImplTestConfig {
    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    @Autowired
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }
}