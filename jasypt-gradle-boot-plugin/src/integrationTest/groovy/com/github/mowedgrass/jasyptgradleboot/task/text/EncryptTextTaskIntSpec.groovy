package com.github.mowedgrass.jasyptgradleboot.task.text

import com.github.mowedgrass.jasyptgradleboot.action.text.EncryptTextAction
import com.github.mowedgrass.jasyptgradleboot.task.BaseTaskIntSpec
import org.gradle.api.internal.tasks.options.OptionValidationException
import org.gradle.testfixtures.ProjectBuilder

class EncryptTextTaskIntSpec extends BaseTaskIntSpec {

    EncryptTextTask task

    void setup() {
        def project = ProjectBuilder.builder().build()
        task = project.task(EncryptTextAction.TASK_NAME, type: EncryptTextTask) as EncryptTextTask
    }

    def 'should encrypt text'() {
        given:
        task.text = 'credentials'
        task.password = 'pass123'
        task.resourceFiles = []

        when:
        def encrypted = task.action()

        then:
        'credentials' == getEncryptor(task.password, [:]).decrypt(encrypted)
    }

    def 'should encrypt text using configuration file'() {
        given:
        def properties = ['jasypt.encryptor.stringOutputType':'hexadecimal']

        task.text = 'credentials'
        task.password = 'pass123'
        task.resourceFiles = [new File('dummy.properties'), getConfigurationFileMock(properties), new File('other.yml')]

        when:
        def encrypted = task.action()

        then:
        'credentials' == getEncryptor(task.password, properties).decrypt(encrypted)
    }

    def 'should throw exception when no password given'() {
        given:
        task.text = 'credentials'
        task.resourceFiles = []

        when:
        task.action()

        then:
        def e = thrown(OptionValidationException)
        "'password' parameter missing" == e.message
    }

    def 'should throw exception when no text given'() {
        given:
        task.password = 'pass123'
        task.resourceFiles = []

        when:
        task.action()

        then:
        def e = thrown(OptionValidationException)
        "'text' parameter missing" == e.message
    }
}
