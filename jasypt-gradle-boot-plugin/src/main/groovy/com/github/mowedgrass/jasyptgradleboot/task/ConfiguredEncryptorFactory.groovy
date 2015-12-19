package com.github.mowedgrass.jasyptgradleboot.task
import com.github.mowedgrass.jasyptgradleboot.encryptor.EncryptorFactory
import com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.provider.DefaultConfigurationProvider
import com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.provider.FileConfigurationProvider
import com.github.mowedgrass.jasyptgradleboot.password.DirectPasswordProvider
import com.github.mowedgrass.jasyptgradleboot.password.PasswordProvider
import com.github.mowedgrass.jasyptgradleboot.password.PropertyPasswordProvider
import com.github.mowedgrass.jasyptgradleboot.password.property.PropertyResolver
import org.jasypt.encryption.StringEncryptor
import org.springframework.core.env.StandardEnvironment

class ConfiguredEncryptorFactory {

    public PasswordProvider createPasswordProvider(String password) {
        return (password) ?
                new DirectPasswordProvider(password) :
                new PropertyPasswordProvider(new PropertyResolver(new StandardEnvironment()))
    }

    public StringEncryptor createEncryptor(File configurationFile, PasswordProvider passwordProvider) {
        def configurationProvider = configurationFile ?
                new FileConfigurationProvider(new FileInputStream(configurationFile.path)) :
                new DefaultConfigurationProvider()

        new EncryptorFactory().create(passwordProvider, configurationProvider)
    }
}
