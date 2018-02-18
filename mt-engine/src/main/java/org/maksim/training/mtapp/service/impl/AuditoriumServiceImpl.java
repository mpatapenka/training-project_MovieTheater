package org.maksim.training.mtapp.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.maksim.training.mtapp.entity.Auditorium;
import org.maksim.training.mtapp.service.AuditoriumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuditoriumServiceImpl implements AuditoriumService {
    private static final char PROPERTY_NAME_SEPARATOR = '.';
    private static final String AUDITORIUM_NAME_PROPERTY = "name";
    private static final String AUDITORIUM_SEATSCOUNT_PROPERTY = "seatscount";
    private static final String AUDITORIUM_VIPSEATS_PROPERTY = "vipseats";

    private static final Splitter COMMA_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();

    private final Map<String, Auditorium> storage = Maps.newHashMap();
    private final String auditoriumsDir;

    @Autowired
    public AuditoriumServiceImpl(@Value("${auditoriums.dir}") String auditoriumsDir) {
        this.auditoriumsDir = auditoriumsDir;
    }

    @PostConstruct
    public void init() {
        Properties auditoriumsProperties = readAuditoriumsDirectory();
        auditoriumsProperties.stringPropertyNames().stream()
                .map(name -> name.substring(0, name.lastIndexOf(PROPERTY_NAME_SEPARATOR)))
                .map(name -> buildAuditorium(name, auditoriumsProperties))
                .forEach(auditorium -> storage.put(auditorium.getName(), auditorium));
        log.debug("Initialized auditoriums: {}", storage);
    }

    @Override
    public Auditorium getByName(String name) {
        return storage.get(name);
    }

    @Override
    public Collection<Auditorium> getAll() {
        return Lists.newArrayList(storage.values());
    }

    @SneakyThrows
    private Properties readAuditoriumsDirectory() {
        URL auditoriumSystemResource = ClassLoader.getSystemResource(auditoriumsDir);
        Preconditions.checkNotNull(auditoriumSystemResource, "Directory " + auditoriumsDir + " not found.");
        Path auditoriumsPath = Paths.get(auditoriumSystemResource.toURI());
        try {
            Properties properties = new Properties();
            Files.walk(auditoriumsPath)
                    .filter(p -> p.toString().endsWith(".properties"))
                    .forEach(p -> {
                        try (InputStream is = Files.newInputStream(p)) {
                            properties.load(is);
                        } catch (IOException e) {
                            log.warn("Property file '" + p + "' can't be read, so skip it.", e);
                        }
                    });
            return properties;
        } catch (IOException e) {
            log.error("Properties weren't be loaded, properties directory: {}", auditoriumsPath);
            throw new RuntimeException("Check properties directory configuration for auditoriums.", e);
        }
    }

    private Auditorium buildAuditorium(String name, Properties properties) {
        String nameProperty = properties.getProperty(name + PROPERTY_NAME_SEPARATOR + AUDITORIUM_NAME_PROPERTY);
        String numberOfSeatsProperty = properties.getProperty(name + PROPERTY_NAME_SEPARATOR + AUDITORIUM_SEATSCOUNT_PROPERTY);
        String vipSeatsProperty = properties.getProperty(name + PROPERTY_NAME_SEPARATOR + AUDITORIUM_VIPSEATS_PROPERTY);

        Set<Integer> vipSeats = COMMA_SPLITTER.splitToList(vipSeatsProperty).stream()
                .map(Integer::valueOf).collect(Collectors.toSet());

        return Auditorium.builder()
                .name(nameProperty)
                .numberOfSeats(Integer.parseInt(numberOfSeatsProperty))
                .vipSeats(vipSeats)
                .build();
    }
}