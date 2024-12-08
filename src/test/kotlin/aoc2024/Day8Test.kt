package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day8Test {

    @Nested
    inner class Day8RunnerTest {
        private val runner = Day8(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 14
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 34
        }
    }

}
