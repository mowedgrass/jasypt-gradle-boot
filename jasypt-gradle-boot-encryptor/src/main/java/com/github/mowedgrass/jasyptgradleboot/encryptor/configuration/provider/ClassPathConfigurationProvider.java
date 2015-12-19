package com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.provider;

public class ClassPathConfigurationProvider extends FileConfigurationProvider {

    public static final String CONFIGURATION_FILE = "jasypt.properties";

    public ClassPathConfigurationProvider() {
        super(ClassLoader.getSystemResourceAsStream(CONFIGURATION_FILE));
    }
}
