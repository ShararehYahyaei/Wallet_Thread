package org.WalletHomeWork.repositiry;

import org.WalletHomeWork.config.Database;
import org.WalletHomeWork.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;


public class UserRepository {

    Connection connection = Database.getConnection();
    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS NEWUSER(
             ID        SERIAL PRIMARY KEY,
            USERNAME  VARCHAR(255) NOT NULL UNIQUE,
            PASSWORD  VARCHAR(255) NOT NULL,
            WALLET_ID INTEGER REFERENCES WALLET(ID) ON DELETE CASCADE )""";

    public UserRepository() {
        initTable();
    }

    public void initTable() {

        try {
            var statement = connection.prepareStatement(CREATE_TABLE);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private static final String CHECK_EXISTED_USERNAME = """               
            SELECT Count(ID) AS c1 FROM NEWUSER
            WHERE USERNAME = ?
            """;

    public boolean checkExistedUsername(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(CHECK_EXISTED_USERNAME);
            statement.setString(1, user.getUserName());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int count = rs.getInt("c1");
                if (count > 0) {
                    System.out.println("user : " + user.getUserName() +" "+count);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static final String INSERT_SQL = """
              insert into NEWUSER(username,password)values (?,?)
            """;

    public Long insert(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL, RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.execute();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getLong("ID");
                statement.close();
                return id;
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String LOGIN_WITH_USERNAME = """
            SELECT * FROM NEWUSER
             WHERE USERNAME=?
            """;

    public User login(String username) {

        try {
            PreparedStatement statement = connection.prepareStatement(LOGIN_WITH_USERNAME);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong("ID");
                String name = rs.getString("USERNAME");
                String pass = rs.getString("PASSWORD");
                User user = new User();
                user.setId(id);
                user.setUserName(name);
                user.setPassword(pass);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String UPDATE_WALLET_ID = """
            UPDATE NEWUSER SET WALLET_ID=? WHERE id=?
            """;

    public void updateWalletId(Long walletId, Long userId) {
        try {
            PreparedStatement statement = connection.prepareStatement(UPDATE_WALLET_ID);
            statement.setLong(1, walletId);
            statement.setLong(2, userId);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
