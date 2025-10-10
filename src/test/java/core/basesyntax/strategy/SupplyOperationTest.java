package core.basesyntax.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.common.FruitTransaction;
import core.basesyntax.db.Storage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SupplyOperationTest {
    private static final String BANANA = "banana";
    private static final int DEFAULT_QUANTITY = 10;
    private static final int ZERO_QUANTITY = 0;
    private static final int NEGATIVE_QUANTITY = -1;
    private static OperationHandler operationHandler;

    @BeforeAll
    static void beforeAll() {
        operationHandler = new SupplyOperation();
    }

    @AfterEach
    void afterEach() {
        Storage.clear();
    }

    @Test
    void process_supplyNegativeQuantity_notOk() {
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.SUPPLY, BANANA, NEGATIVE_QUANTITY
        );
        assertThrows(IllegalArgumentException.class,
                () -> operationHandler.apply(fruitTransaction));
    }

    @Test
    void process_supplyZeroQuantity_ok() {
        Storage.setQuantity(BANANA, DEFAULT_QUANTITY);
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.SUPPLY, BANANA, ZERO_QUANTITY
        );
        operationHandler.apply(fruitTransaction);
        int actualQuantity = Storage.getFruits().getOrDefault(BANANA, ZERO_QUANTITY);
        assertEquals(DEFAULT_QUANTITY, actualQuantity);
    }

    @Test
    void process_supplyDefaultQuantityToEmptyStorage_ok() {
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.SUPPLY, BANANA, DEFAULT_QUANTITY
        );
        operationHandler.apply(fruitTransaction);
        int actualQuantity = Storage.getFruits().getOrDefault(BANANA, ZERO_QUANTITY);
        assertEquals(DEFAULT_QUANTITY, actualQuantity);
    }
}
