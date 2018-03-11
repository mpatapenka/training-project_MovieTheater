package org.maksim.training.mtapp.config;

import org.maksim.training.mtapp.repository.EventRepository;
import org.maksim.training.mtapp.service.CounterService;
import org.maksim.training.mtapp.service.EventService;
import org.maksim.training.mtapp.service.impl.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(value = {
        "org.maksim.training.mtapp.repository.inmemory",
        "org.maksim.training.mtapp.service.impl",
        "org.maksim.training.mtapp.aop"
}, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        EventService.class,
        CounterService.class
}))
@Import(DiscountStrategyTestConfig.class)
public class AopTestConfig {
    @Bean
    public CounterService mockCounterService() {
        return mock(CounterService.class);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    @Autowired
    public EventService eventService(EventRepository eventRepository) {
        return new EventServiceImpl(eventRepository);
    }

    @Bean
    public Random spiedRandom() {
        return spy(new Random());
    }
}