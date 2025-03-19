package dataAccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData user);//throws DataAccessException
    UserData getUser(String userName);//throws DataAccessException
    boolean verifyUser(String userName, String password);//throws DataAccessException
    void clear();
}
// build out exceptions