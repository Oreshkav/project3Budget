import java.io.*;

public class WriteToFile {

  final public static String SEP = "; ";

  public static void addMovingMoneyToFile() throws IOException {
    File myBudgetFile = new File("res/budget.txt");

    FileWriter fileWriter = new FileWriter("res/budget.txt", true);
    if (!myBudgetFile.exists()) {
      myBudgetFile.createNewFile();
    }

    // получить из класса Money добавление статьи затрат (addMoving())
    Money movingMoney = Money.addMoneyMoving();
    String line =
        String.format(movingMoney.getDate() + SEP + movingMoney.getName() + SEP
            + movingMoney.getCategory() + SEP + movingMoney.getSum() + "\n");
    fileWriter.write(String.valueOf(line));
    fileWriter.close();

    System.out.println("Нажмите 1 для добавление ещё одной записи и 2 для выхода в меню");
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int vybor = Integer.parseInt(br.readLine());
    if (vybor == 1) {
      addMovingMoneyToFile();
    }
    System.out.println("Здесь будет переход в главное меню.");
//    Menu.menuStart();
  }


}
