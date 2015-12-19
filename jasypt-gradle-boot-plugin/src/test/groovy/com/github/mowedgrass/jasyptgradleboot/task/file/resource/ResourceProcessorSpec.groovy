package com.github.mowedgrass.jasyptgradleboot.task.file.resource
import com.github.mowedgrass.jasyptgradleboot.task.file.metadata.FileMetadata
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.DecryptionProcessDefinition
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.EncryptionProcessDefinition
import spock.lang.Shared
import spock.lang.Specification

import static com.github.mowedgrass.jasyptgradleboot.task.file.metadata.PropertyMetadata.ofType
import static com.github.mowedgrass.jasyptgradleboot.task.file.process.ProcessRequest.request
import static com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessType.DECRYPT
import static com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessType.ENCRYPT

class ResourceProcessorSpec extends Specification {

    File original
    File preview
    BufferedReader reader
    BufferedWriter writer

    ResourceProcessingRewriter previewWriter
    FileSystemWrapper fileSystem
    ResourceProcessor resourceProcessor

    @Shared def encryptionProcess = new EncryptionProcessDefinition()
    @Shared def decryptionProcess = new DecryptionProcessDefinition()

    def setup() {
        original = Mock(File)
        preview = Mock(File)
        reader = Mock(BufferedReader)
        writer = Mock(BufferedWriter)

        previewWriter = Mock(ResourceProcessingRewriter)
        fileSystem = Mock(FileSystemWrapper)

        resourceProcessor = new ResourceProcessor(previewWriter, fileSystem)
    }

    def 'should read, process and write or delete property file'() {
        given:
        def request = request(process).build()
        def metadata = new FileMetadata()
        metadata.addLine(0, ofType(type).build())

        and:
        1 * fileSystem.createFrom(original, resourceProcessor.PREVIEW_EXTENSION) >> preview
        1 * fileSystem.getReader(original) >> reader
        1 * fileSystem.getWriter(preview) >> writer
        1 * previewWriter.write(reader, writer, request) >> metadata

        when:
        def actualMetadata = resourceProcessor.process(original, request, toPersist)

        then:
        persisted * fileSystem.backup(original, resourceProcessor.BACKUP_EXTENSION)
        persisted * fileSystem.move(original, preview)
        removed * fileSystem.remove(preview)

        metadata == actualMetadata

        where:
        process           | type    | toPersist |  persisted | removed
        encryptionProcess | ENCRYPT | true      |  1         | 0
        decryptionProcess | ENCRYPT | true      |  0         | 1
        encryptionProcess | DECRYPT | true      |  0         | 1
        decryptionProcess | DECRYPT | true      |  1         | 0
        encryptionProcess | ENCRYPT | false     |  0         | 1
        decryptionProcess | ENCRYPT | false     |  0         | 1
        encryptionProcess | DECRYPT | false     |  0         | 1
        decryptionProcess | DECRYPT | false     |  0         | 1
    }
}
