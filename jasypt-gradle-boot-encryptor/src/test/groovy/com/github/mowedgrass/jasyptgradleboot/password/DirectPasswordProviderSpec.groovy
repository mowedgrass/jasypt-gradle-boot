package com.github.mowedgrass.jasyptgradleboot.password
import spock.lang.Specification

class DirectPasswordProviderSpec extends Specification {

    PasswordProvider passwordProvider

    def 'should get password from provider'() {
        given:
        def password = 'secret'
        passwordProvider = new DirectPasswordProvider(password)

        when:
        def actualPassword = passwordProvider.getPassword()

        then:
        password == actualPassword
    }
}
