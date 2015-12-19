package com.github.mowedgrass.jasyptgradleboot.task.file.property
import com.github.mowedgrass.jasyptgradleboot.task.file.metadata.PropertyMetadata
import com.github.mowedgrass.jasyptgradleboot.task.file.property.MatchedLineProcessor
import com.github.mowedgrass.jasyptgradleboot.task.file.property.PropertyLineProcessor
import com.github.mowedgrass.jasyptgradleboot.task.file.property.pattern.ProcessPatternMatcher
import com.github.mowedgrass.jasyptgradleboot.task.file.property.pattern.ProcessPatternMatches
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.EncryptionProcessDefinition
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessDefinition
import spock.lang.Specification

class PropertyLineProcessorSpec extends Specification {

    ProcessPatternMatcher matcher
    MatchedLineProcessor matchedProcessor
    ProcessDefinition process
    PropertyLineProcessor lineProcessor

    def setup() {
        matcher = Mock(ProcessPatternMatcher)
        matchedProcessor = Mock(MatchedLineProcessor)
        process = new EncryptionProcessDefinition()

        lineProcessor = new PropertyLineProcessor(matcher, matchedProcessor)
    }

    def 'should match pattern and return metadata'() {
        given:
        def preceding = 'data: '
        def value = 'plain'
        def succeeding = ''
        def line = "${preceding}ENCRYPT(${value})$succeeding"

        and:
        def matches = new ProcessPatternMatches(preceding, value, succeeding)
        def metadata = PropertyMetadata.ofType(process.type).build()

        and:
        1 * matcher.getMatches(line, process.linePattern) >> Optional.of(matches)
        1 * matchedProcessor.process(line, matches, process) >> metadata

        when:
        def actualMetadata = lineProcessor.process(line, process)

        then:
        actualMetadata.isPresent()
        metadata == actualMetadata.get()
    }

    def 'should not match pattern and return empty metadata'() {
        given:
        def line = 'data: just text'

        and:
        1 * matcher.getMatches(line, process.linePattern) >> Optional.empty()

        when:
        def actualMetadata = lineProcessor.process(line, process)

        then:
        0 * matchedProcessor._

        and:
        !actualMetadata.isPresent()
    }
}
