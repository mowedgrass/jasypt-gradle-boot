package com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.executor

import org.jasypt.encryption.StringEncryptor

interface ProcessExecutor {

    public String process(String string, StringEncryptor encryptor)
}
