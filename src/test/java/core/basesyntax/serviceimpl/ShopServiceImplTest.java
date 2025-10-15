package core.basesyntax.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.common.FruitTransaction;
import core.basesyntax.db.Storage;
import core.basesyntax.service.OperationStrategy;
import core.basesyntax.strategy.BalanceOperation;
import core.basesyntax.strategy.OperationHandler;
import core.basesyntax.strategy.PurchaseOperation;
import core.basesyntax.strategy.ReturnOperation;
import core.basesyntax.strategy.SupplyOperation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShopServiceImplTest {
    private ShopServiceImpl shopService;
    private OperationStrategy operationStrategy;

    @BeforeEach
    void setUp() {
        Storage.clear();
        Map<FruitTransaction.Operation, OperationHandler> operationHandlers = Map.of(
                FruitTransaction.Operation.BALANCE, new BalanceOperation(),
                FruitTransaction.Operation.PURCHASE, new PurchaseOperation(),
                FruitTransaction.Operation.RETURN, new ReturnOperation(),
                FruitTransaction.Operation.SUPPLY, new SupplyOperation());
        operationStrategy = new OperationStrategyImpl(operationHandlers);
        shopService = new ShopServiceImpl(operationStrategy);
    }

    @Test
    void process_nullList_notOk() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> shopService.process(null));
        assertTrue(ex.getMessage().contains("Transactions list must not be null"));
    }

    @Test
    void process_listWithNullTransaction_notOk() {
        List<FruitTransaction> transactions = new ArrayList<>();
        transactions.add(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> shopService.process(transactions));
        assertTrue(ex.getMessage().contains("Transaction at index 0 is null"));
    }

    @Test
    void process_transactionWithNullOperation_notOk() {
        List<FruitTransaction> transactions = List.of(
                new FruitTransaction(null, "apple", 10));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> shopService.process(transactions));
        assertTrue(ex.getMessage().contains("Transaction operation at index 0 is null"));
    }

    @Test
    void process_transactionWithUnknownOperation_notOk() {
        Map<FruitTransaction.Operation, OperationHandler> handlers = Map.of(
                FruitTransaction.Operation.BALANCE, new BalanceOperation());
        OperationStrategy strategy = new OperationStrategyImpl(handlers);
        ShopServiceImpl service = new ShopServiceImpl(strategy);

        List<FruitTransaction> transactions = List.of(
                new FruitTransaction(FruitTransaction.Operation.PURCHASE, "apple", 5));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.process(transactions));
        assertTrue(ex.getMessage().contains("Failed to get handler for transaction"));
    }

    @Test
    void process_operationsAppliedCorrectly_ok() {
        List<FruitTransaction> transactions = List.of(
                new FruitTransaction(FruitTransaction.Operation.BALANCE, "apple", 50),
                new FruitTransaction(FruitTransaction.Operation.SUPPLY, "apple", 20),
                new FruitTransaction(FruitTransaction.Operation.PURCHASE, "apple", 30),
                new FruitTransaction(FruitTransaction.Operation.RETURN, "apple", 10)
        );

        shopService.process(transactions);
        assertEquals(50, Storage.getFruits().get("apple"));
    }

    @Test
    void process_emptyList_ok() {
        shopService.process(Collections.emptyList());
        assertEquals(0, Storage.getFruits().size());
    }
}
