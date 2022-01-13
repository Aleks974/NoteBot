package ru.notebot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.notebot.service.util.ApiUriHelper;

@Configuration
public class WebConfig {

    @Bean
    public ApiUriHelper uriHelper() {
        return new ApiUriHelper("http://localhost:8080");
    }
}
