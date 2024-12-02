package aoc2023

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day17Test {

    @Nested
    inner class Day17RunnerTest {
        private val runner = Day17(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 102
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 94
        }
    }

}
