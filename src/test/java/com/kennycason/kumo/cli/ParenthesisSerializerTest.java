package com.kennycason.kumo.cli;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by kenny on 6/12/16.
 */
public class ParenthesisSerializerTest {

    @Test
    public void serialize() {
        assertEquals("", ParenthesisSerializer.serialize(Collections.emptyList()));
        assertEquals("(a)", ParenthesisSerializer.serialize(Arrays.asList("a")));
        assertEquals("(a),(b),(c)", ParenthesisSerializer.serialize(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void deserialize() {
        assertTrue(ParenthesisSerializer.deserialize("").isEmpty());
        assertEquals(1, ParenthesisSerializer.deserialize("(a)").size());
        assertEquals("a", ParenthesisSerializer.deserialize("(a)").get(0));

        assertEquals(2, ParenthesisSerializer.deserialize("(a),(b)").size());
        assertEquals("a", ParenthesisSerializer.deserialize("(a),(b)").get(0));
        assertEquals("b", ParenthesisSerializer.deserialize("(a),(b)").get(1));
    }
}
