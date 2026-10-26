package model;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;

public class FixedDepositAccount extends Account {

private static final long serialVersionUID = 1L;

public FixedDepositAccount(String ownerName, long balance) {
    super(ownerName, balance);
}

@Override
public void withdraw(long amount)
        throws InsufficientFundsException, InvalidAmountException {

    if (amount <= 0) {
        throw new InvalidAmountException(
                "Withdrawal amount must be positive.");
    }

    throw new InsufficientFundsException(
            "Withdrawal is not allowed from this fixed deposit.");
}

@Override
public String toString() {
    return "Fixed Deposit " + super.toString();
}

}
