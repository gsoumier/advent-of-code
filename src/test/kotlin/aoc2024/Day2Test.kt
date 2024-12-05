package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day2Test {

    @Nested
    inner class Day2RunnerTest {
        private val runner = Day2(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 2
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 4
        }
    }

}
