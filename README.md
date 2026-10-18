

# <img src="logo/Kumo02.png" alt="Kumo" height="28px"> Kumo

Kumo's goal is to create a powerful and user friendly Word Cloud API in Kotlin. Kumo directly generates an image file without the need to create an applet as many other libraries do.

Please feel free to jump in and help improve Kumo! There are many places for performance optimization in Kumo!

UPDATE: 2025-06-04. I plan on coming back to this project sometime to address issues, convert the project to Kotlin. Apologies for going dark <3

See the [Kotlin roadmap](ROADMAP.md) for development ideas and pending migration follow-ups.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.kennycason/kumo-core/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.kennycason/kumo-core)  [![CircleCI](https://circleci.com/gh/kennycason/kumo.svg?style=svg)](https://circleci.com/gh/kennycason/kumo)<br/>

### Current Features

- Draw Rectangle, Circle or Image Overlay word clouds. Image Overlay will draw words over all non-transparent pixels.
- Linear, Square-Root Font Scalars. Fully extendable.
- Variable Font Sizes.
- Word Rotation. Just provide a Start Angle, End Angle, and number of slices.
- Custom BackGround Color. Fully customizable BackGrounds coming soon.
- Word Padding.
- Load Custom Color Palettes. Also supports color gradients.
- Two Modes that of Collision and Padding: PIXEL_PERFECT and RECTANGLE.
- Polar Word Clouds. Draw two opposing word clouds in one image to easily compare/contrast date sets.
- Layered Word Clouds. Overlay multiple word clouds.
- WhiteSpace and Chinese Word Tokenizer. Fully extendable. 
- Frequency Analyzer to tokenize, filter and compute word counts.
- Command Line Interface

#### CLI Install via Brew (NEW!)

`brew install kumo`

### Available from Maven Central

```xml
<dependency>
    <groupId>com.kennycason</groupId>
    <artifactId>kumo-core</artifactId>
    <version>1.28</version>
</dependency>
```

Include `kumo-tokenizers` if you want Chinese serialization. More languages to come.
```xml
<dependency>
    <groupId>com.kennycason</groupId>
    <artifactId>kumo-tokenizers</artifactId>
    <version>1.28</version>
</dependency>
```

### Screenshots

<table>
<tr><td>
<img src="kumo-core/output/whale_wordcloud_large_impact.png?raw=true" width="300"/>
</td><td>
<img src="kumo-core/output/simplymeasured.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="kumo-core/output/layered_haskell.png?raw=true" width="300"/>
</td><td>
<img src="kumo-core/output/layered_pho_bowl.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="kumo-core/output/polar_newyork_rectangle_blur.png?raw=true" width="300"/>
</td><td>
<img src="kumo-core/output/polar_tide_chinese_vs_english2.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="kumo-core/output/datarank_wordcloud_circle_sqrt_font.png?raw=true" width="300"/>
</td><td>
<img src="kumo-core/output/chinese_language_circle.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="kumo-core/output/polar_newyork_whale_large_blur.png?raw=true" width="300"/>
</td><td>
<img src="kumo-core/output/layered_word_cloud.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="kumo-core/output/whale_wordcloud_large_angles.png?raw=true" width="300"/>
</td><td>
<img src="kumo-core/output/wordcloud_rectangle.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="kumo-core/output/wordcloud_gradient_redbluegreen.png?raw=true" width="300"/>
</td><td>
<img src="kumo-core/output/wordcloud_gradient_whiteredblue.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="kumo-core/output/bubbletext.png?raw=true" width="300"/>
</td><td>
<img src="kumo-core/output/wordcloud_emoji.png?raw=true" width="300"/>
</td></tr>
</table>

### Examples

The current development branch targets Kotlin on Java 11 or newer. These examples use the Kotlin API in development for 1.29; the Maven coordinates above refer to the published 1.28 release. The Kotlin API uses properties and default arguments and does not preserve every previous Java call signature.

Example to generate a Word Cloud on top of an image.

```kotlin
val frequencyAnalyzer = FrequencyAnalyzer()
frequencyAnalyzer.wordFrequenciesToReturn = 300
frequencyAnalyzer.minWordLength = 4
frequencyAnalyzer.stopWords = loadStopWords()

val wordFrequencies = frequencyAnalyzer.load("text/datarank.txt")
val dimension = Dimension(500, 312)
val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
wordCloud.padding = 2
wordCloud.background = PixelBoundaryBackground("backgrounds/whale_small.png")
wordCloud.colorPalette = ColorPalette(Color(0x4055F1), Color(0x408DF1), Color(0x40AAF1), Color(0x40C5F1), Color(0x40D3F1), Color(0xFFFFFF))
wordCloud.fontScalar = LinearFontScalar(10, 40)
wordCloud.build(wordFrequencies)
wordCloud.writeToFile("kumo-core/output/whale_wordcloud_small.png")
```

Example to generate a circular Word Cloud.

```kotlin
val frequencyAnalyzer = FrequencyAnalyzer()
val wordFrequencies = frequencyAnalyzer.load("text/my_text_file.txt")
val dimension = Dimension(600, 600)
val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
wordCloud.padding = 2
wordCloud.background = CircleBackground(300)
wordCloud.colorPalette = ColorPalette(Color(0x4055F1), Color(0x408DF1), Color(0x40AAF1), Color(0x40C5F1), Color(0x40D3F1), Color(0xFFFFFF))
wordCloud.fontScalar = SqrtFontScalar(10, 40)
wordCloud.build(wordFrequencies)
wordCloud.writeToFile("kumo-core/output/datarank_wordcloud_circle_sqrt_font.png")
```

Example to generate a rectangle Word Cloud

```kotlin
val frequencyAnalyzer = FrequencyAnalyzer()
val wordFrequencies = frequencyAnalyzer.load("text/my_text_file.txt")
val dimension = Dimension(600, 600)
val wordCloud = WordCloud(dimension, CollisionMode.RECTANGLE)
wordCloud.padding = 0
wordCloud.background = RectangleBackground(dimension)
wordCloud.colorPalette = ColorPalette(Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE)
wordCloud.fontScalar = LinearFontScalar(10, 40)
wordCloud.build(wordFrequencies)
wordCloud.writeToFile("kumo-core/output/wordcloud_rectangle.png")
```


Example using Linear Color Gradients

```kotlin
val frequencyAnalyzer = FrequencyAnalyzer()
frequencyAnalyzer.wordFrequenciesToReturn = 500
frequencyAnalyzer.minWordLength = 4
val wordFrequencies = frequencyAnalyzer.load("text/my_text_file.txt")
val dimension = Dimension(600, 600)
val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
wordCloud.padding = 2
wordCloud.background = CircleBackground(300)
// colors followed by and steps between
wordCloud.colorPalette = LinearGradientColorPalette(Color.RED, Color.BLUE, Color.GREEN, 30, 30)
wordCloud.fontScalar = SqrtFontScalar(10, 40)
wordCloud.build(wordFrequencies)
wordCloud.writeToFile("kumo-core/output/wordcloud_gradient_redbluegreen.png")
```

Example of tokenizing chinese text into a circle

```kotlin
val frequencyAnalyzer = FrequencyAnalyzer()
frequencyAnalyzer.wordFrequenciesToReturn = 600
frequencyAnalyzer.minWordLength = 2
frequencyAnalyzer.wordTokenizer = ChineseWordTokenizer()

val wordFrequencies = frequencyAnalyzer.load("text/chinese_language.txt")
val dimension = Dimension(600, 600)
val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
wordCloud.padding = 2
wordCloud.background = CircleBackground(300)
wordCloud.colorPalette = ColorPalette(Color(0xD5CFFA), Color(0xBBB1FA), Color(0x9A8CF5), Color(0x806EF5))
wordCloud.fontScalar = SqrtFontScalar(12, 45)
wordCloud.build(wordFrequencies)
wordCloud.writeToFile("kumo-core/output/chinese_language_circle.png")
```

Create a polarity word cloud to contrast two datasets

```kotlin
val frequencyAnalyzer = FrequencyAnalyzer()
frequencyAnalyzer.wordFrequenciesToReturn = 750
frequencyAnalyzer.minWordLength = 4
frequencyAnalyzer.stopWords = loadStopWords()

val wordFrequencies = frequencyAnalyzer.load("text/new_york_positive.txt")
val wordFrequencies2 = frequencyAnalyzer.load("text/new_york_negative.txt")
val dimension = Dimension(600, 600)
val wordCloud = PolarWordCloud(dimension, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR)
wordCloud.padding = 2
wordCloud.background = CircleBackground(300)
wordCloud.fontScalar = SqrtFontScalar(10, 40)
wordCloud.build(wordFrequencies, wordFrequencies2)
wordCloud.writeToFile("kumo-core/output/polar_newyork_circle_blur_sqrt_font.png")
```


Create a Layered Word Cloud from two images/two word sets

```kotlin
val frequencyAnalyzer = FrequencyAnalyzer()
frequencyAnalyzer.wordFrequenciesToReturn = 300
frequencyAnalyzer.minWordLength = 5
frequencyAnalyzer.stopWords = loadStopWords()

val wordFrequencies = frequencyAnalyzer.load("text/new_york_positive.txt")
val wordFrequencies2 = frequencyAnalyzer.load("text/new_york_negative.txt")
val dimension = Dimension(600, 386)
val layeredWordCloud = LayeredWordCloud(2, dimension, CollisionMode.PIXEL_PERFECT)

layeredWordCloud[0].padding = 1
layeredWordCloud[1].padding = 1

layeredWordCloud[0].kumoFont = KumoFont("LICENSE PLATE", FontWeight.BOLD)
layeredWordCloud[1].kumoFont = KumoFont("Comic Sans MS", FontWeight.BOLD)

layeredWordCloud[0].background = PixelBoundaryBackground("backgrounds/cloud_bg.bmp")
layeredWordCloud[1].background = PixelBoundaryBackground("backgrounds/cloud_fg.bmp")

layeredWordCloud[0].colorPalette = ColorPalette(Color(0xABEDFF), Color(0x82E4FF), Color(0x55D6FA))
layeredWordCloud[1].colorPalette = ColorPalette(Color(0xFFFFFF), Color(0xDCDDDE), Color(0xCCCCCC))

layeredWordCloud[0].fontScalar = SqrtFontScalar(10, 40)
layeredWordCloud[1].fontScalar = SqrtFontScalar(10, 40)

layeredWordCloud.build(0, wordFrequencies)
layeredWordCloud.build(1, wordFrequencies2)
layeredWordCloud.writeToFile("kumo-core/output/layered_word_cloud.png")
```

Create a ParallelLayeredWordCloud using 4 distinct Rectangles.<br>
Every Rectangle will be processed in a separate thread, thus minimizing build-time significantly
NOTE: This will eventually be natively handled along with better internal data structures.

```kotlin
val dimension = Dimension(2000, 2000)
val cloud = ParallelLayeredWordCloud(4, dimension, CollisionMode.PIXEL_PERFECT)
val layerNormalizers = listOf(
    UpperCaseNormalizer(), LowerCaseNormalizer(), BubbleTextNormalizer(), StringToHexNormalizer()
)
val fonts = listOf("Lucida Sans", "Comic Sans", "Yu Gothic Light", "Meiryo")
val positions = listOf(Point(0, 0), Point(0, 1000), Point(1000, 0), Point(1000, 1000))
val colors = listOf(Color.RED, Color.WHITE, Color(0x008080), Color.GREEN)

for (layer in 0 until cloud.layers) {
    val frequencies = FrequencyAnalyzer().apply {
        minWordLength = 3
        normalizers = listOf(layerNormalizers[layer])
        wordFrequenciesToReturn = 1000
    }.load("text/english_tide.txt")

    cloud[layer].apply {
        angleGenerator = AngleGenerator(0)
        padding = 3
        wordStartStrategy = CenterWordStart()
        kumoFont = KumoFont(fonts[layer], FontWeight.PLAIN)
        colorPalette = ColorPalette(colors[layer])
        background = RectangleBackground(positions[layer], Dimension(1000, 1000))
        fontScalar = LinearFontScalar(10, 40)
    }
    cloud.build(layer, frequencies)
}
cloud.writeToFile("parallelBubbleText.png")
```

Refer to JPanelDemo.kt for an example integrating into a JPanel.

In IntelliJ IDEA, select the shared **JPanelDemo (classpath)** run configuration. It uses an Application configuration with the module path disabled. The Kotlin runner can mistake the JAR's automatic module name for a module in the compiled output directory and fail before the demo starts.


### Word Frequency File / Analyzer

The most common way to generate word frequencies is to pass a String of text directly to `FrequencyAnalyzer`.
The `FrequencyAnalyzer` contains many options to process and normalize input text.

Sometimes the word counts and word frequencies are already known and a consumer would like to load them directly into Kumo.
To do so, you can manually construct the `List<WordFrequency>` yourself, or you can load in a text file containing the word frequency and word pairs.
The `FrequencyFileLoader` can be used to load such files. The required format is:
```
100: frog
94: dog
43: cog
20: bog
3: fog
1: log
1: pog
```

Order does not matter as the `FrequencyFileLoader` will automatically sort the pairs.

### Tokenizers

Tokenizers are the code that splits a sentence/text into a list of words. Currently only two tokenizers are built into Kumo.
Implement WordTokenizer (or provide a lambda) and assign it to FrequencyAnalyzer.wordTokenizer.

| Tokenizer   |
|-------------|
| WhiteSpaceWordTokenizer |
| ChineseWordTokenizer |


### Filters

After tokenization, filters are applied to each word to determine whether or not should be omitted from the word list. 

Configure FrequencyAnalyzer.filters with a list of filters; use += to append one.


| Tokenizer   | Description |
|-------------|-------------|
| UrlFilter | A filter to remove words that are urls. |
| CompositeFilter | A wrapper of a collection of filters. |
| StopWordFilter | Internally used, the FrequencyAnalyzer makes this filter easy to use via `FrequencyAnalyzer.stopWords`. |
| WordSizeFilter | Internally used, the FrequencyAnalyzer makes this filter easy to use via `FrequencyAnalyzer.minWordLength` and `FrequencyAnalyzer.maxWordLength`. |

### Normalizers

Normalizers transform each token before filtering. By default, FrequencyAnalyzer applies TrimToEmptyNormalizer, CharacterStrippingNormalizer, and LowerCaseNormalizer, in that order.

Configure FrequencyAnalyzer.normalizers with a list of normalizers; use += to append one.

| Normalizers | Description |
|-------------|-------------|
| CharacterStrippingNormalizer | Constructed with a Pattern, it will replace all matched occurrences with a configurable 'replaceWith' string. The default pattern is `\\.|:|;|\\(|\\)|\"|,|\\?|,|!|<|>|/`|
| LowerCaseNormalizer | Converts all text to lowercase. |
| UpperCaseNormalizer | Converts all text to uppercase. |
| TrimToEmptyNormalizer | Removes leading and trailing whitespace. |
| UpsideDownNormalizer | Converts A-Z,a-z,0-9 character to an upside-down variant. |
| StringToHexNormalizer | Converts each character to it's hex value and concatenates them. |
| BubbleTextNormalizer | Replaces A-Z,a-z with characters enclosed in Bubbles ⓐ-ⓩⒶ-Ⓩ (requires a supporting font) |


### Command Line Interface (CLI)

Kumo can now be accessed via CLI. It is not quite as flexible as the programmatic interface yet but should support most of the common needs.

The CLI Documentation can be found [here](https://github.com/kennycason/kumo/blob/master/CLI.md).

The below examples assume you have the jar installed under the name of "kumo". To install via Brew run the following command.

`brew install https://raw.githubusercontent.com/kennycason/kumo/master/script/kumo.rb`

Examples:

Create a standard word cloud.
```
kumo --input "https://en.wikipedia.org/wiki/Nintendo" --output "/tmp/wordcloud.png"
```

Create a standard word cloud excluding stop words.
```
kumo --input "https://en.wikipedia.org/wiki/Nintendo" --output "/tmp/wordcloud.png" --stop-words "nintendo,the"
```

Create a standard word cloud with a limited word count.
```
kumo --input "https://en.wikipedia.org/wiki/Nintendo" --output "/tmp/wordcloud.png" --word-count 10
```

Create a standard word cloud with a custom width and height.
```
kumo --input "https://en.wikipedia.org/wiki/Nintendo" --output "/tmp/wordcloud.png" --width 256 --height 256
```

Create a standard word cloud with custom font configuration.
```
kumo --input "https://en.wikipedia.org/wiki/Nintendo" --output "/tmp/wordcloud.png" --font-scalar sqrt --font-type Impact --font-weight plain --font-size-min 4 --font-size-max 60
```

Create a standard word cloud with a custom shape.
```
kumo --input "https://en.wikipedia.org/wiki/Nintendo" --output "/tmp/wordcloud.png" --width 990 --height 618 --background "https://raw.githubusercontent.com/kennycason/kumo/master/src/test/resources/backgrounds/whale.png
```

Create a standard word cloud with a custom color palette.
```
kumo --input "https://en.wikipedia.org/wiki/Nintendo" --output "/tmp/wordcloud.png" --color "(255,0,0),(0,255,0),(0,0,255)"
```
```
kumo --input "https://en.wikipedia.org/wiki/Nintendo" --output "/tmp/wordcloud.png" --color "(0xffffff),(0xcccccc),(0x999999),(0x666666),(0x333333)"
```

Create a standard word cloud using a Chinese tokenizer
```
kumo --input "https://zh.wikipedia.org/wiki/%E4%BB%BB%E5%A4%A9%E5%A0%82" --output "/tmp/wordcloud.png" --tokenizer chinese
```

Create a polar word cloud
```
kumo --input "https://en.wikipedia.org/wiki/Nintendo,https://en.wikipedia.org/wiki/PlayStation" --output "/tmp/nintendo_vs_playstation.png" --type polar --color "(0x00ff00),(0x00dd00),(0x007700)|(0xff0000),(0xdd0000),(0x770000)"
```

Create a layered word cloud
```
kumo --input "https://www.haskell.org/, https://en.wikipedia.org/wiki/Haskell_(programming_language)" --output "/tmp/nintendo_vs_playstation.png" --type layered --background "https://raw.githubusercontent.com/kennycason/kumo/master/src/test/resources/backgrounds/haskell_1.bmp,https://raw.githubusercontent.com/kennycason/kumo/master/src/test/resources/backgrounds/haskell_2.bmp" --color "(0xFA6C07),(0xFF7614),(0xFF8936)|(0x080706),(0x3B3029),(0x47362A)"
```


### Contributing

Tests use JUnit Jupiter and Strikt. Strikt 0.34.1 keeps the test suite compatible with Java 11; 0.35.1 requires Java 17.

Run the suite with a Java 11 or newer JDK:

```sh
mvn -B test -Djava.awt.headless=true
```

Image-generating example classes ending in ITest are excluded from the default run.

My primary IDE of choice is IntelliJ due to their robust tooling as well as code analysis/inspections. If using [IntelliJ IDEA](https://www.jetbrains.com/idea/), I recommend importing `KumoIntelliJInspections.xml`. I am also considering adding Checkstyle support.

New tests that write images should write images out to `kumo-core/output_test/` instead of `kumo-core/output/` which is now used for images to showcase Kumo.
