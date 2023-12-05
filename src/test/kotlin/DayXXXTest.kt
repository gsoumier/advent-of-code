import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

@Ignore
class DayXXXTest {

    private val sampleDayXXX = DayXXXLine("Bla Bla")

    @Nested
    inner class DayXXXParserTest {
        private val parser = DayXXXParser()

        @Test
        fun testParse() {
            parser.parseLine(1, "Bla Bla Bla") shouldBe sampleDayXXX
        }
    }


    @Nested
    inner class DayXXXLineTest {
        @Test
        fun test() {
            sampleDayXXX.line shouldBe "Bla"
        }
    }

    @Nested
    inner class DayXXXRunnerTest {
        private val runner = DayXXX(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 51
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 99
        }
    }

}
