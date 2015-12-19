package com.github.mowedgrass.jasyptgradleboot.task.file.property

import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.executor.ProcessExecutor
import org.jasypt.encryption.StringEncryptor

class PropertyValueProcessor {

    protected final StringEncryptor encryptor

    PropertyValueProcessor(StringEncryptor encryptor) {
        this.encryptor = encryptor
    }

    public String processValue(String value, ProcessExecutor executor) {
        executor.process(value, encryptor)
    }
}
