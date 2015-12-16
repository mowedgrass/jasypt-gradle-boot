package com.github.mowedgrass.jasyptgradleboot.encryptor;

import com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.ConfigurationFactory;
import com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.provider.ClassPathConfigurationProvider;
import com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.provider.ConfigurationProvider;
import com.github.mowedgrass.jasyptgradleboot.password.PasswordProvider;
import com.github.mowedgrass.jasyptgradleboot.password.PropertyPasswordProvider;
import com.github.mowedgrass.jasyptgradleboot.password.property.PropertyResolver;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.core.env.StandardEnvironment;

public class EncryptorFactory {

    public StringEncryptor create(StandardEnvironment environment) {
        PropertyResolver propertyResolver = new PropertyResolver(environment);
        PropertyPasswordProvider passwordProvider = new PropertyPasswordProvider(propertyResolver);
        ConfigurationProvider configurationProvider = new ClassPathConfigurationProvider();

        return create(passwordProvider, configurationProvider);
    }

    public StringEncryptor create(PasswordProvider passwordProvider, ConfigurationProvider configurationProvider) {
        ConfigurationFactory configurationFactory = new ConfigurationFactory(passwordProvider, configurationProvider);
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(configurationFactory.create());

        return encryptor;
    }
}
