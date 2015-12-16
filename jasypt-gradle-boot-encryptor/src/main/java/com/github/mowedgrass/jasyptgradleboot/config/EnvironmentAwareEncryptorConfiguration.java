package com.github.mowedgrass.jasyptgradleboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(EnvironmentAwareEncryptor.class)
public class EnvironmentAwareEncryptorConfiguration {
}
