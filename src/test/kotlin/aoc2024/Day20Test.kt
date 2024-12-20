package aoc2024

import Coord
import InputType
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day20Test {

    @Nested
    inner class Day20RunnerTest {
        private val runner = Day20(InputType.SAMPLE)

        @Test
        fun testPath() {
            runner.dijikstra[runner.end]!!.distance shouldBe 84
        }

        @Test
        fun partOne() {
            runner.partOne() shouldBe 10
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 41
        }
    }

}
