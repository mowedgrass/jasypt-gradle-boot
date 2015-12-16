package com.github.mowedgrass.jasyptgradleboot.config;

import com.github.mowedgrass.jasyptgradleboot.encryptor.EncryptorFactory;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.StandardEnvironment;

@Configuration
public class EnvironmentAwareEncryptor {

    @Bean
    @ConditionalOnMissingBean(StringEncryptor.class)
    public StringEncryptor stringEncryptor(StandardEnvironment environment) {
        return new EncryptorFactory().create(environment);
    }
}
