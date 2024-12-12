package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day12Test {

    @Nested
    inner class Day12RunnerTest {
        private val runner = Day12(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 1930
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 1206
        }
    }

}
