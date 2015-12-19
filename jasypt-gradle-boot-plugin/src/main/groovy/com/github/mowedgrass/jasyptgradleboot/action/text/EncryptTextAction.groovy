package com.github.mowedgrass.jasyptgradleboot.action.text

import com.github.mowedgrass.jasyptgradleboot.action.EncryptionAction
import com.github.mowedgrass.jasyptgradleboot.task.text.EncryptTextTask

class EncryptTextAction extends EncryptionAction<EncryptTextTask> {

    public static final String TASK_DESCRIPTION = 'Encrypts given text'
    public static final String TASK_NAME = 'encryptText'

    public EncryptTextAction(Set<File> resourceFiles) {
        super(TASK_DESCRIPTION, resourceFiles)
    }
}
