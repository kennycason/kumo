package com.kennycason.kumo.cli

import com.beust.jcommander.IStringConverter
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.PolarBlendMode
import com.kennycason.kumo.font.FontWeight
import java.awt.Color

/**
 * Created by kenny on 6/11/16.
 *
 * Refer to CLI.md for usage.
 */
class CliParameters {

    @Parameter(names = ["--type"], description = "The type of word cloud to generate.", converter = TypeConverter::class)
    var type: Type = Type.STANDARD

    @Parameter(names = ["--input", "-i"], required = true, description = "One ore more input sources. Input sources may be local files or Urls. If more than one input source is provided they must be comma separated. For standard word clouds only the first input source will be analyzed. Multiple input sources are only relevant for polar or layered word clouds.")
    var inputSources: List<String> = ArrayList()

    @Parameter(names = ["--output", "-o"], required = true, description = "Output file for the generated word cloud.")
    var outputSource: String = ""

    @Parameter(names = ["--min-word-length"], description = "The minimum word length required to be allowed in the word cloud.")
    var minWordLength: Int = 2

    @Parameter(names = ["--word-count"], description = "Number of words from data set to draw to word cloud. After the words are sorted by frequency, the words are attempted to be placed in descending order.")
    var wordCount: Int = 1000

    @Parameter(names = ["--stop-words"], description = "A comma separated list of words to exclude from the word cloud.")
    var stopWords: List<String> = ArrayList()

    @Parameter(names = ["--stop-words-file"], description = "A file of stop words. Format should be one word per line.")
    var stopWordsFile: String? = null

    @Parameter(names = ["--width", "-w"], description = "Width of the word cloud. Default is 640px.")
    var width: Int = 640

    @Parameter(names = ["--height", "-h"], description = "Height of the word cloud. Default is 480px.")
    var height: Int = 480

    @Parameter(names = ["--collision"], description = "The collision algorithm to use when placing text into the word cloud.", converter = CollisionConverter::class)
    var collisionMode: CollisionMode = CollisionMode.PIXEL_PERFECT

    @Parameter(names = ["--padding"], description = "The minimum padding allowed between two words in the word cloud. This works with pixel-perfect collision detection as well. Default is 2px.")
    var padding: Int = 2

    @Parameter(names = ["--background", "-bg"], description = "One ore more input sources. Input sources may be local files or Urls of an image used to define the shape of the word cloud. By default the word cloud is drawn onto a rectangle. The word cloud will place text only in places where background image has non-transparent pixels. For standard word clouds only the first input source will be used. Multiple input sources are only relevant for layered word clouds. Each background image will be applied to a layer in the order they are listed.")
    var backgrounds: List<String> = ArrayList()

    @Parameter(names = ["--background-color"], description = "Background color. Default is Black.", converter = ColorConverter::class)
    var backgroundColor: Color = Color.BLACK

    @Parameter(names = ["--color", "-c"], description = "A comma separated list of colors to use for the word cloud text. Values most be provided in one of the below formats. Refer to CLI.md for usage examples.")
    // perform actual parsing in the getter, the commas in our color format cause issues with jCommander
    var colorRaw: String? = null

    @Parameter(names = ["--polar-blend-mode"], description = "Determine how to blend the two poles of the word cloud.", converter = PolarBlendModeConverter::class)
    var polarBlendMode: PolarBlendMode = PolarBlendMode.BLUR

    @Parameter(names = ["--font-scalar"], description = "Method to scale font. Default is Linear.", converter = FontScaleConverter::class)
    var fontScalarType: FontScalarType = FontScalarType.LINEAR

    @Parameter(names = ["--font-size-min"], description = "Minimum font size, default is 10px.")
    var fontSizeMin: Int = 10

    @Parameter(names = ["--font-size-max"], description = "Maximum font size, default is 40px.")
    var fontSizeMax: Int = 40

    @Parameter(names = ["--font-weight"], description = "A font weight. Default is Bold.", converter = FontWeightConverter::class)
    var fontWeight: FontWeight = FontWeight.BOLD

    @Parameter(names = ["--font-type"], description = "The name of the font to use. The system must have the font loaded already. Default is \"Comic Sans MS\".")
    var fontType: String = "Comic Sans MS"

    @Parameter(names = ["--encoding"], description = "Character Encoding. Default is UTF-8")
    var characterEncoding: String = "UTF-8"

    @Parameter(names = ["--word-start"], description = "Determine where to start drawing text to the word cloud.", converter = WordStartConverter::class)
    var wordStartType: WordStartType = WordStartType.CENTER

    @Parameter(names = ["--normalizer"], description = "One or more normalizers to apply to words in the word cloud.", converter = NormalizerConverter::class)
    var normalizers: MutableList<NormalizerType> = ArrayList()

    @Parameter(names = ["--tokenizer"], description = "Determine where to start drawing text to the word cloud.", converter = TokenizerConverter::class)
    var tokenizer: TokenizerType = TokenizerType.WHITE_SPACE

    fun getColors(): List<Color> {
        val raw = colorRaw
        if (raw.isNullOrBlank()) return emptyList()
        return ColorsConverter().convert(raw)
    }

    fun getLayeredColors(): List<List<Color>> {
        val raw = colorRaw
        if (raw.isNullOrBlank()) return emptyList()
        return raw.split("\\|".toRegex()).map { ColorsConverter().convert(it) }
    }

    // enums
    enum class Type {
        STANDARD, POLAR, LAYERED
    }

    enum class FontScalarType {
        LINEAR, SQRT, LOG
    }

    enum class WordStartType {
        CENTER, RANDOM
    }

    enum class NormalizerType {
        UPPERCASE, LOWERCASE, TRIM, UPSIDE_DOWN, BUBBLE, CHARACTER_STRIPPING
    }

    enum class TokenizerType {
        WHITE_SPACE, ENGLISH, CHINESE
    }

    // parameter type converters
    class ColorConverter : IStringConverter<Color> {
        override fun convert(input: String): Color {
            try {
                return if (input.contains(",")) {
                    parseRGBValues(input)
                } else {
                    Color(parseNumber(input))
                }
            } catch (e: RuntimeException) {
                throw ParameterException("Failed to parse Color from input: [$input]")
            }
        }

        companion object {
            private fun parseRGBValues(input: String): Color {
                val rgb = input.split(",")
                if (rgb.size != 3) {
                    throw ParameterException("Expected to find 3 numbers (RGB), instead found ${rgb.size}, when parsing: [$input]")
                }
                return Color(parseNumber(rgb[0]), parseNumber(rgb[1]), parseNumber(rgb[2]))
            }

            private fun parseNumber(number: String): Int {
                return if (number.startsWith("0x")) {
                    Integer.parseInt(number.substring(2), 16)
                } else {
                    Integer.parseInt(number)
                }
            }
        }
    }

    class ColorsConverter : IStringConverter<List<Color>> {
        override fun convert(input: String): List<Color> {
            val colorConverter = ColorConverter()
            return ParenthesisSerializer.deserialize(input).map { colorConverter.convert(it) }
        }
    }

    class TypeConverter : IStringConverter<Type> {
        override fun convert(input: String): Type = EnumConverter(Type::class.java).convert(input)
    }

    class CollisionConverter : IStringConverter<CollisionMode> {
        override fun convert(input: String): CollisionMode = EnumConverter(CollisionMode::class.java).convert(input)
    }

    class FontScaleConverter : IStringConverter<FontScalarType> {
        override fun convert(input: String): FontScalarType = EnumConverter(FontScalarType::class.java).convert(input)
    }

    class FontWeightConverter : IStringConverter<FontWeight> {
        override fun convert(input: String): FontWeight = EnumConverter(FontWeight::class.java).convert(input)
    }

    class WordStartConverter : IStringConverter<WordStartType> {
        override fun convert(input: String): WordStartType = EnumConverter(WordStartType::class.java).convert(input)
    }

    class NormalizerConverter : IStringConverter<NormalizerType> {
        override fun convert(input: String): NormalizerType = EnumConverter(NormalizerType::class.java).convert(input)
    }

    class TokenizerConverter : IStringConverter<TokenizerType> {
        override fun convert(input: String): TokenizerType = EnumConverter(TokenizerType::class.java).convert(input)
    }

    class PolarBlendModeConverter : IStringConverter<PolarBlendMode> {
        override fun convert(input: String): PolarBlendMode = EnumConverter(PolarBlendMode::class.java).convert(input)
    }

    class EnumConverter<T : Enum<T>>(private val enumClass: Class<T>) : IStringConverter<T> {
        override fun convert(input: String): T {
            return fromString(input)
                ?: throw ParameterException("Input value [$input] unknown. Valid values are: ${enumClass.enumConstants.toList()}")
        }

        private fun fromString(code: String): T? {
            return enumClass.enumConstants.firstOrNull { it.name.equals(code, ignoreCase = true) }
        }
    }
}
