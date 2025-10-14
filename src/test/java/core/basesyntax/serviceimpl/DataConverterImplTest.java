package core.basesyntax.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.common.FruitTransaction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DataConverterImplTest {
    private static final String APPLE = "apple";
    private static final String BANANA = "banana";
    private static final String TITLE = "type,fruit,quantity";
    private static DataConverterImpl dataConverterService;

    @BeforeAll
    static void beforeAll() {
        dataConverterService = new DataConverterImpl();
    }

    @Test
    void convertReport_validReport_ok() {
        List<String> REPORT = List.of(TITLE, "b,banana,20,", "b,apple,100",
                "s,banana,100", "p,banana,13", "r,apple,10",
                "p,apple,20", "p,banana,5", "s,banana,50");
        List<FruitTransaction> expected = List.of(
                new FruitTransaction(FruitTransaction.Operation.BALANCE, BANANA, 20),
                new FruitTransaction(FruitTransaction.Operation.BALANCE, APPLE, 100),
                new FruitTransaction(FruitTransaction.Operation.SUPPLY, BANANA, 100),
                new FruitTransaction(FruitTransaction.Operation.PURCHASE, BANANA, 13),
                new FruitTransaction(FruitTransaction.Operation.RETURN, APPLE, 10),
                new FruitTransaction(FruitTransaction.Operation.PURCHASE, APPLE, 20),
                new FruitTransaction(FruitTransaction.Operation.PURCHASE, BANANA, 5),
                new FruitTransaction(FruitTransaction.Operation.SUPPLY, BANANA, 50));

        List<FruitTransaction> actual = dataConverterService.convertToTransaction(REPORT);

        assertIterableEquals(expected, actual);
    }

    @Test
    void convertReport_emptyList_ok() {
        List<String> empty = Collections.emptyList();
        List<FruitTransaction> expected = Collections.emptyList();
        List<FruitTransaction> actual = dataConverterService.convertToTransaction(empty);
        assertIterableEquals(expected, actual);
    }

    @Test
    void convertReport_invalidInput_notOk() {
        List<String> invalidList = Arrays.asList(TITLE, "f,Apple,5", "s,Banana,10");
        assertThrows(IllegalArgumentException.class,
                () -> dataConverterService.convertToTransaction(invalidList));
    }

    @Test
    void convertReport_validInput_ok() {
        List<String> validList = Arrays.asList(TITLE, "b,apple,5", "s,banana,10");
        List<FruitTransaction> expectedTransactions = new ArrayList<>();

        expectedTransactions.add(new FruitTransaction(
                FruitTransaction.Operation.BALANCE, APPLE, 5));
        expectedTransactions.add(new FruitTransaction(
                FruitTransaction.Operation.SUPPLY, BANANA, 10));

        List<FruitTransaction> actualTransactions
                = dataConverterService.convertToTransaction(validList);
        assertEquals(expectedTransactions, actualTransactions);
    }

    @Test
    void convertReport_missingFieldsInRow_notOk() {
        List<String> invalidList = Arrays.asList(TITLE,
                "b,banana,1", "b,apple,100,", "s,banana",
                "p,banana,13,", "r,apple,10,", "p,apple,1",
                "p,banana,155,", "s,banana");
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> dataConverterService.convertToTransaction(invalidList));
    }
}
