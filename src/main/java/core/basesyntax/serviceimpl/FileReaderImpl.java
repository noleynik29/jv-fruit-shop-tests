package core.basesyntax.serviceimpl;

import static java.util.stream.Collectors.toList;

import core.basesyntax.service.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileReaderImpl implements FileReader {
    @Override
    public List<String> read(String filePath) {
        try {
            return Files.lines(Path.of(filePath))
                    .map(String::strip)
                    .collect(toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }
}
