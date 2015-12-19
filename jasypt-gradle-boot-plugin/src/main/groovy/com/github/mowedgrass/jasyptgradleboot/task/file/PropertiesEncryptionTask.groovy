package com.github.mowedgrass.jasyptgradleboot.task.file
import com.github.mowedgrass.jasyptgradleboot.task.PasswordAwareTask
import com.github.mowedgrass.jasyptgradleboot.task.file.metadata.FileMetadata
import com.github.mowedgrass.jasyptgradleboot.task.file.resource.FileSystemWrapper
import com.github.mowedgrass.jasyptgradleboot.task.file.process.ProcessRequest
import com.github.mowedgrass.jasyptgradleboot.task.file.resource.ResourceFilter
import com.github.mowedgrass.jasyptgradleboot.task.file.resource.ResourceProcessor
import com.github.mowedgrass.jasyptgradleboot.task.file.resource.ResourceProcessorFactory
import org.gradle.api.internal.tasks.options.Option
import org.jasypt.encryption.StringEncryptor

abstract class PropertiesEncryptionTask extends PasswordAwareTask<Map<File, FileMetadata>> {

    protected boolean confirm

    protected ResourceFilter resourceFilter
    protected ResourceProcessorFactory resourceProcessorFactory
    protected ResourceProcessor resourceProcessor
    protected ProcessRequest request
    protected FileMetadataViewer metadataViewer

    PropertiesEncryptionTask(ProcessRequest request) {
        this.metadataViewer = new FileMetadataViewer(output)
        this.request = request
        this.resourceFilter = new ResourceFilter(new FileSystemWrapper())
        this.resourceProcessorFactory = new ResourceProcessorFactory()
    }

    @Override
    protected Map<File, FileMetadata> doTaskAction(StringEncryptor encryptor) {
        resourceProcessor = resourceProcessorFactory.create(encryptor)

        def files = resourceFiles.findAll { resourceFilter.filter(it, request) }
        if (files.size() && showPassword) {
            output.plain("\nProcessing with password '${password}':")
        }
        def changes = files.collectEntries { file -> [file, processFile(file, request)] }

        if (!confirm && files.size()) {
            output.warn("\nFor confirmation run task with parameter '--confirm'")
        }

        changes
    }

    protected FileMetadata processFile(File file, ProcessRequest request) {
        def FileMetadata fileMetadata = resourceProcessor.process(file, request, confirm)
        metadataViewer.print(file, fileMetadata, request)

        fileMetadata
    }

    @Option(option = 'confirm', description = 'confirm changes')
    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }
}
