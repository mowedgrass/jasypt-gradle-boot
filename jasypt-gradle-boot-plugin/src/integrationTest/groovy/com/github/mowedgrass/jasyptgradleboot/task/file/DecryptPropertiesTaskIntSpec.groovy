package com.github.mowedgrass.jasyptgradleboot.task.file
import com.github.mowedgrass.jasyptgradleboot.action.file.DecryptPropertiesAction
import com.github.mowedgrass.jasyptgradleboot.task.BaseTaskIntSpec
import com.github.mowedgrass.jasyptgradleboot.task.file.metadata.PropertyMetadata
import org.gradle.testfixtures.ProjectBuilder

import static com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessType.DECRYPT

class DecryptPropertiesTaskIntSpec extends BaseTaskIntSpec {

    DecryptPropertiesTask task

    void setup() {
        def project = ProjectBuilder.builder().build()
        task = project.task(DecryptPropertiesAction.TASK_NAME, type: DecryptPropertiesTask) as DecryptPropertiesTask
    }

    def 'should not decrypt any resource'() {
        given:
        task.password = 'pass123'
        task.resourceFiles = []
        task.tag = false

        when:
        def metadata = task.action()

        then:
        0 == metadata.size()
    }

    def 'should prepare a preview of decrypted resource'() {
        given:
        def password = 'pass123'
        def propertyValue = '2sOYdnVm7AWVHobymE/blqqfjqCkrU4i'
        def propertyName = 'secret'
        def resourceFile = getResourceFile('properties', [(propertyName): "ENC($propertyValue)"])
        def checksum = generateChecksum(resourceFile)

        and:
        task.confirm = false
        task.password = password
        task.resourceFiles = [resourceFile]
        task.tag = tag

        when:
        def fileMetadata = task.action()

        then:
        fileMetadata
        [resourceFile] == fileMetadata.keySet().asList()

        and:
        [1] == getLineNumbers(fileMetadata[resourceFile])
        with(fileMetadata[resourceFile].getLine(1)) { PropertyMetadata metadata ->
            DECRYPT == type

            !error
            with(metadata.line) {
                "$propertyName=ENC($propertyValue)" == initial
                "$propertyName=$lineValue" == processed
            }

            with(metadata.value) {
                propertyValue == initial
                'credentials' == processed
            }
        }

        and:
        checksum == generateChecksum(resourceFile)

        where:
        tag   | lineValue
        false | 'credentials'
        true  | 'ENCRYPT(credentials)'
    }

    def 'should filter resources and properties'() {
        given:
        def resourceFile1 = getResourceFile('properties', ['lorem': 'ipsum', 'secret1': 'ENC(2sOYdnVm7AWVHobymE/blqqfjqCkrU4i)'])
        def resourceFile2 = getResourceFile('another', ['secret2': 'ENC(Nso24FPU1jFULf4Y7SB5Hw==)'])
        def resourceFile3 = getResourceFile('yml', ['secret3': 'ENC(woSUIeqrSOs3Gk5MBpAdr72xQClCU6SZ)'])
        def resourceFile4 = getResourceFile('properties', ['secret4': 'ENCRYPT(secret)'])

        and:
        task.confirm = false
        task.password = 'pass123'
        task.resourceFiles = [resourceFile1, resourceFile2, resourceFile3, resourceFile4]

        when:
        def metadata = task.action()

        then:
        metadata
        [resourceFile1, resourceFile3] == metadata.keySet().asList()

        and:
        with(metadata[resourceFile1]) {
            [2] == getLineNumbers(it)
            DECRYPT == getLine(2).type

            with(getLine(2).line) {
                'secret1=ENC(2sOYdnVm7AWVHobymE/blqqfjqCkrU4i)' == initial
                'secret1=credentials' == processed
            }
        }

        and:
        with(metadata[resourceFile3]) {
            [1] == getLineNumbers(it)
            DECRYPT == getLine(1).type

            with(getLine(1).line) {
                'secret3=ENC(woSUIeqrSOs3Gk5MBpAdr72xQClCU6SZ)' == initial
                'secret3=top-secret' == processed
            }
        }
    }

    def 'should try to decrypt other properties with given password'() {
        given:
        def resourceFile = getResourceFile('yml', [
                'secret1': 'ENC(KpcygOF7bj+Tu0EtAaz08w==)',
                'secret2': 'ENCRYPT(12345)',
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
        [1, 3] == getLineNumbers(fileMetadata)

        and:
        with (fileMetadata.getLine(1)) {
            DECRYPT == type
            'KpcygOF7bj+Tu0EtAaz08w==' == value.initial
            'test' == value.processed
        }

        and:
        with (fileMetadata.getLine(3)) {
            DECRYPT == type
            'XY6vZ5eh0WR0Z6kgfb7wWw==' == value.initial
            !value.processed
            error
        }
    }

    def 'should decrypt resource'() {
        given:
        def password = 'pass123'
        def propertyValue = 'afwEjy05LWC0LJOhPgcSQyCWcNj6gRKj'
        def propertyName = 'secret'
        def resourceFile = getResourceFile('properties',
                ['lorem': 'ipsum', (propertyName): "ENC($propertyValue)", 'dolor': 'sit'])
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
            DECRYPT == type
            "$propertyName=ENC($propertyValue)" == line.initial
            "$propertyName=credentials" == line.processed
            propertyValue == value.initial
            'credentials' == value.processed
        }

        and:
        checksum != generateChecksum(resourceFile)
        checksum == generateChecksum(new File(resourceFile.path + '.bak'))

        resourceFile.readLines().with { lines ->
            3 == size()
            lines.iterator().with {
                'lorem=ipsum' == next()
                "secret=${fileMetadata.getLine(2).value.processed}" == next()
                'dolor=sit' == next()
            }
        }
    }
}
