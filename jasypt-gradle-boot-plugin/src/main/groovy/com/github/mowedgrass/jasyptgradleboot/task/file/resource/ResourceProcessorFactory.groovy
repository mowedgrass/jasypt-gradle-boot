package com.github.mowedgrass.jasyptgradleboot.task.file.resource

import com.github.mowedgrass.jasyptgradleboot.task.file.property.MatchedLineProcessor
import com.github.mowedgrass.jasyptgradleboot.task.file.property.pattern.ProcessPatternMatcher
import com.github.mowedgrass.jasyptgradleboot.task.file.property.PropertyLineFormatter
import com.github.mowedgrass.jasyptgradleboot.task.file.property.PropertyLineProcessor
import com.github.mowedgrass.jasyptgradleboot.task.file.property.PropertyValueProcessor
import org.jasypt.encryption.StringEncryptor

class ResourceProcessorFactory {

    public create(StringEncryptor encryptor) {
        def matchedLineProcessor = new MatchedLineProcessor(
                new PropertyValueProcessor(encryptor), new PropertyLineFormatter())

        def lineProcessor = new PropertyLineProcessor(
                new ProcessPatternMatcher(), matchedLineProcessor)

        new ResourceProcessor(new ResourceProcessingRewriter(lineProcessor), new FileSystemWrapper())
    }
}
