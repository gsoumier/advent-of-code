package aoc2023

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day19Test {

    @Nested
    inner class Day19RunnerTest {
        private val runner = Day19(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 19114
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 167409079868000L
        }
    }

}
