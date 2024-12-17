package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day17Test {

    @Nested
    inner class Day17RunnerTest {

        @Test
        fun partOne() {
            Day17(InputType.SAMPLE).partOne() shouldBe "4,6,3,5,6,3,5,2,1,0"
        }

        @Test
        fun partTwo() {
            Day17(InputType.SAMPLE2).partTwo() shouldBe "117440"
        }

    }

}
