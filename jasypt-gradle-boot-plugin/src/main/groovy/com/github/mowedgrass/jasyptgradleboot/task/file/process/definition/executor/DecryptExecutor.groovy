package com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.executor

import org.jasypt.encryption.StringEncryptor

class DecryptExecutor implements ProcessExecutor{

    @Override
    String process(String string, StringEncryptor encryptor) {
        return encryptor.decrypt(string)
    }
}
