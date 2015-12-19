package com.github.mowedgrass.jasyptgradleboot.task.file.process.definition

import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.executor.ProcessExecutor

abstract class ProcessDefinition {

    public final ProcessType type
    public final String tagPattern
    public final String linePattern
    public final String outputFormat
    public final ProcessExecutor executor

    ProcessDefinition(ProcessType type, String tagPattern, String linePattern, String outputFormat, ProcessExecutor executor) {
        this.type = type
        this.tagPattern = tagPattern
        this.linePattern = linePattern
        this.outputFormat = outputFormat
        this.executor = executor
    }
}
