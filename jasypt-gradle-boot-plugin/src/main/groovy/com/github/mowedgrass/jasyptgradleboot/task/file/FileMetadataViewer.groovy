package com.github.mowedgrass.jasyptgradleboot.task.file
import com.github.mowedgrass.jasyptgradleboot.task.ConsoleOutput
import com.github.mowedgrass.jasyptgradleboot.task.file.metadata.FileMetadata
import com.github.mowedgrass.jasyptgradleboot.task.file.metadata.PropertyMetadata
import com.github.mowedgrass.jasyptgradleboot.task.file.process.ProcessRequest

class FileMetadataViewer {

    protected ConsoleOutput output

    public FileMetadataViewer(ConsoleOutput output) {
        this.output = output
    }

    public void print(File file, FileMetadata fileMetadata, ProcessRequest request) {
        output.info(" * ${file.name}")

        fileMetadata.eachLine { lineNo, propertyMetadata ->
            if (filterProperties(propertyMetadata, request)) {
                printProperty(lineNo, propertyMetadata)
            }
        }
    }

    protected boolean filterProperties(PropertyMetadata metadata, ProcessRequest request) {
        (metadata.error || metadata.type == request.primary.type)
    }

    protected void printProperty(Integer lineNo, PropertyMetadata metadata) {
        printPropertyDetails(lineNo, metadata)
        printPropertyWarning(metadata)
    }

    private void printPropertyDetails(int lineNumber, PropertyMetadata metadata) {
        def log = new StringBuilder()
                .append("${lineNumber}".padLeft(6))
                .append(": ${metadata.value.initial}")

        if (!metadata.error) {
            log.append(" -> ${metadata.value.processed}")
        }

        output.plain(log)
    }

    private void printPropertyWarning(PropertyMetadata metadata) {
        if (metadata.error) {
            def message = "Warning: unable to ${metadata.type.name} this entry with given password"
            output.error(" ".multiply(8) + message)
        }
    }
}
