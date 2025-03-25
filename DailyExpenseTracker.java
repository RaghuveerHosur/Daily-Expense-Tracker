import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class Expense {
    private String date;
    private double amount;
    private String category;
    private String description;

    public Expense(double amount, String category, String description) {
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public String toString() {
        return date + ", " + amount + ", " + category + ", " + description;
    }

    public String getDate() { return date; }
    public double getAmount() { return amount; }
}

class ExpenseManager {
    private static final String FILE_NAME = "expenses.txt";

    public static void addExpense(Expense expense) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(expense.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static List<Expense> getExpenses() {
        List<Expense> expenses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                expenses.add(new Expense(Double.parseDouble(parts[1]), parts[2], parts[3]));
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
        return expenses;
    }

    public static double calculateTotal(String period) {
        List<Expense> expenses = getExpenses();
        double total = 0;
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        String weekStart = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, -23);
        String monthStart = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

        for (Expense e : expenses) {
            if ((period.equals("day") && e.getDate().equals(today)) ||
                    (period.equals("week") && e.getDate().compareTo(weekStart) >= 0) ||
                    (period.equals("month") && e.getDate().compareTo(monthStart) >= 0)) {
                total += e.getAmount();
            }
        }
        return total;
    }
}

public class DailyExpenseTracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nDaily Expense Tracker");
            System.out.println("1. Add Expense");
            System.out.println("2. View Total Expenses");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    Expense expense = new Expense(amount, category, description);
                    ExpenseManager.addExpense(expense);
                    System.out.println("Expense added successfully!");
                    break;
                case 2:
                    System.out.print("View total expenses for (day/week/month): ");
                    String period = scanner.nextLine();
                    double total = ExpenseManager.calculateTotal(period);
                    System.out.println("Total expenses for " + period + ": " + total);
                    break;
                case 3:
                    System.out.println("Exiting... Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }
}
