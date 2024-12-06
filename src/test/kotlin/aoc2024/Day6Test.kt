package aoc2024

import Coord
import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day6Test {

    @Nested
    inner class Day6RunnerTest {
        private val runner = Day6(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 41
        }

        @Test
        fun testLoop() {
            doPatrol(runner.map.changeChar(Coord(3,6), '#'), runner.initial) shouldBe null
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 6
        }
    }

}
