import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

public class Budget {

  private LocalDate date;

  private String name;

  private String category;
  private static final Set<String> categories = new TreeSet<>();
  private int sum;

  public Budget(LocalDate date, String name, String category, int sum) {
    this.date = date;
    this.name = name;
    this.category = category;
    this.sum = sum;
    categories.add(category);
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public int getSum() {
    return sum;
  }

  public void setSum(int sum) {
    this.sum = sum;
  }

  public static Set<String> getCategories() {
    return categories;
  }

  public static Budget addMoneyMoving() throws IOException, ParseException {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

//    LocalDate date = LocalDate.now();
    System.out.print("Введите дату в формате 2023-12-31: ");
    String dateString = br.readLine();
    LocalDate date = LocalDate.parse(dateString);

    System.out.print("Введите описание движения денег: ");
    String name = br.readLine();

    System.out.print("Введите категорию: ");
    String category = br.readLine();

    System.out.print("Введите сумму: "); //, Расход - с Минусом, например -77:
    int sum = 0;

    try {
      sum = Integer.parseInt(br.readLine());
    } catch (NumberFormatException e) {
      System.err.println("Неправильный формат числа: " + e.getMessage());
    }
    //Это доход или расход???  тогда + или сум  = -сум
    System.out.println("Это приход или расход? Введите + или - ");
    String debitCredit = br.readLine();
    if (debitCredit.equals("-")) {
      sum = - sum;
    }


    Budget rowAddMoneyMoving = new Budget(date, name, category, sum);
    System.out.printf("Добавлена запись: " + rowAddMoneyMoving);
    return new Budget(date, name, category, sum);
  }

  @Override
  public String toString() {
    return date + " " + category + ", " + name + ", " + sum;
  }
}
