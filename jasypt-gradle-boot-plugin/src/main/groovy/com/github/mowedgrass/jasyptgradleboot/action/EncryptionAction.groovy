package com.github.mowedgrass.jasyptgradleboot.action

import com.github.mowedgrass.jasyptgradleboot.task.PasswordAwareTask
import org.gradle.api.Action

class EncryptionAction<T extends PasswordAwareTask> implements Action<T> {

    public static final String GROUP = "encryption"

    private String taskDescription
    private Set<File> resourceFiles

    public EncryptionAction(String taskDescription, Set<File> resourceFiles) {
        this.taskDescription = taskDescription
        this.resourceFiles = resourceFiles
    }

    public void execute(T task) {
        task.group = GROUP
        task.description = taskDescription
        task.resourceFiles = resourceFiles
    }
}