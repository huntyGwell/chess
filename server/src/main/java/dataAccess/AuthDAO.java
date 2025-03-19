package dataAccess;

//I got stumped on phase 3 and am now crazy far behind as a result I am now working on phase 4 too.(also late on that...oops)
//It seems like this should actually be an interface and I will build the use-ability out in classes for phase 3 and also
//for phase 4

import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class AuthDAO {
    private final Map<String, AuthData> authTokens = new HashMap<>();

    public void createAuth(AuthData auth) {
        authTokens.put(auth.authToken(), auth);
    }

    public AuthData getAuth(String authToken) {
        return authTokens.get(authToken);
    }

    public void deleteAuth(String authToken) {
        authTokens.remove(authToken);
    }

    public void clear() {
        authTokens.clear();
    }
}
