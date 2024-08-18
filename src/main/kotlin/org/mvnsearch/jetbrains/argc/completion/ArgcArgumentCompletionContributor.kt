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
                                result.addElement(
                                    LookupElementBuilder.create(name!!).appendTailText("argc_${name}", true)
                                )
                            }
                        }
                    }
                }
            }
        )
    }


    fun shouldCompleteForVariable(variableName: String): Boolean {
        return variableName.startsWith("\$argc_") || variableName.startsWith("argc_")
    }
}