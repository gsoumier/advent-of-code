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
//
//
//    @Nested
//    inner class Day12LineTest {
//        @Test
//        fun matches() {
//            sampleDay12.matches(".###.##..#..") shouldBe true
//            sampleDay12.matches("###..##..#..") shouldBe false
//        }
//        @Test
//        fun test() {
//            sampleDay12.countMatchingArrangments() shouldBe 10
//        }
//        @Test
//        fun test2() {
//            Day12Line(".??....??????.", listOf(1)).countMatchingArrangments() shouldBe 8
//        }
//        @Test
//        fun testEnd() {
//            Day12Line("??", listOf(1)).countMatchingArrangments() shouldBe 2
//        }
//    }
    
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
