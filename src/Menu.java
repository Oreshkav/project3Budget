import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


public class Menu {

  //Основное меню
//    1. Добавить запись
//    1.1.Внести доход
//    1.2.Внести расход
//    2. Удалить запись
//    3. Редактировать запись
//    4. Вывести список затрат (с суммами и итоговой суммой)
//        по категориям
//    5. Сальдо (остаток денег)
  enum MenuCommands {
    ADD,
    INCOME,
    EXPENSIVE,
    DELETE,
    EDIT,
    TOTAL,
    BALANCE
    }

  public static void (String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Выберите команду:");
    for (MenuCommands commands : MenuCommands.values()) {
      System.out.print("  " + commands);
    }
  }
}