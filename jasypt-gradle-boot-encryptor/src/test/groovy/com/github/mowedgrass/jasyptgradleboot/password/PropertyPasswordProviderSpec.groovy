package com.github.mowedgrass.jasyptgradleboot.password

import com.github.mowedgrass.jasyptgradleboot.password.property.PropertyResolver
import spock.lang.Specification

import static com.github.mowedgrass.jasyptgradleboot.password.PropertyPasswordProvider.*

class PropertyPasswordProviderSpec extends Specification {

    PropertyResolver propertyResolver
    PropertyPasswordProvider passwordProvider

    void setup() {
        propertyResolver = Mock(PropertyResolver)
        passwordProvider = new PropertyPasswordProvider(propertyResolver)
    }

    def 'should get password from system property'() {
        given:
        1 * propertyResolver.getEnvironmentProperty(PASSWORD_PROPERTY) >> Optional.empty()
        1 * propertyResolver.getEnvironmentProperty(SHORT_PASSWORD_PROPERTY) >> Optional.empty()
        1 * propertyResolver.getSystemProperty(SYSTEM_PASSWORD_PROPERTY) >> Optional.of('secret')
        1 * propertyResolver.getSystemProperty(SYSTEM_SHORT_PASSWORD_PROPERTY) >> Optional.empty()

        0 * propertyResolver.getEnvironmentProperty(_ as String)
        0 * propertyResolver.getSystemProperty(_ as String)

        when:
        def password = passwordProvider.password

        then:
        'secret' == password
    }

    def 'should get password from environment property'() {
        given:
        1 * propertyResolver.getEnvironmentProperty(PASSWORD_PROPERTY) >> Optional.empty()
        1 * propertyResolver.getEnvironmentProperty(SHORT_PASSWORD_PROPERTY) >> Optional.of('right secret')
        1 * propertyResolver.getSystemProperty(SYSTEM_PASSWORD_PROPERTY) >> Optional.of('other secret')
        1 * propertyResolver.getSystemProperty(SYSTEM_SHORT_PASSWORD_PROPERTY) >> Optional.of('secret')

        0 * propertyResolver.getEnvironmentProperty(_ as String)
        0 * propertyResolver.getSystemProperty(_ as String)

        when:
        def password = passwordProvider.password

        then:
        'right secret' == password
    }

    def 'should get password from jasypt environment property'() {
        given:
        1 * propertyResolver.getEnvironmentProperty(PASSWORD_PROPERTY) >> Optional.of('right secret')
        1 * propertyResolver.getEnvironmentProperty(SHORT_PASSWORD_PROPERTY) >> Optional.of('wrong secret')
        1 * propertyResolver.getSystemProperty(SYSTEM_PASSWORD_PROPERTY) >> Optional.of('other secret')
        1 * propertyResolver.getSystemProperty(SYSTEM_SHORT_PASSWORD_PROPERTY) >> Optional.of('other secret')

        0 * propertyResolver.getEnvironmentProperty(_ as String)
        0 * propertyResolver.getSystemProperty(_ as String)

        when:
        def password = passwordProvider.password

        then:
        'right secret' == password
    }

    def 'should not get password from properties'() {
        given:
        1 * propertyResolver.getEnvironmentProperty(PASSWORD_PROPERTY) >> Optional.empty()
        1 * propertyResolver.getEnvironmentProperty(SHORT_PASSWORD_PROPERTY) >> Optional.empty()
        1 * propertyResolver.getSystemProperty(SYSTEM_PASSWORD_PROPERTY) >> Optional.empty()
        1 * propertyResolver.getSystemProperty(SYSTEM_SHORT_PASSWORD_PROPERTY) >> Optional.empty()

        0 * propertyResolver.getEnvironmentProperty(_ as String)
        0 * propertyResolver.getSystemProperty(_ as String)

        when:
        def password = passwordProvider.password

        then:
        password.isEmpty()
    }
}
