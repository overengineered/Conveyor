package com.github.overengineered.conveyor

import org.gradle.api.Project
import org.gradle.api.Plugin

class GradlePlugin implements Plugin<Project> {
    void apply(Project target) {
        target.extensions.create("conveyor", GradleExtension)
        target.task('conveyorUpdate', type: GradleUpdateTask)
    }
}
