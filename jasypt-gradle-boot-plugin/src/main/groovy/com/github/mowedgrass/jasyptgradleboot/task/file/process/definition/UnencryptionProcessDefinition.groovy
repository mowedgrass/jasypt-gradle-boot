package com.github.mowedgrass.jasyptgradleboot.task.file.process.definition

class UnencryptionProcessDefinition extends DecryptionProcessDefinition {

    public static final String OUTPUT_FORMAT = 'ENCRYPT(%s)'

    public UnencryptionProcessDefinition() {
        super(OUTPUT_FORMAT)
    }
}
