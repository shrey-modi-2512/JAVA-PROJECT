package model;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;

public class SavingsAccount extends Account {

private static final long serialVersionUID = 1L;

private long minimumBalance;

public SavingsAccount(String ownerName, long balance,
                      long minimumBalance) {
    super(ownerName, balance);
    this.minimumBalance = minimumBalance;
}

public long getMinimumBalance() {
    return minimumBalance;
}

@Override
public void withdraw(long amount)
        throws InsufficientFundsException, InvalidAmountException {

    if (amount <= 0) {
        throw new InvalidAmountException(
                "Withdrawal amount must be positive.");
    }

    if (balance - amount < minimumBalance) {
        throw new InsufficientFundsException(
                "Minimum balance requirement would be violated.");
    }

    balance -= amount;
}

@Override
public String toString() {
    return "Savings " + super.toString()
            + ", Minimum Balance: ₹" + minimumBalance;
}

}
