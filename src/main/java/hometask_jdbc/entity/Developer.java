package hometask_jdbc.entity;

public class Developer {
    private long id;
    private String name;
    private String surname;
    private String  sex;


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    private int salary;

    public Developer() {
    }

    public Developer(long id, String name, String surname, String sex, int salary) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.salary = salary;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
    @Override
    public String toString() {
        return "Developer{ " +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", surname = '" + surname + '\'' +
                ", sex = '" + sex + '\'' +
                ", salary= " + salary +
                '}';
    }
}
