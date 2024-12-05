package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day4Test {

    @Nested
    inner class Day4RunnerTest {
        private val runner = Day4(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 18
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 9
        }
    }

}
