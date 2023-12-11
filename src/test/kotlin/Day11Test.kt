import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class Day11Test {

    @Nested
    inner class Day11RunnerTest {
        private val runner = Day11(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 374
        }

        @Test
        fun partTwo() {
            runner.calculate(100L) shouldBe 8410
        }
    }

}
