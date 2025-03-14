package model;

import model.AuthData;

import java.util.HashSet;

public record AuthData(String authToken, String username) {

    HashSet<AuthData> db;

    public AuthData() {
        db = new HashSet<>();
    }

    void addAuthData(AuthData authData) {}

    void removeAuthData(String authToken) {}

    AuthData getAuthData(String authToken) {}

    void clear();
}
///////////////
//////////////
/////////////
//all of this will need to be re written