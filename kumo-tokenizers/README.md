# Kumo Tokenizers

This module is separated from Kumo Core to prevent Kumo Core from becoming too bloated. This module will contain language tokenizers for various languages. 
Currently, the only languages included are the `EnglishWordTokenizer` and the `ChineseWordTokenizer`.

*Note*: All the examples will soon be extracted to another module for better clarity. This readme is just a place holder while I refactor.

Below are a few examples of how to use the `ChineseWordTokenizer`.

```java
@Test
public void dragonChinese() throws IOException {
    final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
    frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
    frequencyAnalyzer.setWordFrequenciesToReturn(900);
    frequencyAnalyzer.setMinWordLength(1);
    frequencyAnalyzer.setStopWords(Arrays.asList("是", "不", "了", "的", "个", "子"));

    final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/chinese_dragon.txt"));
    final Dimension dimension = new Dimension(555, 555);
    final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
    wordCloud.setPadding(1);
    wordCloud.setBackgroundColor(new Color(0xE35A05));
    wordCloud.setAngleGenerator(new AngleGenerator(0));
    wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/dragon.png")));
    wordCloud.setColorPalette(new ColorPalette(new Color(0x0), new Color(0x333333), new Color(0x555555)));
    wordCloud.setFontScalar(new SqrtFontScalar(6, 50));
    wordCloud.build(wordFrequencies);
    wordCloud.writeToFile("output/dragon_chinese.png");
}

@Test
public void chineseCircle() throws IOException {
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
    final long startTime = System.currentTimeMillis();
    wordCloud.build(wordFrequencies);
    LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
    wordCloud.writeToFile("output/chinese_language_circle.png");
}

@Test
public void chineseVsEnglishTideComments() throws IOException {
    final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
    frequencyAnalyzer.setWordFrequenciesToReturn(750);
    frequencyAnalyzer.setMinWordLength(3);
    frequencyAnalyzer.setStopWords(loadStopWords());
    final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/english_tide.txt"));

    final FrequencyAnalyzer chineseFrequencyAnalyzer = new FrequencyAnalyzer();
    chineseFrequencyAnalyzer.setWordFrequenciesToReturn(750);
    chineseFrequencyAnalyzer.setMinWordLength(2);
    chineseFrequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
    final List<WordFrequency> wordFrequencies2 = chineseFrequencyAnalyzer.load(getInputStream("text/chinese_tide.txt"));

    final Dimension dimension = new Dimension(800, 600);
    final PolarWordCloud wordCloud = new PolarWordCloud(dimension, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR);
    wordCloud.setPadding(2);
    wordCloud.setBackground(new RectangleBackground(dimension));
    wordCloud.setFontScalar(new SqrtFontScalar(10, 70));

    final ColorPalette colorPalette = new ColorPalette(new Color(0xD5CFFA), new Color(0xBBB1FA), new Color(0x9A8CF5), new Color(0x806EF5));
    final ColorPalette colorPalette2 = new ColorPalette(new Color(0xFA8E8E), new Color(0xF77979), new Color(0xF55F5F), new Color(0xF24949));
    wordCloud.setColorPalette(colorPalette);
    wordCloud.setColorPalette2(colorPalette2);

    final long startTime = System.currentTimeMillis();
    wordCloud.build(wordFrequencies, wordFrequencies2);
    LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
    wordCloud.writeToFile("output/polar_tide_chinese_vs_english2.png");
}
```