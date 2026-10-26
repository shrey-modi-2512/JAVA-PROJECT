package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {

public static void generate(Path logsFolder, Path reportFile)
        throws IOException {

    long totalDeposits = 0;
    long totalWithdrawals = 0;

    List<String> fileDetails = new ArrayList<>();

    if (Files.exists(logsFolder)) {

        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(logsFolder)) {

            for (Path file : stream) {

                if (!Files.isRegularFile(file)) {
                    continue;
                }

                BasicFileAttributes attributes =
                        Files.readAttributes(
                                file,
                                BasicFileAttributes.class
                        );

                fileDetails.add(
                        file.getFileName()
                                + " | Size: " + attributes.size()
                                + " bytes | Modified: "
                                + attributes.lastModifiedTime()
                );

                try (BufferedReader reader =
                             Files.newBufferedReader(file)) {

                    String line;

                    while ((line = reader.readLine()) != null) {

                        String[] parts = line.split(",");

                        if (parts.length != 3) {
                            continue;
                        }

                        try {
                            long amount = Long.parseLong(
                                    parts[2].trim()
                            );

                            if (amount < 0) {
                                continue;
                            }

                            switch (parts[0].trim().toUpperCase()) {

                                case "DEPOSIT":
                                    totalDeposits =
                                            Math.addExact(
                                                    totalDeposits,
                                                    amount
                                            );
                                    break;

                                case "WITHDRAW":
                                    totalWithdrawals =
                                            Math.addExact(
                                                    totalWithdrawals,
                                                    amount
                                            );
                                    break;

                                default:
                                    break;
                            }

                        } catch (NumberFormatException e) {
                            System.out.println(
                                    "Skipping invalid transaction: "
                                            + line
                            );
                        }
                    }
                }
            }
        }
    }

    long netFlow = Math.subtractExact(
            totalDeposits, totalWithdrawals
    );

    StringBuilder report = new StringBuilder();

    report.append("========== MODI BANKERS ==========\n");
    report.append("Transaction Summary Report\n");
    report.append("Generated: ")
            .append(LocalDateTime.now())
            .append("\n\n");

    report.append("Total Deposits: ₹")
            .append(totalDeposits)
            .append("\n");

    report.append("Total Withdrawals: ₹")
            .append(totalWithdrawals)
            .append("\n");

    report.append("Net Transaction Flow: ₹")
            .append(netFlow)
            .append("\n\n");

    report.append("Log File Details:\n");

    if (fileDetails.isEmpty()) {
        report.append("No log files found.\n");
    } else {
        for (String details : fileDetails) {
            report.append(details).append("\n");
        }
    }

    report.append("==================================\n");

    Path parent = reportFile.toAbsolutePath().getParent();

    if (parent != null) {
        Files.createDirectories(parent);
    }

    Files.writeString(reportFile, report.toString());

    System.out.println("Report generated: " + reportFile);
}

public static void printReport(Path reportFile)
        throws IOException {

    if (Files.exists(reportFile)) {
        System.out.println(Files.readString(reportFile));
    } else {
        System.out.println("Report file does not exist.");
    }
}

}
