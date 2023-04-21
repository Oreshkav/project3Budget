import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public record Budget(LocalDate date, String name, String category, int sum) {

  public static final String ANSI_RED = "\u001B[31m";

  public static final String ANSI_RESET = "\u001B[0m";

  public static final String ANSI_GREEN = "\u001B[32m";

  public static final String ANSI_PURPLE = "\u001B[35m";

  private static final Set<String> categories = new TreeSet<>();

  public Budget(LocalDate date, String name, String category, int sum) {
    this.date = Objects.requireNonNull(date);

    if (name == null) {
      throw new NullPointerException("Описание не может быть null");
    }
    this.name = name;

    if (category == null) {
      throw new NullPointerException("Каиегория не может быть null");
    }
    this.category = category;
    categories.add(category);

    this.sum = sum;
  }

  public static Set<String> getCategories() {
    return categories;
  }

  public String getCsvString(String sep) {
    return date + sep + name + sep + category + sep + sum + "\n";
  }

  // создание записи класса Бюджет
  public static Budget addMoneyMoving() throws IOException {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    System.out.print("Введите дату в формате 2023-12-31: ");
    String dateString = br.readLine();
    LocalDate date = null;
    while (date == null) {
      try {
        date = LocalDate.parse(dateString);
      } catch (DateTimeParseException e) {
        System.out.println("Неправильный формат даты: " + e.getMessage());
        System.out.print(ANSI_PURPLE + "Введите дату в формате 2023-12-31: " + ANSI_RESET);
        dateString = br.readLine();
      }
    }

    System.out.print("Введите описание затраты или поступления денег: ");
    String name = br.readLine();

    System.out.print("Введите категорию, к которй относится денежная операция: ");
    String category = br.readLine();

    System.out.print("Введите сумму: ");
    int sum;

    try {
      sum = Integer.parseInt(br.readLine());
    } catch (NumberFormatException e) {
      System.err.println("Неправильный формат числа: " + e.getMessage());
      System.out.print(ANSI_PURPLE + "Введите сумму: " + ANSI_RESET);
      sum = Integer.parseInt(br.readLine());
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
      List<Budget> expenses = FileReadWrite.parser();

      // предполагаю, что здесь нужно сделать как-то иначе,
      // чтобы не считать каждый раз длины
      //задать вопрос

      //расчет максимальной длины имени категории или названия
      // для красивого вывода по столбцам
      int categoryLength = category.length();
      int nameLength = name.length();

      for (Budget row : expenses) {
        if (row.category().length() > categoryLength) {
          categoryLength = row.category().length();
        }
        if (row.name().length() > nameLength) {
          nameLength = row.name().length();
        }
      }

      String categoryFull = String.format("%1$-" + categoryLength + "s", category);
      String nameFull = String.format("%1$-" + nameLength + "s", name);

      if (sum <= 0) {
        return ANSI_RED + date + "  " + categoryFull + "  " + nameFull + " " + sum + ANSI_RESET;
      } else {
        return ANSI_GREEN + date + "  " + categoryFull + "  " + nameFull + " " + sum + ANSI_RESET;
      }
    } catch (IOException | ParseException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}