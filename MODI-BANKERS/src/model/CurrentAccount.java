package model;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;

public class CurrentAccount extends Account {

private static final long serialVersionUID = 1L;

private long overdraftLimit;

public CurrentAccount(String ownerName, long balance,
                      long overdraftLimit) {
    super(ownerName, balance);
    this.overdraftLimit = overdraftLimit;
}

public long getOverdraftLimit() {
    return overdraftLimit;
}

@Override
public void withdraw(long amount)
        throws InsufficientFundsException, InvalidAmountException {

    if (amount <= 0) {
        throw new InvalidAmountException(
                "Withdrawal amount must be positive.");
    }

    if (balance - amount < -overdraftLimit) {
        throw new InsufficientFundsException(
                "Overdraft limit exceeded.");
    }

    balance -= amount;
}

@Override
public String toString() {
    return "Current " + super.toString()
            + ", Overdraft Limit: ₹" + overdraftLimit;
}

}
