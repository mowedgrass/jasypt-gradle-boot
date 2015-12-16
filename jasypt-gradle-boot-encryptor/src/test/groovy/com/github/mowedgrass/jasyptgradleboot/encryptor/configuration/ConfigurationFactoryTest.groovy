package com.github.mowedgrass.jasyptgradleboot.encryptor.configuration

import com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.provider.ConfigurationProvider
import com.github.mowedgrass.jasyptgradleboot.password.PasswordProvider
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import spock.lang.Specification

import static com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.ConfigurationProperty.*

class ConfigurationFactoryTest extends Specification {

    PasswordProvider passwordProvider
    ConfigurationProvider configurationProvider
    ConfigurationFactory configurationFactory

    void setup() {
        passwordProvider = Mock(PasswordProvider)
        configurationProvider = Mock(ConfigurationProvider)

        configurationFactory = new ConfigurationFactory(passwordProvider, configurationProvider)
    }

    def 'should create configuration with default values'() {
        given:
        1 * configurationProvider.getProperty(ALGORITHM) >> null
        1 * configurationProvider.getProperty(KEY_OBTENTION_ITERATIONS) >> null
        1 * configurationProvider.getProperty(POOL_SIZE) >> null
        1 * configurationProvider.getProperty(PROVIDER_NAME) >> null
        1 * configurationProvider.getProperty(SALT_GENERATOR) >> null
        1 * configurationProvider.getProperty(OUTPUT_TYPE) >> null

        0 * configurationProvider.getProperty(_ as ConfigurationProperty)

        when:
        def config = configurationFactory.create()

        then:
        with(config) {
            ALGORITHM.defaultValue == config.algorithm
            KEY_OBTENTION_ITERATIONS.defaultValue.toInteger() == config.keyObtentionIterations
            POOL_SIZE.defaultValue.toInteger() == config.poolSize
            PROVIDER_NAME.defaultValue == config.providerName
            SALT_GENERATOR.defaultValue == config.saltGenerator.class.name
            OUTPUT_TYPE.defaultValue == ((SimpleStringPBEConfig) config).stringOutputType
        }
    }

    def 'should create configuration from provider'() {
        given:
        1 * configurationProvider.getProperty(ALGORITHM) >> 'PBEWithMD5AndTripleDES'
        1 * configurationProvider.getProperty(KEY_OBTENTION_ITERATIONS) >> '666'
        1 * configurationProvider.getProperty(POOL_SIZE) >> '1337'
        1 * configurationProvider.getProperty(PROVIDER_NAME) >> 'provider'
        1 * configurationProvider.getProperty(SALT_GENERATOR) >> 'org.jasypt.salt.ZeroSaltGenerator'
        1 * configurationProvider.getProperty(OUTPUT_TYPE) >> 'hexadecimal'

        0 * configurationProvider.getProperty(_ as ConfigurationProperty)

        when:
        def config = configurationFactory.create()

        then:
        with(config) {
            'PBEWithMD5AndTripleDES' == config.algorithm
            666 == config.keyObtentionIterations
            1337 == config.poolSize
            'provider' == config.providerName
            'org.jasypt.salt.ZeroSaltGenerator' == config.saltGenerator.class.name
            'hexadecimal' == ((SimpleStringPBEConfig) config).stringOutputType
        }
    }

    def 'should create configuration from defaults overridden by provider'() {
        given:
        1 * configurationProvider.getProperty(ALGORITHM) >> 'PBEWithMD5AndTripleDES'
        1 * configurationProvider.getProperty(KEY_OBTENTION_ITERATIONS) >> null
        1 * configurationProvider.getProperty(POOL_SIZE) >> '1337'
        1 * configurationProvider.getProperty(PROVIDER_NAME) >>null
        1 * configurationProvider.getProperty(SALT_GENERATOR) >> null
        1 * configurationProvider.getProperty(OUTPUT_TYPE) >> null

        0 * configurationProvider.getProperty(_ as ConfigurationProperty)

        when:
        def config = configurationFactory.create()

        then:
        with(config) {
            'PBEWithMD5AndTripleDES' == config.algorithm
            KEY_OBTENTION_ITERATIONS.defaultValue.toInteger() == config.keyObtentionIterations
            1337 == config.poolSize
            PROVIDER_NAME.defaultValue == config.providerName
            SALT_GENERATOR.defaultValue == config.saltGenerator.class.name
            OUTPUT_TYPE.defaultValue == ((SimpleStringPBEConfig) config).stringOutputType
        }
    }
}
