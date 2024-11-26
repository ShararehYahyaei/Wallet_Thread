package org.WalletHomeWork.repositiry;


import org.WalletHomeWork.config.Database;
import org.WalletHomeWork.entity.Wallet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;


public class WalletRepository {

    Connection connection = Database.getConnection();
    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS WALLET (
            ID SERIAL PRIMARY KEY,
            BALANCE  Double PRECISION DEFAULT 0,
            USER_ID INTEGER REFERENCES NEWUSER(ID) ON DELETE CASCADE
            );
                        """;

    public WalletRepository() {
        initTable();
    }

    public void initTable() {
        try {
            var statement = Database.getConnection().prepareStatement(CREATE_TABLE);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static final String INSERT_SQL = """
                INSERT INTO WALLET (BALANCE, USER_ID) VALUES (?, ?);
                       
            """;


    public Wallet saveWallet(Wallet wallet) {
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL, RETURN_GENERATED_KEYS);
            statement.setDouble(1, wallet.getBalance());
            statement.setLong(2, wallet.getUserId());
            statement.execute();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getLong(1);
                Wallet newWallet = new Wallet();
                newWallet.setBalance(wallet.getBalance());
                newWallet.setId(id);
                newWallet.setUserId(wallet.getUserId());
                statement.close();
                return newWallet;
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String GET_WALLET = """
            SELECT BALANCE,ID FROM WALLET WHERE USER_ID = ?
            """;

    public  Wallet getWallet(long userId) {
        try {
            PreparedStatement statement = connection.prepareStatement(GET_WALLET);
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Wallet wallet = new Wallet();
               wallet.setBalance(resultSet.getDouble(1));
                wallet.setId(resultSet.getLong("ID"));
               System.out.println(wallet.getBalance());
              System.out.println(wallet.getId());
                return wallet;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    private static String UPDATE_AMOUNT = """
            UPDATE WALLET SET balance = ? WHERE ID = ?
            """;

    public void updateWallet( double balance,Long id) {
        try {
            PreparedStatement statement = connection.prepareStatement(UPDATE_AMOUNT);
            statement.setDouble(1, balance);
            statement.setLong(2, id);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private static String GET_BALANCE= """
            SELECT BALANCE FROM WALLET WHERE USER_ID = ?
            """;

    public double getBalance(long userId) {
        try{
            PreparedStatement statement=connection.prepareStatement(GET_BALANCE);
            statement.setLong(1, userId);
            ResultSet resultSet=statement.executeQuery();
            if (resultSet.next()) {
               double res=resultSet.getDouble(1);
                System.out.println(res);
                return res;
            }
        }catch (SQLException e){
            e.printStackTrace();

        }
        return 0;
    }
}
