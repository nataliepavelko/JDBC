package hometask_jdbc.entity;



//import java.sql.Date;


public class Project {
    private long id;
    private String name;
    private double cost;
    private String date;

    public Project() {
    }

    public Project(long id, String name, double cost, String date) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", cost = " + cost +
                ", date = " + date +
                '}';
    }
}
