package com.kennycason.kumo.cli;

import com.beust.jcommander.JCommander;
import com.kennycason.kumo.LayeredWordCloud;
import com.kennycason.kumo.PolarWordCloud;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.draw.Dimension;
import com.kennycason.kumo.bg.Background;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.cli.CliParameters.FontScalarType;
import com.kennycason.kumo.cli.CliParameters.NormalizerType;
import com.kennycason.kumo.cli.CliParameters.WordStartType;
import com.kennycason.kumo.draw.FontFace;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.FontScalar;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.font.scale.LogFontScalar;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.normalize.*;
import com.kennycason.kumo.nlp.tokenizer.WhiteSpaceWordTokenizer;
import com.kennycason.kumo.nlp.tokenizer.WordTokenizer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.nlp.tokenizers.EnglishWordTokenizer;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.wordstart.CenterWordStart;
import com.kennycason.kumo.wordstart.RandomWordStart;
import com.kennycason.kumo.wordstart.WordStartStrategy;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Manifest;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by kenny on 6/12/16.
 */
public class KumoCli {
    private final CliParameters cliParameters = new CliParameters();

    public static void main(final String[] args) {
        new KumoCli().runWithArguments(args);
    }

    public void runWithArguments(final String[] args) {
        if (args.length > 0 && args[0].equals("--version")) {
            printVersion();
            return;
        }
        new JCommander(cliParameters).parse(args);
        switch (cliParameters.getType()) {
            case STANDARD:
                buildStandardWordCloud();
                break;
            case POLAR:
                buildPolarWordCloud();
                break;
            case LAYERED:
                buildLayeredWordCloud();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported type: " + cliParameters.getType());
        }
    }

    private void printVersion() {
        try {
            final Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                final Manifest manifest = new Manifest(resources.nextElement().openStream());
                final String version = manifest.getMainAttributes().getValue("Implementation-Version");
                if (isNotBlank(version)) {
                    System.out.println("Kumo Version: " + version);
                    return;
                }
            }
            throw new RuntimeException("Failed to load version from manifest");
        } catch (final IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void buildLayeredWordCloud() {
        if (cliParameters.getInputSources().size() == 1) {
            buildStandardWordCloud();
            return;
        }
        if (cliParameters.getInputSources().size() != cliParameters.getBackgrounds().size()) {
            throw new IllegalArgumentException("Number of input sources does not equal the number of backgrounds.");
        }
        if (cliParameters.getInputSources().size() != cliParameters.getLayeredColors().size()) {
            throw new IllegalArgumentException("Number of input sources does not equal the number of colors.");
        }

        final LayeredWordCloud wordCloud = new LayeredWordCloud(
                cliParameters.getInputSources().size(),
                new Dimension(cliParameters.getWidth(), cliParameters.getHeight()),
                cliParameters.getCollisionMode()
        );
        wordCloud.setBackgroundColor(cliParameters.getBackgroundColor());

        for (int i = 0; i < cliParameters.getInputSources().size(); i++) {
            wordCloud.setBackground(i, buildBackground(cliParameters.getBackgrounds().get(i)));
            wordCloud.setColorPalette(i, new ColorPalette(cliParameters.getLayeredColors().get(i)));
            wordCloud.setFontScalar(i, buildFontScalar(cliParameters.getFontScalarType()));
            wordCloud.setPadding(i, cliParameters.getPadding());
            wordCloud.setKumoFont(i, buildKumoFont(cliParameters.getFontFace()));
            wordCloud.build(i, loadFrequencies(cliParameters.getInputSources().get(i)));
        }
        wordCloud.writeToFile(cliParameters.getOutputSource());
    }

    private void buildPolarWordCloud() {
        if (cliParameters.getInputSources().size() != 2) {
            throw new IllegalArgumentException("Polar word clouds require exactly 2 input sources. Found: " + cliParameters.getInputSources().size());
        }
        final PolarWordCloud wordCloud = new PolarWordCloud(
                new Dimension(cliParameters.getWidth(), cliParameters.getHeight()),
                cliParameters.getCollisionMode(),
                cliParameters.getPolarBlendMode()
        );
        if (!cliParameters.getBackgrounds().isEmpty()) {
            wordCloud.setBackground(buildBackground(cliParameters.getBackgrounds().get(0)));
        }
        wordCloud.setBackgroundColor(cliParameters.getBackgroundColor());
        if (cliParameters.getLayeredColors().size() >= 1) {
            wordCloud.setColorPalette(new ColorPalette(cliParameters.getLayeredColors().get(0)));
        }
        if (cliParameters.getLayeredColors().size() >= 2) {
            wordCloud.setColorPalette2(new ColorPalette(cliParameters.getLayeredColors().get(1)));
        }
        wordCloud.setFontScalar(buildFontScalar(cliParameters.getFontScalarType()));
        wordCloud.setPadding(cliParameters.getPadding());
        wordCloud.setWordStartStrategy(buildWordStart(cliParameters.getWordStartType()));
        wordCloud.setKumoFont(buildKumoFont(cliParameters.getFontFace()));
        wordCloud.build(loadFrequencies(cliParameters.getInputSources().get(0)), loadFrequencies(cliParameters.getInputSources().get(1)));
        wordCloud.writeToFile(cliParameters.getOutputSource());
    }

    private void buildStandardWordCloud() {
        final WordCloud wordCloud = new WordCloud(
                new Dimension(cliParameters.getWidth(), cliParameters.getHeight()),
                cliParameters.getCollisionMode()
        );
        if (!cliParameters.getBackgrounds().isEmpty()) {
            wordCloud.setBackground(buildBackground(cliParameters.getBackgrounds().get(0)));
        }
        wordCloud.setBackgroundColor(cliParameters.getBackgroundColor());
        if (!cliParameters.getColors().isEmpty()) {
            wordCloud.setColorPalette(new ColorPalette(cliParameters.getColors()));
        }
        wordCloud.setFontScalar(buildFontScalar(cliParameters.getFontScalarType()));
        wordCloud.setPadding(cliParameters.getPadding());
        wordCloud.setWordStartStrategy(buildWordStart(cliParameters.getWordStartType()));
        wordCloud.setKumoFont(buildKumoFont(cliParameters.getFontFace()));
        wordCloud.build(loadFrequencies(cliParameters.getInputSources().get(0)));
        wordCloud.writeToFile(cliParameters.getOutputSource());
    }

    private List<WordFrequency> loadFrequencies(final String input) {
        try {
            final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
            frequencyAnalyzer.setWordFrequenciesToReturn(cliParameters.getWordCount());
            frequencyAnalyzer.setMinWordLength(cliParameters.getMinWordLength());
            frequencyAnalyzer.setStopWords(cliParameters.getStopWords());
            frequencyAnalyzer.setCharacterEncoding(cliParameters.getCharacterEncoding());

            if (cliParameters.getNormalizers().isEmpty()) {
                cliParameters.getNormalizers().addAll(Arrays.asList(NormalizerType.TRIM, NormalizerType.CHARACTER_STRIPPING, NormalizerType.LOWERCASE));
            }
            for (final NormalizerType normalizer : cliParameters.getNormalizers()) {
                frequencyAnalyzer.addNormalizer(buildNormalizer(normalizer));
            }

            frequencyAnalyzer.setWordTokenizer(buildTokenizer());

            return frequencyAnalyzer.load(toInputStream(input));

        } catch (final IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private WordTokenizer buildTokenizer() {
        switch (cliParameters.getTokenizer()) {
            case WHITE_SPACE: return new WhiteSpaceWordTokenizer();
            case ENGLISH: return new EnglishWordTokenizer();
            case CHINESE: return new ChineseWordTokenizer();
        }
        throw new IllegalStateException("Unknown tokenizer: " + cliParameters.getTokenizer());
    }

    private Normalizer buildNormalizer(final NormalizerType normalizer) {
        switch (normalizer) {
            case LOWERCASE: return new LowerCaseNormalizer();
            case UPPERCASE: return new UpperCaseNormalizer();
            case BUBBLE: return new BubbleTextNormalizer();
            case CHARACTER_STRIPPING: return new CharacterStrippingNormalizer();
            case UPSIDE_DOWN: return new UpsideDownNormalizer();
            case TRIM: return new TrimToEmptyNormalizer();
        }
        throw new IllegalStateException("Unknown normalizer: " + normalizer);
    }

    private KumoFont buildKumoFont(final FontFace fontWeight) {
        return new KumoFont(cliParameters.getFontType(), fontWeight);
    }

    private static WordStartStrategy buildWordStart(final WordStartType wordStartType) {
        switch (wordStartType) {
            case CENTER: return new CenterWordStart();
            case RANDOM: return new RandomWordStart();
        }
        throw new IllegalStateException("Unknown word start: " + wordStartType);
    }


    private FontScalar buildFontScalar(final FontScalarType fontScalarType) {
        switch (fontScalarType) {
            case LINEAR: return new LinearFontScalar(cliParameters.getFontSizeMin(), cliParameters.getFontSizeMax());
            case SQRT: return new SqrtFontScalar(cliParameters.getFontSizeMin(), cliParameters.getFontSizeMax());
            case LOG: return new LogFontScalar(cliParameters.getFontSizeMin(), cliParameters.getFontSizeMax());
        }
        throw new IllegalStateException("Unknown font scalar type: " + fontScalarType);
    }

    private static Background buildBackground(final String background) {
        try {
            return new PixelBoundryBackground(toInputStream(background));
        } catch (final IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static InputStream toInputStream(final String path) {
        final File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            try {
                return new FileInputStream(file);

            } catch (final FileNotFoundException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        try {
            return new URL(path).openStream();
        } catch (final IOException ignored) {
        }
        throw new RuntimeException("Input path [" + path + "] not a file or url.");
    }

}
