import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChangesBudget {

  public static final String ANSI_BLUE = "\u001B[34m";

  public static final String ANSI_RESET = "\u001B[0m";

  public static final String ANSI_CYAN = "\u001B[36m";

  public static final String ANSI_PURPLE = "\u001B[35m";

  final public static String SEP = "; ";

  //Создание и добавление записей в файл бюджет
  public static void addMovingMoneyToFile() throws IOException, ParseException {
    File myBudgetFile = new File("res/budget.txt");

    FileWriter fileWriter = new FileWriter("res/budget.txt", true);
    if (!myBudgetFile.exists()) {
      myBudgetFile.createNewFile();
    }

    Budget movingMoney = Budget.addMoneyMoving();
    String line =
        String.format(movingMoney.getDate() + SEP + movingMoney.getName() + SEP
            + movingMoney.getCategory() + SEP + movingMoney.getSum() + "\n");
    fileWriter.write(String.valueOf(line));
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

    System.out.println("Выбранной категории " + ANSI_PURPLE + categoryChoice.toUpperCase() +
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
    System.out.printf(ANSI_BLUE + "Итого по категории %s расход = %s, доход = %s", categoryChoice,
        totalMinus, totalPlus + ANSI_RESET);

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
//    System.out.printf(ANSI_BLUE + "\nИтого расход = %s, доход = %s", totalMinus, totalPlus + ANSI_RESET);
    printTotal(totalMinus,totalPlus);

    nextStep(ChangesBudget::printBudget);

//   for (String category : Budget.getCategories()){
//     System.out.println(category);
//   }
//    Main.menuStart();
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
//    System.out.printf(ANSI_BLUE + "\nИтого расход = %s, доход = %s", totalMinus, totalPlus + ANSI_RESET);
    printTotal(totalMinus,totalPlus);

    //перезаписать в файл
    File myBudgetFile = new File("res/budget.txt");

    FileWriter fileWriter = new FileWriter("res/budget.txt");
    if (!myBudgetFile.exists()) {
      myBudgetFile.createNewFile();
    }

    for (int i = 0; i < budget.size(); ++i) {
      Budget movingMoney = budget.get(i);
      String line =
          String.format(movingMoney.getDate() + SEP + movingMoney.getName() + SEP
              + movingMoney.getCategory() + SEP + movingMoney.getSum() + "\n");
      fileWriter.write(String.valueOf(line));
    }
    fileWriter.close();

    nextStep(ChangesBudget::delRowFromBudget);
  }

  // печать всех строк бюджета За период
  public static void printBudgetByPeriod() throws IOException, ParseException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Введите дату начала периода: ");
    String dateFirstString = br.readLine();
    LocalDate dateFirst = LocalDate.parse(dateFirstString);

    System.out.println("Введите дату окончания периода: ");
    String dateLastString = br.readLine();
    LocalDate dateLast = LocalDate.parse(dateLastString);

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
    //    System.out.printf(ANSI_BLUE + "\nИтого расход = %s, доход = %s", totalMinus, totalPlus + ANSI_RESET);
    printTotal(totalMinus,totalPlus);

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

    System.out.println("Введите дату начала периода: ");
    String dateFirstString = br.readLine();
    // поймать DateTimeParseException
    LocalDate dateFirst = LocalDate.parse(dateFirstString);

    System.out.println("Введите дату окончания периода: ");
    String dateLastString = br.readLine();
    LocalDate dateLast = LocalDate.parse(dateLastString);

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
    printTotal(totalMinus,totalPlus);

    nextStep(ChangesBudget::printBudgetByCategoryByPeriod);
  }

  //посчитать дебет кредит перед выходом
  public static void saldo() throws IOException, ParseException {
    List<Budget> expenses = parser();
    expenses.sort(new BudgetComparator.BudgetDateCategoryNameComparator());
    int total = 0;
    for (Budget row : expenses) {
      total = total + row.getSum();
    }
    System.out.printf("\nСальдо = %d", total);
    System.out.println("\nПриходи еще, приноси денежек!");
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
    System.out.printf(ANSI_BLUE + "Итого расход = %s, доход = %s\nСальдо: %s", totalMinus,
        totalPlus, totalMinus + totalPlus + ANSI_RESET);
  }
}