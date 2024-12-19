package aoc2024

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
            runner.partOne() shouldBe 6
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 16
        }

        @Test
        fun testFindPossiblePatterns() {
            runner.findPossiblesPattern("br") shouldBe 2
            runner.findPossiblesPattern("gb") shouldBe 2
            runner.findPossiblesPattern("gbbr") shouldBe 4
        }
    }

}
