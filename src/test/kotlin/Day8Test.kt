import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class Day8Test {

    private val sampleDay8 = Day8Line("BBB", "AAA" to "ZZZ")

    @Nested
    inner class Day8ParserTest {
        private val parser = Day8Parser()

        @Test
        fun testParse() {
            parser.parseLine(3, "BBB = (AAA, ZZZ)") shouldBe sampleDay8
        }
    }


    @Nested
    inner class Day8LineTest {
        @Test
        fun test() {
            sampleDay8.nextPlaces.next('L') shouldBe "AAA"
        }
    }

    @Nested
    inner class Day8RunnerTest {
        private val runner = Day8(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 2
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 6
        }
    }

}
