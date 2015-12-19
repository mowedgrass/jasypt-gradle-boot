package com.github.mowedgrass.jasyptgradleboot.task.file.resource

import com.github.mowedgrass.jasyptgradleboot.task.file.process.ProcessRequest
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessDefinition
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.mowedgrass.jasyptgradleboot.task.file.process.ProcessRequest.*

class ResourceFilterSpec extends Specification {

    BufferedReader reader
    File file
    ProcessDefinition primaryProcess
    ProcessDefinition validationProcess
    ProcessRequest request
    FileSystemWrapper fileSystemWrapper
    ResourceFilter resourceFilter

    def setup() {
        reader = Mock(BufferedReader)
        file = Mock(File)
        fileSystemWrapper = Mock(FileSystemWrapper)

        primaryProcess = Mock(ProcessDefinition)
        validationProcess = Mock(ProcessDefinition)

        resourceFilter= new ResourceFilter(fileSystemWrapper)

        request = request(primaryProcess).with(validationProcess).build()
    }

    @Unroll
    def 'should filter file - #description'() {
        given:
        1 * file.name >> filename
        1 * fileSystemWrapper.getReader(file) >> reader
        3 * reader.readLine() >>> lines.plus(null)

        _ * primaryProcess.getProperty('linePattern') >> process.first()
        _ * validationProcess.getProperty('linePattern') >> process.last()

        when:
        def match = resourceFilter.filter(file, request)

        then:
        result == match
        2 * reader.close()

        where:
        filename       | lines          | process         | result | description
        'a.yml'        | ['ABC', 'DEF'] | [/ABC/, /Z{3}/] | true   | 'primary definition matches'
        'a.properties' | ['ABC', 'DEF'] | [/ABC/, /Z{3}/] | true   | 'properties extension'
        'a.properties' | ['DEF', 'ABC'] | [/ABC/, /Z{3}/] | true   | 'primary definition matches on second line'
        'a.properties' | ['DEF', 'ZZZ'] | [/ABC/, /Z{3}/] | true   | 'validation definition matches'
        'a.properties' | ['ZZZ', 'AAA'] | [/ABC/, /Z{3}/] | true   | 'validation definition matches first line'
        'a.YML'        | ['ABC', 'DEF'] | [/ABC/, /Z{3}/] | true   | 'uppercase extension'
        'a.yml'        | ['XYZ', 'OXO'] | [/ABC/, /Z{3}/] | false  | 'custom regular expression fails'
    }

    def 'should filter file by extension'() {
        given:
        1 * file.name >> filename

        when:
        def match = resourceFilter.filter(file, request)

        then:
        !match

        and:
        0 * reader.readLine()
        0 * primaryProcess.getProperty('linePattern')
        0 * validationProcess.getProperty('linePattern')
        0 * fileSystemWrapper.getReader(file)
        0 * reader.close()

        where:
        filename << ['app.zip', 'images.jpg', 'file.txt', 'README.md']
    }
}
