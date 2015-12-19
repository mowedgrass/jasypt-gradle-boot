package com.github.mowedgrass.jasyptgradleboot.task.file.process

import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.DecryptionProcessDefinition
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.EncryptionProcessDefinition
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessDefinition
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.UnencryptionProcessDefinition

class ProcessRequest implements Iterable<ProcessDefinition> {

    public static final ENCRYPT = request(new EncryptionProcessDefinition())
            .with(new DecryptionProcessDefinition())
            .build()

    public static final DECRYPT = request(new DecryptionProcessDefinition()).build()

    public static final UNENCRYPT = request(new UnencryptionProcessDefinition()).build()

    public final ProcessDefinition primary

    public final ProcessDefinition validation

    private ProcessRequest(ProcessDefinition primary, ProcessDefinition validation) {
        this.primary = primary
        this.validation = validation
    }

    @Override
    public Iterator<ProcessDefinition> iterator() {
        [primary, validation]
                .findAll { it }
                .iterator()
    }

    public static final request(ProcessDefinition process) {
        new Builder(process)
    }

    private static class Builder {

        private ProcessDefinition primary
        private ProcessDefinition validation = null

        public Builder(ProcessDefinition primary) {
            this.primary = primary
        }

        public Builder with(ProcessDefinition validation) {
            this.validation = validation

            this
        }

        public ProcessRequest build() {
            new ProcessRequest(primary, validation)
        }
    }
}
