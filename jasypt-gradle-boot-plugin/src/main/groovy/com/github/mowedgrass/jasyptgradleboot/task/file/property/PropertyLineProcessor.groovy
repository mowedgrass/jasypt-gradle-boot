package com.github.mowedgrass.jasyptgradleboot.task.file.property
import com.github.mowedgrass.jasyptgradleboot.task.file.metadata.PropertyMetadata
import com.github.mowedgrass.jasyptgradleboot.task.file.property.pattern.ProcessPatternMatcher
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessDefinition

class PropertyLineProcessor {

    private final ProcessPatternMatcher patternMatcher
    private final MatchedLineProcessor matchedLineProcessor

    PropertyLineProcessor(ProcessPatternMatcher patternMatcher, MatchedLineProcessor matchedLineProcessor) {
        this.patternMatcher = patternMatcher
        this.matchedLineProcessor = matchedLineProcessor
    }

    public Optional<PropertyMetadata> process(String line, ProcessDefinition process) {
        def metadata = Optional.empty()

        patternMatcher.getMatches(line, process.linePattern).ifPresent { matches ->
            metadata = Optional.of(matchedLineProcessor.process(line, matches, process))
        }

        metadata
    }
}
