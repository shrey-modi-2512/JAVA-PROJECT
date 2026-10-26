package util;

import java.util.List;

public class StatementFormatter {

public static String format(String accountNumber,
                            List<String> transactions) {

    StringBuilder statement = new StringBuilder();

    statement.append("========== MODI BANKERS ==========\n");
    statement.append("Account Number: ")
            .append(accountNumber)
            .append("\n");

    statement.append("----------------------------------\n");
    statement.append("Transaction History:\n");

    if (transactions == null || transactions.isEmpty()) {
        statement.append("No transactions available.\n");
    } else {
        for (String transaction : transactions) {
            statement.append(transaction).append("\n");
        }
    }

    statement.append("==================================\n");

    return statement.toString();
}

}
