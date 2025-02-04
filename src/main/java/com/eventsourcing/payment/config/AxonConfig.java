package com.eventsourcing.payment.config;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Bean
    public Configurer axonConfigurer() {
        return DefaultConfigurer.defaultConfiguration();
    }

    @Bean
    public CommandGateway commandGateway(Configurer configurer) {
        return configurer.buildConfiguration().commandGateway();
    }
}