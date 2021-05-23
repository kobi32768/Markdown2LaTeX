package io.github.kobi32768.md2latex

fun replaceHeader(lineIn: String): String {
    var line = lineIn
    if ("# " in line) {
        line = line.replace("### ", """\subsubsection{""")
        line = line.replace("## ", """\subsection{""")
        line = line.replace("# ", """\section{""")
        line = "$line}"
    }
    return line
}

fun replaceInlineCode(lineIn: String): String {
    var line = lineIn
    while ("`" in line) {
        line = line.replaceFirst("`", """\texttt{""")
        line = line.replaceFirst("`", "}")
    }
    return line
}

fun replaceDecoration(lineIn: String): String {
    var line = lineIn
    while ("**" in line) { // Bold
        line = line.replaceFirst("**", """\textbf{""")
        line = line.replaceFirst("**", "}")
    }
    while ("*" in line) { // Italic
        line = line.replaceFirst("*", """\textit{""")
        line = line.replaceFirst("*", "}")
    }
    // Underline
    line = line.replace("<u>", """\underline{""")
    line = line.replace("</u>", "}")

    return line
}

fun replaceNewline(lineIn: String): String {
    var line = lineIn
    if ("  " in line) {
        line = line.replace("  ", """\\""")
    }
    return line
}

fun replaceEscapes(lineIn: String): String {
    var line = lineIn
    line = line.replace("""$""", """\$""")
    return line
}
