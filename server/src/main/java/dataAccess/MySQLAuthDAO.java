package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.sql.SQLException;

public class MySQLAuthDAO implements AuthDAO {


    @Override
    public void createAuth(AuthData authData) throws DataAccessException, ResponseException, SQLException {
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, authData.authToken(), authData.username());
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var statement = "SELECT authToken, username FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    rs.next();
                    String username = rs.getString("username");
                    return new AuthData(authToken, username);
                }
            }
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        var statement = "SELECT username FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    rs.next();
                    return rs.getString("username");
                }
            }
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException, ResponseException, SQLException {
        var statement = "DELETE FROM auth WHERE authToken = ?";
        executeUpdate(statement, authData.authToken());
    }

    @Override
    public void deleteAllAuths() throws DataAccessException, ResponseException, SQLException {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }

    private void executeUpdate(String statement, Object...params) throws ResponseException, DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                String sql = statement.trim();
                if (sql.toUpperCase().startsWith("INSERT")) {
                    ps.setString(1, params[0].toString());
                    ps.setString(2, params[1].toString());
                }
                else if (sql.toUpperCase().startsWith("DELETE")) {
                    ps.setString(1, params[0].toString());
                }
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }
}
