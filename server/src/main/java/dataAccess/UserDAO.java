package dataAccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData user);
    UserData getUser(String userName);
    boolean verifyUser(String userName, String password);
    void clear();
}
