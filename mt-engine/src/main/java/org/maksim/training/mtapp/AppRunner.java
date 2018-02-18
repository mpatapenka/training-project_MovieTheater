package org.maksim.training.mtapp;

import org.maksim.training.mtapp.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppRunner {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(AppConfig.class);
    }
}
