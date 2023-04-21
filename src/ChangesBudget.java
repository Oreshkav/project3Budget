import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ChangesBudget {

  public static final String BLUE_BOLD = "\033[1;34m";

  public static final String ANSI_RESET = "\u001B[0m";

  public static final String ANSI_CYAN = "\u001B[36m";

  public static final String ANSI_PURPLE = "\u001B[35m";

  // печать всех строк бюджета с сортировкой 1-9 по дате, категории, названию
  public static void printBudget() throws IOException, ParseException, InterruptedException {

    List<Budget> expenses = FileReadWrite.parser();
    expenses.sort(new BudgetComparator.BudgetDateCategoryNameComparator());
    int totalPlus = 0;
    int totalMinus = 0;

    for (Budget row : expenses) {
      if (row.sum() > 0) {
        totalPlus = totalPlus + row.sum();
        System.out.println(row);
      } else {
        totalMinus = totalMinus + row.sum();
        System.out.println(row);
      }
    }
    printTotal(totalMinus, totalPlus);

    Main.nextStep(ChangesBudget::printBudget);
  }

  // печать всех строк бюджета по выбранной категории
  public static void printBudgetByCategory() throws IOException, ParseException, InterruptedException {
    System.out.println(ANSI_PURPLE + "Перечень категорий бюджета:" + ANSI_RESET);

    List<Budget> expenses = FileReadWrite.parser();
    expenses.sort(new BudgetComparator.BudgetDateCategoryNameComparator());

    for (String category : Budget.getCategories()) {
      System.out.println(ANSI_CYAN + category + ANSI_RESET);
    }

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Выберите категорию для вывода записей бюджета");
    String categoryChoice = br.readLine();
    int totalPlus = 0;
    int totalMinus = 0;

    System.out.println("\nВыбранной категории " + ANSI_PURPLE + categoryChoice.toUpperCase() +
        ANSI_RESET + " соответствуют записи бюджета: ");
    for (Budget row : expenses) {
      if (row.category().equalsIgnoreCase(categoryChoice)) {
        if (row.sum() > 0) {
          totalPlus = totalPlus + row.sum();
        } else {
          totalMinus = totalMinus + row.sum();
        }
        System.out.println(row);
      }
    }
    printTotal(totalMinus, totalPlus);

    Main.nextStep(ChangesBudget::printBudgetByCategory);
  }

  // печать всех строк бюджета За период
  public static void printBudgetByPeriod() throws IOException, ParseException, InterruptedException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Введите дату начала периода в формате 2023-12-31: ");
    String dateFirstString = br.readLine();
    LocalDate dateFirst = null;
    while (dateFirst == null) {
      try {
        dateFirst = LocalDate.parse(dateFirstString);
      } catch (DateTimeParseException e) {
        System.out.println("Неправильный формат даты: " + e.getMessage());
        System.out.print(ANSI_PURPLE + "Введите дату в формате 2023-12-31: " + ANSI_RESET);
        dateFirstString = br.readLine();
      }
    }

    System.out.println("Введите дату окончания периода в формате 2023-12-31: ");
    String dateLastString = br.readLine();
    LocalDate dateLast = null;
    while (dateLast == null) {
      try {
        dateLast = LocalDate.parse(dateLastString);
      } catch (DateTimeParseException e) {
        System.out.println("Неправильный формат даты: " + e.getMessage());
        System.out.print(ANSI_PURPLE + "Введите дату в формате 2023-12-31: " + ANSI_RESET);
        dateLastString = br.readLine();
      }
    }

    List<Budget> expenses = FileReadWrite.parser();
    expenses.sort(new BudgetComparator.BudgetDateCategoryNameComparator());

    int totalPlus = 0;
    int totalMinus = 0;

    for (Budget row : expenses) {
      if (row.date().isAfter(dateFirst) && row.date().isBefore(dateLast)) {
        if (row.sum() > 0) {
          totalPlus = totalPlus + row.sum();
        } else {
          totalMinus = totalMinus + row.sum();
        }
        System.out.println(row);
      }
    }
    printTotal(totalMinus, totalPlus);

    Main.nextStep(ChangesBudget::printBudgetByPeriod);
  }

  // печать всех строк бюджета по категории  за период
  public static void printBudgetByCategoryByPeriod() throws IOException, ParseException, InterruptedException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    List<Budget> expenses = FileReadWrite.parser();
    expenses.sort(new BudgetComparator.BudgetDateCategoryNameComparator());

    System.out.println(ANSI_PURPLE + "Перечень категорий бюджета:" + ANSI_RESET);
    for (String category : Budget.getCategories()) {
      System.out.println(ANSI_CYAN + category + ANSI_RESET);
    }

    System.out.println("Выберите категорию для вывода записей бюджета");
    String categoryChoice = br.readLine();

    System.out.println("Введите дату начала периода в формате 2023-12-31: ");
    String dateFirstString = br.readLine();
    LocalDate dateFirst = null;
    while (dateFirst == null) {
      try {
        dateFirst = LocalDate.parse(dateFirstString);
      } catch (DateTimeParseException e) {
        System.out.println("Неправильный формат даты: " + e.getMessage());
        System.out.print(ANSI_PURPLE + "Введите дату в формате 2023-12-31: " + ANSI_RESET);
        dateFirstString = br.readLine();
      }
    }

    System.out.println("Введите дату окончания периода в формате 2023-12-31: ");
    String dateLastString = br.readLine();
    LocalDate dateLast = null;
    while (dateLast == null) {
      try {
        dateLast = LocalDate.parse(dateLastString);
      } catch (DateTimeParseException e) {
        System.out.println("Неправильный формат даты: " + e.getMessage());
        System.out.print(ANSI_PURPLE + "Введите дату в формате 2023-12-31: " + ANSI_RESET);
        dateLastString = br.readLine();
      }
    }

    int totalPlus = 0;
    int totalMinus = 0;

    System.out.println("\nЗаписи из категории " + ANSI_PURPLE + categoryChoice.toUpperCase()
        + ANSI_RESET + "за период с " + dateFirstString + " по " + dateLastString + ": ");
    for (Budget row : expenses) {
      if (row.date().isAfter(dateFirst) && row.date().isBefore(dateLast) && row.category().equalsIgnoreCase(categoryChoice)) {
        if (row.sum() > 0) {
          totalPlus = totalPlus + row.sum();
        } else {
          totalMinus = totalMinus + row.sum();
        }
        System.out.println(row);
      }
    }
    printTotal(totalMinus, totalPlus);

    Main.nextStep(ChangesBudget::printBudgetByCategoryByPeriod);
  }

  //посчитать дебет кредит перед выходом
  public static void balance() throws IOException, ParseException, InterruptedException {
    List<Budget> expenses = FileReadWrite.parser();
    expenses.sort(new BudgetComparator.BudgetDateCategoryNameComparator());
    int total = 0;
    for (Budget row : expenses) {
      total = total + row.sum();
    }
    System.out.printf(BLUE_BOLD + "\nСальдо = %d", total);
    System.out.println(ANSI_PURPLE + "\nДо свидания, друг мой! Приходи еще, приноси денежек!" + ANSI_RESET);
  }

  public static void printTotal(int totalMinus, int totalPlus) {
    System.out.printf(BLUE_BOLD + "Итого расход = %s, доход = %s\nСальдо: %s\n", totalMinus,
        totalPlus, totalMinus + totalPlus + ANSI_RESET);
  }

}