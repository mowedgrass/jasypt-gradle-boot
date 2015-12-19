package com.github.mowedgrass.jasyptgradleboot.action.text

import com.github.mowedgrass.jasyptgradleboot.action.EncryptionAction
import com.github.mowedgrass.jasyptgradleboot.task.text.DecryptTextTask

class DecryptTextAction extends EncryptionAction<DecryptTextTask> {

    public static final String TASK_DESCRIPTION = 'Decrypts given text'
    public static final String TASK_NAME = 'decryptText'

    public DecryptTextAction(Set<File> resourceFiles) {
        super(TASK_DESCRIPTION, resourceFiles)
    }
}
