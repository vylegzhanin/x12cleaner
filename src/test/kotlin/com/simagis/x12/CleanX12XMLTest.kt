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
class CleanX12XMLTest {
    @Test
    fun action() {
        object : CleanX12XML(arrayOf()) {
            override fun openInputStream(): InputStream = CleanX12XML::class.java.getResourceAsStream("CleanX12XML.test1.xml")
            override fun openOutputStream(): OutputStream = File("temp/CleanX12XML.test1.xml").outputStream()
        }.action()
    }
}