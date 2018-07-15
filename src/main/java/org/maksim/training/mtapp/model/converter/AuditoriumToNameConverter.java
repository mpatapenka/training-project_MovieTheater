package org.maksim.training.mtapp.model.converter;

import lombok.extern.slf4j.Slf4j;
import org.maksim.training.mtapp.model.Auditorium;
import org.maksim.training.mtapp.service.AuditoriumService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Converter
@Component
@Slf4j
public final class AuditoriumToNameConverter
        implements AttributeConverter<Auditorium, String>, ApplicationContextAware {
    private final Lock lock = new ReentrantLock();

    private static ApplicationContext applicationContext;
    private volatile AuditoriumService auditoriumService;

    @Autowired
    void setAuditoriumService(AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }

    @Override
    public String convertToDatabaseColumn(Auditorium auditorium) {
        return auditorium != null ? auditorium.getName() : null;
    }

    @Override
    public Auditorium convertToEntityAttribute(String name) {
        return getAuditoriumService().getByName(name);
    }

    private AuditoriumService getAuditoriumService() {
        if (auditoriumService == null) {
            lock.lock();
            try {
                if (auditoriumService == null) {
                    applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
                }
            } finally {
                lock.unlock();
            }
            log.debug("AuditoriumService injected into converter: {}.", auditoriumService != null);
        }
        return auditoriumService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AuditoriumToNameConverter.applicationContext = applicationContext;
    }
}