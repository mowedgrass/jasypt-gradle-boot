package com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.provider

import spock.lang.Specification

import java.nio.charset.StandardCharsets

import static com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.ConfigurationProperty.*

class FileConfigurationProviderTest extends Specification {

    FileConfigurationProvider provider

    def 'should throw exception and close stream on i/o error'() {
        given:
        def stream = Mock(InputStream)
        stream.read(_ as byte[]) >> { throw new IOException() }

        when:
        provider = new FileConfigurationProvider(stream)

        then:
        1 * stream.close()
        def e = thrown(ConfigurationProviderException)
        'Unable to read properties from stream' == e.message
    }

    def 'should not throw exception when stream is empty'() {
        when:
        provider = new FileConfigurationProvider(null)

        then:
        notThrown(ConfigurationProviderException)
    }

    def 'should get empty property from empty stream'() {
        given:
        provider = new FileConfigurationProvider(null)

        when:
        def providerName = provider.getProperty(PROVIDER_NAME)

        then:
        !providerName
    }

    def 'should get property from input stream'() {
        given:
        def data = [
                'jasypt.encryptor.providerName': 'SunJCE',
                'jasypt.encryptor.poolSize'    : '1337'
        ];
        provider = new FileConfigurationProvider(getStream(data))

        when:
        def providerName = provider.getProperty(PROVIDER_NAME)
        def poolSize = provider.getProperty(POOL_SIZE)
        def algorithm = provider.getProperty(ALGORITHM)

        then:
        'SunJCE' == providerName
        '1337' == poolSize
        !algorithm
    }

    private InputStream getStream(Map<String, String> data) {
        def inputStreamData = data
                .collect({ key, value -> "$key:$value" })
                .join(System.lineSeparator())

        new ByteArrayInputStream(inputStreamData.getBytes(StandardCharsets.UTF_8));
    }
}
