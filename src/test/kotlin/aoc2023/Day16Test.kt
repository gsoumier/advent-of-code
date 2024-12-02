package aoc2023

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day16Test {

    @Nested
    inner class Day16RunnerTest {
        private val runner = Day16(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 46
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 99
        }
    }

}
