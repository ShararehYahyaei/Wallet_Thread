package org.WalletHomeWork.service;


import org.WalletHomeWork.Exception.BusinessException;
import org.WalletHomeWork.entity.Transaction;
import org.WalletHomeWork.entity.TransactionType;
import org.WalletHomeWork.entity.Wallet;
import org.WalletHomeWork.repositiry.WalletRepository;

public class WalletService {
    WalletRepository walletRepository;
    TransactionService transactionService = new TransactionService();

    public WalletService() {
        this.walletRepository = new WalletRepository();
    }

    public Wallet add(Wallet wallet) {
        try {
            Wallet existWallet = walletRepository.getWallet(wallet.getUserId());
            if (existWallet != null) {
                return existWallet;
            }
            Wallet newWAllet = walletRepository.saveWallet(wallet);
            System.out.println("Wallet added successfully");
            return newWAllet;
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException("wallet already exists");
        }
    }

    public  synchronized     void Withdraw(Long userId, double amount) {

        Wallet wallet = walletRepository.getWallet(userId);
        if (wallet == null) {
            throw new BusinessException("wallet not found");
        }
        if (wallet.getBalance() < amount) {
            throw new BusinessException("insufficient balance");
        }
        if (wallet.getBalance() > amount) {
            double newBalance = wallet.getBalance() - amount;
            System.out.println("t**********" + newBalance);
          System.out.println("t**********"+Thread.currentThread().getName());
            wallet.setBalance(newBalance);
            Transaction transaction = new Transaction(userId, wallet.getId(), TransactionType.Withdraw, amount);
            transactionService.saveTransaction(transaction);
            updateWallet(newBalance, wallet.getId());
        }

    }

    public void updateWallet(double balance, Long id) {
        walletRepository.updateWallet(balance, id);
    }

    public  synchronized  void deposit(Long userId, double amount) {
        Wallet wallet = walletRepository.getWallet(userId);
        if (wallet == null) {
            throw new BusinessException("wallet not found");
        }

       System.out.println(Thread.currentThread().getName());
      System.out.println("&&&&&&&"+wallet.getBalance());

        wallet.setBalance(wallet.getBalance() + amount);
        System.out.println(Thread.currentThread().getName());
       System.out.println("########" + wallet.getBalance());
        Transaction transaction = new Transaction(userId, wallet.getId(), TransactionType.Deposit, amount);
        transactionService.saveTransaction(transaction);
        updateWallet(wallet.getBalance(), wallet.getId());

    }

    public   double getBalanceByUsrId(Long userId) {
        return walletRepository.getBalance(userId);
    }
}
