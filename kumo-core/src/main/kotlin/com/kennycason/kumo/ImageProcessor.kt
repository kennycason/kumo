package com.kennycason.kumo

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import javax.imageio.ImageIO

/**
 * Class to process image with a required size.
 */
object ImageProcessor {

    @JvmStatic
    fun getInstance(): ImageProcessor = this

    /**
     * @param fileName is the path of the file
     * @param imageType can define the type of image
     * @param width is the resized image's width
     * @param height is the resized image's height
     * @return the resized image in ByteArrayInputStream form
     */
    @JvmStatic
    @Throws(IOException::class)
    fun readImage(fileName: String, width: Int, height: Int, imageType: String): InputStream {
        val originImage = ImageIO.read(getInputStream(fileName))
        val scaledImage = originImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)
        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR)

        val graphics = bufferedImage.graphics
        try {
            graphics.drawImage(scaledImage, 0, 0, null)
        } finally {
            graphics.dispose()
        }

        val outputStream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, imageType, outputStream)
        return ByteArrayInputStream(outputStream.toByteArray())
    }

    private fun getInputStream(path: String): InputStream =
        Thread.currentThread().contextClassLoader.getResourceAsStream(path)
}
