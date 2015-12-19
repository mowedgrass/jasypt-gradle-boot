package com.github.mowedgrass.jasyptgradleboot.task.file.process.definition
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.executor.DecryptExecutor

import static com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessType.DECRYPT

class DecryptionProcessDefinition extends ProcessDefinition {

    public static final String TAG_PATTERN = /ENC\((.*)\)/
    public static final String LINE_PATTERN = /^(.*[:=]\s*)$TAG_PATTERN(\s*)$/
    public static final String OUTPUT_FORMAT = '%s'

    public DecryptionProcessDefinition() {
        this(OUTPUT_FORMAT)
    }

    public DecryptionProcessDefinition(String format) {
        super(DECRYPT, TAG_PATTERN, LINE_PATTERN, format, new DecryptExecutor())
    }

}
