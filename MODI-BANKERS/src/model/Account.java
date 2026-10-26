package model;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import java.io.Serializable;

public abstract class Account implements Serializable, Comparable<Account> {

private static final long serialVersionUID = 1L;

private static long accountCounter = 1000;

private final String accountNumber;
private String ownerName;
protected long balance;

public Account(String ownerName, long balance) {
    this.accountNumber = "AC" + (++accountCounter);
    this.ownerName = ownerName;
    this.balance = balance;
}

public String getAccountNumber() {
    return accountNumber;
}

public String getOwnerName() {
    return ownerName;
}

public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
}

public long getBalance() {
    return balance;
}

public void deposit(long amount) throws InvalidAmountException {
    if (amount <= 0) {
        throw new InvalidAmountException("Deposit amount must be positive.");
    }

    balance += amount;
}

public abstract void withdraw(long amount)
        throws InsufficientFundsException, InvalidAmountException;

@Override
public int compareTo(Account other) {
    return this.accountNumber.compareTo(other.accountNumber);
}

@Override
public String toString() {
    return "Account Number: " + accountNumber
            + ", Owner: " + ownerName
            + ", Balance: ₹" + balance;
}

}
