package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day3Test {

    @Nested
    inner class Day3RunnerTest {
        private val runner = Day3(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 161
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 48
        }


    }

}
