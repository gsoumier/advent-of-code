import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class Day18Test {

    private val sampleDay18 = Day18Line(Direction.S, 2,  "d2c081")

    @Nested
    inner class Day18ParserTest {
        private val parser = Day18Parser()

        @Test
        fun testParse() {
            parser.parseLine("D 2 (#d2c081)") shouldBe sampleDay18
            sampleDay18.realLength shouldBe 863240
            sampleDay18.realDirection shouldBe Direction.S
        }
    }


    @Nested
    inner class Day18RunnerTest {
        private val runner = Day18(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 62
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 952408144115
        }
    }

}
