package com.simagis.x12

import org.w3c.dom.Document

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 1/24/2017.
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: XmlCleanerX12C837 <input-file-path> [output-file-path]")
        return
    }
    XmlCleanerX12C837(args).clean()
}

open class XmlCleanerX12C837(args: Array<String>) : XmlCleaner(args) {
    companion object {
        val loopId = "2300"
    }

    override fun clean(document: Document) {
        TODO()
    }
}
