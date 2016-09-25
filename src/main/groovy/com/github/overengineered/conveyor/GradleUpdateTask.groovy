package com.github.overengineered.conveyor

import org.apache.commons.io.FilenameUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GradleUpdateTask extends DefaultTask {
    @TaskAction
    def update() {
        Engine engine = new Engine();
        project.files(project.conveyor.files).each { file ->
            engine.update(file)
        }
    }
}
