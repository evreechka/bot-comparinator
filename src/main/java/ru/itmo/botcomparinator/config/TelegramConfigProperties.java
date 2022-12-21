package ru.itmo.botcomparinator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("telegram")
@Getter
@Setter
public class TelegramConfigProperties {
    String url;
    String botName;
    String username;
    String botToken;
}
