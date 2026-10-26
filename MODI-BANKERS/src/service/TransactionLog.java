package service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class TransactionLog {

public static void append(Path file, String line)
        throws IOException {

    Path parent = file.toAbsolutePath().getParent();

    if (parent != null) {
        Files.createDirectories(parent);
    }

    Files.write(
            file,
            (line + System.lineSeparator())
                    .getBytes(StandardCharsets.UTF_8),
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
    );
}

}
