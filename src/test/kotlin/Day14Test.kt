import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class Day14Test {

    @Nested
    inner class Day14RunnerTest {
        private val runner = Day14(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 136
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 64
        }
    }

}
