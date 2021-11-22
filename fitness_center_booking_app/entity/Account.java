package fitness_center_booking_app.entity;

import java.io.Serializable;
import java.util.List;

public class Account implements Serializable {
    private String key;
    private String userName;
    private String password;
    private Role role;

    public Account() {

    }

    public Account(String key, String userName, String password, Role role) {
        this.key = key;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "key='" + key + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    public static boolean contains(List<Account> userAccounts, String userName) {
        for (Account account : userAccounts) {
            if (account.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(List<Account> userAccounts, String userName, String password) {
        for (Account account : userAccounts) {
            if (account.getUserName().equals(userName) && account.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}

