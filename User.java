import java.util.Objects;

public class User {
    private final String id;
    private String name;
    private String login;
    private String password;
    private String group;

    public User(String id, String name, String login, String password, String group) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.group = group;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public boolean checkPassword(String pwd) {
        return Objects.equals(this.password, pwd);
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "\nID: '" + id + "'\nАты-жөні: " + name + "\nЛогин: " + login + "\nОқитын тобы: " + group;
    }
}
