package com.kennycason.kumo.cli;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.PolarBlendMode;
import com.kennycason.kumo.abst.ColorAbst;
import com.kennycason.kumo.abst.FontAbst;
import com.kennycason.kumo.abst.InstanceCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by kenny on 6/11/16.
 *
 * Refer to CLI.md for usage.
 *
 */
public class CliParameters {

    @Parameter(names = "--type", description = "The type of word cloud to generate.", converter = TypeConverter.class)
    private Type type = Type.STANDARD;

    @Parameter(names = { "--input", "-i" }, required = true, description = "One ore more input sources. Input sources may be local files or Urls. If more than one input source is provided they must be comma separated. For standard word clouds only the first input source will be analyzed. Multiple input sources are only relevant for polar or layered word clouds.")
    private List<String> inputSources = new ArrayList<>();

    @Parameter(names = { "--output", "-o" }, required = true, description = "Output file for the generated word cloud.")
    private String outputSource;

    @Parameter(names = "--min-word-length", description = "The minimum word length required to be allowed in the word cloud.")
    private int minWordLength = 2;

    @Parameter(names = "--word-count", description = "Number of words from data set to draw to word cloud. After the words are sorted by frequency, the words are attempted to be placed in descending order.")
    private int wordCount = 1000;

    @Parameter(names = "--stop-words", description = "A comma separated list of words to exclude from the word cloud.")
    private List<String> stopWords = new ArrayList<>();

    @Parameter(names = "--stop-words-file", description = "A file of stop words. Format should be one word per line.")
    private String stopWordsFile;

    @Parameter(names = { "--width", "-w" }, description = "Width of the word cloud. Default is 640px.")
    private int width = 640;

    @Parameter(names = { "--height", "-h" }, description = "Height of the word cloud. Default is 480px.")
    private int height = 480;

    @Parameter(names = "--collision", description = "The collision algorithm to use when placing text into the word cloud.", converter = CollisionConverter.class)
    private CollisionMode collisionMode = CollisionMode.PIXEL_PERFECT;

    @Parameter(names = "--padding", description = "The minimum padding allowed between two words in the word cloud. This works with pixel-perfect collision detection as well. Default is 2px.")
    private int padding = 2;

    @Parameter(names = { "--background", "-bg" }, description = "One ore more input sources. Input sources may be local files or Urls of an image used to define the shape of the word cloud. By default the word cloud is drawn onto a rectangle. The word cloud will place text only in places where background image has non-transparent pixels. For standard word clouds only the first input source will be used. Multiple input sources are only relevant for layered word clouds. Each background image will be applied to a layer in the order they are listed.")
    private List<String> backgrounds = new ArrayList<>();

    @Parameter(names = "--background-color", description = "Background color. Default is Black.", converter = ColorConverter.class)
    private ColorAbst backgroundColor = InstanceCreator.color(0, 0, 0);

    @Parameter(names = { "--color", "-c" }, description = "A comma separated list of colors to use for the word cloud text. Values most be provided in one of the below formats. Refer to CLI.md for usage examples.")
    // perform actual parsing in the getter, the commas in our color format cause issues with jCommander
    private String colorRaw;

    @Parameter(names = "--polar-blend-mode", description = "Determine how to blend the two poles of the word cloud.", converter = PolarBlendModeConverter.class)
    private PolarBlendMode polarBlendMode = PolarBlendMode.BLUR;

    @Parameter(names = "--font-scalar", description = "Method to scale font. Default is Linear.", converter = FontScaleConverter.class)
    private FontScalarType fontScalarType = FontScalarType.LINEAR;

    @Parameter(names = "--font-size-min", description = "Minimum font size, default is 10px.")
    private int fontSizeMin = 10;

    @Parameter(names = "--font-size-max", description = "Maximum font size, default is 40px.")
    private int fontSizeMax = 40;

    @Parameter(names = "--font-weight", description = "A font weight. Default is Bold.", converter = FontFaceConverter.class)
    private FontAbst.Face fontWeight = FontAbst.Face.BOLD;

    @Parameter(names = "--font-type", description = "The name of the font to use. The system must have the font loaded already. Default is \"Comic Sans MS\".")
    private String fontType = "Comic Sans MS";

    @Parameter(names = "--encoding", description = "Character Encoding. Default is UTF-8")
    private String characterEncoding = "UTF-8";

    @Parameter(names = "--word-start", description = "Determine where to start drawing text to the word cloud.", converter = WordStartConverter.class)
    private WordStartType wordStartType = WordStartType.CENTER;

    @Parameter(names = "--normalizer", description = "One or more normalizers to apply to words in the word cloud.", converter = NormalizerConverter.class)
    private List<NormalizerType> normalizers = new ArrayList<>();

    @Parameter(names = "--tokenizer", description = "Determine where to start drawing text to the word cloud.", converter = TokenizerConverter.class)
    private TokenizerType tokenizer = TokenizerType.WHITE_SPACE;

    public List<String> getBackgrounds() {
        return backgrounds;
    }

    public CollisionMode getCollisionMode() {
        return collisionMode;
    }

    public ColorAbst getBackgroundColor() {
        return backgroundColor;
    }

    public List<ColorAbst> getColors() {
        if (isBlank(colorRaw)) {
            return Collections.emptyList();
        }
        return new ColorsConverter().convert(colorRaw);
    }

    public List<List<ColorAbst>> getLayeredColors() {
        if (isBlank(colorRaw)) {
            return Collections.emptyList();
        }
        final List<List<ColorAbst>> layeredColors = new ArrayList<>();
        for (final String layeredColorSet : colorRaw.split("\\|")) {
            layeredColors.add(new ColorsConverter().convert(layeredColorSet));
        }
        return layeredColors;
    }

    public PolarBlendMode getPolarBlendMode() {
        return polarBlendMode;
    }

    public FontScalarType getFontScalarType() {
        return fontScalarType;
    }

    public int getFontSizeMax() {
        return fontSizeMax;
    }

    public int getFontSizeMin() {
        return fontSizeMin;
    }

    public String getFontType() {
        return fontType;
    }

    public FontAbst.Face getFontFace() {
        return fontWeight;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public int getHeight() {
        return height;
    }

    public List<String> getInputSources() {
        return inputSources;
    }

    public int getMinWordLength() {
        return minWordLength;
    }

    public List<NormalizerType> getNormalizers() {
        return normalizers;
    }

    public String getOutputSource() {
        return outputSource;
    }

    public int getPadding() {
        return padding;
    }

    public List<String> getStopWords() {
        return stopWords;
    }

    public String getStopWordsFile() {
        return stopWordsFile;
    }

    public TokenizerType getTokenizer() {
        return tokenizer;
    }

    public Type getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public int getWordCount() {
        return wordCount;
    }

    public WordStartType getWordStartType() {
        return wordStartType;
    }

    // enums
    public enum Type {
        STANDARD,
        POLAR,
        LAYERED
    }
    public enum FontScalarType {
        LINEAR,
        SQRT,
        LOG
    }
    public enum WordStartType {
        CENTER,
        RANDOM
    }
    public enum NormalizerType {
        UPPERCASE,
        LOWERCASE,
        TRIM,
        UPSIDE_DOWN,
        BUBBLE,
        CHARACTER_STRIPPING
    }
    public enum TokenizerType {
        WHITE_SPACE,
        ENGLISH,
        CHINESE
    }

    // parameter type converters
    public static class ColorConverter implements IStringConverter<ColorAbst> {
        @Override
        public ColorAbst convert(final String input) {
            try {
                if (input.contains(",")) {
                    return parseRGBValues(input);
                }
                return InstanceCreator.color(parseNumber(input));

            } catch (final RuntimeException e) {
                throw new ParameterException("Failed to parse ColorAbst from input: [" + input + "]");
            }
        }

        private static ColorAbst parseRGBValues(final String input) {
            final String[] rgb = input.split(",");
            if (rgb.length != 3) {
                throw new ParameterException("Expected to find 3 numbers (RGB), instead found " + rgb.length + ", when parsing: [" + input + "]");
            }
            return InstanceCreator.color(
                    parseNumber(rgb[0]),
                    parseNumber(rgb[1]),
                    parseNumber(rgb[2]));
        }

        private static int parseNumber(final String number) {
            if (number.startsWith("0x")) {
                return Integer.parseInt(number.substring(2), 16);
            }
            return Integer.parseInt(number);
        }
    }
    public static class ColorsConverter implements IStringConverter<List<ColorAbst>> {
        private static final ColorConverter COLOR_CONVERTER = new ColorConverter();
        @Override
        public List<ColorAbst> convert(final String input) {
            final List<String> colorStrings = ParenthesisSerializer.deserialize(input);
            final List<ColorAbst> colors = new ArrayList<>(colorStrings.size());
            for (final String colorString : colorStrings) {
                colors.add(COLOR_CONVERTER.convert(colorString));
            }
            return colors;
        }
    }
    public static class TypeConverter implements IStringConverter<Type> {
        @Override
        public Type convert(final String input) {
            return new EnumConverter<>(Type.class).convert(input);
        }
    }
    public static class CollisionConverter implements IStringConverter<CollisionMode> {
        @Override
        public CollisionMode convert(final String input) {
            return new EnumConverter<>(CollisionMode.class).convert(input);
        }
    }
    public static class FontScaleConverter implements IStringConverter<FontScalarType> {
        @Override
        public FontScalarType convert(final String input) {
            return new EnumConverter<>(FontScalarType.class).convert(input);
        }
    }
    public static class FontFaceConverter implements IStringConverter<FontAbst.Face> {
        @Override
        public FontAbst.Face convert(final String input) {
            return new EnumConverter<>(FontAbst.Face.class).convert(input);
        }
    }
    public static class WordStartConverter implements IStringConverter<WordStartType> {
        @Override
        public WordStartType convert(final String input) {
            return new EnumConverter<>(WordStartType.class).convert(input);
        }
    }
    public static class NormalizerConverter implements IStringConverter<NormalizerType> {
        @Override
        public NormalizerType convert(final String input) {
            return new EnumConverter<>(NormalizerType.class).convert(input);
        }
    }
    public static class TokenizerConverter implements IStringConverter<TokenizerType> {
        @Override
        public TokenizerType convert(final String input) {
            return new EnumConverter<>(TokenizerType.class).convert(input);
        }
    }
    public static class PolarBlendModeConverter implements IStringConverter<PolarBlendMode> {
        @Override
        public PolarBlendMode convert(final String input) {
            return new EnumConverter<>(PolarBlendMode.class).convert(input);
        }
    }
    public static class EnumConverter<T extends Enum> implements IStringConverter<T> {
        private final Class<T> enumClass;
        public EnumConverter(final Class<T> enumClass) {
            this.enumClass = enumClass;
        }

        @Override
        public T convert(final String input) {
            final T value = fromString(input);
            if (value == null) {
                throw new ParameterException("Input value [" + input + "] unknown. Valid values are: [" + enumClass.getEnumConstants().toString() + "]");
            }
            return value;
        }

        private T fromString(final String code) {
            for (final T value : enumClass.getEnumConstants()) {
                if (value.toString().equalsIgnoreCase(code)) { return value; }
            }
            return null;
        }
    }
}
