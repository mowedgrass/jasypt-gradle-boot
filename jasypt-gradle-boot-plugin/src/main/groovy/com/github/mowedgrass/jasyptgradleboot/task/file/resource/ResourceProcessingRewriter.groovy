package com.github.mowedgrass.jasyptgradleboot.task.file.resource

import com.github.mowedgrass.jasyptgradleboot.task.file.metadata.FileMetadata
import com.github.mowedgrass.jasyptgradleboot.task.file.metadata.PropertyMetadata
import com.github.mowedgrass.jasyptgradleboot.task.file.property.PropertyLineProcessor
import com.github.mowedgrass.jasyptgradleboot.task.file.process.ProcessRequest

class ResourceProcessingRewriter {

    private PropertyLineProcessor lineProcessor

    public ResourceProcessingRewriter(PropertyLineProcessor lineProcessor) {
        this.lineProcessor = lineProcessor
    }

    public FileMetadata write(BufferedReader reader, BufferedWriter writer, ProcessRequest request) {
        def fileMetadata = new FileMetadata()
        def lineNo = 0

        try {
            reader.eachLine { line ->
                lineNo++

                def processingMetadata = processLine(line, request)
                def processedLine = getProcessedLine(line, processingMetadata, request)
                writer.writeLine(processedLine)

                processingMetadata.ifPresent { fileMetadata.addLine(lineNo, it) }
            }
        } finally {
            writer.close()
        }

        fileMetadata
    }

    private Optional<PropertyMetadata> processLine(String line, ProcessRequest request) {
        request
                .collect { process -> lineProcessor.process(line, process) }
                .findAll { metadata -> metadata.isPresent() }
                .collect { metadata -> metadata.get() }
                .stream()
                .findFirst()
    }

    private String getProcessedLine(String line, Optional<PropertyMetadata> metadata, ProcessRequest request) {
        def processType = request.primary.type

        (metadata.isPresent() && !metadata.get().error && metadata.get().type == processType) ?
                metadata.get().line.processed : line
    }
}
