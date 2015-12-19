package com.github.mowedgrass.jasyptgradleboot.task.file.metadata

class ValueSnapshot {

    public final String initial
    public final String processed

    public ValueSnapshot(String initial, String processed) {
        this.initial = initial
        this.processed = processed
    }
}
