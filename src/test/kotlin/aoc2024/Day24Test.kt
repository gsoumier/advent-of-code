package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day24Test {

    @Nested
    inner class Day24RunnerTest {
        private val runner = Day24(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 2024
        }

    }

}
