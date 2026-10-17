package com.kennycason.kumo.cli

import com.beust.jcommander.JCommander
import com.kennycason.kumo.LayeredWordCloud
import com.kennycason.kumo.PolarWordCloud
import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.WordFrequency
import com.kennycason.kumo.bg.Background
import com.kennycason.kumo.bg.PixelBoundaryBackground
import com.kennycason.kumo.cli.CliParameters.FontScalarType
import com.kennycason.kumo.cli.CliParameters.NormalizerType
import com.kennycason.kumo.cli.CliParameters.WordStartType
import com.kennycason.kumo.exception.KumoException
import com.kennycason.kumo.font.FontWeight
import com.kennycason.kumo.font.KumoFont
import com.kennycason.kumo.font.scale.FontScalar
import com.kennycason.kumo.font.scale.LinearFontScalar
import com.kennycason.kumo.font.scale.LogFontScalar
import com.kennycason.kumo.font.scale.SqrtFontScalar
import com.kennycason.kumo.logging.Logging
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.nlp.normalize.BubbleTextNormalizer
import com.kennycason.kumo.nlp.normalize.CharacterStrippingNormalizer
import com.kennycason.kumo.nlp.normalize.LowerCaseNormalizer
import com.kennycason.kumo.nlp.normalize.Normalizer
import com.kennycason.kumo.nlp.normalize.TrimToEmptyNormalizer
import com.kennycason.kumo.nlp.normalize.UpsideDownNormalizer
import com.kennycason.kumo.nlp.normalize.UpperCaseNormalizer
import com.kennycason.kumo.nlp.tokenizer.core.WhiteSpaceWordTokenizer
import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer
import com.kennycason.kumo.nlp.tokenizers.EnglishWordTokenizer
import com.kennycason.kumo.palette.ColorPalette
import com.kennycason.kumo.wordstart.CenterWordStart
import com.kennycason.kumo.wordstart.RandomWordStart
import com.kennycason.kumo.wordstart.WordStartStrategy
import java.awt.Dimension
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.jar.Manifest

class KumoCli : Logging {
    private val cliParameters = CliParameters()

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            KumoCli().runWithArguments(args)
        }

        private fun buildBackground(background: String): Background {
            try {
                return PixelBoundaryBackground(toInputStream(background))
            } catch (e: IOException) {
                throw KumoException("Failed to load background: $background", e)
            }
        }

        private fun buildWordStart(wordStartType: WordStartType): WordStartStrategy = when (wordStartType) {
            WordStartType.CENTER -> CenterWordStart()
            WordStartType.RANDOM -> RandomWordStart()
        }

        private fun toInputStream(path: String): InputStream {
            val file = File(path)
            if (file.exists() && !file.isDirectory) {
                return FileInputStream(file)
            }
            try {
                return URL(path).openStream()
            } catch (_: IOException) {
            }
            throw KumoException("Input path [$path] is not a valid file or URL")
        }
    }

    fun runWithArguments(args: Array<String>) {
        if (args.isNotEmpty() && args[0] == "--version") {
            printVersion()
            return
        }
        JCommander(cliParameters).parse(*args)
        when (cliParameters.type) {
            CliParameters.Type.STANDARD -> buildStandardWordCloud()
            CliParameters.Type.POLAR -> buildPolarWordCloud()
            CliParameters.Type.LAYERED -> buildLayeredWordCloud()
        }
    }

    private fun printVersion() {
        try {
            val resources = javaClass.classLoader.getResources("META-INF/MANIFEST.MF")
            while (resources.hasMoreElements()) {
                val manifest = Manifest(resources.nextElement().openStream())
                val version = manifest.mainAttributes.getValue("Implementation-Version")
                if (!version.isNullOrBlank()) {
                    logger.info { "Kumo Version: $version" }
                    return
                }
            }
            logger.warn { "Failed to load version from manifest" }
        } catch (e: IOException) {
            logger.error(e) { "Error reading version from manifest" }
        }
    }

    private fun buildLayeredWordCloud() {
        if (cliParameters.inputSources.size == 1) {
            buildStandardWordCloud()
            return
        }
        if (cliParameters.inputSources.size != cliParameters.backgrounds.size) {
            throw IllegalArgumentException("Number of input sources does not equal the number of backgrounds.")
        }
        if (cliParameters.inputSources.size != cliParameters.getLayeredColors().size) {
            throw IllegalArgumentException("Number of input sources does not equal the number of colors.")
        }

        val wordCloud = LayeredWordCloud(
            cliParameters.inputSources.size,
            Dimension(cliParameters.width, cliParameters.height),
            cliParameters.collisionMode
        )
        wordCloud.setBackgroundColor(cliParameters.backgroundColor)

        for (i in cliParameters.inputSources.indices) {
            wordCloud.setBackground(i, buildBackground(cliParameters.backgrounds[i]))
            wordCloud.setColorPalette(i, ColorPalette(cliParameters.getLayeredColors()[i]))
            wordCloud.setFontScalar(i, buildFontScalar(cliParameters.fontScalarType))
            wordCloud.setPadding(i, cliParameters.padding)
            wordCloud.setKumoFont(i, buildKumoFont(cliParameters.fontWeight))
            wordCloud.build(i, loadFrequencies(cliParameters.inputSources[i]))
        }
        wordCloud.writeToFile(cliParameters.outputSource)
    }

    private fun buildPolarWordCloud() {
        if (cliParameters.inputSources.size != 2) {
            throw IllegalArgumentException("Polar word clouds require exactly 2 input sources. Found: ${cliParameters.inputSources.size}")
        }
        val wordCloud = PolarWordCloud(
            Dimension(cliParameters.width, cliParameters.height),
            cliParameters.collisionMode,
            cliParameters.polarBlendMode
        )
        if (cliParameters.backgrounds.isNotEmpty()) {
            wordCloud.background = buildBackground(cliParameters.backgrounds[0])
        }
        wordCloud.backgroundColor = cliParameters.backgroundColor
        if (cliParameters.getLayeredColors().size >= 1) {
            wordCloud.colorPalette = ColorPalette(cliParameters.getLayeredColors()[0])
        }
        if (cliParameters.getLayeredColors().size >= 2) {
            wordCloud.colorPalette2 = ColorPalette(cliParameters.getLayeredColors()[1])
        }
        wordCloud.fontScalar = buildFontScalar(cliParameters.fontScalarType)
        wordCloud.padding = cliParameters.padding
        wordCloud.wordStartStrategy = buildWordStart(cliParameters.wordStartType)
        wordCloud.kumoFont = buildKumoFont(cliParameters.fontWeight)
        wordCloud.build(
            loadFrequencies(cliParameters.inputSources[0]),
            loadFrequencies(cliParameters.inputSources[1])
        )
        wordCloud.writeToFile(cliParameters.outputSource)
    }

    private fun buildStandardWordCloud() {
        val wordCloud = WordCloud(
            Dimension(cliParameters.width, cliParameters.height),
            cliParameters.collisionMode
        )
        if (cliParameters.backgrounds.isNotEmpty()) {
            wordCloud.background = buildBackground(cliParameters.backgrounds[0])
        }
        wordCloud.backgroundColor = cliParameters.backgroundColor
        if (cliParameters.getColors().isNotEmpty()) {
            wordCloud.colorPalette = ColorPalette(cliParameters.getColors())
        }
        wordCloud.fontScalar = buildFontScalar(cliParameters.fontScalarType)
        wordCloud.padding = cliParameters.padding
        wordCloud.wordStartStrategy = buildWordStart(cliParameters.wordStartType)
        wordCloud.kumoFont = buildKumoFont(cliParameters.fontWeight)
        wordCloud.build(loadFrequencies(cliParameters.inputSources[0]))
        wordCloud.writeToFile(cliParameters.outputSource)
    }

    private fun loadFrequencies(input: String): List<WordFrequency> {
        try {
            val frequencyAnalyzer = FrequencyAnalyzer()
            frequencyAnalyzer.setWordFrequenciesToReturn(cliParameters.wordCount)
            frequencyAnalyzer.setMinWordLength(cliParameters.minWordLength)
            frequencyAnalyzer.setStopWords(cliParameters.stopWords)
            frequencyAnalyzer.setCharacterEncoding(cliParameters.characterEncoding)

            if (cliParameters.normalizers.isEmpty()) {
                cliParameters.normalizers.addAll(
                    listOf(NormalizerType.TRIM, NormalizerType.CHARACTER_STRIPPING, NormalizerType.LOWERCASE)
                )
            }
            for (normalizer in cliParameters.normalizers) {
                frequencyAnalyzer.addNormalizer(buildNormalizer(normalizer))
            }

            frequencyAnalyzer.setWordTokenizer(buildTokenizer())

            return frequencyAnalyzer.load(toInputStream(input))
        } catch (e: IOException) {
            throw KumoException("Failed to load frequencies from: $input", e)
        }
    }

    private fun buildTokenizer(): WordTokenizer = when (cliParameters.tokenizer) {
        CliParameters.TokenizerType.WHITE_SPACE -> WhiteSpaceWordTokenizer()
        CliParameters.TokenizerType.ENGLISH -> EnglishWordTokenizer()
        CliParameters.TokenizerType.CHINESE -> ChineseWordTokenizer()
    }

    private fun buildNormalizer(normalizer: NormalizerType): Normalizer = when (normalizer) {
        NormalizerType.LOWERCASE -> LowerCaseNormalizer()
        NormalizerType.UPPERCASE -> UpperCaseNormalizer()
        NormalizerType.BUBBLE -> BubbleTextNormalizer()
        NormalizerType.CHARACTER_STRIPPING -> CharacterStrippingNormalizer()
        NormalizerType.UPSIDE_DOWN -> UpsideDownNormalizer()
        NormalizerType.TRIM -> TrimToEmptyNormalizer()
    }

    private fun buildKumoFont(fontWeight: FontWeight): KumoFont =
        KumoFont(cliParameters.fontType, fontWeight)

    private fun buildFontScalar(fontScalarType: FontScalarType): FontScalar = when (fontScalarType) {
        FontScalarType.LINEAR -> LinearFontScalar(cliParameters.fontSizeMin, cliParameters.fontSizeMax)
        FontScalarType.SQRT -> SqrtFontScalar(cliParameters.fontSizeMin, cliParameters.fontSizeMax)
        FontScalarType.LOG -> LogFontScalar(cliParameters.fontSizeMin, cliParameters.fontSizeMax)
    }
}
