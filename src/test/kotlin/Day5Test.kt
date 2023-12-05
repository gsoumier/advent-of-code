import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class Day5Test {

    private val sampleDay5 = "Bla Bla"

    @Nested
    inner class Day5ParserTest {
        private val parser = Day5Parser()

        @Test
        fun testParse() {
            parser.parseLine(1, "Bla Bla Bla") shouldBe sampleDay5
        }
    }

    @Nested
    inner class RangeMapLineTest {
        @Test
        fun test() {
            sampleDay5 shouldBe "Bla"
        }
    }

    @Nested
    inner class Day5RunnerTest {
        private val runner = Day5(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 35
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 46
        }
    }

}
