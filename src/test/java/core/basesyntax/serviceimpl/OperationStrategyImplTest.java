package core.basesyntax.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.common.FruitTransaction;
import core.basesyntax.strategy.OperationHandler;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

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
            map.put(op, simpleHandler);
        }
        OperationStrategyImpl strategy = new OperationStrategyImpl(map);

        for (FruitTransaction.Operation op : FruitTransaction.Operation.values()) {
            OperationHandler handler = assertDoesNotThrow(() -> strategy.getOperationHandler(op));
            assertNotNull(handler);
        }
    }

    @Test
    void externalMapModification_doesNotAffectStrategy() {
        Map<FruitTransaction.Operation, OperationHandler> map = new HashMap<>();
        map.put(FruitTransaction.Operation.BALANCE, simpleHandler);
        OperationStrategyImpl strategy = new OperationStrategyImpl(map);
        map.put(FruitTransaction.Operation.SUPPLY, simpleHandler);
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> strategy.getOperationHandler(FruitTransaction.Operation.SUPPLY));
        assertTrue(ex.getMessage().contains("Supported operations"));
        OperationHandler handler = assertDoesNotThrow(() ->
                strategy.getOperationHandler(FruitTransaction.Operation.BALANCE));
        assertNotNull(handler);
    }
}
