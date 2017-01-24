package com.simagis.x12

import java.io.*
import java.util.*

fun main(args: Array<String>) {
    if (args.size < 3) {
        println("Usage: UnbundleAndCleanX12XML <input-file-path> <loop-id> <output-directory>")
        return
    }

    val inputFile = File(args[0])
    val loopId = args[1]
    val tmpDir = File(".tmp").apply { mkdir() }
    val tmpX12Dir = tmpDir.resolve("X12-${UUID.randomUUID()}").createDirectory()
    val tmpXMLDir = tmpDir.resolve("XML-${UUID.randomUUID()}").createDirectory()

    try {
        ProcessBuilder("UnbundleX12.exe", inputFile.canonicalPath, loopId, tmpX12Dir.canonicalPath)
                .start()
                .waitForProcess()

        val outDir = File(args[2]).apply { mkdir() }
        tmpX12Dir.listFiles { pathname -> pathname?.isFile ?: false }.forEach { x12 ->
            println("processing $x12")
            val xml = tmpXMLDir.resolve("${x12.name}.xml")
            ProcessBuilder("X12Parser.exe", x12.canonicalPath, xml.canonicalPath)
                    .start()
                    .waitForProcess()

            val xmlOut = outDir.resolve(xml.name)
            when (loopId) {
                XmlCleanerX12C835.loopId -> XmlCleanerX12C835(arrayOf(xml.canonicalPath, xmlOut.canonicalPath)).clean()
                XmlCleanerX12C837.loopId -> XmlCleanerX12C837(arrayOf(xml.canonicalPath, xmlOut.canonicalPath)).clean()
            }
        }
    } finally {
        tmpX12Dir.deleteRecursively()
        tmpXMLDir.deleteRecursively()
    }
}

private fun Process.waitForProcess() {
    waitFor()
    if (exitValue() != 0) {
        println("Invalid exit code: ${exitValue()}")
        println("Program aborted")
        throw IOException("")
    }
}

private fun File.createDirectory(): File {
    if (!mkdir()) {
        println(if (exists()) "${this} already exists" else "unable to create ${this}")
        println("Program aborted")
        throw IOException("")
    }
    return this
}

