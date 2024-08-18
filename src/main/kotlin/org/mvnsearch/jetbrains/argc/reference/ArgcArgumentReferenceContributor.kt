package org.mvnsearch.jetbrains.argc.reference

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.sh.ShLanguage
import com.intellij.sh.psi.ShString
import com.intellij.util.ProcessingContext


/**
 * Argc argument reference contributor
 *
 * @author linux_china
 */
class ArgcArgumentReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(ShString::class.java)
            .withLanguage(ShLanguage.INSTANCE),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<out PsiReference> {
                    val text = element.text
                    if (text.startsWith("\"")) {
                        if (text.startsWith("\"\${argc_")
                            || text.contains("argc_")
                        ) {
                            val scriptFile = element.containingFile
                            if (scriptFile != null) {
                                val offset = text.indexOf("argc_")
                                var endOffset = text.length - 1
                                if (text.indexOf(' ', offset) > 0) {
                                    endOffset = text.indexOf(' ', offset)
                                }
                                if (text[endOffset-1] == '}') {
                                    endOffset -= 1
                                }
                                val textRange = TextRange(offset, endOffset)
                                return arrayOf(ArgcArgumentReference(element, textRange))
                            }
                        }
                    }

                    return PsiReference.EMPTY_ARRAY
                }

            })
    }
}