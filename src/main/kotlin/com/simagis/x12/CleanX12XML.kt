package com.simagis.x12

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathExpression
import javax.xml.xpath.XPathFactory

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 1/24/2017.
 */
fun main(args: Array<String>) {
    CleanX12XML(args).action()
}

open class CleanX12XML(val args: Array<String>) {
    companion object {
        val domFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val xPathFactory: XPathFactory = XPathFactory.newInstance()
        val transformerFactory: TransformerFactory = TransformerFactory.newInstance()
        val CLP01: XPathExpression = xPathFactory.newXPath().compile(
                "/Interchange/FunctionGroup/Transaction/Loop[@LoopId=2000]/Loop[@LoopId=2100]/CLP/CLP01")
        val NM103: XPathExpression = xPathFactory.newXPath().compile(
                "/Interchange/FunctionGroup/Transaction/Loop[@LoopId=2000]/Loop[@LoopId=2100]/NM1/NM103")
        val NM104: XPathExpression = xPathFactory.newXPath().compile(
                "/Interchange/FunctionGroup/Transaction/Loop[@LoopId=2000]/Loop[@LoopId=2100]/NM1/NM104")
    }

    fun action() {
        val document: Document = read()
        transform(document)
        write(document)
    }

    private fun read(): Document = openInputStream().use {
        domFactory.newDocumentBuilder().parse(it)
    }

    private fun transform(document: Document) {
        (CLP01.evaluate(document, XPathConstants.NODE) as Node?)?.let { node ->
            val hashCode = node.textContent.hashCode()
            node.textContent = "BOW${hashCode}s"
        }

        (NM103.evaluate(document, XPathConstants.NODE) as Node?)?.let { node ->
            val hashCode = node.textContent.hashCode()
            node.textContent = "FirstName$hashCode"
        }

        (NM104.evaluate(document, XPathConstants.NODE) as Node?)?.let { node ->
            val hashCode = node.textContent.hashCode()
            node.textContent = "LastName$hashCode"
        }
    }

    private fun write(document: Document) = openOutputStream().use {
        transformerFactory.newTransformer().transform(DOMSource(document), StreamResult(it))
    }

    open fun openInputStream(): InputStream = File(args[0]).inputStream()

    open fun openOutputStream(): OutputStream = File(args[1]).outputStream()
}
