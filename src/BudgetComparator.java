import java.util.Comparator;

public class BudgetComparator {

  public static class BudgetDateCategoryNameComparator implements Comparator<Budget> {

    @Override
    public int compare(Budget row1, Budget row2) {
      if (!row1.date().equals(row2.date())) {
        return row1.date().compareTo(row2.date());
      }
      if (!row1.category().equals(row2.category())) {
        return row1.category().compareTo(row2.category());
      }
      return row1.name().compareTo(row2.name());
    }
  }
}
