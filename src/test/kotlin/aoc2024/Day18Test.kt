package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day18Test {

    @Nested
    inner class Day18RunnerTest {
        private val runner = Day18(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe "22"
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe "6,1"
        }
    }

}
