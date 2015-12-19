package com.github.mowedgrass.jasyptgradleboot.task

import com.github.mowedgrass.jasyptgradleboot.encryptor.EncryptorFactory
import com.github.mowedgrass.jasyptgradleboot.encryptor.configuration.provider.FileConfigurationProvider
import com.github.mowedgrass.jasyptgradleboot.password.DirectPasswordProvider
import com.github.mowedgrass.jasyptgradleboot.task.file.metadata.FileMetadata
import org.jasypt.encryption.StringEncryptor
import spock.lang.Specification

import java.security.MessageDigest

abstract class BaseTaskIntSpec extends Specification {

    protected File getConfigurationFileMock(Map<String, String> properties) {
        Mock(File).with { file ->
            1 * file.name >> 'jasypt.properties'
            1 * file.path >> getResourceFile('.properties', properties).path

            file
        }
    }

    protected File getResourceFile(String extension, Map<String, String> properties) {
        File.createTempFile('encryptPluginTest', ".$extension").with { file ->
            deleteOnExit()
            withWriter { writer ->
                properties.forEach { key, value -> writer.writeLine("$key=$value") }
            }

            file
        }
    }

    protected StringEncryptor getEncryptor(String password, Map<String, String> properties) {
        def configurationFile = getResourceFile('.properties', properties)
        def configurationProvider = new FileConfigurationProvider(new FileInputStream(configurationFile))

        new EncryptorFactory().create(new DirectPasswordProvider(password), configurationProvider)
    }

    protected String decrypt(String password, String text) {
        getEncryptor(password, [:]).decrypt(text)
    }

    protected String generateChecksum(File file) {
        MessageDigest.getInstance('MD5').digest(file.text.bytes).encodeHex().toString()
    }

    protected List<Integer> getLineNumbers(FileMetadata fileProcessPreview) {
        fileProcessPreview.lines().collect { e -> e.key }
    }
}
