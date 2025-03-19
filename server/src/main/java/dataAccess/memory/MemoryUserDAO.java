package dataAccess.memory;

import dataAccess.UserDAO;
import model.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {

    private HashSet<UserData> userSet;
    public MemoryUserDAO() {userSet = new HashSet<>(32);}

    @Override
    public UserData getUser(String userName) {}

    @Override
    public void createUser(UserData user) {}

    @Override
    public boolean verifyUser(String userName, String password) {
        //check if user is in the set
    }

    @Override
    public void clear() {userSet.clear();} //or just new up a new hashset

}
