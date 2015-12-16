package com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.provider;

import com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.ConfigurationProperty;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileConfigurationProvider implements ConfigurationProvider {

    private final Properties properties = new Properties();

    public FileConfigurationProvider(InputStream stream) {
        if (stream != null) {
            try {
                properties.load(stream);
            } catch (IOException e) {
                throw new ConfigurationProviderException("Unable to read properties from stream", e);
            } finally {
                try {
                    stream.close();
                } catch (IOException ignored) {}
            }
        }
    }

    public String getProperty(ConfigurationProperty property) {
        return properties.getProperty(property.getName());
    }
}
