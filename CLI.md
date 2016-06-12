# Kumo CLI Usage 

### --type, -t

The type of word cloud to generate.

| Value | Description |
|-------|-------------|
| default | (default) Generate a standard word cloud. |
| polar | Generate a word cloud from two text sources. The two word clouds will be drawn on the same canvas and will bleed into each other. This is ideal for contrasting two pieces of data such as positive and negative sentiment. |
| layered | Generate multiple word clouds and layer them over one another. This is done by providing layered imaged. The word clouds will be plotted on the non-transparent pixels of the layered images. |

### --min-word-length, -mwl

The minimum word length required to be allowed in the word cloud.

### --stopwords, -sw

A comma separated list of words to exclude from the word cloud.

### --stopwords-file, -swf 

A file of stop words. Format should be one word per line.

### --word-count, -wc

Number of words from data set to draw to word cloud. After the words are sorted by frequency, the words are attempted to be placed in descending order.

### --input, -i

One ore more input sources. Input sources may be local files or Urls. If more than one input source is provided they must be comma separated.

For standard word clouds only the first input source will be analyzed. Multiple input sources are only relevant for polar or layered word clouds.

### --output, -o

Output file for the generated word cloud.

### --width, -w

Width of the word cloud. Default is 640px.

### --height, -h

Height of the word cloud. Default is 480px.

### --collision, -col

| Value | Description |
|-------|-------------|
| pixel_perfect | (default) When placing text into the word cloud check pixel-by-pixel to determine if text overlaps. |
| rectangle | Perform simple rectangular collision detection when placing text. This is results in faster generation of word clouds but they may not be aesthetically pleasing. |

### --padding, -p

The minimum padding allowed between two words in the word cloud. This works with pixel-perfect collision detection as well. Default 2.

### --background, -bg

One ore more input sources. Input sources may be local files or Urls of an image used to define the shape of the word cloud. By default the word cloud is drawn onto a rectangle. The word cloud will place text only in places where background image has non-transparent pixels.

For standard word clouds only the first input source will be used. Multiple input sources are only relevant for layered word clouds. Each background image will be applied to a layer in the order they are listed. 

### --color, -c

A comma separated list of colors to use in the word cloud. Values most be provided in one of the below formats

| Format | Description/Example |
|--------|-------------|
| rgb    | (r,g,b)[,(r,g,b)] <br/>e.g. (0xff,0x00,0x00),(0x00,0xff,0x00),(0x00,0x00,0xff) <br\>e.g.(255,0,0),(0,255,0),(0,0,255)|
| hex    | hex[,hex2]<br/>e.g. 0xff0000,0x00ff00,0x0000ff|
| base10 | hex[,hex2]<br/>e.g. 16711680,65280,255 |

### --font-scalar, -fs

Method to scale font

| Value | Description |
|-------|-------------|
| linear | (default) Linear scale font between lower and upper font sizes. |
| sqrt | Square Root scale font between lower and upper font sizes. |
| log | Logarithmically (base e) scale font between lower and upper font sizes. |

### --font-size-min, -fmin

Minimum font size, default 10

### -- font-size-max, -fmax

Maximum font size, default 40

### --font-weight, -fw

One or more fonts. If more than one font is listed they must be comma separated.

| Value |
|--------|
| plain (default) |
| bold |
| italic |

### --font-type, -ft

The name of the font to use. The system must have the font loaded already.

### --word-start, -ws

Determine where to start drawing text to the word cloud.

| Value | Description |
|-------|-------------|
| center | (default) Starts placing in the center of the word cloud. |
| random | Select a random position to start placing words. |

### --normalizer, -n

One or more normalizers to apply to words in the word cloud.

| Value | Description |
|-------|-------------|
| uppercase | Convert each character to uppercase. |
| lowercase | Convert each character to lowercase. |
| trim | Trim spaces from words. (Typically a good tokenizer resolves this. |
| upsidedown | Draws the fonts upside down. Only works with alphanumeric characters. e.g. Y -> ʎ |
| bubble | replace alphabet characters with bubble representations. e.g. a -> ⓐ |
| character-stripping | By default this normalizer will remove common punctuation characters. It is programmatically configurable and will eventually be supported in the CLI as well. |

### --tokenizer,,m  ,,,, -t

Determine how to tokenize the input text. It is still TBD on the future of tokenization existing in the Kumo core package. 

| Value | Description |
|-------|-------------|
| white-space | (default) Performs a simple white space tokenization of the text. |
| english | Use an English language aware tokenizer to tokenize. (org.languagetool.language-en) |
| chinese | Use an Chinese language aware tokenizer to tokenize. (org.languagetool.language-zh) |