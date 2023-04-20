import org.junit.Test;
import java.io.IOException;
import java.text.ParseException;
import org.junit.jupiter.api.Assertions;
import java.time.LocalDate;
public class BudgetTest {

    @Test
    public void test1() throws IOException, ParseException {
        String dateString = "2023-12-31";
        LocalDate date = LocalDate.parse(dateString);
        Budget budget1 = new Budget(date,"aa","1",24);
        Budget budget2 = new Budget(date,"aa","2",24);
        Budget budget4 = new Budget(date,"aa","1",24);


        Assertions.assertEquals(2, Budget.getCategories().size());
    }

    @Test
    public void test3() throws IOException, ParseException {
        String dateString = "2023-12-31";
        LocalDate date = LocalDate.parse(dateString);
        Budget budget1 = new Budget(date,"a","1",24);
        Budget budget2 = new Budget(date,"a","2",24);
        Budget budget4 = new Budget(date,"a","3",24);

        Assertions.assertEquals(3, Budget.getCategories().size());
    }
}
