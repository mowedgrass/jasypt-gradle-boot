package com.github.mowedgrass.jasyptgradleboot.task.file.property

import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.executor.ProcessExecutor
import com.github.mowedgrass.jasyptgradleboot.task.file.property.PropertyValueProcessor
import org.jasypt.encryption.StringEncryptor
import spock.lang.Specification

class PropertyValueProcessorSpec extends Specification {

    StringEncryptor encryptor
    ProcessExecutor executor
    PropertyValueProcessor valueProcessor

    def setup() {
        encryptor = Mock(StringEncryptor)
        executor = Mock(ProcessExecutor)

        valueProcessor = new PropertyValueProcessor(encryptor)
    }

    def 'should return processed value'() {
        given:
        def initial = 'plain'
        def processed = 'encrypted'

        and:
        1 * executor.process(initial, encryptor) >> processed

        when:
        def actual = valueProcessor.processValue(initial, executor)

        then:
        processed == actual
    }
}
