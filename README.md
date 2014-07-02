WordCloud
==============

Java Implementation of a fully functional Word Cloud.
Currently It's pretty slow, but work is being done to speed it up.

Example to generate a Word Cloud on top of an image.

```
final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
frequencyAnalizer.setWordFrequencesToReturn(300);
frequencyAnalizer.setMinWordLength(4);
frequencyAnalizer.setStopWords(loadStopWords());

final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/datarank.txt"));
final WordCloud wordCloud = new WordCloud(500, 312);
wordCloud.setPadding(2);
wordCloud.setCollisionChecker(new RectanglePixelCollisionChecker());
wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/whale_small.png")));
wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
wordCloud.setFontScalar(new LinearFontScalar(10, 40));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("output/whale_wordcloud_small.png");
```

Example to generate a circular Word Cloud.

```
final List<WordFrequency> wordFrequencies = buildWordFrequences().subList(0, 150);

final WordCloud wordCloud = new WordCloud(600, 600);
wordCloud.setPadding(0);
wordCloud.setBackground(new CircleBackground(300));
wordCloud.setColorPalette(buildRandomColorPallete(20));
wordCloud.setFontScalar(new LinearFontScalar(10, 40));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("output/wordcloud_circle.png");
```

Example to generate a rectangle Word Cloud

```
final List<WordFrequency> wordFrequencies = buildWordFrequences().subList(0, 150);

final WordCloud wordCloud = new WordCloud(600, 600);
wordCloud.setPadding(0);
wordCloud.setBackground(new RectangleBackground(600, 600));
wordCloud.setColorPalette(buildRandomColorPallete(20));
wordCloud.setFontScalar(new LinearFontScalar(10, 40));
wordCloud.build(wordFrequencies);
wordCloud.writeToFile("output/wordcloud_rectangle.png");
```

