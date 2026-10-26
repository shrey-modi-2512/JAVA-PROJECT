package service;

import model.Account;
import model.SavingsAccount;
import model.CurrentAccount;
import model.FixedDepositAccount;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;

import java.util.ArrayList;
import java.util.Scanner;

public class MiniBank {

private static final Scanner scanner = new Scanner(System.in);
private static final AccountStore store = new AccountStore();

public static void main(String[] args) {

    boolean running = true;

    while (running) {
        System.out.println("\n========== MODI BANKERS ==========");
        System.out.println("1. Create Account");
        System.out.println("2. View All Accounts");
        System.out.println("3. Search Account");
        System.out.println("4. Sort Accounts by Balance");
        System.out.println("5. Deposit Money");
        System.out.println("6. Withdraw Money");
        System.out.println("7. View Transaction History");
        System.out.println("8. Exit");
        System.out.println("==================================");

        int choice = readInt("Enter your choice: ");

        switch (choice) {
            case 1:
                createAccount();
                break;

            case 2:
                viewAllAccounts();
                break;

            case 3:
                searchAccount();
                break;

            case 4:
                sortAccounts();
                break;

            case 5:
                depositMoney();
                break;

            case 6:
                withdrawMoney();
                break;

            case 7:
                viewTransactionHistory();
                break;

            case 8:
                running = false;
                System.out.println("Thank you for using MODI BANKERS!");
                break;

            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    scanner.close();
}

private static void createAccount() {

    System.out.println("\n----- CREATE ACCOUNT -----");

    System.out.println("1. Savings Account");
    System.out.println("2. Current Account");
    System.out.println("3. Fixed Deposit Account");

    int type = readInt("Select account type: ");

    if (type < 1 || type > 3) {
        System.out.println("Invalid account type.");
        return;
    }

    String name;

    while (true) {
        System.out.print("Enter account holder name: ");
        name = scanner.nextLine().trim();

        if (!name.isEmpty()) {
            break;
        }

        System.out.println("Name cannot be empty.");
    }

    long balance = readLong("Enter initial balance in rupees: ");

    if (balance < 0) {
        System.out.println("Initial balance cannot be negative.");
        return;
    }

    Account account;

    switch (type) {
        case 1:
            long minimumBalance =
                    readLong("Enter minimum balance: ");

            if (minimumBalance < 0 || balance < minimumBalance) {
                System.out.println(
                        "Balance must be at least the minimum balance.");
                return;
            }

            account = new SavingsAccount(
                    name, balance, minimumBalance);
            break;

        case 2:
            long overdraftLimit =
                    readLong("Enter overdraft limit: ");

            if (overdraftLimit < 0) {
                System.out.println(
                        "Overdraft limit cannot be negative.");
                return;
            }

            account = new CurrentAccount(
                    name, balance, overdraftLimit);
            break;

        case 3:
            account = new FixedDepositAccount(name, balance);
            break;

        default:
            return;
    }

    store.add(account);

    String transaction = "ACCOUNT_CREATED,"
            + account.getAccountNumber() + "," + balance;

    store.addTransaction(transaction);

    System.out.println("\nAccount created successfully!");
    System.out.println("Account Number: "
            + account.getAccountNumber());
    System.out.println("Account Holder: "
            + account.getOwnerName());
    System.out.println("Initial Balance: ₹"
            + account.getBalance());
}

private static void viewAllAccounts() {

    System.out.println("\n----- ALL ACCOUNTS -----");

    ArrayList<Account> accounts = store.getAll();

    if (accounts.isEmpty()) {
        System.out.println("No accounts have been created yet.");
        return;
    }

    for (Account account : accounts) {
        System.out.println(account);
    }
}

private static void searchAccount() {

    System.out.println("\n----- SEARCH ACCOUNT -----");

    System.out.print("Enter account number: ");
    String accountNumber = scanner.nextLine().trim();

    Account account = store.get(accountNumber);

    if (account == null) {
        System.out.println("Account not found.");
    } else {
        System.out.println("Account found:");
        System.out.println(account);
    }
}

private static void sortAccounts() {

    System.out.println("\n----- ACCOUNTS SORTED BY BALANCE -----");

    ArrayList<Account> accounts = store.sortedByBalance();

    if (accounts.isEmpty()) {
        System.out.println("No accounts available.");
        return;
    }

    for (Account account : accounts) {
        System.out.println(account);
    }
}

private static void depositMoney() {

    System.out.println("\n----- DEPOSIT MONEY -----");

    Account account = findAccountForTransaction();

    if (account == null) {
        return;
    }

    long amount = readLong("Enter deposit amount: ");

    try {
        synchronized (account) {
            account.deposit(amount);
        }

        store.addTransaction(
                "DEPOSIT,"
                        + account.getAccountNumber()
                        + "," + amount);

        System.out.println("Deposit successful!");
        System.out.println("Updated Balance: ₹"
                + account.getBalance());

    } catch (InvalidAmountException e) {
        System.out.println("Deposit failed: " + e.getMessage());
    }
}

private static void withdrawMoney() {

    System.out.println("\n----- WITHDRAW MONEY -----");

    Account account = findAccountForTransaction();

    if (account == null) {
        return;
    }

    long amount = readLong("Enter withdrawal amount: ");

    try {
        synchronized (account) {
            account.withdraw(amount);
        }

        store.addTransaction(
                "WITHDRAW,"
                        + account.getAccountNumber()
                        + "," + amount);

        System.out.println("Withdrawal successful!");
        System.out.println("Updated Balance: ₹"
                + account.getBalance());

    } catch (InsufficientFundsException
             | InvalidAmountException e) {
        System.out.println("Withdrawal failed: " + e.getMessage());
    }
}

private static Account findAccountForTransaction() {

    System.out.print("Enter account number: ");
    String accountNumber = scanner.nextLine().trim();

    Account account = store.get(accountNumber);

    if (account == null) {
        System.out.println("Account not found.");
    }

    return account;
}

private static void viewTransactionHistory() {

    System.out.println("\n----- TRANSACTION HISTORY -----");

    ArrayList<String> transactions =
            store.getTransactionHistory();

    if (transactions.isEmpty()) {
        System.out.println("No transactions available.");
        return;
    }

    for (String transaction : transactions) {
        System.out.println(transaction);
    }
}

private static int readInt(String message) {

    while (true) {
        System.out.print(message);
        String input = scanner.nextLine().trim();

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid whole number.");
        }
    }
}

private static long readLong(String message) {

    while (true) {
        System.out.print(message);
        String input = scanner.nextLine().trim();

        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid amount.");
        }
    }
}

}
