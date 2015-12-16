package com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.provider;

import java.io.File;

public class ClassPathConfigurationProvider extends FileConfigurationProvider {

    public static final String CONFIGURATION_FILE = "jasypt.properties";

    public ClassPathConfigurationProvider() {
        super(ClassLoader.getSystemResourceAsStream(File.separator + CONFIGURATION_FILE));
    }
}
