package com.github.mowedgrass.jasyptgradleboot.encryptor.configuration;

import com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.provider.ConfigurationProvider;
import com.github.mowedgrass.jasyptgradleboot.password.PasswordProvider;
import org.jasypt.encryption.pbe.config.PBEConfig;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

import static com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.ConfigurationProperty.*;
import static java.util.Optional.ofNullable;

public class ConfigurationFactory {

    private PasswordProvider passwordProvider;
    private ConfigurationProvider configurationProvider;

    public ConfigurationFactory(PasswordProvider passwordProvider, ConfigurationProvider configurationProvider) {
        this.passwordProvider = passwordProvider;
        this.configurationProvider = configurationProvider;
    }

    public PBEConfig create() {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword(passwordProvider.getPassword());

        config.setAlgorithm(getValue(ALGORITHM));
        config.setKeyObtentionIterations(getValue(KEY_OBTENTION_ITERATIONS));
        config.setPoolSize(getValue(POOL_SIZE));
        config.setProviderName(getValue(PROVIDER_NAME));
        config.setSaltGeneratorClassName(getValue(SALT_GENERATOR));
        config.setStringOutputType(getValue(OUTPUT_TYPE));

        return config;
    }

    private String getValue(ConfigurationProperty property) {
        return ofNullable(configurationProvider.getProperty(property))
                .orElse(property.getDefaultValue());
    }
}
