import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day12Test {

    private val sampleDay12 = Day12Line("?###????????", listOf(3,2,1))

    @Nested
    inner class Day12ParserTest {
        private val parser = Day12Parser()

        @Test
        fun testParse() {
            parser.parseLine(1, "?###???????? 3,2,1") shouldBe sampleDay12
        }
    }


    @Nested
    inner class Day12LineTest {
        @Test
        fun shouldMatch() {
            sampleDay12.damagedString.matchesStartOf("###..##..#..") shouldBe false
        }
        @Test
        fun shouldNotMatch() {
            sampleDay12.damagedString.matchesStartOf("###..##..#..") shouldBe false
        }
    }
    
    @Nested
    inner class Day12RunnerTest {
        private val runner = Day12(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 21
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 525152
        }
    }

}
