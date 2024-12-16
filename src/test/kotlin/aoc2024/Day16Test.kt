package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day16Test {

    @Nested
    inner class Day16RunnerTest {
        private val runner = Day16(InputType.SAMPLE)
        private val runner2 = Day16(InputType.SAMPLE2)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 7036
            runner2.partOne() shouldBe 11048
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 45
            runner2.partTwo() shouldBe 64
        }
    }

}
