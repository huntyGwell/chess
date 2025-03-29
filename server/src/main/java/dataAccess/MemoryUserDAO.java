package dataaccess;

import model.UserData;
import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {
    final private HashSet<UserData> users = new HashSet<>();

    public void createUser(UserData user) {
        if (getUser(user.username()) == null) { //user doesn't exist
            users.add(user);
        }
    }

    public UserData getUser(String username) {
        for (UserData user : users) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void deleteAllUsers() {
        users.clear();
    }

    public HashSet<UserData> getUsers() {
        return users;
    }

    public boolean verifyPassword(String username, String password) {
        for (UserData user : users) {
            if (user.username().equals(username) && user.password().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
