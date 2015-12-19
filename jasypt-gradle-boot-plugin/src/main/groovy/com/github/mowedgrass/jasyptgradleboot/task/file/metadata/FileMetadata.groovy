package com.github.mowedgrass.jasyptgradleboot.task.file.metadata

import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessType

import java.util.function.BiConsumer

class FileMetadata {

    private final Map<Integer, PropertyMetadata> propertyMap = [:]

    public void addLine(Integer lineNo, PropertyMetadata metadata) {
        propertyMap.put(lineNo, metadata)
    }

    public PropertyMetadata getLine(int lineNo) {
        propertyMap.get(lineNo)
    }

    public Set<Map.Entry<Integer, PropertyMetadata>> lines() {
        propertyMap.entrySet()
    }

    public eachLine(BiConsumer<? super Integer, ? super PropertyMetadata> action) {
        propertyMap.forEach(action)
    }

    public boolean contains(ProcessType type) {
        propertyMap.any { lineNo, propertyPreview -> propertyPreview.type == type }
    }
}
