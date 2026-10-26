package service;

import model.Account;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AccountStore {

private final HashMap<String, Account> accounts;
private final ArrayList<String> transactionHistory;

public AccountStore() {
    accounts = new HashMap<>();
    transactionHistory = new ArrayList<>();
}

public void add(Account account) {
    if (account == null) {
        throw new IllegalArgumentException("Account cannot be null.");
    }

    String accountNumber = account.getAccountNumber();

    if (accounts.containsKey(accountNumber)) {
        throw new IllegalArgumentException(
                "Account already exists: " + accountNumber);
    }

    accounts.put(accountNumber, account);
}

public Account get(String accountNumber) {
    return accounts.get(accountNumber);
}

public ArrayList<Account> getAll() {
    return new ArrayList<>(accounts.values());
}

public void addTransaction(String transaction) {
    if (transaction == null || transaction.isBlank()) {
        throw new IllegalArgumentException(
                "Transaction cannot be empty.");
    }

    transactionHistory.add(transaction);
}

public ArrayList<String> getTransactionHistory() {
    return new ArrayList<>(transactionHistory);
}

public ArrayList<Account> sortedByBalance() {
    ArrayList<Account> sortedAccounts = getAll();

    sortedAccounts.sort(
            Comparator.comparingLong(Account::getBalance)
    );

    return sortedAccounts;
}

public int size() {
    return accounts.size();
}

public boolean contains(String accountNumber) {
    return accounts.containsKey(accountNumber);
}

public void printAll() {
    for (Account account : accounts.values()) {
        System.out.println(account);
    }
}

}
