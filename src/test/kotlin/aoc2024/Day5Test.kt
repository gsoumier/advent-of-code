package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day5Test {

    @Nested
    inner class Day5RunnerTest {
        private val runner = Day5(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 143
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 123
        }
    }

}
