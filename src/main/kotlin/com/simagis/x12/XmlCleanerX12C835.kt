package com.simagis.x12

import org.w3c.dom.Document
import org.w3c.dom.Node
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathExpression

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 1/24/2017.
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: XmlCleanerX12C835 <input-file-path> [output-file-path]")
        return
    }
    XmlCleanerX12C835(args).clean()
}

open class XmlCleanerX12C835(args: Array<String>) : XmlCleaner(args) {
    companion object {
        val loopId = "2100"
        val CLP01: XPathExpression = xPathFactory.newXPath().compile(
                "/Interchange/FunctionGroup/Transaction/Loop[@LoopId=2000]/Loop[@LoopId=$loopId]/CLP/CLP01")
        val NM103: XPathExpression = xPathFactory.newXPath().compile(
                "/Interchange/FunctionGroup/Transaction/Loop[@LoopId=2000]/Loop[@LoopId=$loopId]/NM1/NM103")
        val NM104: XPathExpression = xPathFactory.newXPath().compile(
                "/Interchange/FunctionGroup/Transaction/Loop[@LoopId=2000]/Loop[@LoopId=$loopId]/NM1/NM104")
    }

    override fun clean(document: Document) {
        (CLP01.evaluate(document, XPathConstants.NODE) as Node?)?.let { node ->
            val hashCode = node.textContent.hashCode()
            node.textContent = "BOW${hashCode}s"
        }

        (NM103.evaluate(document, XPathConstants.NODE) as Node?)?.let { node ->
            val hashCode = node.textContent.hashCode()
            node.textContent = "LastName$hashCode"
        }

        (NM104.evaluate(document, XPathConstants.NODE) as Node?)?.let { node ->
            val hashCode = node.textContent.hashCode()
            node.textContent = "FirstName$hashCode"
        }
    }
}
