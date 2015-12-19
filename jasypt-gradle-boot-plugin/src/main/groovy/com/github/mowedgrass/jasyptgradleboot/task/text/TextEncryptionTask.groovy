package com.github.mowedgrass.jasyptgradleboot.task.text
import com.github.mowedgrass.jasyptgradleboot.task.PasswordAwareTask
import com.github.mowedgrass.jasyptgradleboot.task.file.process.definition.ProcessDefinition
import org.gradle.api.internal.tasks.options.Option
import org.jasypt.encryption.StringEncryptor

abstract class TextEncryptionTask extends PasswordAwareTask<String> {

    public String text
    public final ProcessDefinition processDefinition

    TextEncryptionTask(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition
    }

    protected validateOptions() {
        super.validateOptions()
        text || printHelpAndStopExecution("'text' parameter missing")
    }

    @Override
    final protected String doTaskAction(StringEncryptor encryptor) {
        String processed = process(text, encryptor)
        printOutput(text, processed)

        processed
    }

    private String process(String text, StringEncryptor encryptor) {
        def filtered = text.replaceFirst(/^${processDefinition.tagPattern}$/, '$1')

        processDefinition.executor.process(filtered, encryptor)
    }

    protected void printOutput(String initial, String processed) {
        def summary = "Processed '$initial'"
        summary <<= showPassword ? " with password '$password':" : ':'

        output.plain(summary)
        output.info(sprintf(processDefinition.outputFormat, processed))
    }

    @Option(option = 'text', description = 'text to write [required]')
    public void setText(String text) {
        this.text = text;
    }
}