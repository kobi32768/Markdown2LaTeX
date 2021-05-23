package io.github.kobi32768.md2latex

import java.nio.file.*
import java.io.IOException
import java.nio.charset.Charset
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.io.FileAlreadyExistsException
import kotlin.io.NoSuchFileException

fun main() {
    println("Enter Markdown file path")
    var pathIn = readLine()!!
    if (pathIn == "") { println("Invalid path"); return }
    if ("." !in pathIn) pathIn = "$pathIn.md"
    if (pathIn.split(".").last() != "md") return

    val file: String
    val texFilename: String
    val savePath: String
    try {
        val pathDocument = Paths.get(pathIn)
        val pathHeader = Paths.get(ClassLoader.getSystemResource("header.txt").toURI())
        val pathFooter = Paths.get(ClassLoader.getSystemResource("footer.txt").toURI())
        val streamD = Files.lines(pathDocument)
        val streamH = Files.lines(pathHeader)
        val streamF = Files.lines(pathFooter)
        val sb = StringBuilder()

        streamH.forEach { sb.append( "$it\n" ) }
        sb.append( "\n" )
        streamD.forEach { sb.append( replaceElements(it) ) }
        streamF.forEach { sb.append( "$it\n" ) }
        file = sb.toString()

        val mdFilename = pathIn.split("/").last()
        savePath = pathIn.replace(mdFilename, "")
        texFilename = mdFilename.replace(".md", ".tex")
        val texFile = Paths.get(savePath, texFilename)

        if (Files.exists(texFile))
            Files.delete(texFile)
        Files.createFile(texFile)
        Files.write(texFile, file.toByteArray(Charsets.UTF_8), StandardOpenOption.WRITE)
    } catch (ex: InvalidPathException) {
        println("Invalid path"); return
    } catch (ex: NoSuchFileException) {
        println("No such file"); return
    } catch (ex: Exception) {
        ex.printStackTrace(); return
    }

    // There is unreachable if exception were caught
    println("Convert succeed")
    println("Path: $savePath$texFilename")
}

fun replaceElements(lineIn: String): String {
    var line = lineIn
    line = replaceHeader(line)
    line = replaceInlineCode(line)
    line = replaceDecoration(line)
    line = replaceNewline(line)
    line = replaceEscapes(line)
    return "$line\n"
}