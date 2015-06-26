package wordcloud.nlp;

/**
 * Defines file types
 * @author Satish
 *
 */
public enum FileType {
	/**
	 * This type of files would have data present in key-value format, i.e.
	 * word followed by its frequency.
	 * Example:
	 * <p>
	 * <br>foo 23
	 * <br>bar 5
	 * <br>hello 13
	 * </p>
	 */
	KEY_VALUE
	/**
	 * This type of files would have data present in regular format.
	 * Example:
	 * <p> 
	 * Early to bed and early to rise, makes a man healthy, wealthy and wise.
	 * </p>
	 */
	,REGULAR;
}
