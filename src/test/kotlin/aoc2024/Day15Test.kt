package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day15Test {

    @Nested
    inner class Day15RunnerTest {
        private val runner = Day15(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 10092
        }
        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 9021
        }
    }

}
