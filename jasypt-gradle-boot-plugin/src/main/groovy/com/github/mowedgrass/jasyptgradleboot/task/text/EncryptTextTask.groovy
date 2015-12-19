package com.github.mowedgrass.jasyptgradleboot.task.text
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.EncryptionProcessDefinition

class EncryptTextTask extends TextEncryptionTask {

    public EncryptTextTask() {
        super(new EncryptionProcessDefinition())
    }
}
