import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static final String PURPLE_BOLD = "\033[1;35m";

  public static final String ANSI_BLUE = "\u001B[34m";

  public static final String ANSI_RESET = "\u001B[0m";

  public static void main(String[] args) throws IOException, ParseException, InterruptedException {

    System.out.println(PURPLE_BOLD + "\nПривет, мой уважаемый друг!\nТы сегодня принес " +
        "денег или потратил?\nЗаходи, расскажешь!" + ANSI_RESET);
    menuStart();
  }

  private static final List<String> menuMain = new ArrayList<>();

  static {
    menuMain.add("Добавить запись.");
    menuMain.add("Редактировать запись.");
    menuMain.add("Вывести список всех записей.");
    menuMain.add("Вывести список записей за выбранный период.");
    menuMain.add("Вывести список записей по категории.");
    menuMain.add("Вывести список записей по категории за выбранный период.");
    menuMain.add("Удалить запись.");
    menuMain.add("Закончить работу с программой.");
  }

  public static void menuList() {
    System.out.println(PURPLE_BOLD + "\nМЕНЮ" + ANSI_RESET);

    for (int i = 0; i < menuMain.size(); ++i) {
      System.out.println(ANSI_BLUE + ((i + 1) + ". " + menuMain.get(i)) + ANSI_RESET);
    }
  }

  public static void menuStart() throws IOException, ParseException, InterruptedException {

    menuList();

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int menuNumber = 0;
    while (menuNumber < 1 || menuNumber > menuMain.size()) {
      System.out.printf("Выберите действие и введите номер от 1 до %d:  -  ", menuMain.size());
      try {
        menuNumber = Integer.parseInt(br.readLine());
      } catch (NumberFormatException e) {
        System.err.println("\nНеправильный формат числа: " + e.getMessage());
        menuList();
      }
    }

    switch (menuNumber) {
      case 1 ->                                  //добавление записи в бюджет
          FileReadWrite.addMovingMoneyToFile();
      case 2 ->                                  //редактирование записей
          FileReadWrite.editBudget();
      case 3 ->                                  //вывод всего списка бюджета
          ChangesBudget.printBudget();
      case 4 ->                                  //вывод списка бюджета за период
          ChangesBudget.printBudgetByPeriod();
      case 5 ->                                  //вывод списка бюджета по категории
          ChangesBudget.printBudgetByCategory();
      case 6 ->                                  //вывод списка бюджета по категории за период
          ChangesBudget.printBudgetByCategoryByPeriod();
      case 7 ->                                  //удаление строк из бюджета
          FileReadWrite.delRowFromBudget();
      case 8 ->                                  //выход из программы
          ChangesBudget.balance();

      default -> System.out.println("Когда определишься, тогда и приходи! :-D");
    }
  }

  //выбор следующего действия или возврат в меню
  public static void nextStep(RunnableStep nameMethod) throws IOException, ParseException, InterruptedException {
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
