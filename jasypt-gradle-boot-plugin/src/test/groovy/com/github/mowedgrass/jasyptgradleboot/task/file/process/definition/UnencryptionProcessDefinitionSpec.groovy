package com.github.mowedgrass.jasyptgradleboot.task.file.process.definition

import org.jasypt.encryption.StringEncryptor
import spock.lang.Specification

class UnencryptionProcessDefinitionSpec extends Specification {

    StringEncryptor encryptor
    UnencryptionProcessDefinition processDefinition

    def setup() {
        encryptor = Mock(StringEncryptor)

        processDefinition = new UnencryptionProcessDefinition()
    }

    def 'should format output'() {
        given:
        def decrypted = 'secret'

        when:
        def formatted = sprintf(processDefinition.outputFormat, decrypted)

        then:
        "ENCRYPT($decrypted)" == formatted
    }

    def 'should decrypt string'() {
        given:
        def encrypted = 'secret'
        def decrypted = 'plain'
        1 * encryptor.decrypt(encrypted) >> decrypted

        when:
        def actualDecrypted = processDefinition.executor.process(encrypted, encryptor)

        then:
        decrypted == actualDecrypted
    }
}
