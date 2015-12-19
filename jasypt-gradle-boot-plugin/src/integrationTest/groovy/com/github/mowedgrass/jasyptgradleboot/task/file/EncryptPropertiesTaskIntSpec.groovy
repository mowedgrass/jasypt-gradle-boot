package com.github.mowedgrass.jasyptgradleboot.task.file
import com.github.mowedgrass.jasyptgradleboot.action.file.EncryptPropertiesAction
import com.github.mowedgrass.jasyptgradleboot.task.BaseTaskIntSpec
import com.github.mowedgrass.jasyptgradleboot.task.file.metadata.PropertyMetadata
import org.gradle.testfixtures.ProjectBuilder

import static com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessType.DECRYPT
import static com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessType.ENCRYPT

class EncryptPropertiesTaskIntSpec extends BaseTaskIntSpec {

    EncryptPropertiesTask task

    void setup() {
        def project = ProjectBuilder.builder().build()
        task = project.task(EncryptPropertiesAction.TASK_NAME, type: EncryptPropertiesTask) as EncryptPropertiesTask
    }

    def 'should not encrypt any resource'() {
        given:
        task.password = 'pass123'
        task.resourceFiles = []

        when:
        def metadata = task.action()

        then:
        0 == metadata.size()
    }

    def 'should prepare a preview of encrypted resource'() {
        given:
        def password = 'pass123'
        def propertyValue = 'to-be-encrypted'
        def propertyName = 'secret'
        def resourceFile = getResourceFile('properties', ["$propertyName": "ENCRYPT($propertyValue)"])
        def checksum = generateChecksum(resourceFile)

        and:
        task.confirm = false
        task.password = password
        task.resourceFiles = [resourceFile]

        when:
        def fileMetadata = task.action()

        then:
        fileMetadata
        [resourceFile] == fileMetadata.keySet().asList()

        and:
        [1] == getLineNumbers(fileMetadata[resourceFile])
        with(fileMetadata[resourceFile].getLine(1)) { PropertyMetadata metadata ->
            ENCRYPT == type

            !error
            with(metadata.line) {
                "$propertyName=ENCRYPT($propertyValue)".toString() == initial
                processed.matches(/$propertyName=ENC\(.*\)/)
            }

            with(metadata.value) {
                propertyValue == initial
                propertyValue == decrypt(password, processed)
            }
        }

        and:
        checksum == generateChecksum(resourceFile)
    }

    def 'should filter resources and properties'() {
        given:
        def resourceFile1 = getResourceFile('properties', ['lorem': 'ipsum', 'secret1': 'ENCRYPT(qwerty)'])
        def resourceFile2 = getResourceFile('another', ['secret2': 'ENCRYPT(zaqwsx)'])
        def resourceFile3 = getResourceFile('yml', ['secret3': 'ENCRYPT(123456)'])
        def resourceFile4 = getResourceFile('properties', ['secret4': 'ENC(KpcygOF7bj+Tu0EtAaz08w==)'])

        and:
        task.confirm = false
        task.password = 'pass123'
        task.resourceFiles = [resourceFile1, resourceFile2, resourceFile3, resourceFile4]

        when:
        def metadata = task.action()

        then:
        metadata
        [resourceFile1, resourceFile3, resourceFile4] == metadata.keySet().asList()

        and:
        with(metadata[resourceFile1]) {
            [2] == getLineNumbers(it)
            ENCRYPT == getLine(2).type
            'qwerty' == getLine(2).value.initial
        }

        and:
        with(metadata[resourceFile3]) {
            [1] == getLineNumbers(it)
            ENCRYPT == getLine(1).type
            '123456' == getLine(1).value.initial
        }

        and:
        with(metadata[resourceFile4]) {
            [1] == getLineNumbers(it)
            DECRYPT == getLine(1).type
            'KpcygOF7bj+Tu0EtAaz08w==' == getLine(1).value.initial
        }
    }

    def 'should try to decrypt other properties with given password'() {
        given:
        def resourceFile = getResourceFile('yml', [
                'secret1': 'ENCRYPT(123456)',
                'secret2': 'ENC(KpcygOF7bj+Tu0EtAaz08w==)',
                'secret3': 'ENC(XY6vZ5eh0WR0Z6kgfb7wWw==)'
        ])

        and:
        task.confirm = false
        task.password = 'pass123'
        task.resourceFiles = [resourceFile]

        when:
        def metadata = task.action()

        then:
        metadata
        [resourceFile] == metadata.keySet().asList()
        def fileMetadata = metadata[resourceFile]
        [1, 2, 3] == getLineNumbers(fileMetadata)

        and:
        with (fileMetadata.getLine(1)) {
            ENCRYPT == type
            '123456' == value.initial
        }

        and:
        with (fileMetadata.getLine(2)) {
            DECRYPT == type
            'KpcygOF7bj+Tu0EtAaz08w==' == value.initial
            'test' == value.processed
            !error
        }

        and:
        with (fileMetadata.getLine(3)) {
            DECRYPT == type
            'XY6vZ5eh0WR0Z6kgfb7wWw==' == value.initial
            error
        }
    }

    def 'should encrypt resource'() {
        given:
        def password = 'pass123'
        def propertyValue = 'to-be-encrypted'
        def propertyName = 'secret'
        def resourceFile = getResourceFile('properties',
                ['lorem': 'ipsum', "$propertyName": "ENCRYPT($propertyValue)", 'dolor': 'sit'])
        def checksum = generateChecksum(resourceFile)

        and:
        task.confirm = true
        task.password = password
        task.resourceFiles = [resourceFile]

        when:
        def metadata = task.action()

        then:
        metadata
        [resourceFile] == metadata.keySet().toList()
        def fileMetadata = metadata[resourceFile]

        and:
        [2] == getLineNumbers(fileMetadata)

        with(fileMetadata.getLine(2)) {
            ENCRYPT == type
            "$propertyName=ENCRYPT($propertyValue)".toString() == line.initial
            line.processed.matches(/$propertyName=ENC\(.*\)/)
            propertyValue == value.initial
            propertyValue == decrypt(password, value.processed)
        }

        and:
        checksum != generateChecksum(resourceFile)
        checksum == generateChecksum(new File(resourceFile.path + '.bak'))

        resourceFile.readLines().with { lines ->
            3 == size()
            lines.iterator().with {
                'lorem=ipsum' == next()
                "secret=ENC(${fileMetadata.getLine(2).value.processed})".toString() == next()
                'dolor=sit' == next()
            }
        }
    }
}
