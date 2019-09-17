

# <img src="logo/Kumo02.png" alt="Kumo" height="28px"> Kumo

Kumo's goal is to create a powerful and user friendly Word Cloud API in Java. Kumo directly generates an image file without the need to create an applet as many other libraries do.

Please feel free to jump in and help improve Kumo! There are many places for performance optimization in Kumo!

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
    <version>1.22</version>
</dependency>
```

Include `kumo-tokenizers` if you want Chinese serialization. More languages to come.
```xml
<dependency>
    <groupId>com.kennycason</groupId>
    <artifactId>kumo-tokenizers</artifactId>
    <version>1.22</version>
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

Example to generate a Word Cloud on top of an image.

```java
final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
frequencyAnalyzer.setWordFrequenciesToReturn(300);
frequencyAnalyzer.setMinWordLength(4);
frequencyAnalyzer.setStopWords(loadStopWords());

final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load("text/datarank.txt");
final Dimension dimension = new Dimension(500, 312);
final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
wordCloud.setPadding(2);
wordCloud.setBackground(new PixelBoundryBackground("backgrounds/whale_small.png"));
wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
wordCloud.setFontScalar(new LinearFontScalar(10, 40));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("kumo-core/output/whale_wordcloud_small.png");
```

Example to generate a circular Word Cloud.

```java
final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load("text/my_text_file.txt");
final Dimension dimension = new Dimension(600, 600);
final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
wordCloud.setPadding(2);
wordCloud.setBackground(new CircleBackground(300));
wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("kumo-core/output/datarank_wordcloud_circle_sqrt_font.png");
```

Example to generate a rectangle Word Cloud

```java
final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load("text/my_text_file.txt");
final Dimension dimension = new Dimension(600, 600);
final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
wordCloud.setPadding(0);
wordCloud.setBackground(new RectangleBackground(dimension));
wordCloud.setColorPalette(new ColorPalette(Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE));
wordCloud.setFontScalar(new LinearFontScalar(10, 40));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("kumo-core/output/wordcloud_rectangle.png");
```


Example using Linear Color Gradients

```java
final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
frequencyAnalyzer.setWordFrequenciesToReturn(500);
frequencyAnalyzer.setMinWordLength(4); 
final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load("text/my_text_file.txt");
final Dimension dimension = new Dimension(600, 600);
final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
wordCloud.setPadding(2);
wordCloud.setBackground(new CircleBackground(300));
// colors followed by and steps between
wordCloud.setColorPalette(new LinearGradientColorPalette(Color.RED, Color.BLUE, Color.GREEN, 30, 30));
wordCloud.setFontScalar( new SqrtFontScalar(10, 40));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("kumo-core/output/wordcloud_gradient_redbluegreen.png");
```

Example of tokenizing chinese text into a circle

```java
final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
frequencyAnalyzer.setWordFrequenciesToReturn(600);
frequencyAnalyzer.setMinWordLength(2);
frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());

final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load("text/chinese_language.txt");
final Dimension dimension = new Dimension(600, 600);
final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
wordCloud.setPadding(2);
wordCloud.setBackground(new CircleBackground(300));
wordCloud.setColorPalette(new ColorPalette(new Color(0xD5CFFA), new Color(0xBBB1FA), new Color(0x9A8CF5), new Color(0x806EF5)));
wordCloud.setFontScalar(new SqrtFontScalar(12, 45));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("kumo-core/output/chinese_language_circle.png");
```

Create a polarity word cloud to contrast two datasets

```java
final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
frequencyAnalyzer.setWordFrequenciesToReturn(750);
frequencyAnalyzer.setMinWordLength(4);
frequencyAnalyzer.setStopWords(loadStopWords());

final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load("text/new_york_positive.txt");
final List<WordFrequency> wordFrequencies2 = frequencyAnalyzer.load("text/new_york_negative.txt");
final Dimension dimension = new Dimension(600, 600);
final PolarWordCloud wordCloud = new PolarWordCloud(dimension, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR);
wordCloud.setPadding(2);
wordCloud.setBackground(new CircleBackground(300));
wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
wordCloud.build(wordFrequencies, wordFrequencies2);
wordCloud.writeToFile("kumo-core/output/polar_newyork_circle_blur_sqrt_font.png");
```


Create a Layered Word Cloud from two images/two word sets

```java
final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
frequencyAnalyzer.setWordFrequenciesToReturn(300);
frequencyAnalyzer.setMinWordLength(5);
frequencyAnalyzer.setStopWords(loadStopWords());

final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load("text/new_york_positive.txt");
final List<WordFrequency> wordFrequencies2 = frequencyAnalyzer.load("text/new_york_negative.txt");
final Dimension dimension = new Dimension(600, 386);
final LayeredWordCloud layeredWordCloud = new LayeredWordCloud(2, dimension, CollisionMode.PIXEL_PERFECT);

layeredWordCloud.setPadding(0, 1);
layeredWordCloud.setPadding(1, 1);

layeredWordCloud.setFontOptions(0, new KumoFont("LICENSE PLATE", FontWeight.BOLD));
layeredWordCloud.setFontOptions(1, new KumoFont("Comic Sans MS", FontWeight.BOLD));

layeredWordCloud.setBackground(0, new PixelBoundryBackground("backgrounds/cloud_bg.bmp"));
layeredWordCloud.setBackground(1, new PixelBoundryBackground("backgrounds/cloud_fg.bmp"));

layeredWordCloud.setColorPalette(0, new ColorPalette(new Color(0xABEDFF), new Color(0x82E4FF), new Color(0x55D6FA)));
layeredWordCloud.setColorPalette(1, new ColorPalette(new Color(0xFFFFFF), new Color(0xDCDDDE), new Color(0xCCCCCC)));

layeredWordCloud.setFontScalar(0, new SqrtFontScalar(10, 40));
layeredWordCloud.setFontScalar(1, new SqrtFontScalar(10, 40));

layeredWordCloud.build(0, wordFrequencies);
layeredWordCloud.build(1, wordFrequencies2);
layeredWordCloud.writeToFile("kumo-core/output/layered_word_cloud.png");
```

Create a ParallelLayeredWordCloud using 4 distinct Rectangles.<br>
Every Rectangle will be processed in a separate thread, thus minimizing build-time significantly
NOTE: This will eventually be natively handled along with better internal data structures.

```java
final Dimension dimension = new Dimension(2000, 2000);
ParallelLayeredWordCloud parallelLayeredWordCloud = new ParallelLayeredWordCloud(4, dimension, CollisionMode.PIXEL_PERFECT);

// Setup parts for word clouds
final Normalizer[] NORMALIZERS = new Normalizer[] { 
    new UpperCaseNormalizer(), 
    new LowerCaseNormalizer(),
    new BubbleTextNormalizer(),
    new StringToHexNormalizer() 
};
final Font[] FONTS = new Font[] { 
            new Font("Lucida Sans", Font.PLAIN, 10), 
            new Font("Comic Sans", Font.PLAIN, 10),
            new Font("Yu Gothic Light", Font.PLAIN, 10), 
            new Font("Meiryo", Font.PLAIN, 10) 
};
final List<List<WordFrequency>> listOfWordFrequencies = new ArrayList<>();
final Point[] positions = new Point][] { new Point(0, 0), new Point(0, 1000), new Point(1000, 0), new Point(1000, 1000) };
final Color[] colors = new Color[] { Color.RED, Color.WHITE, new Color(0x008080)/* TEAL */, Color.GREEN };

// set up word clouds
for (int i = 0; i < lwc.getLayers(); i++) {
    final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
    frequencyAnalyzer.setMinWordLength(3);
    frequencyAnalyzer.setNormalizer(NORMALIZERS[i]);
    frequencyAnalyzer.setWordFrequenciesToReturn(1000);
    listOfWordFrequencies.add(frequencyAnalyzer.load("text/english_tide.txt"));

    final WordCloud worldCloud = parallelLayeredWordCloud.getAt(i);
    worldCloud.setAngleGenerator(new AngleGenerator(0));
    worldCloud.setPadding(3);
    worldCloud.setWordStartStrategy(new CenterWordStart());
    worldCloud.setKumoFont(new KumoFont(FONTS[i]));
    worldCloud.setColorPalette(new ColorPalette(colors[i]));

    worldCloud.setBackground(new RectangleBackground(positions[i], dimension));
    worldCloud.setFontScalar(new LinearFontScalar(10, 40));
}

// start building
for (int i = 0; i < lwc.getLayers(); i++) {
    parallelLayeredWordCloud.build(i, listOfWordFreqs.get(i));
}

parallelLayeredWordCloud.writeToFile("parallelBubbleText.png");
```

Refer to JPanelDemo.java for an example integrating into a JPanel.


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
To add your own just create a class that override the `Tokenizer` interface and call the `FrequencyAnalyzer.setTokenizer()` or `FrequencyAnalyzer.addTokenizer()`.

| Tokenizer   |
|-------------|
| WhiteSpaceWordTokenizer |
| ChineseWordTokenizer |


### Filters

After tokenization, filters are applied to each word to determine whether or not should be omitted from the word list. 

To add set the filter, call `FrequencyAnalyzer.setFilter()` or `FrequencyAnalyzer.addFilter()`


| Tokenizer   | Description |
|-------------|-------------|
| UrlFilter | A filter to remove words that are urls. |
| CompositeFilter | A wrapper of a collection of filters. |
| StopWordFilter | Internally used, the FrequencyAnalyzer makes this filter easy to use via `FrequencyAnalyzer.setStopWords()`. |
| WordSizeFilter | Internally used, the FrequencyAnalyzer makes this filter easy to use via `FrequencyAnalyzer.setMinWordLength()` and `FrequencyAnalyzer.setMaxWordLength()`. |

### Normalizers

After word tokenization and filtering has occurred you can further transform each word via a normalizer.
The default normalizer ia `lowerCase•characterStripping*trimToEmpty(word)`, the normalizer is even named `DefaultNormalizer`

To add set the normalizer, call `FrequencyAnalyzer.setNormalizer()` or `FrequencyAnalyzer.addNormalizer()`

| Normalizers | Description |
|-------------|-------------|
| CharacterStrippingNormalizer | Constructed with a Pattern, it will replace all matched occurrences with a configurable 'replaceWith' string. The default pattern is `\\.|:|;|\\(|\\)|\"|,|\\?|,|!|<|>|/`|
| LowerCaseNormalizer | Converts all text to lowercase. |
| UpperCaseNormalizer | Converts all text to uppercase. |
| TrimToEmptyNormalizer | Trims the text down to an empty string, even if null. |
| UpsideDownNormalizer | Converts A-Z,a-z,0-9 character to an upside-down variant. |
| StringToHexNormalizer | Converts each character to it's hex value and concatenates them. |
| DefaultNormalizer | Combines the TrimToEmptyNormalizer, CharacterStrippingNormalizer, and LowerCaseNormalizer. |
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

My primary IDE of choice is IntelliJ due to their robust tooling as well as code analysis/inspections. If using [IntelliJ IDEA](https://www.jetbrains.com/idea/), I recommend importing `KumoIntelliJInspections.xml`. I am also consiering adding Checkstyle support.
