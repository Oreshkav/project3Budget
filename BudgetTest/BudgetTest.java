import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.time.LocalDate;
public class BudgetTest {
        @Test
        public void test1_suma() throws IOException {

            Budget budget = new Budget();
            budget.setSum(3);
            Assertions.assertEquals(3, budget.getSum());
        }

        @Test
        public void test3() throws IOException {

            Budget budget = new Budget();
            budget.setName("aaa");
            Assertions.assertEquals("aaa", budget.getName());
        }

        @Test
        public void test4() throws IOException {

            Budget budget = new Budget();
            budget.setCategory("bbb");
            Assertions.assertEquals("bbb", budget.getCategory());
        }
}
