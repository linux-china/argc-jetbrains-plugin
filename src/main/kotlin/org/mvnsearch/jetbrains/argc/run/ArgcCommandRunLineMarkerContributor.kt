package org.mvnsearch.jetbrains.argc.run

import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.sh.psi.ShCommandsList
import com.intellij.sh.run.ArgcCommandRunAction

/**
 * Argc Command Run Line Marker Contributor
 *
 * @author linux_china
 */
class ArgcCommandRunLineMarkerContributor : RunLineMarkerContributor(), DumbAware {
    override fun getInfo(element: PsiElement): Info? {
        if (element is ShCommandsList) {
            val psiFile = element.containingFile
            if (psiFile != null && psiFile.name == "Argcfile.sh") {
                val emptyLineElement = element.prevSibling
                if (emptyLineElement.text == "\n") {
                    val argcComment = emptyLineElement.prevSibling
                    if (argcComment.text.startsWith("# @")) {
                        val argcCommandRunAction = ArgcCommandRunAction(element)
                        return Info(
                            AllIcons.RunConfigurations.TestState.Run,
                            arrayOf(argcCommandRunAction),
                            { "Run Argc Command" })
                    }
                }
            }
        }
        return null
    }
}