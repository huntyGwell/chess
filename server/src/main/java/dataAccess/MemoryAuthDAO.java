package dataaccess;

import model.AuthData;
import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO {
    final private HashSet<AuthData> auths = new HashSet<>();

    public void createAuth(AuthData authData) {
        auths.add(authData);
    }

    public AuthData getAuth(String authToken) {
        for (AuthData auth : auths) {
            if (auth.authToken().equals(authToken)) {
                return auth;
            }
        }
        return null;
    }

    public String getUsername(String authToken) {
        for (AuthData auth : auths) {
            if (auth.authToken().equals(authToken)) {
                return auth.username();
            }
        }
        return null;
    }

    public void deleteAuth(AuthData authData) {
        for (AuthData auth : auths) {
            if (auth.equals(authData)) {
                auths.remove(auth);
                break;
            }
        }
    }

    public void deleteAllAuths() {
        auths.clear();
    }

    public HashSet<AuthData> getAuths() {
        return auths;
    }
}