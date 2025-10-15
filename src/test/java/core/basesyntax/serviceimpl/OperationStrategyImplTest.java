package core.basesyntax.serviceimpl;

import core.basesyntax.common.FruitTransaction;
import core.basesyntax.strategy.OperationHandler;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperationStrategyImplTest {

    private final OperationHandler simpleHandler = t -> {};

    @Test
    void constructor_nullMap_notOk() {
        assertThrows(RuntimeException.class, () -> new OperationStrategyImpl(null));
    }

    @Test
    void constructor_emptyMap_notOk() {
        assertThrows(RuntimeException.class, () -> new OperationStrategyImpl(new HashMap<>()));
    }

    @Test
    void constructor_validMap_ok() {
        Map<FruitTransaction.Operation, OperationHandler> map = new HashMap<>();
        map.put(FruitTransaction.Operation.BALANCE, simpleHandler);
        OperationStrategyImpl strategy = new OperationStrategyImpl(map);
        assertNotNull(strategy);
    }

    @Test
    void getHandler_nullOperation_notOk() {
        Map<FruitTransaction.Operation, OperationHandler> map = new HashMap<>();
        map.put(FruitTransaction.Operation.BALANCE, simpleHandler);
        OperationStrategyImpl strategy = new OperationStrategyImpl(map);

        assertThrows(RuntimeException.class, () -> strategy.getOperationHandler(null));
    }

    @Test
    void getHandler_unknownOperation_notOk() {
        Map<FruitTransaction.Operation, OperationHandler> map = new HashMap<>();
        map.put(FruitTransaction.Operation.BALANCE, simpleHandler);
        OperationStrategyImpl strategy = new OperationStrategyImpl(map);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> strategy.getOperationHandler(FruitTransaction.Operation.PURCHASE));
        assertTrue(ex.getMessage().contains("Supported operations"));
    }

    @Test
    void getHandler_knownOperations_ok() {
        Map<FruitTransaction.Operation, OperationHandler> map = new HashMap<>();
        for (FruitTransaction.Operation op : FruitTransaction.Operation.values()) {
            map.put(op, t -> {});
        }
        OperationStrategyImpl strategy = new OperationStrategyImpl(map);

        for (FruitTransaction.Operation op : FruitTransaction.Operation.values()) {
            OperationHandler handler = strategy.getOperationHandler(op);

            assertNotNull(handler, "Handler must not be null for operation: " + op);
            assertEquals(map.get(op), handler, "Handler should match the one in the map for: " + op);
        }
    }


    @Test
    void externalMapModification_doesNotAffectStrategy() {
        Map<FruitTransaction.Operation, OperationHandler> originalMap = new HashMap<>();
        originalMap.put(FruitTransaction.Operation.BALANCE, t -> {});

        OperationStrategyImpl strategy = new OperationStrategyImpl(originalMap);
        originalMap.put(FruitTransaction.Operation.SUPPLY, t -> {});
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                        strategy.getOperationHandler(FruitTransaction.Operation.SUPPLY),
                "Strategy must not be affected by external map modifications");
        assertTrue(exception.getMessage().contains("Supported operations"));

        OperationHandler existingHandler = strategy.getOperationHandler(FruitTransaction.Operation.BALANCE);
        assertNotNull(existingHandler, "Existing handler must still be available");
        assertEquals(originalMap.get(FruitTransaction.Operation.BALANCE), existingHandler);
    }

}
