package com.github.mowedgrass.jasyptgradleboot.task.file.process.definition

import org.jasypt.encryption.StringEncryptor
import spock.lang.Specification

class DecryptionProcessDefinitionSpec extends Specification {

    StringEncryptor encryptor
    DecryptionProcessDefinition processDefinition

    def setup() {
        encryptor = Mock(StringEncryptor)

        processDefinition = new DecryptionProcessDefinition()
    }

    def 'should format output'() {
        given:
        def decrypted = 'secret'

        when:
        def formatted = sprintf(processDefinition.outputFormat, decrypted)

        then:
        decrypted == formatted
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
