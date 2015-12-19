package com.github.mowedgrass.jasyptgradleboot.task.file.property
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.DecryptionProcessDefinition
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.EncryptionProcessDefinition
import com.github.mowedgrass.jasyptgradleboot.task.file.property.pattern.ProcessPatternMatches
import org.jasypt.exceptions.EncryptionOperationNotPossibleException
import spock.lang.Specification

class MatchedLineProcessorSpec extends Specification {

    PropertyValueProcessor valueProcessor
    PropertyLineFormatter lineFormatter
    MatchedLineProcessor matchedLineProcessor

    def setup() {
        valueProcessor = Mock(PropertyValueProcessor)
        lineFormatter = Mock(PropertyLineFormatter)

        matchedLineProcessor = new MatchedLineProcessor(valueProcessor, lineFormatter)
    }

    def 'should return metadata'() {
        given:
        def preceding = 'data: '
        def initialValue = 'plain'
        def processedValue = 'encrypted'
        def succeeding = ''

        and:
        def initialLine = "${preceding}${initialValue}${succeeding}"
        def processedLine = "${preceding}ENC(${processedValue})$succeeding"

        and:
        def matches = new ProcessPatternMatches(preceding, initialValue, succeeding)
        def process = new EncryptionProcessDefinition()

        1 * valueProcessor.processValue(initialValue, process.executor) >> processedValue
        1 * lineFormatter.format(matches, processedValue, process.outputFormat) >> processedLine

        when:
        def metadata = matchedLineProcessor.process(initialLine, matches, process)

        then:
        with (metadata) {
            process.type == type

            initialValue == value.initial
            processedValue == value.processed

            initialLine == line.initial
            processedLine == line.processed

            !error
        }
    }

    def 'should return metadata with error'() {
        given:
        def preceding = 'data: '
        def initialValue = 'encrypted'
        def succeeding = ''

        and:
        def initialLine = preceding + initialValue + succeeding

        and:
        def matches = new ProcessPatternMatches(preceding, initialValue, succeeding)
        def process = new DecryptionProcessDefinition()

        1 * valueProcessor.processValue(initialValue, process.executor) >>
                { throw new EncryptionOperationNotPossibleException()}

        when:
        def metadata = matchedLineProcessor.process(initialLine, matches, process)

        then:
        0 * lineFormatter._

        with (metadata) {
            process.type == type

            initialValue == value.initial
            !value.processed

            initialLine == line.initial
            !line.processed

            error
        }
    }
}
