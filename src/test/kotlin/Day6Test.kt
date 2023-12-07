import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class Day6Test {

    private val sampleDay6 = Day6Line(listOf(7,15,30), 71530)

    @Nested
    inner class Day6ParserTest {
        private val parser = Day6Parser()

        @Test
        fun testParse() {
            parser.parseLine(1, "Time:      7  15   30") shouldBe sampleDay6
        }
    }


    @Nested
    inner class Day6LineTest {
        @Test
        fun test() {
//            sampleDay6.line shouldBe "Bla"
        }
    }

    @Nested
    inner class Day6RunnerTest {
        private val runner = Day6(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 288
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 71503
        }
    }

}
