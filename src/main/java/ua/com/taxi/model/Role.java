package ua.com.taxi.model;

public class Role {

    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    private Integer id;
    private String name;

    public static String getUSER() {
        return USER;
    }

    public static String getADMIN() {
        return ADMIN;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
