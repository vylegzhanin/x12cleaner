package com.simagis.x12

import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 *
 *
 * Created by alexei.vylegzhanin@gmail.com on 1/24/2017.
 */
class XmlCleanerX12C835Test {
    @Test
    fun action() {
        object : XmlCleanerX12C835(arrayOf()) {
            override fun openInputStream(): InputStream = XmlCleanerX12C835::class.java.getResourceAsStream("XmlCleanerX12C835.xml")
            override fun openOutputStream(): OutputStream = File("temp/XmlCleanerX12C835.test1.xml").outputStream()
        }.clean()
    }
}