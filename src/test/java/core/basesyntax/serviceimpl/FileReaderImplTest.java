package core.basesyntax.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FileReaderImplTest {
    public static final String EXISTING_REPORT = "src/main/resources/data.csv";
    public static final String EMPTY_REPORT = "src/main/resources/emptyFile.csv";
    public static final String NON_EXISTING_FILE = "src/main/resources/nonExistingFile.csv";
    private static FileReaderImpl fileReader;
    private static List<String> report;

    @BeforeAll
    static void beforeAll() {
        fileReader = new FileReaderImpl();
        report = List.of("type,fruit,quantity", "b,banana,20", "b,apple,100",
                "s,banana,100", "p,banana,13", "r,apple,10",
                "p,apple,20", "p,banana,5", "s,banana,50");
    }

    @Test
    void read_existingReport_ok() {
        List<String> containText = fileReader.read(EXISTING_REPORT);
        Assertions.assertEquals(report, containText);
    }

    @Test
    void read_nonExistingReport_notOk() {
        assertThrows(RuntimeException.class,
                () -> fileReader.read(NON_EXISTING_FILE));
    }

    @Test
    void read_emptyFile_ok() {
        List<String> expected = Collections.emptyList();
        List<String> actual = fileReader.read(EMPTY_REPORT);
        Assertions.assertEquals(expected, actual);
    }
}
