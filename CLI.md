# Kumo CLI Usage 

### --type

The type of word cloud to generate.

| Value | Description |
|-------|-------------|
| standard | (default) Generate a standard word cloud. |
| polar | Generate a word cloud from two text sources. The two word clouds will be drawn on the same canvas and will bleed into each other. This is ideal for contrasting two pieces of data such as positive and negative sentiment. |
| layered | Generate multiple word clouds and layer them over one another. This is done by providing layered imaged. The word clouds will be plotted on the non-transparent pixels of the layered images. |

### --input, -i

One ore more input sources. Input sources may be local files or Urls. If more than one input source is provided they must be comma separated.

For standard word clouds only the first input source will be analyzed. Multiple input sources are only relevant for polar or layered word clouds.

### --output, -o

Output file for the generated word cloud.

### --min-word-length

The minimum word length required to be allowed in the word cloud. Default is 2.

### --stop-words

A comma separated list of words to exclude from the word cloud.

### --stop-words-file

A file of stop words. Format should be one word per line.

### --word-count

Number of words from data set to draw to word cloud. After the words are sorted by frequency, the words are attempted to be placed in descending order.

Default is 1,000 words.

### --width, -w

Width of the word cloud. Default is 640px.

### --height, -h

Height of the word cloud. Default is 480px.

### --collision

The collision algorithm to use when placing text into the word cloud.

| Value | Description |
|-------|-------------|
| pixel_perfect | (default) When placing text into the word cloud check pixel-by-pixel to determine if text overlaps. |
| rectangle | Perform simple rectangular collision detection when placing text. This is results in faster generation of word clouds but they may not be aesthetically pleasing. |

### --padding

The minimum padding allowed between two words in the word cloud. This works with pixel-perfect collision detection as well. Default is 2px.

### --background, -bg

One ore more input sources. Input sources may be local files or Urls of an image used to define the shape of the word cloud. By default the word cloud is drawn onto a rectangle. The word cloud will place text only in places where background image has non-transparent pixels.

For standard word clouds only the first input source will be used. Multiple input sources are only relevant for layered word clouds. Each background image will be applied to a layer in the order they are listed. 

### --background-color

Background color. Default is Black.

### --color, -c

A comma separated list of colors to use for the word cloud text. Values most be provided in one of the below formats

| Format | Description/Example |
|--------|-------------|
| rgb    | (r,g,b)[,(r,g,b)] <br/>e.g. (0xff,0x00,0x00),(0x00,0xff,0x00),(0x00,0x00,0xff) <br\>e.g.(255,0,0),(0,255,0),(0,0,255)|
| hex    | hex[,hex2]<br/>e.g. 0xff0000,0x00ff00,0x0000ff|
| base10 | hex[,hex2]<br/>e.g. 16711680,65280,255 |


For layered and polar word clouds, separate the color sets with the "|" character.

e.g. `(255,0,0),(0,255,0),(0,0,255)|(255,255,255),(128,128,128),(0,0,0)` This represents two different color palettes.


### --polar-blend-mode

Determine how to blend the two poles of the word cloud.

| Format | Description |
|--------|-------------|
| blur   | The words will bleed over each other causing an appearance of smooth blending. |
| even   | The words stop abruptly after colliding with the other pole. |

### --font-scalar

Method to scale font.

| Value | Description |
|-------|-------------|
| linear | (default) Linear scale font between lower and upper font sizes. |
| sqrt | Square Root scale font between lower and upper font sizes. |
| log | Logarithmically (base e) scale font between lower and upper font sizes. |

### --font-size-min

Minimum font size, default is 10px.

### -- font-size-max

Maximum font size, default is 40px.

### --font-weight

A font weight.

| Value |
|--------|
| plain |
| bold (default) |
| italic |

### --font-type

The name of the font to use. The system must have the font loaded already. Default is "Comic Sans MS".

### --encoding

Character Encoding. Default is UTF-8

### --word-start

Determine where to start drawing text to the word cloud.

| Value | Description |
|-------|-------------|
| center | (default) Starts placing in the center of the word cloud. |
| random | Select a random position to start placing words. |

### --normalizer

One or more normalizers to apply to words in the word cloud.

| Value | Description |
|-------|-------------|
| uppercase | Convert each character to uppercase. |
| lowercase | Convert each character to lowercase. |
| trim | Trim spaces from words. (Typically a good tokenizer resolves this. |
| upsidedown | Draws the fonts upside down. Only works with alphanumeric characters. e.g. Y -> ʎ |
| bubble | replace alphabet characters with bubble representations. e.g. a -> ⓐ |
| character-stripping | By default this normalizer will remove common punctuation characters. It is programmatically configurable and will eventually be supported in the CLI as well. |

### --tokenizer

Determine how to tokenize the input text. It is still TBD on the future of tokenization existing in the Kumo core package. 

| Value | Description |
|-------|-------------|
| white-space | (default) Performs a simple white space tokenization of the text. |
| english | Use an English language aware tokenizer to tokenize. (org.languagetool.language-en) |
| chinese | Use an Chinese language aware tokenizer to tokenize. (org.languagetool.language-zh) |


## TODO

- Add support for background shapes (circle, rectangle)
- Add support for word angles.
- Add support for word placing strategies.
- Add better support for per-layer settings in layered word cloud.