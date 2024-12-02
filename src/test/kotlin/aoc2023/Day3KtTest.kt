package aoc2023

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day3KtTest {

    @Test
    fun toDay3() {
        assertEquals(
            Day3(
                0, listOf(
                    SchematicNumber(467, 0, 3, 0),
                    SchematicNumber(114, 5, 8, 0),
                ), listOf(
                    SchematicSymbol("&", 4, 5, 0)
                )
            ), "467.&114..".toDay3(0)
        )
    }
    @Test
    fun isCloseTo() {
        assertTrue(
            SchematicNumber(114, 5, 7, 0).isCloseTo(
                    SchematicSymbol("&", 4, 5, 0)
                )
            )

    }
    @Test
    fun isCloseToOtherLine() {
        assertTrue(
            SchematicNumber(114, 5, 8, 0).isCloseTo(
                    SchematicSymbol("&", 6, 7, 1)
                )
            )

    }
}
