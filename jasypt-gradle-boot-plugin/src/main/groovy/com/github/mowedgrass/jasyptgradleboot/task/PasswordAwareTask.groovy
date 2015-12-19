package com.github.mowedgrass.jasyptgradleboot.task

import org.gradle.api.DefaultTask
import org.gradle.api.internal.tasks.options.Option
import org.gradle.api.internal.tasks.options.OptionValidationException
import org.gradle.api.tasks.TaskAction
import org.gradle.logging.StyledTextOutputFactory
import org.jasypt.encryption.StringEncryptor

import static com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.provider.ClassPathConfigurationProvider.CONFIGURATION_FILE

abstract class PasswordAwareTask<T> extends DefaultTask {

    protected String password
    protected Set<File> resourceFiles
    protected boolean showPassword = false

    protected ConsoleOutput output
    protected ConfiguredEncryptorFactory encryptorFactory

    public PasswordAwareTask() {
        encryptorFactory = new ConfiguredEncryptorFactory()
        output = new ConsoleOutput(services.get(StyledTextOutputFactory))
    }

    @TaskAction
    public T action() {
        def passwordProvider = encryptorFactory.createPasswordProvider(password)
        def configurationFile = resourceFiles.find { it.name == CONFIGURATION_FILE }
        def encryptor = encryptorFactory.createEncryptor(configurationFile, passwordProvider)

        password = passwordProvider.getPassword()
        validateOptions()

        doTaskAction(encryptor)
    }

    protected validateOptions() {
        password || printHelpAndStopExecution("'password' parameter missing")
    }

    protected abstract T doTaskAction(StringEncryptor encryptor)

    protected printHelpAndStopExecution(String message) {
        if (project.tasks.names.contains('help')) {
            project.help {
                setTaskPath(this.name)
                execute()
            }
        }

        throw new OptionValidationException(message)
    }

    public String getPassword() {
        return password
    }

    @Option(option = 'password', description = 'password [required] - will be loaded from environment if not given as argument')
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getShowPassword() {
        return showPassword
    }

    @Option(option = 'show-password', description = 'show password in result')
    public void setShowPassword(boolean showPassword) {
        this.showPassword = showPassword;
    }

    public Set<File> getResourceFiles() {
        return resourceFiles
    }

    public void setResourceFiles(Set<File> resourceFiles) {
        this.resourceFiles = resourceFiles;
    }
}
