package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day9Test {

    @Nested
    inner class Day9RunnerTest {
        private val runner = Day9(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 1928
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 2858
        }
    }

}
