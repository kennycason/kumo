package com.kennycason.kumo.font

import com.kennycason.kumo.exception.KumoException
import java.awt.Font
import java.awt.FontFormatException
import java.awt.GraphicsEnvironment
import java.io.File
import java.io.IOException
import java.io.InputStream

class KumoFont {

    val font: Font

    constructor(type: String, weight: FontWeight) {
        this.font = Font(type, weight.weight, DEFAULT_WEIGHT)
    }

    constructor(font: Font) {
        this.font = font
    }

    constructor(file: File) : this(buildAndRegisterFont(file))

    constructor(inputStream: InputStream) : this(buildAndRegisterFont(inputStream))

    companion object {
        private const val DEFAULT_WEIGHT = 10

        private fun buildAndRegisterFont(file: File): Font {
            try {
                val font = Font.createFont(Font.TRUETYPE_FONT, file)
                registerFont(font)
                return font
            } catch (e: Exception) {
                when (e) {
                    is FontFormatException, is IOException ->
                        throw KumoException(e.message ?: "", e)
                    else -> throw e
                }
            }
        }

        private fun buildAndRegisterFont(inputStream: InputStream): Font {
            try {
                val font = Font.createFont(Font.TRUETYPE_FONT, inputStream)
                registerFont(font)
                return font
            } catch (e: Exception) {
                when (e) {
                    is FontFormatException, is IOException ->
                        throw KumoException(e.message ?: "", e)
                    else -> throw e
                }
            }
        }

        private fun registerFont(font: Font) {
            val graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment()
            graphicsEnvironment.registerFont(font)
        }
    }
}
