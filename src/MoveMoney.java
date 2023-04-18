import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;

public class MoveMoney {

  private LocalDate date;

  private String name;

  private String category;

  private int sum;

  public Money(LocalDate date, String name, String category, int sum){
    this.date = date;
    this.name = name;
    this.category = category;
    this.sum = sum;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public int getSum() {
    return sum;
  }

  public void setSum(int sum) {
    this.sum = sum;
  }

  public static Money addMoneyMoving() throws IOException {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    LocalDate date = LocalDate.now();

    System.out.print("Введите расход/приход денег: ");
    String name = br.readLine();

    System.out.print("Введите категорию: ");
    String category = br.readLine();

    System.out.print("Введите сумму: ");
    int sum = 0;

    try {
      sum = Integer.parseInt(br.readLine());
    } catch (NumberFormatException e) {
      System.err.println("Неправильный формат числа: " + e.getMessage());
    }

    Money rowAddMoneyMoving = new Money(date, name, category, sum);
    System.out.printf("Добавлена запись: " + rowAddMoneyMoving);
    return new Money(date, name, category, sum);
  }

}
