package com.github.mowedgrass.jasyptgradleboot.task.file.property
import com.github.mowedgrass.jasyptgradleboot.task.file.property.pattern.ProcessPatternMatches

class PropertyLineFormatter {

    public String format(ProcessPatternMatches matches, String processedValue, String format) {
        matches.preceding + sprintf(format, processedValue) + matches.succeeding
    }
}
