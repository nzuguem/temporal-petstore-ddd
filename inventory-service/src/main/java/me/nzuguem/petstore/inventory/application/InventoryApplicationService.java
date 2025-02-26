package me.nzuguem.petstore.inventory.application;

import io.temporal.spring.boot.ActivityImpl;
import lombok.extern.slf4j.Slf4j;
import me.nzuguem.petstore.shared.api.inventory.exceptions.OutOfStockException;
import me.nzuguem.petstore.shared.api.inventory.models.CheckInventoryRequest;
import me.nzuguem.petstore.shared.api.inventory.temporal.InventoryActivities;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@ActivityImpl(taskQueues = InventoryActivities.TASK_QUEUE)
public class InventoryApplicationService implements InventoryActivities {

    private AtomicInteger stock = new AtomicInteger(20);


    @Override
    public void checkInventory(CheckInventoryRequest request) {

        var products = request.products();

        log.info("Checking inventory for {} products", products.size());

        for (var product : products) {
            if (stock.get() < product.quantity()) {
                throw new OutOfStockException("Items are out of stock");
            }
            stock.set(stock.get() - product.quantity());
        }

        log.info("All {} products are in stock", products.size());

    }
}
