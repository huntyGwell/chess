package dataAccess.memory;

import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import model.AuthData;

import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO {
    private HashSet<AuthData> authDataSet;

    public MemoryAuthDAO() {
        authDataSet = new HashSet<>();
    }

    @Override
    public void addAuthData(AuthData authData) {
        authDataSet.add(authData);
    }

    @Override
    public void removeAuthData(AuthData authData) {
        authDataSet.remove(authData); // do I need to check the set like with a for each loop?
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        for (AuthData authData : authDataSet) {
            if (authData.authToken().equals(authToken)) {
                return authData;
            }
        }
        throw new DataAccessException("Auth Token Not Found " + authToken);
    }
    @Override
    public void clearAuthData() {
        authDataSet.clear(); // new up new hash set??
    }
}
