package com.github.mowedgrass.jasyptgradleboot.task.file.resource

import com.github.mowedgrass.jasyptgradleboot.task.file.process.ProcessRequest

class ResourceFilter {

    private static final EXTENSIONS = ['yml', 'properties']

    private FileSystemWrapper fileSystem

    public ResourceFilter(FileSystemWrapper fileSystem) {
        this.fileSystem = fileSystem
    }

    public boolean filter(File file, ProcessRequest request) {
        matchExtension(file) && matchAnyLine(file, request)
    }

    private boolean matchExtension(File file) {
        EXTENSIONS.contains(file.name.tokenize('.').last().toLowerCase())
    }

    private boolean matchAnyLine(File file, ProcessRequest request) {
        fileSystem
                .getReader(file)
                .readLines()
                .find { it -> matchLine(it, request) }
    }

    protected boolean matchLine(String line, ProcessRequest request) {
        request.any { process ->
            line.matches(process.linePattern)
        }
    }
}
