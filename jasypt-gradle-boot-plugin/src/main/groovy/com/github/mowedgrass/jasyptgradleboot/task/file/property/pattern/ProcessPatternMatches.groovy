package com.github.mowedgrass.jasyptgradleboot.task.file.property.pattern

class ProcessPatternMatches {

    public final String preceding
    public final String value
    public final String succeeding

    public ProcessPatternMatches(String preceding, String value, String succeeding) {
        this.preceding = preceding
        this.value = value
        this.succeeding = succeeding
    }
}
