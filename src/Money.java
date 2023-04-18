import java.util.Scanner;
public class Money {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите свой бюджет: ");
        double budget = sc.nextDouble();
        System.out.print("Введите свои расходы: ");
        double expenses = sc.nextDouble();
        double remainingBudget = budget - expenses;
        System.out.println("Оставшийся бюджет: " + remainingBudget);
    }
}

