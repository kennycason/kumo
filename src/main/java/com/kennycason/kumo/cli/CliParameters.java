package com.kennycason.kumo.cli;

import ch.lambdaj.Lambda;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.kennycason.kumo.CollisionMode;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kenny on 6/11/16.
 *
 * Refer to CLI.md for usage.
 *
 */
public class CliParameters {

    @Parameter(names = { "--type", "-t" }, description = "The type of word cloud to generate.", converter = TypeConverter.class)
    private Type type = Type.STANDARD;

    @Parameter(names = { "--input", "-i" }, description = "One ore more input sources. Input sources may be local files or Urls. If more than one input source is provided they must be comma separated. For standard word clouds only the first input source will be analyzed. Multiple input sources are only relevant for polar or layered word clouds.")
    private List<String> inputSources = new ArrayList<>();

    @Parameter(names = { "--output", "-o" }, description = "Output file for the generated word cloud.")
    private List<String> outputSources = new ArrayList<>();

    @Parameter(names = { "--min-word-length", "-mwl" }, description = "The minimum word length required to be allowed in the word cloud.")
    private int minWordLength = 4;

    @Parameter(names = { "--word-count", "-wc" }, description = "Number of words from data set to draw to word cloud. After the words are sorted by frequency, the words are attempted to be placed in descending order.")
    private int wordCount = 1000;

    @Parameter(names = { "--stop-words", "-sw" }, description = "A comma separated list of words to exclude from the word cloud.")
    private List<String> stopWords = new ArrayList<>();

    @Parameter(names = { "--stop-words-file", "-swf" }, description = "A file of stop words. Format should be one word per line.")
    private String stopWordsFile;

    @Parameter(names = { "--width", "-w" }, description = "Width of the word cloud. Default is 640px.")
    private int width = 640;

    @Parameter(names = { "--height", "-h" }, description = "Height of the word cloud. Default is 480px.")
    private int height = 480;

    @Parameter(names = { "--collision", "-col" }, description = "The collision algorithm to use when placing text into the word cloud.", converter = CollisionConverter.class)
    private CollisionMode collisionMode = CollisionMode.PIXEL_PERFECT;

    @Parameter(names = { "--padding", "-p" }, description = "The minimum padding allowed between two words in the word cloud. This works with pixel-perfect collision detection as well. Default is 2px.")
    private int padding = 2;

    @Parameter(names = { "--background", "-bg" }, description = "One ore more input sources. Input sources may be local files or Urls of an image used to define the shape of the word cloud. By default the word cloud is drawn onto a rectangle. The word cloud will place text only in places where background image has non-transparent pixels. For standard word clouds only the first input source will be used. Multiple input sources are only relevant for layered word clouds. Each background image will be applied to a layer in the order they are listed.")
    private List<String> backgrounds = new ArrayList<>();

    @Parameter(names = { "--background-color", "-bgc" }, description = "Background color. Default is Black.", converter = ColorConverter.class)
    private Color color = Color.BLACK;

    @Parameter(names = { "--color", "-c" }, description = "A comma separated list of colors to use in the word cloud. Values most be provided in one of the below formats. Refer to CLI.md for usage examples.", converter = ColorsConverter.class)
    private List<Color> colors = new ArrayList<>();

    @Parameter(names = { "--font-scalar", "-fs" }, description = "Method to scale font. Default is Linear.", converter = FontScaleConverter.class)
    private FontScale fontScale = FontScale.LINEAR;

    @Parameter(names = { "--font-size-min", "-fmin" }, description = "Minimum font size, default is 10px.")
    private int fontSizeMin = 10;

    @Parameter(names = { "--font-size-max", "-fmax" }, description = "Maximum font size, default is 40px.")
    private int fontSizeMax = 40;

    @Parameter(names = { "--font-weight", "-fw" }, description = "One or more fonts. If more than one font is listed they must be comma separated. Default is Bold")
    private List<FontWeight> fontWeight = Arrays.asList(FontWeight.BOLD);

    @Parameter(names = { "--font-type", "-ft" }, description = "The name of the font to use. The system must have the font loaded already. Default is \"Comic Sans MS\".")
    private String fontType = "Comic Sans MS";

    @Parameter(names = { "--word-start", "-ws" }, description = "Determine where to start drawing text to the word cloud.")
    private WordStart wordStart = WordStart.CENTER;

    @Parameter(names = { "--normalizer", "-n" }, description = "One or more normalizers to apply to words in the word cloud.", converter = NormalizerConverter.class)
    private List<NormalizerType> normalizers = new ArrayList<>();

    @Parameter(names = { "--tokenizer", "-t" }, description = "Determine where to start drawing text to the word cloud.", converter = TokenizerConverter.class)
    private TokenizerType tokenizer = TokenizerType.WHITE_SPACE;

    public List<String> getBackgrounds() {
        return backgrounds;
    }

    public CollisionMode getCollisionMode() {
        return collisionMode;
    }

    public Color getColor() {
        return color;
    }

    public List<Color> getColors() {
        return colors;
    }

    public FontScale getFontScale() {
        return fontScale;
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

    public List<FontWeight> getFontWeight() {
        return fontWeight;
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

    public List<String> getOutputSources() {
        return outputSources;
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

    public WordStart getWordStart() {
        return wordStart;
    }

    // enums
    public enum Type {
        STANDARD,
        POLAR,
        LAYERED
    }
    public enum FontScale {
        LINEAR,
        SQRT,
        LOG
    }
    public enum FontWeight {
        PLAIN,
        BOLD,
        ITALIC
    }
    public enum WordStart {
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
    public static class ColorConverter implements IStringConverter<Color> {
        @Override
        public Color convert(final String input) {
            try {
                if (input.contains(",")) {
                    return parseRGBValues(input);
                }
                return new Color(Integer.parseInt(input));

            } catch (final RuntimeException e) {
                throw new ParameterException("Failed to parse Color from input: [" + input + "]");
            }
        }

        private static Color parseRGBValues(final String input) {
            final String[] rgb = input.split(",");
            if (rgb.length != 3) {
                throw new ParameterException("Expected to find 3 numbers (RGB), instead found " + rgb.length + ", when parsing: [" + input + "]");
            }
            return new Color(
                    Integer.parseInt(rgb[0]),
                    Integer.parseInt(rgb[1]),
                    Integer.parseInt(rgb[2]));
        }
    }
    public static class ColorsConverter implements IStringConverter<List<Color>> {
        private static final ColorConverter COLOR_CONVERTER = new ColorConverter();
        @Override
        public List<Color> convert(final String input) {
            final List<String> colorStrings = ParenthesisSerializer.deserialize(input);
            final List<Color> colors = new ArrayList<>(colorStrings.size());
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
    public static class FontScaleConverter implements IStringConverter<FontScale> {
        @Override
        public FontScale convert(final String input) {
            return new EnumConverter<>(FontScale.class).convert(input);
        }
    }
    public static class FontWeightConverter implements IStringConverter<FontWeight> {
        @Override
        public FontWeight convert(final String input) {
            return new EnumConverter<>(FontWeight.class).convert(input);
        }
    }
    public static class WordStartConverter implements IStringConverter<WordStart> {
        @Override
        public WordStart convert(final String input) {
            return new EnumConverter<>(WordStart.class).convert(input);
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
    public static class EnumConverter<T extends Enum> implements IStringConverter<T> {
        private final Class<T> enumClass;
        public EnumConverter(final Class<T> enumClass) {
            this.enumClass = enumClass;
        }

        @Override
        public T convert(final String input) {
            final T value = fromString(input);
            if (value == null) {
                throw new ParameterException("Input value [" + input + "] unknown. Valid values are: [" + Lambda.join(enumClass.getEnumConstants()) + "]");
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
