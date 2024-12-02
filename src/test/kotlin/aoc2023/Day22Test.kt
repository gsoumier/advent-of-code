package aoc2023

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day22Test {

    @Nested
    inner class Day22RunnerTest {
        private val runner = Day22(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 5
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 7
        }
    }

}
