package com.github.mowedgrass.jasyptgradleboot.task.file

import static com.github.mowedgrass.jasyptgradleboot.task.file.process.ProcessRequest.ENCRYPT

public class EncryptPropertiesTask extends PropertiesEncryptionTask {

    EncryptPropertiesTask() {
        super(ENCRYPT)
    }
}
