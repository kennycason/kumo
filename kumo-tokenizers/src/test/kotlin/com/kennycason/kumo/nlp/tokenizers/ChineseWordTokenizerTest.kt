package com.kennycason.kumo.nlp.tokenizers

import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.Assert.assertEquals
import org.junit.Test

private val logger = KotlinLogging.logger {}

class ChineseWordTokenizerTest {

    @Test
    fun test() {
        val time = System.currentTimeMillis()
        val tokenizer: WordTokenizer = ChineseWordTokenizer()
        logger.info { "load time: ${System.currentTimeMillis() - time} ms" }

        assertEquals(listOf("弹道导弹"), tokenizer.tokenize("弹道导弹"))

        assertEquals(listOf("我", "是", "美国", "人"), tokenizer.tokenize("我是美国人"))

        assertEquals(
            listOf("美国", "人", "的", "文化", ".", "I", "like", "dogs"),
            tokenizer.tokenize("美国人的文化. I like dogs")
        )

        assertEquals(
            listOf("国家", "都", "有", "自己", "的", "政府", "。", "政府", "是", "税收", "的", "主体", "，", "可以", "实现", "福利", "的", "合理", "利用", "。"),
            tokenizer.tokenize("国家都有自己的政府。政府是税收的主体，可以实现福利的合理利用。")
        )

        val words = tokenizer.tokenize("政府依照法律行使执法权，如果超出法律赋予的权限范围，就是\u201C滥用职权\u201D；如果没有完全行使执法权，就是\u201C不作为\u201D。两者都是政府的错误。")
        assertEquals(45, words.size)
    }
}
