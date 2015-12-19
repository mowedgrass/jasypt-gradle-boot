package com.github.mowedgrass.jasyptgradleboot.task.file
import org.gradle.api.internal.tasks.options.Option

import static com.github.mowedgrass.jasyptgradleboot.task.file.process.ProcessRequest.*

public class DecryptPropertiesTask extends PropertiesEncryptionTask {

    DecryptPropertiesTask() {
        super(DECRYPT)
    }

    @Option(option = 'tag', description = 'format with encryption tag')
    public void setTag(boolean tag) {
        request = tag ? UNENCRYPT : DECRYPT
    }
}
