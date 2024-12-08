package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day7Test {

    @Nested
    inner class Day7RunnerTest {
        private val runner = Day7(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 3749
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 11387
        }
    }

}
