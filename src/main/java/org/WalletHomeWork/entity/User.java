package org.WalletHomeWork.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String userName;
    private String password;
    private Long  idWallet;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;

    }

}
