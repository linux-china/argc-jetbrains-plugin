package org.mvnsearch.jetbrains.argc

import junit.framework.TestCase

/**
 * Created with IntelliJ IDEA.
 *
 * @author linux_china
 */
class ArgcTest : TestCase() {

    fun testParse() {
        val scriptText = """
            # @option -v --version show version
            # @option -h --help show help
            # @flag -d debug mode
            # @arg <file> file name
            # @arg val*    Positional param
            # @describe this is a test
        """.trimIndent()
        val argcArguments = parseArgcComment(scriptText)
        for (argcArgument in argcArguments) {
            println(argcArgument)
        }
    }

    fun testIdentifier() {
        assertTrue(!'#'.isJavaIdentifierPart())
        assertTrue(!'/'.isJavaIdentifierPart())
        assertTrue(!'-'.isJavaIdentifierPart())
        assertTrue('9'.isJavaIdentifierPart())
        assertTrue('T'.isJavaIdentifierPart())
        assertTrue('t'.isJavaIdentifierPart())
        assertTrue('_'.isJavaIdentifierPart())
    }
}