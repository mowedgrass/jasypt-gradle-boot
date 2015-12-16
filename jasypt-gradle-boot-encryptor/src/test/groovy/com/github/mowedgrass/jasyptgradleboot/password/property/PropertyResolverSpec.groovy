package com.github.mowedgrass.jasyptgradleboot.password.property

import org.springframework.core.env.StandardEnvironment
import spock.lang.Specification

class PropertyResolverSpec extends Specification {

    PropertyResolver resolver
    StandardEnvironment environment

    void setup() {
        environment = Mock(StandardEnvironment)

        resolver = new PropertyResolver(environment)
    }

    def 'should get environment property'() {
        given:
        1 * environment.getProperty('password') >> 'secret'

        when:
        def password = resolver.getEnvironmentProperty('password')

        then:
        with (password) {
            it.isPresent()
            'secret' == get()
        }
    }

    def 'should not get environment property'() {
        given:
        1 * environment.getProperty('password') >> null

        when:
        def password = resolver.getEnvironmentProperty('password')

        then:
        !password.isPresent()
    }

    def 'should get system property'() {
        given:
        1 * environment.systemEnvironment >> [password: 'secret']

        when:
        def password = resolver.getSystemProperty('password')

        then:
        with (password) {
            it.isPresent()
            'secret' == get()
        }
    }

    def 'should not get system property'() {
        given:
        1 * environment.systemEnvironment >> [password: null]

        when:
        def password = resolver.getSystemProperty('password')

        then:
        !password.isPresent()
    }
}
