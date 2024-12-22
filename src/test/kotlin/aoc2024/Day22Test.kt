package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day22Test {

    @Nested
    inner class Day22RunnerTest {
        private val runner = Day22(InputType.SAMPLE)

        @Test
        fun testEvolve() {
            123L.evolve() shouldBe 15887950L
        }

        @Test
        fun partOne() {
            runner.partOne() shouldBe 37990510L
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 23
        }
    }

}
