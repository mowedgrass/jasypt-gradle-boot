package com.github.mowedgrass.jasyptgradleboot.task.file.process.definition

import org.jasypt.encryption.StringEncryptor
import spock.lang.Specification

class EncryptionProcessDefinitionSpec extends Specification {

    StringEncryptor encryptor
    EncryptionProcessDefinition processDefinition

    def setup() {
        encryptor = Mock(StringEncryptor)

        processDefinition = new EncryptionProcessDefinition()
    }

    def 'should format output'() {
        given:
        def encrypted = 'secret'

        when:
        def formatted = sprintf(processDefinition.outputFormat, encrypted)

        then:
        "ENC($encrypted)" == formatted
    }

    def 'should encrypt string'() {
        given:
        def plain = 'plain'
        def encrypted = 'encrypted'
        1 * encryptor.encrypt(plain) >> encrypted

        when:
        def actualEncrypted = processDefinition.executor.process(plain, encryptor)

        then:
        encrypted == actualEncrypted
    }
}
