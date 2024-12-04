import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day4Test {

    @Nested
    inner class Day4RunnerTest {
        private val runner = Day4(InputType.SAMPLE)

        @Test
        fun getWord() {
            runner.getWord(Coord(5,0), Next.HORIZONTAL, 1) shouldBe "XMAS"
        }
        @Test
        fun partOne() {
            runner.partOne() shouldBe 18
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 9
        }
    }

}
