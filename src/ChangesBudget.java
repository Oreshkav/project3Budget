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

    System.out.println("\nНажмите 1 для добавление ещё одной записи и 2 для выхода в меню");
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int vybor = Integer.parseInt(br.readLine());
    if (vybor == 1) {
      addMovingMoneyToFile();
    }
    Main.menuStart();
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

  // печать всех строк бюджета по выбранной категории
  public static void printBudgetByCategory() throws IOException, ParseException {
    System.out.println("Перечень категорий бюджета:");

    List<Budget> expenses = parser();
    expenses.sort(new BudgetComparator.BudgetDateCategoryNameComparator());

   for (String category : Budget.getCategories()){
     System.out.println(category);
   }

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Выберите категорию для вывода записей бюджета");
    String categoryChoice = br.readLine();

    System.out.println("Выбранной категории " + categoryChoice.toUpperCase() + " соответствуют " +
        "записи бюджета: ");
    for (Budget row : expenses) {
      if (row.getCategory().equals(categoryChoice)) {
        System.out.println(row);
      }
    }
    Main.menuStart();
  }

  // печать всех строк бюджета с сортировкой по дате, по категории
  public static void printBudget() throws IOException, ParseException {

    List<Budget> expenses = parser();
    expenses.sort(new BudgetComparator.BudgetDateCategoryNameComparator());
    for (Budget row : expenses) {
      System.out.println(row);
    }
//   for (String category : Budget.getCategories()){
//     System.out.println(category);
//   }
    Main.menuStart();
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

    //выбор следующего действия или возврат в меню
    System.out.println("\nНажмите 1 для продолжения удаления и 2 для выхода в меню");

    int vybor = Integer.parseInt(br.readLine());
    if (vybor == 1) {
      delRowFromBudget();
    }
    Main.menuStart();
  }
}


