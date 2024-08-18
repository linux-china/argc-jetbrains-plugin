package org.mvnsearch.jetbrains.argc.highlight

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import org.mvnsearch.jetbrains.argc.ALL_TAGS


class ArgcCommentTagHighlighterAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is PsiComment) {
            val comment: String = element.text
            val commentTag = getArgcCommentTag(comment)
            if (commentTag != null) {
                val startOffset = element.textRange.startOffset + 3
                val endOffset = startOffset + commentTag.length + 1 // include @
                val range = TextRange(startOffset-1, endOffset)
                holder.newAnnotation(HighlightSeverity.INFORMATION, "Argc comment tag")
                    .range(range)
                    .textAttributes(DefaultLanguageHighlighterColors.MARKUP_TAG)
                    .create()
            }

        }

    }

    fun getArgcCommentTag(line: String): String? {
        if (line.startsWith("# @")) {
            var directive = line.substring(2)
            if (directive.contains(' ')) {
                directive = directive.substring(1, directive.indexOf(' '))
            }
            if (ALL_TAGS.contains(directive)) {
                return directive
            }
        }
        return null
    }
}
