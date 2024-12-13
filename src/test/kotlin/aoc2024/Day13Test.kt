package aoc2024

import Coord
import InputType
import LongCoord
import Vector
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day13Test {

    @Nested
    inner class Day13RunnerTest {
        private val runner = Day13(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 480
        }


        @Test
        fun testError() {
            ClawMachine(
                Vector(Coord(74,90)),
                Vector(Coord(52,13)),
                LongCoord(3526,3384),
            ).resolution(false) shouldBe 123
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 0
        }
    }

}
