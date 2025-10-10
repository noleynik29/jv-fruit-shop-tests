package core.basesyntax.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.Storage;
import java.util.StringJoiner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportGeneratorImplTest {
    private static final String TITLE = "fruit,quantity";
    private static final String APPLE = "apple";
    private static final String BANANA = "banana";
    private static final String APPLES = "apple,10";
    private static final String BANANAS = "banana,10";
    private static final String BANANA_WITH_ZERO_VALUE = "banana,0";
    private static final int ZERO_QUANTITY = 0;
    private static final int NEGATIVE_VALUE = -1;
    private static ReportGeneratorImpl reportGenerator;

    @BeforeEach
    void setUp() {
        reportGenerator = new ReportGeneratorImpl();
    }

    @AfterEach
    void tearDown() {
        Storage.clear();
    }

    @Test
    void reportInfo_validData_ok() {
        Storage.setQuantity(APPLE, 10);
        Storage.setQuantity(BANANA, 10);
        String actual = reportGenerator.getReport();
        String expected = new StringJoiner(System.lineSeparator(),"",System.lineSeparator())
                .add(TITLE).add(BANANAS).add(APPLES).toString();
        assertEquals(expected, actual);
    }

    @Test
    void reportInfo_bananaWithZeroQuantity_ok() {
        Storage.setQuantity(BANANA, ZERO_QUANTITY);
        String expected = new StringJoiner(System.lineSeparator(),"",System.lineSeparator())
                .add(TITLE).add(BANANA_WITH_ZERO_VALUE).toString();
        String actual = reportGenerator.getReport();
        assertEquals(expected, actual);
    }

    @Test
    void reportInfo_applesWithNegativeQuantity_notOk() {
        assertThrows(RuntimeException.class,
                () -> Storage.setQuantity(APPLE, NEGATIVE_VALUE));
    }

    @Test
    void reportInfo_fromEmptyStorage_ok() {
        String expected = TITLE
                + System.lineSeparator();
        String actual = reportGenerator.getReport();
        assertEquals(expected, actual);
    }
}
