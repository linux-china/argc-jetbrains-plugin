package org.mvnsearch.jetbrains.argc.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.util.childrenOfType


/**
 * Argc argument reference
 *
 * @author linux_china
 */
class ArgcArgumentReference : PsiReferenceBase<PsiElement>, PsiPolyVariantReference {

    constructor(element: PsiElement) : super(element)
    constructor(element: PsiElement, rangeInElement: TextRange) : super(element, rangeInElement)


    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val scriptFile = myElement.containingFile
        val results: MutableList<ResolveResult> = ArrayList()
        for (comment in scriptFile.childrenOfType<PsiElement>()) {
            val text = comment.text
            if (text.startsWith("# @")) {
                val argumentDeclare = text.substring(3)
                if (argumentDeclare.startsWith("option") || argumentDeclare.startsWith("flag")) {
                    val argumentName =
                        element.text.substring(rangeInElement.startOffset, rangeInElement.endOffset).substring(5)
                    if (argumentName.length == 1 && text.contains("-$argumentName")) {
                        results.add(PsiElementResolveResult(comment))
                    } else {
                        if (text.contains("--$argumentName")) {
                            results.add(PsiElementResolveResult(comment))
                        }
                    }
                } else if (argumentDeclare.startsWith("arg")) {
                    val argumentName =
                        element.text.substring(rangeInElement.startOffset, rangeInElement.endOffset).substring(5)
                    if (text.contains(" $argumentName")) {
                        results.add(PsiElementResolveResult(comment))
                    }
                }
            }
        }
        return results.toTypedArray()
    }

    override fun resolve(): PsiElement? {
        val resolveResults = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun getVariants(): Array<out Any> {
        return EMPTY_ARRAY
    }

}