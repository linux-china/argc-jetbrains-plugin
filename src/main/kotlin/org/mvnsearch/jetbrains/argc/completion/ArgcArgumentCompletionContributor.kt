package org.mvnsearch.jetbrains.argc.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.DumbAware
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.sh.ShLanguage
import com.intellij.util.ProcessingContext
import org.mvnsearch.jetbrains.argc.parseArgcComment

/**
 * completion contributor for argc argument
 *
 * @author linux_china
 */
class ArgcArgumentCompletionContributor : CompletionContributor(), DumbAware {
    companion object {
        val ARGC_ENV_VARIABLES = mapOf(
            "SHELL_PATH" to "Specifies the path to the shell/bash executable used by Argc",
            "SCRIPT_NAME" to "Overrides the default script filename",
            "PWD" to "Current working directory",
            "CWORD" to "final word",
            "LAST_ARG" to "last argument",
        )
    }

    init {
        extend(
            CompletionType.BASIC, PlatformPatterns.psiElement(PsiElement::class.java).withLanguage(ShLanguage.INSTANCE),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val variable = parameters.position
                    if (shouldCompleteForVariable(variable.text)) {
                        val scriptFile = variable.containingFile
                        scriptFile?.let {
                            val argcArguments = parseArgcComment(scriptFile.text)
                            argcArguments.forEach {
                                val name = it.longName ?: it.shortName
                                if (it.description != null) {
                                    result.addElement(LookupElementBuilder.create("argc_${name}").withPresentableText("argc_${name} - ${it.description}"))
                                } else {
                                    result.addElement(LookupElementBuilder.create("argc_${name}"))
                                }
                            }
                        }
                    } else if (shouldCompleteForEnv(variable.text)) {
                        ARGC_ENV_VARIABLES.forEach {
                            result.addElement(LookupElementBuilder.create("ARGC_${it.key}"))
                        }
                    }
                }
            }
        )
    }


    fun shouldCompleteForVariable(variableName: String): Boolean {
        return variableName.startsWith("\$argc_")
                || variableName.startsWith("\${argc_")
                || variableName.startsWith("argc_")
    }

    fun shouldCompleteForEnv(variableName: String): Boolean {
        return variableName.startsWith("\$ARGC_")
                || variableName.startsWith("\${ARGC_")
                || variableName.startsWith("ARGC_")
    }
}