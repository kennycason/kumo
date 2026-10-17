package com.kennycason.kumo.cli

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ParenthesisSerializerTest {

    @Test
    fun serialize() {
        assertEquals("", ParenthesisSerializer.serialize(emptyList<String>()))
        assertEquals("(a)", ParenthesisSerializer.serialize(listOf("a")))
        assertEquals("(a),(b),(c)", ParenthesisSerializer.serialize(listOf("a", "b", "c")))
    }

    @Test
    fun deserialize() {
        assertTrue(ParenthesisSerializer.deserialize("").isEmpty())
        assertEquals(1, ParenthesisSerializer.deserialize("(a)").size)
        assertEquals("a", ParenthesisSerializer.deserialize("(a)")[0])

        assertEquals(2, ParenthesisSerializer.deserialize("(a),(b)").size)
        assertEquals("a", ParenthesisSerializer.deserialize("(a),(b)")[0])
        assertEquals("b", ParenthesisSerializer.deserialize("(a),(b)")[1])
    }
}
