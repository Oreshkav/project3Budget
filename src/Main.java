import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) throws IOException, ParseException {

    // Класс РасходДоход
    //    Наименование
    //    Категория (Еда, Развлечения, Одежда, Обучение)
    //    Сумма

    //Основное меню
//    1. Добавить запись
//    1.1.Внести доход
//    1.2.Внести расход
//    2. Удалить запись
//    3. Редактировать запись
//    4. Вывести список затрат (с суммами и итоговой суммой)
//        по категориям
//    5. Сальдо (остаток денег)

    menuStart();
  }
  public static void menuStart() throws IOException, ParseException {

    List<String> menuMain = menuList();
    int numMenu = readMenu(menuMain);

    switch (numMenu) {
      case 1 ->                                  //добавление записи в бюджет
          ChangesBudget.addMovingMoneyToFile();
      case 2 ->                                  //редактирование записей
          System.out.println("Редактировать запись в разработке.");
      case 3 ->                                  //вывод списка бюджета
          ChangesBudget.printBudget();
      case 4 ->                                  //вывод списка бюджета по категории
          ChangesBudget.printBudgetByCategory();
      case 5 ->                                  //удаление строк из бюджета
          ChangesBudget.delRowFromBudget();
      case 6 ->                                  //выход из программы
          System.out.println("Приходи еще, приноси денежек!");
    }
  }

  public static List<String> menuList() {
    System.out.println("\nMENU");

    List<String> menuMain = new ArrayList<>();
    menuMain.add("Добавить запись.");
    menuMain.add("Редактировать запись.");
    menuMain.add("Вывести список записей бюджета.");
    menuMain.add("Вывести список записей бюджета по категории.");
    menuMain.add("Удалить запись.");
    menuMain.add("Выйти.");

    for (int i = 0; i < menuMain.size(); ++i) {
      System.out.println(((i + 1) + ". " + menuMain.get(i)));
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
