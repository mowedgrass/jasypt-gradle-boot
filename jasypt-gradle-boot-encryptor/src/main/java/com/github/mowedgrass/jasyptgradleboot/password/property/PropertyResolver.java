package com.github.mowedgrass.jasyptgradleboot.password.property;

import org.springframework.core.env.StandardEnvironment;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

public class PropertyResolver {

    private StandardEnvironment environment;

    public PropertyResolver(StandardEnvironment environment) {
        this.environment = environment;
    }

    public Optional<String> getEnvironmentProperty(String key) {
        return getProperty(environment::getProperty, key);
    }

    public Optional<String> getSystemProperty(String key) {
        return getProperty(
                property -> ((String) environment.getSystemEnvironment().get(property)),
                key
        );
    }

    private Optional<String> getProperty(Function<String, String> source, String key) {
        return ofNullable(source.apply(key))
                .filter(s -> !s.isEmpty());
    }
}
