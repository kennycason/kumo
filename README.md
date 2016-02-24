Kumo
==============

Kumo's goal is to create a powerful and user friendly Word Cloud API in Java. Kumo directly generates an image file without the need to create an applet as many other libraries do.

Please feel free to jump in and help improve Kumo! There are many places for performance optimization in Kumo!

**Current Features**
- Draw Rectangle, Circle or Image Overlay word clouds. Image Overlay will draw words over all non-transparent pixels.
- Linear, Square-Root Font Scalars. Fully extendable.
- Variable Font Sizes.
- Word Rotation. Just provide a Start Angle, End Angle, and number of slices.
- Custom BackGround Color. Fully customizable BackGrounds coming soon.
- Word Padding.
- Load Custom Color Palettes.
- Two Modes that of Collision and Padding: PIXEL_PERFECT and RECTANGLE.
- Polar Word Clouds. Draw two opposing word clouds in one image to easily compare/contrast date sets.
- Layered Word Clouds. Overlay multiple word clouds.
- WhiteSpace and Chinese Word Tokenizer. Fully extendable. 
- Frequency Analyzer to tokenize, filter and compute word counts.

**Available from Maven Central**
```xml
<dependency>
    <groupId>com.kennycason</groupId>
    <artifactId>kumo</artifactId>
    <version>1.5</version>
</dependency>
```

**Screenshots**
<table>
<tr><td>
<img src="output/whale_wordcloud_large_impact.png?raw=true" width="300"/>
</td><td>
<img src="output/simplymeasured.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="output/layered_haskell.png?raw=true" width="300"/>
</td><td>
<img src="output/layered_pho_bowl.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="output/polar_newyork_rectangle_blur.png?raw=true" width="300"/>
</td><td>
<img src="output/polar_tide_chinese_vs_english2.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="output/datarank_wordcloud_circle_sqrt_font.png?raw=true" width="300"/>
</td><td>
<img src="output/chinese_language_circle.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="output/polar_newyork_whale_large_blur.png?raw=true" width="300"/>
</td><td>
<img src="output/layered_word_cloud.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="output/whale_wordcloud_large_angles.png?raw=true" width="300"/>
</td><td>
<img src="output/wordcloud_rectangle.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="output/wordcloud_gradient_redbluegreen.png?raw=true" width="300"/>
</td><td>
<img src="output/wordcloud_gradient_whiteredblue.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="output/bubbletext.png?raw=true" width="300"/>
</td><td>
<img src="output/parallelBubbleText.png?raw=true" width="300"/>
</td></tr>
</table>

**Examples**

Example to generate a Word Cloud on top of an image.

```java
final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
frequencyAnalyzer.setWordFrequenciesToReturn(300);
frequencyAnalyzer.setMinWordLength(4);
frequencyAnalyzer.setStopWords(loadStopWords());

final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
final Dimension dimension = new Dimension(500, 312);
final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
wordCloud.setPadding(2);
wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/whale_small.png")));
wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
wordCloud.setFontScalar(new LinearFontScalar(10, 40));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("output/whale_wordcloud_small.png");
```

Example to generate a circular Word Cloud.

```java
final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/my_text_file.txt"));
final Dimension dimension = new Dimension(600, 600);
final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
wordCloud.setPadding(2);
wordCloud.setBackground(new CircleBackground(300));
wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("output/datarank_wordcloud_circle_sqrt_font.png");
```

Example to generate a rectangle Word Cloud

```java
final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/my_text_file.txt"));
final Dimension dimension = new Dimension(600, 600);
final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
wordCloud.setPadding(0);
wordCloud.setBackground(new RectangleBackground(dimension));
wordCloud.setColorPalette(buildRandomColorPalette(20));
wordCloud.setFontScalar(new LinearFontScalar(10, 40));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("output/wordcloud_rectangle.png");
```


Example using Linear Color Gradients

```java
final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
frequencyAnalyzer.setWordFrequenciesToReturn(500);
frequencyAnalyzer.setMinWordLength(4); 
final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/my_text_file.txt"));
final Dimension dimension = new Dimension(600, 600);
final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
wordCloud.setPadding(2);
wordCloud.setBackground(new CircleBackground(300));
wordCloud.setColorPalette(new LinearGradientColorPalette(Color.RED, 30, Color.BLUE, 30 , Color.GREEN));
wordCloud.setFontScalar( new SqrtFontScalar(10, 40));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("output/wordcloud_gradient_redbluegreen.png");
```

Example of tokenizing chinese text into a circle

```java
final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
frequencyAnalyzer.setWordFrequenciesToReturn(600);
frequencyAnalyzer.setMinWordLength(2);
frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());

final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/chinese_language.txt"));
final Dimension dimension = new Dimension(600, 600);
final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
wordCloud.setPadding(2);
wordCloud.setBackground(new CircleBackground(300));
wordCloud.setColorPalette(new ColorPalette(new Color(0xD5CFFA), new Color(0xBBB1FA), new Color(0x9A8CF5), new Color(0x806EF5)));
wordCloud.setFontScalar(new SqrtFontScalar(12, 45));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("output/chinese_language_circle.png");
```

Create a polarity word cloud to contrast two datasets

```java
final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
frequencyAnalyzer.setWordFrequenciesToReturn(750);
frequencyAnalyzer.setMinWordLength(4);
frequencyAnalyzer.setStopWords(loadStopWords());

final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/new_york_positive.txt"));
final List<WordFrequency> wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/new_york_negative.txt"));
final Dimension dimension = new Dimension(600, 600);
final PolarWordCloud wordCloud = new PolarWordCloud(dimension, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR);
wordCloud.setPadding(2);
wordCloud.setBackground(new CircleBackground(300));
wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
wordCloud.build(wordFrequencies, wordFrequencies2);
wordCloud.writeToFile("output/polar_newyork_circle_blur_sqrt_font.png");
```


Create a Layered Word Cloud from two images/two word sets

```java
final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
frequencyAnalyzer.setWordFrequenciesToReturn(300);
frequencyAnalyzer.setMinWordLength(5);
frequencyAnalyzer.setStopWords(loadStopWords());

final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/new_york_positive.txt"));
final List<WordFrequency> wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/new_york_negative.txt"));
final Dimension dimension = new Dimension(600, 386);
final LayeredWordCloud layeredWordCloud = new LayeredWordCloud(2, dimension, CollisionMode.PIXEL_PERFECT);

layeredWordCloud.setPadding(0, 1);
layeredWordCloud.setPadding(1, 1);

layeredWordCloud.setFontOptions(0, new KumoFont("LICENSE PLATE", FontWeight.BOLD));
layeredWordCloud.setFontOptions(1, new KumoFont("Comic Sans MS", FontWeight.BOLD));

layeredWordCloud.setBackground(0, new PixelBoundryBackground(getInputStream("backgrounds/cloud_bg.bmp")));
layeredWordCloud.setBackground(1, new PixelBoundryBackground(getInputStream("backgrounds/cloud_fg.bmp")));

layeredWordCloud.setColorPalette(0, new ColorPalette(new Color(0xABEDFF), new Color(0x82E4FF), new Color(0x55D6FA)));
layeredWordCloud.setColorPalette(1, new ColorPalette(new Color(0xFFFFFF), new Color(0xDCDDDE), new Color(0xCCCCCC)));

layeredWordCloud.setFontScalar(0, new SqrtFontScalar(10, 40));
layeredWordCloud.setFontScalar(1, new SqrtFontScalar(10, 40));

layeredWordCloud.build(0, wordFrequencies);
layeredWordCloud.build(1, wordFrequencies2);
layeredWordCloud.writeToFile("output/layered_word_cloud.png");
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
    worldCloud.setWordStartScheme(new CenterWordStart());
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

**Tokenizers**

Tokenizers are the code that splits a sentence/text into a list of words. Currently only two tokenizers are built into Kumo.
To add your own just create a class that override the `Tokenizer` interface and call the `FrequencyAnalyzer.setTokenizer()` or `FrequencyAnalyzer.addTokenizer()`.

| Tokenizer   |
|-------------|
| WhiteSpaceWordTokenizer |
| ChineseWordTokenizer |


**Filters**

After tokenization, filters are applied to each word to determine whether or not should be omitted from the word list. 

To add set the filter, call `FrequencyAnalyzer.setFilter()` or `FrequencyAnalyzer.addFilter()`


| Tokenizer   | Description |
|-------------|-------------|
| UrlFilter | A filter to remove words that are urls. |
| CompositeFilter | A wrapper of a collection of filters. |
| StopWordFilter | Internally used, the FrequencyAnalyzer makes this filter easy to use via `FrequencyAnalyzer.setStopWords()`. |
| WordSizeFilter | Internally used, the FrequencyAnalyzer makes this filter easy to use via `FrequencyAnalyzer.setMinWordLength()` and `FrequencyAnalyzer.setMaxWordLength()`. |

**Normalizers**

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
