package core.basesyntax.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import core.basesyntax.common.FruitTransaction;
import core.basesyntax.db.Storage;
import core.basesyntax.service.OperationStrategy;
import core.basesyntax.service.ShopService;
import core.basesyntax.strategy.BalanceOperation;
import core.basesyntax.strategy.OperationHandler;
import core.basesyntax.strategy.PurchaseOperation;
import core.basesyntax.strategy.ReturnOperation;
import core.basesyntax.strategy.SupplyOperation;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ShopServiceImplTest {
    private static final String APPLE = "apple";
    private static final String BANANA = "banana";
    private static List<FruitTransaction> fruitTransaction;
    private static List<FruitTransaction> fruitEmptyTransaction;
    private static ShopService shopService;
    private static Map<String, Integer> fruits;

    @BeforeAll
    static void beforeAll() {
        fruitTransaction = List.of(
                new FruitTransaction(FruitTransaction.Operation.BALANCE, BANANA, 20),
                new FruitTransaction(FruitTransaction.Operation.BALANCE, APPLE, 100),
                new FruitTransaction(FruitTransaction.Operation.SUPPLY, BANANA, 100),
                new FruitTransaction(FruitTransaction.Operation.PURCHASE, BANANA, 13),
                new FruitTransaction(FruitTransaction.Operation.RETURN, APPLE, 10),
                new FruitTransaction(FruitTransaction.Operation.PURCHASE, APPLE, 20),
                new FruitTransaction(FruitTransaction.Operation.PURCHASE, BANANA, 5),
                new FruitTransaction(FruitTransaction.Operation.SUPPLY, BANANA, 50));
        fruitEmptyTransaction = Collections.emptyList();

        Map<FruitTransaction.Operation, OperationHandler> operationHandlers = Map.of(
                FruitTransaction.Operation.BALANCE, new BalanceOperation(),
                FruitTransaction.Operation.PURCHASE, new PurchaseOperation(),
                FruitTransaction.Operation.RETURN, new ReturnOperation(),
                FruitTransaction.Operation.SUPPLY, new SupplyOperation());
        OperationStrategy operationStrategy = new OperationStrategyImpl(operationHandlers);
        shopService = new ShopServiceImpl(operationStrategy);
        fruits = Storage.getFruits();
    }

    @Test
    void process_withNoEmptyTransactions_ok() {
        assertEquals(fruits.size(), 0);
        shopService.process(fruitTransaction);
        assertEquals(fruits.size(), 2);
        Storage.clear();
    }

    @Test
    void process_withEmptyTransaction_ok() {
        assertEquals(fruits.size(), 0);
        shopService.process(fruitEmptyTransaction);
        assertEquals(fruits.size(), 0);
    }
}
