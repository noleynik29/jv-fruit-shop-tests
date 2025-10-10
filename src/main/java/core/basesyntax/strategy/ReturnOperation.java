package core.basesyntax.strategy;

import core.basesyntax.common.FruitTransaction;
import core.basesyntax.db.Storage;

public class ReturnOperation implements OperationHandler {
    @Override
    public void apply(FruitTransaction fruitTransaction) {
        if (fruitTransaction == null) {
            throw new RuntimeException("FruitTransaction must not be null");
        }
        String fruit = fruitTransaction.getFruit();
        if (fruit == null || fruit.trim().isEmpty()) {
            throw new RuntimeException("Fruit name must not be null or empty in transaction: "
                    + fruitTransaction);
        }
        int quantity = fruitTransaction.getQuantity();
        if (quantity < 0) {
            throw new IllegalArgumentException("Return quantity must be non-negative "
                    + "in transaction: "
                    + fruitTransaction);
        }
        if (quantity == 0) {
            throw new IllegalArgumentException("Return quantity mustn't be 0 in transaction: "
                    + fruitTransaction);
        }
        int currentStock = Storage.getQuantity(fruit);
        int newStock = currentStock + quantity;
        Storage.setQuantity(fruit, newStock);
    }
}
