package com.github.mowedgrass.jasyptgradleboot.task.text
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.DecryptionProcessDefinition

class DecryptTextTask extends TextEncryptionTask {

    public DecryptTextTask() {
        super(new DecryptionProcessDefinition())
    }
}
