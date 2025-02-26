package me.nzuguem.petstore.shared.api.inventory.temporal;

import io.temporal.activity.ActivityInterface;
import me.nzuguem.petstore.shared.api.inventory.models.CheckInventoryRequest;

@ActivityInterface
public interface InventoryActivities {

    String TASK_QUEUE = "inventory-tasks";

    void checkInventory(CheckInventoryRequest request);
}
