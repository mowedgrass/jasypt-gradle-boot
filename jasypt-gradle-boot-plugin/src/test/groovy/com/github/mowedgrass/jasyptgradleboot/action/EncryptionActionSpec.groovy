package com.github.mowedgrass.jasyptgradleboot.action

import com.github.mowedgrass.jasyptgradleboot.task.PasswordAwareTask
import spock.lang.Specification

class EncryptionActionSpec extends Specification {

    EncryptionAction<PasswordAwareTask> action

    def 'should set task properties'() {
        given:
        def description = 'task description'.toString()
        def resourceFiles = [new File('application.yml')].toSet()
        def task = Mock(PasswordAwareTask)

        and:
        action = new EncryptionAction(description, resourceFiles)

        when:
        action.execute(task)

        then:
        1 * task.setProperty('group', EncryptionAction.GROUP)
        1 * task.setProperty('resourceFiles', resourceFiles)
        1 * task.setProperty('description', description)
    }
}
