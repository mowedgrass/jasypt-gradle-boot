package com.github.mowedgrass.jasyptgradleboot.task.file.metadata

import spock.lang.Specification

import static com.github.mowedgrass.jasyptgradleboot.task.file.metadata.PropertyMetadata.ofType
import static com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessType.DECRYPT
import static com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessType.ENCRYPT

class FileMetadataSpec extends Specification {

    FileMetadata fileMetadata

    def setup() {
        fileMetadata = new FileMetadata()
    }

    def 'should add and return lines'() {
        given:
        def firstLine = ofType(ENCRYPT).build()
        def secondLine = ofType(DECRYPT).build()

        when:
        fileMetadata.addLine(1, firstLine)
        fileMetadata.addLine(2, secondLine)

        then:
        [1: firstLine, 2: secondLine] == fileMetadata.lines().collectEntries { [(it.key): it.value] }

        and:
        firstLine == fileMetadata.getLine(1)
        secondLine == fileMetadata.getLine(2)
    }

    def 'should collect each line'() {
        given:
        def firstLine = ofType(ENCRYPT).build()
        def secondLine = ofType(DECRYPT).build()

        and:
        fileMetadata.addLine(1, firstLine)
        fileMetadata.addLine(2, secondLine)

        when:
        def entries = [:]
        fileMetadata.eachLine { lineNo, metadata -> entries[lineNo] = metadata }

        then:
        [1: firstLine, 2: secondLine] == entries
    }

    def 'should check if metadata contains process type'() {
        given:
        def firstLine = ofType(firstType).build()
        def secondLine = ofType(secondType).build()

        when:
        fileMetadata.addLine(1, firstLine)
        fileMetadata.addLine(2, secondLine)

        then:
        result == fileMetadata.contains(testType)

        where:
        firstType | secondType | testType | result
        ENCRYPT   | ENCRYPT    | ENCRYPT  | true
        ENCRYPT   | DECRYPT    | ENCRYPT  | true
        DECRYPT   | ENCRYPT    | ENCRYPT  | true
        DECRYPT   | DECRYPT    | ENCRYPT  | false
    }
}
