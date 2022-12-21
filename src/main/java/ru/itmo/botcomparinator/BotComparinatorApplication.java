package ru.itmo.botcomparinator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@PropertySource(value = "classpath:application.properties")
public class BotComparinatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotComparinatorApplication.class, args);
    }

}
