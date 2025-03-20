package dataAccess.memory;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {

    private HashSet<UserData> userSet; //final ?
    public MemoryUserDAO() {userSet = new HashSet<>(32);}

    @Override
    public UserData getUser(String userName) throws DataAccessException {
        for (UserData user : userSet) {
            if (user.username().equals(userName)) {
                return user;
            }
        }
        throw new DataAccessException("User not found");
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        try {
            getUser(user.username());
        }
        catch (DataAccessException e) {
            userSet.add(user);
            return;
        }
        throw new DataAccessException("user already exists");
    }

    @Override
    public boolean verifyUser(String userName, String password) {
        //check if user is in the set
        for (UserData user : userSet) {
            if (user.username().equals(userName) && user.password().equals(password)) {
                return true;
            }
        }
        return false; // potentially need to throw new data access excemption?
    }

    @Override
    public void clear() {userSet.clear();} //or just new up a new hashset

}
