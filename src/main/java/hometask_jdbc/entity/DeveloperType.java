package hometask_jdbc.entity;

public enum DeveloperType {
    Male ("Male"),
    Female("Female");

    private String str;

    DeveloperType(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "DeveloperType{" +
                "str='" + str + '\'' +
                '}';
    }
}

