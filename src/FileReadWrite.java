import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileReadWrite {

  public static final String ANSI_PURPLE = "\u001B[35m";

  public static final String ANSI_RESET = "\u001B[0m";


  final public static String SEP = "; ";

  //Создание и добавление записей в файл бюджет
  public static void addMovingMoneyToFile() throws IOException, ParseException, InterruptedException {
    File myBudgetFile = new File("res/budget.txt");

    FileWriter fileWriter = new FileWriter("res/budget.txt", true);
    if (!myBudgetFile.exists() && !myBudgetFile.createNewFile()) {
      throw new RuntimeException("Не получилось создать файл: " + myBudgetFile.getName());
    }

    Budget movingMoney = Budget.addMoneyMoving();
    String line = movingMoney.getCsvString(SEP);
    fileWriter.write(line);
    fileWriter.close();

    Main.nextStep(FileReadWrite::addMovingMoneyToFile);
  }

  //чтение записей из файла бюджет
  public static List<Budget> parser() throws IOException, ParseException, InterruptedException {

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

      Budget gettingMovingLine = new Budget(date, name, category, sum);
      listBudget.add(gettingMovingLine);
    }
    br.close();
    return listBudget;
  }

  //удаление записей из бюджета
  public static void delRowFromBudget() throws IOException, ParseException, InterruptedException {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    List<Budget> budget = new ArrayList<>(FileReadWrite.parser());
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
    ChangesBudget.printTotal(totalMinus, totalPlus);

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

    Main.nextStep(FileReadWrite::delRowFromBudget);
  }

  //редактирование записей в бюджете
  public static void editBudget() throws IOException, ParseException, InterruptedException {
    System.out.println(ANSI_PURPLE + "Друг мой, проще удалить старую и создать новую запись.");
    System.out.println("При этом кнопок нажать прийдетмся меньше, чем для замены.");
    System.out.println("Я художник, я так вижу " + ANSI_RESET + "\uD83D\uDE00");

    Main.nextStep(FileReadWrite::editBudget);
  }
}
