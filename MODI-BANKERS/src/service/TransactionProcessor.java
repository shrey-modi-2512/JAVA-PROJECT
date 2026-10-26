package service;

import model.Account;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TransactionProcessor {

private final ConcurrentHashMap<String, Account> accounts;
private final ExecutorService executor;

public TransactionProcessor() {
    accounts = new ConcurrentHashMap<>();
    executor = Executors.newFixedThreadPool(4);
}

public void addAccount(Account account) {
    if (account == null) {
        throw new IllegalArgumentException("Account cannot be null.");
    }

    Account previous = accounts.putIfAbsent(
            account.getAccountNumber(), account);

    if (previous != null) {
        throw new IllegalArgumentException(
                "Account already exists: "
                        + account.getAccountNumber());
    }
}

public Account getAccount(String accountNumber) {
    return accounts.get(accountNumber);
}

public ConcurrentHashMap<String, Account> getAccounts() {
    return new ConcurrentHashMap<>(accounts);
}

public void deposit(String accountNumber, long amount) {
    executor.submit(() -> {
        Account account = accounts.get(accountNumber);

        if (account == null) {
            System.out.println(
                    "Account not found: " + accountNumber);
            return;
        }

        try {
            synchronized (account) {
                account.deposit(amount);
            }

            System.out.println(
                    "Deposit successful: " + accountNumber
                            + ", Amount: ₹" + amount);

        } catch (InvalidAmountException e) {
            System.out.println(e.getMessage());
        }
    });
}

public void withdraw(String accountNumber, long amount) {
    executor.submit(() -> {
        Account account = accounts.get(accountNumber);

        if (account == null) {
            System.out.println(
                    "Account not found: " + accountNumber);
            return;
        }

        try {
            synchronized (account) {
                account.withdraw(amount);
            }

            System.out.println(
                    "Withdrawal successful: " + accountNumber
                            + ", Amount: ₹" + amount);

        } catch (InsufficientFundsException
                 | InvalidAmountException e) {
            System.out.println(e.getMessage());
        }
    });
}

public void shutdown() {
    executor.shutdown();

    try {
        if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
            List<Runnable> unfinished = executor.shutdownNow();

            System.out.println(
                    "Stopped with " + unfinished.size()
                            + " tasks not started.");
        }
    } catch (InterruptedException e) {
        executor.shutdownNow();
        Thread.currentThread().interrupt();
    }
}

}
