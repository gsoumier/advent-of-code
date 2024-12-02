package aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day2KtTest {

    @Test
    fun toGame() {

        val game =
            "Game 1: 4 green, 2 blue; 1 red, 1 blue, 4 green; 3 green, 4 blue, 1 red; 7 green, 2 blue, 4 red; 3 red, 7 green; 3 red, 3 green".toGame()
        Assertions.assertEquals(
            Game(
                1, listOf(
                    Draw(
                        mapOf(
                            "green" to 4,
                            "blue" to 2,
                        )
                    ),
                    Draw(
                        mapOf(
                            "red" to 1,
                            "blue" to 1,
                            "green" to 4,
                        )
                    ),
                    Draw(
                        mapOf(
                            "green" to 3,
                            "blue" to 4,
                            "red" to 1,
                        )
                    ),
                    Draw(
                        mapOf(
                            "green" to 7,
                            "blue" to 2,
                            "red" to 4,
                        )
                    ),
                    Draw(
                        mapOf(
                            "red" to 3,
                            "green" to 7,
                        )
                    ),
                    Draw(
                        mapOf(
                            "red" to 3,
                            "green" to 3,
                        )
                    ),
                )
            ),
            game,
        )

        assertEquals(7*4*4, game.getPower())
    }
}
