package wordcloud.nlp.tokenizer;

import ch.lambdaj.Lambda;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by kenny on 4/27/15.
 */
public class TestChineseWordTokenizer {

    private static final Logger LOGGER = Logger.getLogger(TestChineseWordTokenizer.class);

    @Test
    public void test() {
        long time = System.currentTimeMillis();
        WordTokenizer parser = new ChineseWordTokenizer();
        System.out.println("load time: " + (System.currentTimeMillis() - time) + " ms");

        List<String> words = parser.tokenize("弹道导弹");
        LOGGER.info(Lambda.join(words));
        assertEquals(1, words.size());

        words = parser.tokenize("美国人的文化.dog");
        LOGGER.info(Lambda.join(words));
        assertEquals(6, words.size());

        words = parser.tokenize("我是美国人");
        LOGGER.info(Lambda.join(words));
        assertEquals(4, words.size());

        words = parser.tokenize("政府依照法律行使执法权，如果超出法律赋予的权限范围，就是“滥用职权”；如果没有完全行使执法权，就是“不作为”。两者都是政府的错误。");
        LOGGER.info(Lambda.join(words));

        words = parser.tokenize("国家都有自己的政府。政府是税收的主体，可以实现福利的合理利用。");
        LOGGER.info(Lambda.join(words));
    }

}
