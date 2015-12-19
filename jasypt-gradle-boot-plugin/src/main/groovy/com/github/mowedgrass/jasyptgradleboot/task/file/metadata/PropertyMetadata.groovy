package com.github.mowedgrass.jasyptgradleboot.task.file.metadata

import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessType

class PropertyMetadata {

    public final ProcessType type
    public final ValueSnapshot value
    public final ValueSnapshot line
    public final boolean error

    private PropertyMetadata(ProcessType process, ValueSnapshot line, ValueSnapshot value, Boolean error) {
        this.type = process
        this.line = line
        this.value = value
        this.error = error
    }

    public static Builder ofType(ProcessType type) {
        new Builder(type)
    }

    private static class Builder {

        private ProcessType process
        private String value
        private String processedValue
        private String line
        private String processedLine

        private boolean error = false

        private Builder(ProcessType process) {
            this.process = process
        }

        public Builder withInitial(String line, String value) {
            this.value = value
            this.line = line

            this
        }

        public Builder withProcessed(String processedLine, String processedValue) {
            this.processedValue = processedValue
            this.processedLine = processedLine

            this
        }

        public Builder withError() {
            this.error = true

            this
        }

        public PropertyMetadata build() {
            new PropertyMetadata(process,
                    new ValueSnapshot(line, processedLine),
                    new ValueSnapshot(value, processedValue),
                    error
            )
        }
    }
}
