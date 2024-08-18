package org.mvnsearch.jetbrains.argc.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.DumbAware
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiComment
import com.intellij.sh.ShLanguage
import com.intellij.util.ProcessingContext

/**
 * completion contributor for argc comment tag
 *
 * @author linux_china
 */
class ArgcCommentTagCompletionContributor : CompletionContributor(), DumbAware {

    companion object {
        val ARGC_TAGS = mapOf(
            "flag" to "Flag param",
            "option" to "Option param",
            "arg" to "Positional param",
            "describe" to "description for the command",
            "meta" to "metadata",
            "cmd" to "define a subcommand",
            "alias" to "set aliases for the subcommand",
            "env" to "define an environment variable",
        )
    }

    fun shouldCompleteForTag(commentText: String): Boolean {
        return commentText.startsWith("# @")
    }

    init {
        extend(
            CompletionType.BASIC, PlatformPatterns.psiElement(PsiComment::class.java).withLanguage(ShLanguage.INSTANCE),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val comment = parameters.position as PsiComment
                    if (shouldCompleteForTag(comment.text)) {
                        ARGC_TAGS.forEach {
                            result.addElement(
                                LookupElementBuilder.create(it.key + " ").appendTailText(" " + it.value, true)
                                    .withPresentableText(it.key)
                            )
                        }
                    }
                }
            }
        )
    }
}