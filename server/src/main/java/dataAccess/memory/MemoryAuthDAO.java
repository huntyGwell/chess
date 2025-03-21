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
}
