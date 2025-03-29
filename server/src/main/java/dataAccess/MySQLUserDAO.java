package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;


public class MySQLUserDAO implements UserDAO {

    public MySQLUserDAO() throws ResponseException, SQLException, DataAccessException {
        configDatabase();
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException, ResponseException, SQLException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }

    @Override
    public UserData getUser(String username) throws DataAccessException, ResponseException, SQLException {
        var statement = "SELECT username, password, email FROM user WHERE username=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    rs.next();
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(username, password, email);
                }
            }
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void deleteAllUsers() throws DataAccessException, ResponseException, SQLException {
        var statement = "TRUNCATE user";
        executeUpdate(statement);
    }

    private void executeUpdate(String statement, Object...params) throws ResponseException, DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                String sql = statement.trim();
            if (sql.toUpperCase().startsWith("INSERT")) {
                ps.setString(1, params[0].toString());
                ps.setString(2, hashPassword(params[1].toString()));
                ps.setString(3, params[2].toString());
            }
            ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyPassword(String username, String password) throws ResponseException, SQLException, DataAccessException {
        UserData user = getUser(username);
        return BCrypt.checkpw(password, user.password());
    }

    private final String[] createStatements = {
            """
            CREATE TABLE if NOT EXISTS user (
             username varchar(255) NOT NULL,
             password varchar(255) NOT NULL,
             email varchar(255),
             PRIMARY KEY (username)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS auth (
             authToken varchar(255) NOT NULL,
             username varchar(255) NOT NULL,
             PRIMARY KEY (authToken)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS game (
             gameID INT NOT NULL,
             whiteUsername varchar(255),
             blackUsername varchar(255),
             gameName varchar(255),
             chessGame TEXT,
             PRIMARY KEY (gameID)
            )
            """
    };

    private void configDatabase() throws ResponseException, DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
