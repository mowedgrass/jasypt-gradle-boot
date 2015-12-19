package com.github.mowedgrass.jasyptgradleboot

import com.github.mowedgrass.jasyptgradleboot.action.file.DecryptPropertiesAction
import com.github.mowedgrass.jasyptgradleboot.action.file.EncryptPropertiesAction
import com.github.mowedgrass.jasyptgradleboot.action.text.DecryptTextAction
import com.github.mowedgrass.jasyptgradleboot.action.text.EncryptTextAction
import com.github.mowedgrass.jasyptgradleboot.task.file.DecryptPropertiesTask
import com.github.mowedgrass.jasyptgradleboot.task.file.EncryptPropertiesTask
import com.github.mowedgrass.jasyptgradleboot.task.text.DecryptTextTask
import com.github.mowedgrass.jasyptgradleboot.task.text.EncryptTextTask
import org.gradle.api.Plugin
import org.gradle.api.Project

import static com.github.mowedgrass.jasyptgradleboot.password.PropertyPasswordProvider.PASSWORD_PROPERTY
import static com.github.mowedgrass.jasyptgradleboot.password.PropertyPasswordProvider.SHORT_PASSWORD_PROPERTY

public class EncryptPlugin implements Plugin<Project> {

    public static final String ENCRYPTOR_DEPENDENCY = 'com.github.mowedgrass:jasypt-gradle-boot-encryptor:0.1.32'
    public static final String[] DEPENDENT_TASKS = ['bootRun', 'integrationTest', 'test']

    public void apply(Project project) {
        configureTasks(project)
        configureProject(project)
    }

    private void configureTasks(Project project) {
        Set<File> resourceFiles = project.allprojects
                .findAll { it.hasProperty('sourceSets') }
                .collect { it.sourceSets.main.resources.files as Set<File> }
                .flatten()
                .toSet()

        project.tasks.with {
            addPlaceholderAction(EncryptTextAction.TASK_NAME, EncryptTextTask.class, new EncryptTextAction(resourceFiles))
            addPlaceholderAction(DecryptTextAction.TASK_NAME, DecryptTextTask.class, new DecryptTextAction(resourceFiles))
            addPlaceholderAction(EncryptPropertiesAction.TASK_NAME, EncryptPropertiesTask.class, new EncryptPropertiesAction(resourceFiles))
            addPlaceholderAction(DecryptPropertiesAction.TASK_NAME, DecryptPropertiesTask.class, new DecryptPropertiesAction(resourceFiles))
        }
    }

    private void configureProject(Project project) {
        if (project.subprojects) {
            project.subprojects { currentProject ->
                configureProperties(currentProject)
            }
        } else {
            configureProperties(project)
        }
    }

    private void configureProperties(Project project) {
        if (project.hasProperty('sourceSets')) {
            addDependencies(project)
            forwardPassword(project)
        }
    }

    private void addDependencies(Project project) {
        project.dependencies {
            compile ENCRYPTOR_DEPENDENCY
        }
    }

    private void forwardPassword(Project project) {
        project.afterEvaluate { currentProject ->
            currentProject.tasks
                    .findAll { DEPENDENT_TASKS.contains(it.name) }
                    .forEach { task ->
                task.doFirst {
                    systemProperty PASSWORD_PROPERTY, System.getProperty(PASSWORD_PROPERTY)
                    systemProperty SHORT_PASSWORD_PROPERTY, System.getProperty(SHORT_PASSWORD_PROPERTY)
                }
            }
        }
    }
}
