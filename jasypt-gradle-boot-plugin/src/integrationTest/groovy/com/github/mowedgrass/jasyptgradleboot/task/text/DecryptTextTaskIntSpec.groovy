package com.github.mowedgrass.jasyptgradleboot.task.text
import com.github.mowedgrass.jasyptgradleboot.action.text.DecryptTextAction
import com.github.mowedgrass.jasyptgradleboot.task.BaseTaskIntSpec
import org.gradle.api.internal.tasks.options.OptionValidationException
import org.gradle.testfixtures.ProjectBuilder

class DecryptTextTaskIntSpec extends BaseTaskIntSpec {

    DecryptTextTask task

    void setup() {
        def project = ProjectBuilder.builder().build()
        task = project.task(DecryptTextAction.TASK_NAME, type: DecryptTextTask) as DecryptTextTask
    }

    def 'should decrypt text'() {
        given:
        task.text = 'VWkThmN12ycrkDnLrFbP3A=='
        task.password = 'pass123'
        task.resourceFiles = []

        when:
        def decrypted = task.action()

        then:
        'qwerty' == decrypted
    }

    def 'should decrypt text with ENC tag'() {
        given:
        task.text = 'ENC(VWkThmN12ycrkDnLrFbP3A==)'
        task.password = 'pass123'
        task.resourceFiles = []

        when:
        def decrypted = task.action()

        then:
        'qwerty' == decrypted
    }

    def 'should decrypt text using configuration file'() {
        given:
        def properties = ['jasypt.encryptor.stringOutputType': 'hexadecimal']
        task.text = 'F34075A6EEF17C080B5A3E91E1D5A948'
        task.password = 'pass123'
        task.resourceFiles = [new File('dummy.properties'), getConfigurationFileMock(properties), new File('other.yml')]

        when:
        def decrypted = task.action()

        then:
        'qwerty' == decrypted
    }

    def 'should throw exception when no password given'() {
        given:
        task.text = 'VWkThmN12ycrkDnLrFbP3A=='
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
