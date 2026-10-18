package com.kennycason.kumo

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import javax.imageio.ImageIO
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

class ImageProcessorTest {

    @Test
    fun resizesImageAndClosesResource() {
        val bytes = ByteArrayOutputStream().also {
            ImageIO.write(BufferedImage(8, 6, BufferedImage.TYPE_INT_ARGB), "png", it)
        }.toByteArray()
        val resource = object : ByteArrayInputStream(bytes) {
            var closed = false
            override fun close() {
                closed = true
                super.close()
            }
        }

        withResource(resource) {
            val image = ImageProcessor.readImage("image.png", 4, 3, "png").use { ImageIO.read(it) }
            expectThat(image) {
                get { width }.isEqualTo(4)
                get { height }.isEqualTo(3)
            }
        }
        expectThat(resource.closed).isTrue()
    }

    @Test
    fun closesResourceWhenReadingFails() {
        val resource = object : InputStream() {
            var closed = false
            override fun read(): Int = throw IOException("Cannot read image")
            override fun close() {
                closed = true
            }
        }

        withResource(resource) {
            expectThrows<IOException> {
                ImageProcessor.readImage("image.png", 4, 3, "png")
            }
        }
        expectThat(resource.closed).isTrue()
    }

    private fun withResource(resource: InputStream, block: () -> Unit) {
        val thread = Thread.currentThread()
        val originalLoader = thread.contextClassLoader
        thread.contextClassLoader = object : ClassLoader(originalLoader) {
            override fun getResourceAsStream(name: String): InputStream? =
                if (name == "image.png") resource else super.getResourceAsStream(name)
        }
        try {
            block()
        } finally {
            thread.contextClassLoader = originalLoader
        }
    }
}
