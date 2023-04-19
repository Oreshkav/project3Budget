import java.util.Date;
import java.util.HashMap;
public class BudgetPeriod {
        private HashMap<Date, Double> budget;

        public BudgetPeriod() {
            budget = new HashMap<>();
        }

        public void addTask(Date date, double amount) {
            budget.put(date, amount);
        }

        public double getTotal() {
            double total = 0;
            for (Date date : budget.keySet()) {
                total += budget.get(date);
            }
            return total;
        }
    }
