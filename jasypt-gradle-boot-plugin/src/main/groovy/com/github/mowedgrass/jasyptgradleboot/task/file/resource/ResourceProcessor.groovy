package com.github.mowedgrass.jasyptgradleboot.task.file.resource

import com.github.mowedgrass.jasyptgradleboot.task.file.process.ProcessRequest
import com.github.mowedgrass.jasyptgradleboot.task.file.metadata.FileMetadata

class ResourceProcessor {

    public static final PREVIEW_EXTENSION = '.preview'
    public static final BACKUP_EXTENSION = '.bak'

    private final ResourceProcessingRewriter processingRewriter
    private final FileSystemWrapper fileSystem

    public ResourceProcessor(ResourceProcessingRewriter processingRewriter, FileSystemWrapper fileSystem) {
        this.processingRewriter = processingRewriter
        this.fileSystem = fileSystem
    }

    public FileMetadata process(File original, ProcessRequest request, boolean persist) {
        def preview = fileSystem.createFrom(original, PREVIEW_EXTENSION)

        def metadata = processingRewriter.write(
                fileSystem.getReader(original), fileSystem.getWriter(preview), request)

        (persist && metadata.contains(request.primary.type)) ?
                persistPreview(original, preview) : removePreview(preview)

        metadata
    }

    protected persistPreview(File original, File preview) {
        fileSystem.backup(original, BACKUP_EXTENSION)
        fileSystem.move(preview, original)
    }

    protected removePreview(File output) {
        fileSystem.remove(output)
    }
}
