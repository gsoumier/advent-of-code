package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day10Test {

    @Nested
    inner class Day10RunnerTest {
        private val runner = Day10(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 36
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 81
        }
    }

}
