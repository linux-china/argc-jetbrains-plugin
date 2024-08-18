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
            # @describe this is a test
        """.trimIndent()
        val argcArguments = parseArgcComment(scriptText)
        for (argcArgument in argcArguments) {
            println(argcArgument)
        }
    }
}