package com.simagis.x12

import java.io.*

fun main(args: Array<String>) {
    if (args.size < 3) {
        println("Usage: UnbundleAndCleanX12XML <input-file-path> <loop-id> <output-directory>")
        return
    }

    val inputFile = File(args[0])
    val loopId = args[1]
    val outDir = File(args[2]).createDirectory()
    val tempDir = outDir.resolve(".tmp").createDirectory()

    try {
        val process = ProcessBuilder("UnbundleX12.exe", inputFile.canonicalPath, loopId, tempDir.canonicalPath).apply {
            directory(File(".").canonicalFile)
            println("starting UnbundleX12.exe:")
            println("${directory()}>" + command().joinToString(separator = " ", transform = {
                if (it.contains(' ')) "\"$it\"" else it
            }))
        }.start()
        process.waitFor()
        if (process.exitValue() != 0) {
            println("Invalid UnbundleX12 exit code: ${process.exitValue()}")
            println("Program aborted")
            return
        }

        tempDir.listFiles { pathname -> pathname?.isFile ?: false }.forEach {
            println("processing $it")
            when (loopId) {
                XmlCleanerX12C835.loopId -> {
                    XmlCleanerX12C835(arrayOf(it.absolutePath, outDir.resolve(it.name).absolutePath)).clean()
                }
                XmlCleanerX12C837.loopId -> {
                    XmlCleanerX12C837(arrayOf(it.absolutePath, outDir.resolve(it.name).absolutePath)).clean()
                }
            }
        }
    } finally {
        tempDir.deleteRecursively()
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

