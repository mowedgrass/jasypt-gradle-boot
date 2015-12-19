package com.github.mowedgrass.jasyptgradleboot.action.file

import com.github.mowedgrass.jasyptgradleboot.action.EncryptionAction
import com.github.mowedgrass.jasyptgradleboot.task.file.EncryptPropertiesTask

class EncryptPropertiesAction extends EncryptionAction<EncryptPropertiesTask> {

    public static final String TASK_DESCRIPTION = 'Encrypts properties from resources'
    public static final String TASK_NAME = 'encryptProperties'

    public EncryptPropertiesAction(Set<File> resourceFiles) {
        super(TASK_DESCRIPTION, resourceFiles)
    }
}
