import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ChangesBudget {

  public static final String BLUE_BOLD = "\033[1;34m";

  public static final String ANSI_RESET = "\u001B[0m";

  public static final String ANSI_CYAN = "\u001B[36m";

  public static final String ANSI_PURPLE = "\u001B[35m";

  final public static String SEP = "; ";

  //Создание и добавление записей в файл бюджет
  public static void addMovingMoneyToFile() throws IOException, ParseException {
    File myBudgetFile = new File("res/budget.txt");

    FileWriter fileWriter = new FileWriter("res/budget.txt", true);
    if (!myBudgetFile.exists() && !myBudgetFile.createNewFile()) {
      throw new RuntimeException("Не получилось создать файл: " + myBudgetFile.getName());
    }

    Budget movingMoney = Budget.addMoneyMoving();
    String line = movingMoney.getCsvString(SEP);
    fileWriter.write(line);
    fileWriter.close();

    nextStep(ChangesBudget::addMovingMoneyToFile);
  }

  //чтение записей из файла бюджет
  public static List<Budget> parser() throws IOException, ParseException {

    File budgetFile = new File("res/budget.txt");
    if (!budgetFile.exists()) {
      System.out.println("Файл не найден.");
      Main.menuStart();
    }

    BufferedReader br = new BufferedReader(new FileReader("res/budget.txt"));
    List<Budget> listBudget = new ArrayList<>();

    for (String line = br.readLine(); line != null; line = br.readLine()) {
      int lastSep = line.indexOf(SEP);

      LocalDate date = LocalDate.parse(line.substring(0, lastSep));
      line = line.substring(lastSep + 2);
      lastSep = line.indexOf(SEP);

      String name = line.substring(0, lastSep);
      line = line.substring(lastSep + 2);
      lastSep = line.indexOf(SEP);

      String category = line.substring(0, lastSep);

      int sum = Integer.parseInt(line.substring(lastSep + 2));

      Budget readedMovingLine = new Budget(date, name, category, sum);
      listBudget.add(readedMovingLine);
    }
    br.close();
    return listBudget;
  }

  //редактирование записей в бюджете
  public static void editBudget() throws IOException, ParseException {
    System.out.println(ANSI_PURPLE + "Друг мой, проще удалить старую и создать новую запись.");
    System.out.println("При этом кнопок нажать прийдетмся меньше, чем для замены.");
    System.out.println("Я художник, я так вижу " + ANSI_RESET + "\uD83D\uDE00");

    nextStep(ChangesBudget::editBudget);
  }

  // печать всех строк бюджета по выбранной категории
  public static void printBudgetByCategory() throws IOException, ParseException {
    System.out.println(ANSI_PURPLE + "Перечень категорий бюджета:" + ANSI_RESET);

    List<Budget> expenses = parser();
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
      if (row.getCategory().equals(categoryChoice)) {
        if (row.getSum() > 0) {
          totalPlus = totalPlus + row.getSum();
        } else {
          totalMinus = totalMinus + row.getSum();
        }
        System.out.println(row);
      }
    }
    printTotal(totalMinus, totalPlus);

    nextStep(ChangesBudget::printBudgetByCategory);
  }

  // печать всех строк бюджета с сортировкой 1-9 по дате, категории, названию
  public static void printBudget() throws IOException, ParseException {

    List<Budget> expenses = parser();
    expenses.sort(new BudgetComparator.BudgetDateCategoryNameComparator());
    int totalPlus = 0;
    int totalMinus = 0;

    for (Budget row : expenses) {
      if (row.getSum() > 0) {
        totalPlus = totalPlus + row.getSum();
        System.out.println(row);
      } else {
        totalMinus = totalMinus + row.getSum();
        System.out.println(row);
      }
    }
    printTotal(totalMinus, totalPlus);

    nextStep(ChangesBudget::printBudget);
  }

  //удаление записей из бюджета
  public static void delRowFromBudget() throws IOException, ParseException {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    List<Budget> budget = new ArrayList<>(parser());
    budget.sort(new BudgetComparator.BudgetDateCategoryNameComparator());

    for (int i = 0; i < budget.size(); ++i) {
      System.out.println(((i + 1) + ". " + budget.get(i)));
    }

    System.out.printf("Введите номер удаляемого задания от 1 до %d:  -  ", budget.size());
    int indexDelBudget = Integer.parseInt(br.readLine());
    budget.remove(indexDelBudget - 1);

    System.out.println("\nСписолк после удаления строки бюджета: ");
    int totalPlus = 0;
    int totalMinus = 0;

    for (int i = 0; i < budget.size(); ++i) {

      if (budget.get(i).getSum() > 0) {
        totalPlus = totalPlus + budget.get(i).getSum();
        System.out.println(((i + 1) + ". " + budget.get(i)));
      } else {
        totalMinus = totalMinus + budget.get(i).getSum();
        System.out.println(((i + 1) + ". " + budget.get(i)));
      }
    }
    printTotal(totalMinus, totalPlus);

    //перезаписать в файл
    File myBudgetFile = new File("res/budget.txt");

    FileWriter fileWriter = new FileWriter("res/budget.txt");
    if (!myBudgetFile.exists() && !myBudgetFile.createNewFile()) {
      throw new RuntimeException("Не получилось создать файл: " + myBudgetFile.getName());
    }

    for (Budget row : budget) {
      fileWriter.write(row.getCsvString(SEP));
    }
    fileWriter.close();

    nextStep(ChangesBudget::delRowFromBudget);
  }

  // печать всех строк бюджета За период
  public static void printBudgetByPeriod() throws IOException, ParseException {
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

    List<Budget> expenses = parser();
    expenses.sort(new BudgetComparator.BudgetDateCategoryNameComparator());

    int totalPlus = 0;
    int totalMinus = 0;

    for (Budget row : expenses) {
      if (row.getDate().isAfter(dateFirst) && row.getDate().isBefore(dateLast)) {
        if (row.getSum() > 0) {
          totalPlus = totalPlus + row.getSum();
        } else {
          totalMinus = totalMinus + row.getSum();
        }
        System.out.println(row);
      }
    }
    printTotal(totalMinus, totalPlus);

    nextStep(ChangesBudget::printBudgetByPeriod);
  }

  // печать всех строк бюджета по категории  за период
  public static void printBudgetByCategoryByPeriod() throws IOException, ParseException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    List<Budget> expenses = parser();
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
      if (row.getDate().isAfter(dateFirst) && row.getDate().isBefore(dateLast) && row.getCategory().equals(categoryChoice)) {
        if (row.getSum() > 0) {
          totalPlus = totalPlus + row.getSum();
        } else {
          totalMinus = totalMinus + row.getSum();
        }
        System.out.println(row);
      }
    }
    printTotal(totalMinus, totalPlus);

    nextStep(ChangesBudget::printBudgetByCategoryByPeriod);
  }

  //посчитать дебет кредит перед выходом
  public static void balance() throws IOException, ParseException {
    List<Budget> expenses = parser();
    expenses.sort(new BudgetComparator.BudgetDateCategoryNameComparator());
    int total = 0;
    for (Budget row : expenses) {
      total = total + row.getSum();
    }
    System.out.printf(BLUE_BOLD + "\nСальдо = %d", total);
    System.out.println(ANSI_PURPLE + "\nДо свидания, друг мой! Приходи еще, приноси денежек!" + ANSI_RESET);
  }

  //выбор следующего действия или возврат в меню
  public static void nextStep(RunnableStep nameMethod) throws IOException, ParseException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("\nНажмите 1 для продолжения и 2 для выхода в меню");

    int choice = Integer.parseInt(br.readLine());
    switch (choice) {
      case 1 -> nameMethod.run();
      case 2 -> Main.menuStart();
      default -> nextStep(nameMethod);
    }
  }

  public static void printTotal(int totalMinus, int totalPlus) {
    System.out.printf(BLUE_BOLD + "Итого расход = %s, доход = %s\nСальдо: %s\n", totalMinus,
        totalPlus, totalMinus + totalPlus + ANSI_RESET);
  }
}