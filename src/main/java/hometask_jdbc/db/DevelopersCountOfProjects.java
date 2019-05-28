package hometask_jdbc.db;

public class DevelopersCountOfProjects {
    private String name;
    private String date;
    private int amount;

    public DevelopersCountOfProjects(String name, String date, int amount) {
        this.name = name;
        this.date = date;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return amount;
    }

    public void setCount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Project" +
                " name = '" + name + '\'' +
                ", date = '" + date + '\'' +
                ", amount developers = " + amount;
    }
}
