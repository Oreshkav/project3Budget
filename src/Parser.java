import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Parser {

  final public static String SEP = "; ";

  public static List<Money> parser() throws IOException {

    File budgetFile = new File("res/budget.txt");
    if (!budgetFile.exists()) {
      System.out.println("Файл не найден.");
      //здесь будет переход в основное меню
//      Menu.menuStart();
    }

    BufferedReader br = new BufferedReader(new FileReader("res/budget.txt"));
    List<Money> listBudget = new ArrayList<>();

    for (String line = br.readLine(); line != null; line = br.readLine()) {
      int lastSep = line.indexOf(SEP);

      LocalDate date = LocalDate.parse(line.substring(0, lastSep));
      line = line.substring(lastSep + 2);
      lastSep = line.indexOf(SEP);

      String name = line.substring(0, lastSep);
      line = line.substring(lastSep + 2);
      lastSep = line.indexOf(SEP);

      String category = line.substring(0, lastSep);
      line = line.substring(lastSep + 2);
      lastSep = line.indexOf(SEP);

      int sum = Integer.parseInt(line.substring(0, lastSep));

      Money readedMovingLine = new Money(date, name, category, sum);
      listBudget.add(readedMovingLine);
    }

    br.close();
    return listBudget;
  }
}