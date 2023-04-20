import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Budget {

  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_GREEN = "\u001B[32m";

  private LocalDate date;

  private String name;

  private String category;
  private static final Set<String> categories = new TreeSet<>();
  private int sum;

  //условия стражники поставить, чтоб не нулл

  //Сделать тесты, если ничего не создавала, то пустое.
  // Если создала 3 траты с разн категориями, то 3 категории
  //если создала 3 траты с одинаковми категорями, то 1 категория.


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

  // создание записи класса Бюджет
  public static Budget addMoneyMoving() throws IOException {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    System.out.print("Введите дату в формате 2023-12-31: ");
    String dateString = br.readLine();
    LocalDate date = LocalDate.parse(dateString);

    System.out.print("Введите описание затраты или поступления денег: ");
    String name = br.readLine();

    System.out.print("Введите категорию, к которй относится денежная операция: ");
    String category = br.readLine();

    System.out.print("Введите сумму: ");
    int sum = 0;

    try {
      sum = Integer.parseInt(br.readLine());
    } catch (NumberFormatException e) {
      System.err.println("Неправильный формат числа: " + e.getMessage());
    }

    System.out.println("Это приход или расход? Введите + или - ");
    String debitCredit = br.readLine();
    if (debitCredit.equals("-")) {
      sum = -sum;
    }

    Budget rowAddMoneyMoving = new Budget(date, name, category, sum);
    System.out.printf("Добавлена запись: " + rowAddMoneyMoving);
    return new Budget(date, name, category, sum);
  }

  @Override
  public String toString() {

    try {
      List<Budget> expenses = ChangesBudget.parser();

      // предполагаю, что здесь нужно сделать как-то иначе,
      // чтобы не считать каждый раз длины
      //задать вопрос

      //расчет максимальной длины имени категории или названия для
      //красивого вывода по столбцам
      int categoryLength = 0;
      int nameLength = 0;

      for (Budget row : expenses) {
        if (row.getCategory().length() > categoryLength) {
          categoryLength = row.getCategory().length();
        }
        if (row.getName().length() > nameLength) {
          nameLength = row.getName().length();
        }
      }
      String categoryFull = String.format("%1$-" + categoryLength + "s", category);
      String nameFull = String.format("%1$-" + nameLength + "s", name);

      if (sum <= 0) {
        return ANSI_RED + date + "  " + categoryFull + "  " + nameFull + " " + sum + ANSI_RESET;
      } else {
        return ANSI_GREEN + date + "  " + categoryFull + "  " + nameFull + " " + sum + ANSI_RESET;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
}