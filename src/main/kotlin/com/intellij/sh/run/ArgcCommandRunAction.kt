package com.intellij.sh.run

import com.intellij.execution.ExecutionManager
import com.intellij.execution.RunManager
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.sh.psi.ShFile

/**
 * ArgcCommand Run action
 *
 * @author linux_china
 */
class ArgcCommandRunAction(private val element: PsiElement) : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val runConfiguration: ShRunConfiguration
        val file = e.getData(CommonDataKeys.PSI_FILE) ?: return
        if (file !is ShFile) return
        val virtualFile = file.virtualFile ?: return
        val project: Project = file.project
        val configurationSettings =
            RunManager.getInstance(project).createConfiguration(file.name, ShConfigurationType::class.java)
        runConfiguration = configurationSettings.configuration as ShRunConfiguration
        runConfiguration.setScriptPath(virtualFile.path)
        runConfiguration.isExecuteScriptFile = true
        runConfiguration.scriptWorkingDirectory = virtualFile.parent.path
        runConfiguration.interpreterPath = "bash"
        val elementText = element.text
        val commandName = elementText.substring(0, elementText.indexOf("(")).trim()
        runConfiguration.scriptOptions = commandName
        val builder = ExecutionEnvironmentBuilder.createOrNull(
            DefaultRunExecutor.getRunExecutorInstance(),
            runConfiguration as RunConfiguration
        )
        if (builder != null) ExecutionManager.getInstance(project).restartRunProfile(builder.build())
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT;
    }
}