package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day99Test {

    @Nested
    inner class Day99RunnerTest {
        private val runner = Day99(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 0
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 0
        }
    }

}
