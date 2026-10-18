package com.kennycason.kumo.cli

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

class ParenthesisSerializerTest {

    @Test
    fun serialize() {
        expectThat(ParenthesisSerializer.serialize(emptyList<String>())).isEqualTo("")
        expectThat(ParenthesisSerializer.serialize(listOf("a"))).isEqualTo("(a)")
        expectThat(ParenthesisSerializer.serialize(listOf("a", "b", "c"))).isEqualTo("(a),(b),(c)")
    }

    @Test
    fun deserialize() {
        expectThat(ParenthesisSerializer.deserialize("").isEmpty()).isTrue()
        expectThat(ParenthesisSerializer.deserialize("(a)").size).isEqualTo(1)
        expectThat(ParenthesisSerializer.deserialize("(a)")[0]).isEqualTo("a")

        expectThat(ParenthesisSerializer.deserialize("(a),(b)").size).isEqualTo(2)
        expectThat(ParenthesisSerializer.deserialize("(a),(b)")[0]).isEqualTo("a")
        expectThat(ParenthesisSerializer.deserialize("(a),(b)")[1]).isEqualTo("b")
    }
}
