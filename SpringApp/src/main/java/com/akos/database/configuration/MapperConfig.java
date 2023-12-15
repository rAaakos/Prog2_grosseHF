package com.akos.database.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up a ModelMapper bean.
 * This class provides configuration for creating and customizing the ModelMapper instance.
 */
@Configuration
public class MapperConfig {

    /**
     * Creates and configures a ModelMapper bean.
     * The ModelMapper bean is configured with a LOOSE matching strategy.
     *
     * @return A configured ModelMapper instance.
     */
    @Bean
    public ModelMapper modelMapper() {
        // Create a new ModelMapper instance
        ModelMapper modelMapper = new ModelMapper();

        // Set the matching strategy to LOOSE
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        // Return the configured ModelMapper instance
        return modelMapper;
    }
}

