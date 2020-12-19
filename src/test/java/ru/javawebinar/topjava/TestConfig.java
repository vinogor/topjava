package ru.javawebinar.topjava;

import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class TestConfig {

    @Bean
    @Primary
    public CacheManager cacheManager() {
        return new NoOpCacheManager();
    }

    // TODO: не получается засетить пропертю
    @Bean
    @Primary
    public PropertyOverrideConfigurer propertyOverrideConfigurer() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:test.properties");

        PropertyOverrideConfigurer configurer = new PropertyOverrideConfigurer();
        configurer.setLocation(resource);
        configurer.setLocalOverride(true);

        return configurer;
    }

}
