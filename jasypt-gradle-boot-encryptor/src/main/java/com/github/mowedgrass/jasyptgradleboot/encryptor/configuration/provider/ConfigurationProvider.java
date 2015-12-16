package com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.provider;

import com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.ConfigurationProperty;

public interface ConfigurationProvider {

    public String getProperty(ConfigurationProperty property);
}
