import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ChangesBudget {

  final public static String SEP = "; ";

  //Создание и добавление записей в бюджет
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

//    System.out.println("\nНажмите 1 для добавление ещё одной записи и 2 для выхода в меню");
//    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//    int vybor = Integer.parseInt(br.readLine());
//    if (vybor == 1) {
//      addMovingMoneyToFile();
//    }
//    Main.menuStart();
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
    System.out.println("Перечень категорий бюджета:");

    List<Budget> expenses = parser();
    expenses.sort(new BudgetComparator.BudgetDateCategoryNameComparator());

    for (String category : Budget.getCategories()) {
      System.out.println(category);
    }

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Выберите категорию для вывода записей бюджета");
    String categoryChoice = br.readLine();
    int totalPlus = 0;
    int totalMinus = 0;

    System.out.println("Выбранной категории " + categoryChoice.toUpperCase() + " соответствуют " +
        "записи бюджета: ");
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
    System.out.printf("\nИтого по категории %s расход = %s, доход = %s", categoryChoice,
        totalMinus, totalPlus);

    nextStep(ChangesBudget::printBudgetByCategory);
//    Main.menuStart();
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
      } else {
        totalMinus = totalMinus + row.getSum();
      }
      System.out.println(row);
    }
    System.out.printf("\nИтого расход = %s, доход = %s", totalMinus, totalPlus);

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

    System.out.println("Списолк после удаления строки бюджета: ");
    for (int i = 0; i < budget.size(); ++i) {
      System.out.println(((i + 1) + ". " + budget.get(i)));
    }

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

////выбор следующего действия или возврат в меню
//    System.out.println("\nНажмите 1 для продолжения удаления и 2 для выхода в меню");
//
//    int vybor = Integer.parseInt(br.readLine());
//    if (vybor == 1) {
//      delRowFromBudget();
//    }
//    Main.menuStart();
  }

  //****   printBudgetByPeriod
  // печать всех строк бюджета За период
  public static void printBudgetByPeriod() throws IOException, ParseException {
//    System.out.println("Перечень категорий бюджета:");
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Введите дату начала периода: ");
    String dateFirstString = br.readLine();
    LocalDate dateFirst = LocalDate.parse(dateFirstString);

    System.out.println("Введите дату окончания периода: ");
    String dateLastString = br.readLine();
    LocalDate dateLast = LocalDate.parse(dateLastString);

    List<Budget> expenses = parser();
    expenses.sort(new BudgetComparator.BudgetDateCategoryNameComparator());

//    for (String category : Budget.getCategories()) {
//      System.out.println(category);
//    }


//    System.out.println("Выберите категорию для вывода записей бюджета");
//    String categoryChoice = br.readLine();
    int totalPlus = 0;
    int totalMinus = 0;

//    System.out.println("Выбранной категории " + categoryChoice.toUpperCase() + " соответствуют " +
//        "записи бюджета: ");
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
    System.out.printf("\nИтого расход = %s, доход = %s", totalMinus, totalPlus);

    nextStep(ChangesBudget::printBudgetByPeriod);
  }

  //**** printBudgetByPeriod

  //******    посчитать дебет кредит
  public static void saldo() throws IOException, ParseException {
    List<Budget> expenses = parser();
    expenses.sort(new BudgetComparator.BudgetDateCategoryNameComparator());
    int total = 0;
    for (Budget row : expenses) {
      total = total + row.getSum();
//      System.out.println(row);
    }
    System.out.printf("\nСальдо = %d", total);
    System.out.println("\nПриходи еще, приноси денежек!");
  }
  //*********


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
}