package org.WalletHomeWork;

import org.WalletHomeWork.entity.Transaction;
import org.WalletHomeWork.entity.TransactionType;
import org.WalletHomeWork.entity.User;
import org.WalletHomeWork.entity.Wallet;
import org.WalletHomeWork.service.TransactionService;
import org.WalletHomeWork.service.UserService;
import org.WalletHomeWork.service.WalletService;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static UserService service = new UserService();
    static WalletService walletService = new WalletService();
    static TransactionService transactionService = new TransactionService();
    static User currentUser = null;

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName());
        User user = new User("atefeh", "5");
        service.insertNewUser(user);
        currentUser = service.login(user.getUserName(), "5");
        Wallet wallet = new Wallet();
        wallet.setBalance(5000);
        wallet.setUserId(currentUser.getId());
        Wallet wallet1 = walletService.add(wallet);
        service.updateWalletId(wallet1.getId(), currentUser.getId());
        transactionSave(currentUser.getId(), wallet1.getId(), wallet1.getBalance());
        Thread thread = new Thread(new Runnable() {
            public void run() {
                walletService.Withdraw(currentUser.getId(), 500);
                System.out.println("Withdraw completed");
                System.out.println("b"+Thread.currentThread().getName());

            }
        });
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                walletService.deposit(currentUser.getId(), 300);
                System.out.println("Deposit completed");
                System.out.println("a"+Thread.currentThread().getName());
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                walletService.deposit(currentUser.getId(), 1000);
                System.out.println("Deposit completed");
                System.out.println("a"+Thread.currentThread().getName());
            }
        });


        thread.start();
        thread1.start();
        thread2.start();
        getBalanceByUserId(currentUser.getId());
//        getBalanceByUserId(currentUser.getId());
//        getBalanceByUserId(currentUser.getId());


//
//        while (true) {
//            String result = Menu();
//            switch (result) {
//                case "1":
//                    signUp();
//                    break;
//                case "2":
//                    login();
//                case "3":
//                    System.exit(0);
//                default:
//                    System.out.println("Invalid input");
//            }
//
//        }
    }


    private static void transactionSave(Long userId, Long walletID, double amount) {
        Transaction transaction = new Transaction(userId, walletID, TransactionType.Deposit, amount);
        transactionService.saveTransaction(transaction);
        System.out.println("Transaction created successfully.");
    }

    public static void signUp() {
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
        User user = new User(username, password);
        service.insertNewUser(user);

    }

    public static void addWallet(User user) {
        System.out.println("Please enter your amount: ");
        double amount = scanner.nextDouble();
        Wallet wallet = new Wallet();
        wallet.setBalance(amount);
        wallet.setUserId(user.getId());
        Wallet wallet1 = walletService.add(wallet);
        service.updateWalletId(wallet1.getId(), user.getId());
        transactionSave(user.getId(), wallet1.getId(), wallet1.getBalance());
    }

    public static String Menu() {
        System.out.println("Welcome to Wallet Home");
        System.out.println("1 - Sign up ");
        System.out.println("2 - Login ");
        System.out.println("3 - Exit");
        System.out.println("Please enter your request : ");
        return new Scanner(System.in).nextLine();
    }

    private static void login() {
        System.out.println("Please enter Your UserName :");
        String userName = new Scanner(System.in).nextLine();
        System.out.println("Please enter Your Password :");
        String password = new Scanner(System.in).nextLine();
        currentUser = service.login(userName, password);
        userMenu();
    }

    public static void userMenu() {
        while (true) {
            System.out.println(" 1- Create a new wallet");
            System.out.println(" 2- Withdraw");
            System.out.println(" 3- Deposit");
            System.out.println(" 4- current balance");
            System.out.println(" 5- Exit");
            String res = new Scanner(System.in).nextLine();

            switch (res) {
                case "1":
                    addWallet(currentUser);
                    break;
                case "2":
                    withdraw();
                    break;
                case "3":
                    deposit();
                    break;
                case "4":
                    getBalanceByUserId(currentUser.getId());
                    break;
                case "5":
                    System.exit(0);
                default:
                    System.out.println("Invalid input");
            }
        }
    }


    private static void withdraw() {
        System.out.println("Please enter your amount: ");
        double amount = scanner.nextDouble();
        walletService.Withdraw(currentUser.getId(), amount);
        System.out.println("Withdraw successfully.");
    }

    public static void deposit() {
        System.out.println("Please enter your amount: ");
        double amount = scanner.nextDouble();
        walletService.deposit(currentUser.getId(), amount);
        System.out.println("Deposit successfully.");
    }

    public static  void getBalanceByUserId(Long id) {
        double amount = walletService.getBalanceByUsrId(id);
        System.out.println("Your balance is " + amount);
    }

}

