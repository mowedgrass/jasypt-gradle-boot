package com.github.mowedgrass.jasyptgradleboot.task.file.process.definition

enum ProcessType {

    ENCRYPT('encrypt'),
    DECRYPT('decrypt')

    public final String name

    ProcessType(String name) {
        this.name = name
    }
}
