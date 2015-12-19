package com.github.mowedgrass.jasyptgradleboot.task.file.property

import com.github.mowedgrass.jasyptgradleboot.task.file.metadata.PropertyMetadata
import com.github.mowedgrass.jasyptgradleboot.task.file.property.pattern.ProcessPatternMatches
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessDefinition
import org.jasypt.exceptions.EncryptionOperationNotPossibleException

import static PropertyMetadata.ofType

class MatchedLineProcessor {

    private final PropertyValueProcessor valueProcessor
    private final PropertyLineFormatter lineFormatter

    MatchedLineProcessor(PropertyValueProcessor valueProcessor, PropertyLineFormatter lineFormatter) {
        this.valueProcessor = valueProcessor
        this.lineFormatter = lineFormatter
    }

    public PropertyMetadata process(String line, ProcessPatternMatches matches, ProcessDefinition process) {
        def metadataBuilder = ofType(process.type).withInitial(line, matches.value)

        try {
            def processedValue = valueProcessor.processValue(matches.value, process.executor)
            def processedLine = lineFormatter.format(matches, processedValue, process.outputFormat)

            metadataBuilder.withProcessed(processedLine, processedValue)
        } catch (EncryptionOperationNotPossibleException ignored) {
            metadataBuilder.withError()
        }

        metadataBuilder.build()
    }
}
