package com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.executor

import org.jasypt.encryption.StringEncryptor

class EncryptExecutor implements ProcessExecutor{

    @Override
    String process(String string, StringEncryptor encryptor) {
        return encryptor.encrypt(string)
    }
}
