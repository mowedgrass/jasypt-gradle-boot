package com.github.mowedgrass.jasyptgradleboot.task.file.resource
import com.github.mowedgrass.jasyptgradleboot.task.file.property.PropertyLineProcessor
import com.github.mowedgrass.jasyptgradleboot.task.file.process.ProcessRequest
import spock.lang.Specification

import static com.github.mowedgrass.jasyptgradleboot.task.file.metadata.PropertyMetadata.ofType
import static com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessType.DECRYPT
import static com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessType.ENCRYPT
import static java.util.Optional.empty
import static java.util.Optional.of

class ResourceProcessingRewriterSpec extends Specification {

    BufferedReader reader
    BufferedWriter writer
    PropertyLineProcessor lineProcessor
    ResourceProcessingRewriter previewWriter

    ProcessRequest request

    def setup() {
        reader = Mock(BufferedReader)
        writer = Mock(BufferedWriter)
        lineProcessor = Mock(PropertyLineProcessor)

        previewWriter = new ResourceProcessingRewriter(lineProcessor)

        request = ProcessRequest.ENCRYPT
    }

    def 'should read, process and write each of the lines'() {
        given:
        def firstLine = ofType(ENCRYPT)
                .withInitial('data: ENCRYPT(to_encrypt)', 'to_encrypt')
                .withProcessed('data: ENC(encrypted)', 'encrypted')
                .build()
        1 * reader.readLine() >> firstLine.line.initial
        1 * lineProcessor.process(firstLine.line.initial, request.primary) >> of(firstLine)
        1 * lineProcessor.process(firstLine.line.initial, request.validation) >> empty()

        and:
        def secondLine = 'data: normal'
        1 * reader.readLine() >> secondLine
        1 * lineProcessor.process(secondLine, request.primary) >> empty()
        1 * lineProcessor.process(secondLine, request.validation) >> empty()

        and:
        def thirdLine = ofType(DECRYPT)
                .withInitial('data: ENC(encrypted)', 'encrypted')
                .withProcessed('data: decrypted', 'decrypted')
                .build()
        1 * reader.readLine() >> thirdLine.line.initial
        1 * lineProcessor.process(thirdLine.line.initial, request.primary) >> empty()
        1 * lineProcessor.process(thirdLine.line.initial, request.validation) >> of(thirdLine)

        and:
        1 * reader.readLine() >> null

        when:
        def fileMetadata = previewWriter.write(reader, writer, request)

        then:
        1 * writer.write(firstLine.line.processed)
        1 * writer.write(secondLine)
        1 * writer.write(thirdLine.line.initial)
        3 * writer.newLine()

        and:
        fileMetadata
        [1, 3] == fileMetadata.lines().collect { e -> e.key }
        firstLine == fileMetadata.getLine(1)
        thirdLine == fileMetadata.getLine(3)

        and:
        2 * reader.close()
        1 * writer.close()
    }

    def 'should fail on processing and write original line'() {
        given:
        def firstLine = ofType(ENCRYPT)
                .withInitial('data: ENCRYPT(to_encrypt)', 'to_encrypt')
                .withError()
                .build()
        1 * reader.readLine() >> firstLine.line.initial
        1 * lineProcessor.process(firstLine.line.initial, request.primary) >> of(firstLine)
        1 * lineProcessor.process(firstLine.line.initial, request.validation) >> empty()

        and:
        1 * reader.readLine() >> null

        when:
        def fileMetadata = previewWriter.write(reader, writer, request )

        then:
        1 * writer.write(firstLine.line.initial)
        1 * writer.newLine()

        and:
        fileMetadata
        [1] == fileMetadata.lines().collect { e -> e.key }
        firstLine == fileMetadata.getLine(1)

        and:
        2 * reader.close()
        1 * writer.close()
    }
}
