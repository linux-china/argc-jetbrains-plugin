package org.mvnsearch.jetbrains.argc

val ALL_TAGS = listOf("flag", "option", "arg", "describe", "cmd", "describe", "meta", "env", "alias")


data class ArgcArgument(val type: String, val shortName: String?, val longName: String?, val description: String?)

fun parseArgcComment(scriptText: String): List<ArgcArgument> {
    return scriptText.lines()
        .asSequence()
        .filter { it.startsWith("# @") }
        .map { it.substring(3) }
        .filter { it.startsWith("option") || it.startsWith("flag") || it.startsWith("arg") }
        .map { commentTag ->
            val parts = commentTag.split(" ")
            val type = parts[0]
            var shortName: String? = null
            var longName: String? = null
            var description: String? = null
            if (type == "arg") {
                val name = parts[1]
                val offset = name.indexOfFirst { !it.isLetterOrDigit() }
                if (offset > 0) {
                    longName = name.substring(0, offset)
                }  else {
                    longName = name
                }
                description = parts.subList(2, parts.size).joinToString(" ")
            } else {
                var descOffset = 1
                for (i in 1 until parts.size) {
                    if (!(parts[i].startsWith("-")
                                || parts[i].startsWith("+")
                                || parts[i].startsWith("<"))
                    ) {
                        descOffset = i
                        break
                    }
                }
                if (descOffset < parts.size) {
                    description = parts.subList(descOffset, parts.size).joinToString(" ")
                }
                if (descOffset > 1) {
                    for (i in 1 until descOffset) {
                        val part = parts[i]
                        if (part.startsWith("-") || part.startsWith("+")) {
                            var name = if (part.startsWith("--")) {
                                parts[i].substring(2)
                            } else {
                                part.substring(1)
                            }
                            val offset = name.indexOfFirst { !it.isLetterOrDigit() }
                            if (offset > 0) {
                                name = name.substring(offset)
                            }
                            if (name.length == 1) {
                                shortName = name
                            } else {
                                longName = name
                            }
                        }
                    }
                }
            }
            ArgcArgument(type, shortName, longName, description)
        }
        .filter { it.shortName != null || it.longName != null }
        .toList()
}