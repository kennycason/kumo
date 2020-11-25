package com.kennycason.kumo.nlp.tokenizers;

import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by kenny on 4/27/15.
 */
public class ChineseWordTokenizerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChineseWordTokenizerTest.class);

    @Test
    public void test() {
        final long time = System.currentTimeMillis();
        final WordTokenizer tokenizer = new ChineseWordTokenizer();
        LOGGER.info("load time: " + (System.currentTimeMillis() - time) + " ms");

        assertEquals(Arrays.asList("弹道导弹"), tokenizer.tokenize("弹道导弹"));

        assertEquals(Arrays.asList("我", "是", "美国", "人"), tokenizer.tokenize("我是美国人"));

        assertEquals(Arrays.asList("美国", "人", "的", "文化", ".", "I", "like", "dogs"),
                     tokenizer.tokenize("美国人的文化. I like dogs"));

        assertEquals(Arrays.asList("国家", "都", "有", "自己", "的", "政府", "。", "政府", "是", "税收", "的", "主体", "，", "可以", "实现", "福利", "的", "合理", "利用", "。"),
                     tokenizer.tokenize("国家都有自己的政府。政府是税收的主体，可以实现福利的合理利用。"));

        final List<String> words = tokenizer.tokenize("政府依照法律行使执法权，如果超出法律赋予的权限范围，就是“滥用职权”；如果没有完全行使执法权，就是“不作为”。两者都是政府的错误。");
        assertEquals(45, words.size());
    }

}
