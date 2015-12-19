package com.github.mowedgrass.jasyptgradleboot.action.file

import com.github.mowedgrass.jasyptgradleboot.action.EncryptionAction
import com.github.mowedgrass.jasyptgradleboot.task.file.EncryptPropertiesTask

class DecryptPropertiesAction extends EncryptionAction<EncryptPropertiesTask> {

    public static final String TASK_DESCRIPTION = 'Decrypts properties from resources'
    public static final String TASK_NAME = 'decryptProperties'

    public DecryptPropertiesAction(Set<File> resourceFiles) {
        super(TASK_DESCRIPTION, resourceFiles)
    }
}
