import java.util.Comparator;

public class BudgetComparator {

  public static class BudgetDateCategoryNameComparator implements Comparator<Budget> {

    @Override
    public int compare(Budget row1, Budget row2) {
      if (!row1.getDate().equals(row2.getDate())) {
        return row1.getDate().compareTo(row2.getDate());
      }
      if (!row1.getCategory().equals(row2.getCategory())) {
        return row1.getCategory().compareTo(row2.getCategory());
      }
      return row1.getName().compareTo(row2.getName());
    }
  }
}
