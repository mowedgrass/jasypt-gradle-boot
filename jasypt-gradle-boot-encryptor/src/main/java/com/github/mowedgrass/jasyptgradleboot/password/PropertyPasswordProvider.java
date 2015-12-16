package com.github.mowedgrass.jasyptgradleboot.password;

import com.github.mowedgrass.jasyptgradleboot.password.property.PropertyResolver;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

public class PropertyPasswordProvider extends ChainPasswordProvider {

    public static final String PASSWORD_PROPERTY = "jasypt.encryptor.password";
    public static final String SHORT_PASSWORD_PROPERTY = "jasypt.password";

    public static final String SYSTEM_PASSWORD_PROPERTY = "JASYPT_ENCRYPTOR_PASSWORD";
    public static final String SYSTEM_SHORT_PASSWORD_PROPERTY = "JASYPT_PASSWORD";

    private PropertyResolver propertyResolver;

    public PropertyPasswordProvider(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    protected List<Optional<String>> getCandidates() {
        return asList(
                propertyResolver.getEnvironmentProperty(PASSWORD_PROPERTY),
                propertyResolver.getEnvironmentProperty(SHORT_PASSWORD_PROPERTY),
                propertyResolver.getSystemProperty(SYSTEM_PASSWORD_PROPERTY),
                propertyResolver.getSystemProperty(SYSTEM_SHORT_PASSWORD_PROPERTY)
        );
    }
}
