Kumo
==============

My main goal is to create a powerful and user friendly Word Cloud API in Java. Kumo directly generates an image file without the need to create an applet (as many other libraries do). Kumo, with the exception of it's maven dependencies should be easy to integegrate into current Java Maven projects. 

Please feel free to jump in and help improve Kumo! There are many places for performance optimization in Kumo!

Current Features
- Draw Rectangle, Circle or Image Overlay word clouds. Image Overlay will draw words over all non-transparent pixels.
- Linear, Square-Root Font Scalars. Fully extendible.
- Variable Font Sizes.
- Word Rotation. Just provide a Start Angle, End Angle, and number of slices.
- Custom BackGround Color. Fully customizable BackGrounds coming soon.
- Word Padding.
- Load Custom Color Pallettes.
- Two Modes that of Colision and Padding: PIXEL_PERFECT and RECTANGLE.
- Polar Word Clouds. Draw two opposing word clouds in one image to easily compare/contrast date sets.
- Layered Word Clouds. Overlay multiple word clouds.
- WhiteSpace and Chinese Word Tokenizer. Fully extendible. 
- Frequency Analyzer to tokenize, filter and compute word counts.

<table>
<tr><td>
<img src="https://raw.githubusercontent.com/kennycason/kumo/master/output/datarank_wordcloud_circle_sqrt_font.png" width="300"/>
</td><td>
<img src="https://raw.githubusercontent.com/kennycason/kumo/master/output/chinese_language_circle.png" width="300"/>
</td></tr>
<tr><td>
<img src="https://raw.githubusercontent.com/kennycason/kumo/master/output/whale_wordcloud_large2.png" width="300"/>
</td><td>
<img src="https://raw.githubusercontent.com/kennycason/kumo/master/output/whale_wordcloud_large3.png" width="300"/>
</td></tr>
<tr><td>
<img src="https://raw.githubusercontent.com/kennycason/kumo/master/output/polar_newyork_rectangle_blur.png" width="300"/>
</td><td>
<img src="https://raw.githubusercontent.com/kennycason/kumo/master/output/polar_tide_chinese_vs_english2.png" width="300"/>
</td></tr>
<tr><td>
<img src="https://raw.githubusercontent.com/kennycason/kumo/master/output/whale_wordcloud_large_angles.png" width="300"/>
</td><td>
<img src="https://raw.githubusercontent.com/kennycason/kumo/master/output/layered_word_cloud.png" width="300"/>
</td></tr>
<tr><td>
<img src="https://raw.githubusercontent.com/kennycason/kumo/master/output/polar_newyork_whale_large_blur.png" width="300"/>
</td><td>
<img src="https://raw.githubusercontent.com/kennycason/kumo/master/output/wordcloud_rectangle.png" width="300"/>
</td></tr>
</table>

Example to generate a Word Cloud on top of an image.

```java
final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
frequencyAnalizer.setWordFrequencesToReturn(300);
frequencyAnalizer.setMinWordLength(4);
frequencyAnalizer.setStopWords(loadStopWords());

final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/datarank.txt"));
final WordCloud wordCloud = new WordCloud(500, 312, CollisionMode.PIXEL_PERFECT);
wordCloud.setPadding(2);
wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/whale_small.png")));
wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
wordCloud.setFontScalar(new LinearFontScalar(10, 40));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("output/whale_wordcloud_small.png");
```

Example to generate a circular Word Cloud.

```java
final List<WordFrequency> wordFrequencies = buildWordFrequences().subList(0, 150);

final WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.PIXEL_PERFECT);
wordCloud.setPadding(2);
wordCloud.setBackground(new CircleBackground(300));
wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("output/datarank_wordcloud_circle_sqrt_font.png");
```

Example to generate a rectangle Word Cloud

```java
final List<WordFrequency> wordFrequencies = buildWordFrequences().subList(0, 150);

final WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.RECTANGLE);
wordCloud.setPadding(0);
wordCloud.setBackground(new RectangleBackground(600, 600));
wordCloud.setColorPalette(buildRandomColorPallete(20));
wordCloud.setFontScalar(new LinearFontScalar(10, 40));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("output/wordcloud_rectangle.png");
```

Example of tokenizing chinese text into a circle

```java
final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
frequencyAnalizer.setWordFrequencesToReturn(600);
frequencyAnalizer.setMinWordLength(2);
frequencyAnalizer.setWordTokenizer(new ChineseWordTokenizer());

final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/chinese_language.txt"));
final WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.PIXEL_PERFECT);
wordCloud.setPadding(2);
wordCloud.setBackground(new CircleBackground(300));
wordCloud.setColorPalette(new ColorPalette(new Color(0xD5CFFA), new Color(0xBBB1FA), new Color(0x9A8CF5), new Color(0x806EF5)));
wordCloud.setFontScalar(new SqrtFontScalar(12, 45));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("output/chinese_language_circle.png");
```

Create a polarity word cloud to contrast two datasets

```java
final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
frequencyAnalizer.setWordFrequencesToReturn(750);
frequencyAnalizer.setMinWordLength(4);
frequencyAnalizer.setStopWords(loadStopWords());

final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/new_york_positive.txt"));
final List<WordFrequency> wordFrequencies2 = frequencyAnalizer.load(getInputStream("text/new_york_negative.txt"));

final PolarWordCloud wordCloud = new PolarWordCloud(600, 600, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR);
wordCloud.setPadding(2);
wordCloud.setBackground(new CircleBackground(300));
wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
wordCloud.build(wordFrequencies, wordFrequencies2);
wordCloud.writeToFile("output/polar_newyork_circle_blur_sqrt_font.png");
```


Create a Layered Word Cloud from two images/two word sets

```java
final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
frequencyAnalizer.setWordFrequencesToReturn(300);
frequencyAnalizer.setMinWordLength(5);
frequencyAnalizer.setStopWords(loadStopWords());

final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/new_york_positive.txt"));
final List<WordFrequency> wordFrequencies2 = frequencyAnalizer.load(getInputStream("text/new_york_negative.txt"));

final LayeredWordCloud layeredWordCloud = new LayeredWordCloud(2, 600, 386, CollisionMode.PIXEL_PERFECT);

layeredWordCloud.setPadding(0, 1);
layeredWordCloud.setPadding(1, 1);

layeredWordCloud.setFontOptions(0, new FontOptions("LICENSE PLATE", Font.BOLD));
layeredWordCloud.setFontOptions(1, new FontOptions("Comic Sans MS", Font.BOLD));

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

