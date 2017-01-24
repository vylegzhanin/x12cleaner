package com.simagis.x12

import java.io.File

fun main(args: Array<String>) {
    if (args.size < 3) {
        println("Usage: UnbundleAndCleanX12XML <input-file-path> <LoopId> <output-directory>")
        return
    }

    val process = ProcessBuilder("UnbundleX12.exe", args[0], args[1], args[2]).start()
    process.waitFor()
    if (process.exitValue() != 0) {
        println("invalid UnbundleX12 exit code: ${process.exitValue()}")
        println("program aborted")
        return
    }
    val outDir = File("${args[2]}.cleaned")
    if (outDir.mkdir()) {
        println("$outDir already exists")
        println("program aborted")
        return
    }

    File(args[2]).listFiles().forEach {
        CleanX12XML(arrayOf(it.absolutePath, outDir.resolve(it.name).absolutePath)).action()
    }
}

