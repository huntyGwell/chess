package dataAccess;

//I got stumped on phase 3 and am now crazy far behind as a result I am now working on phase 4 too.(also late on that...oops)
//It seems like this should actually be an interface and I will build the use-ability out in classes for phase 3 and also
//for phase 4

import model.AuthData;

public interface AuthDAO {
    void addAuthData(AuthData authData);
    void removeAuthData(AuthData authData);
    AuthData getAuthData(String authToken);// throws DataAccessException;
    void clearAuthData();
}
//need to build out exceptions