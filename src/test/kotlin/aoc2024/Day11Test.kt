package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day11Test {

    @Nested
    inner class Day11RunnerTest {
        private val runner = Day11(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 55312
        }

        @Test
        fun blink75() {
            runner.blinkManyTimes(listOf(0), 25) shouldBe 55312
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 0
        }
    }

}
