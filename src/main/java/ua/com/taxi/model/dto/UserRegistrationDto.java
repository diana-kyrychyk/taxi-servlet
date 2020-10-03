package ua.com.taxi.model.dto;

public class UserRegistrationDto {

    private String name;
    private String phone;
    private String password;
    private String passwordConfirm;

    public UserRegistrationDto(String name, String phone, String password, String passwordConfirm) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    @Override
    public String toString() {
        return "UserRegistrationDto{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
