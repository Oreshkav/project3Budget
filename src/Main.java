import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static final String ANSI_PURPLE = "\u001B[35m";

  public static final String ANSI_BLUE = "\u001B[34m";

  public static final String ANSI_RESET = "\u001B[0m";

  public static final String ANSI_CYAN = "\u001B[36m";

  public static void main(String[] args) throws IOException, ParseException {

    //Основное меню
//    1. Добавить запись
//    1.1.Внести доход
//    1.2.Внести расход
//    2. Удалить запись
//    3. Редактировать запись
//    4. Вывести список затрат (с суммами и итоговой суммой)
//        по категориям
//    5. Сальдо (остаток денег)
    System.out.println(ANSI_CYAN + "\nПривет, мой уважаемый друг!\nТы сегодня принес " +
        "денег или потратил?\nЗаходи, расскажешь!" + ANSI_RESET);
    menuStart();
  }

  public static void menuStart() throws IOException, ParseException {

    List<String> menuMain = menuList();
    int numMenu = readMenu(menuMain);

    switch (numMenu) {
      case 1 ->                                  //добавление записи в бюджет
          ChangesBudget.addMovingMoneyToFile();
      case 2 ->                                  //редактирование записей
          ChangesBudget.editBudget();
      case 3 ->                                  //вывод всего списка бюджета
          ChangesBudget.printBudget();
      case 4 ->                                  //вывод списка бюджета за период
          ChangesBudget.printBudgetByPeriod();
      case 5 ->                                  //вывод списка бюджета по категории
          ChangesBudget.printBudgetByCategory();
      case 6 ->                                  //вывод списка бюджета по категории за период
          ChangesBudget.printBudgetByCategoryByPeriod();
      case 7 ->                                  //удаление строк из бюджета
          ChangesBudget.delRowFromBudget();
      case 8 ->                                  //выход из программы
          ChangesBudget.saldo();

      default -> System.out.println("Когда определишься, тогда и приходи! :-D");
    }
  }

  public static List<String> menuList() {
    System.out.println(ANSI_PURPLE + "\nМЕНЮ" + ANSI_RESET);

    List<String> menuMain = new ArrayList<>();
    menuMain.add("Добавить запись.");
    menuMain.add("Редактировать запись.");
    menuMain.add("Вывести список всех записей.");
    menuMain.add("Вывести список записей за выбранный период.");
    menuMain.add("Вывести список записей по категории.");
    menuMain.add("Вывести список записей по категории за выбранный период.");
    menuMain.add("Удалить запись.");
    menuMain.add("Закончить работу с Бюджетом.");

    for (int i = 0; i < menuMain.size(); ++i) {
      System.out.println(ANSI_BLUE + ((i + 1) + ". " + menuMain.get(i)) + ANSI_RESET);
    }
    return menuMain;
  }

  public static int readMenu(List<String> menuMain) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int menuNumber = 0;
    while (menuNumber < 1 || menuNumber > menuMain.size()) {
      System.out.printf("Выберите действие и введите номер от 1 до %d:  -  ", menuMain.size());
      try {
        menuNumber = Integer.parseInt(br.readLine());
      } catch (NumberFormatException e) {
        System.err.println("\nНеправильный формат числа: " + e.getMessage());
      }
    }
    return menuNumber;
  }
}
