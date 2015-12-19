package com.github.mowedgrass.jasyptgradleboot.task.file.process.definition
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.executor.EncryptExecutor

import static com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessType.ENCRYPT

class EncryptionProcessDefinition extends ProcessDefinition {

    public static final String TAG_PATTERN = /ENCRYPT\((.*)\)/
    public static final String LINE_PATTERN = /^(.*[:=]\s*)$TAG_PATTERN(\s*)$/
    public static final String OUTPUT_FORMAT = 'ENC(%s)'

    EncryptionProcessDefinition() {
        super(ENCRYPT, TAG_PATTERN, LINE_PATTERN, OUTPUT_FORMAT, new EncryptExecutor())
    }

}
