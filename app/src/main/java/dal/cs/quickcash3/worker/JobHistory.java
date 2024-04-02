package dal.cs.quickcash3.worker;

public class JobHistory {
    private String name;
    private double income;
    private double rating;

    public JobHistory(String name, double income, double rating) {
        this.name = name;
        this.income = income;
        this.rating = rating;
    }

    // Getter和Setter方法
    public String getName() {
        return name;
    }

    public double getIncome() {
        return income;
    }

    public double getReputation() {
        return rating;
    }
}
