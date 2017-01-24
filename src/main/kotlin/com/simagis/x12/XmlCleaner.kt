package com.simagis.x12

import org.w3c.dom.Document
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.xpath.XPathFactory

abstract class XmlCleaner(val args: Array<String>) {
    fun clean() {
        val document: Document = read()
        clean(document)
        write(document)
    }

    private fun read(): Document = openInputStream().use {
        domFactory.newDocumentBuilder().parse(it)
    }

    abstract fun clean(document: Document)

    private fun write(document: Document) = openOutputStream().use {
        transformerFactory.newTransformer().transform(DOMSource(document), StreamResult(it))
    }

    open fun openInputStream(): InputStream = File(args[0]).inputStream()

    open fun openOutputStream(): OutputStream = when {
        args.size == 1 -> File(args[0] + ".out.xml").outputStream()
        args.size == 2 -> File(args[1]).outputStream()
        else -> throw AssertionError()
    }

    companion object {
        val domFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val xPathFactory: XPathFactory = XPathFactory.newInstance()
        val transformerFactory: TransformerFactory = TransformerFactory.newInstance()
    }
}