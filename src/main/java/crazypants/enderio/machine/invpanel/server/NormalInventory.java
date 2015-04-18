package crazypants.enderio.machine.invpanel.server;

import crazypants.enderio.conduit.item.NetworkedInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

class NormalInventory extends AbstractInventory {
  final NetworkedInventory ni;

  NormalInventory(NetworkedInventory ni) {
    this.ni = ni;
  }

  @Override
  void scanInventory(InventoryDatabaseServer db, int aiIndex) {
    ISidedInventory inv = ni.getInventoryRecheck();
    int side = ni.getInventorySide();
    int[] slotIndices = inv.getAccessibleSlotsFromSide(side);
    if (slotIndices == null || slotIndices.length == 0) {
      setEmpty(db, aiIndex);
      return;
    }
    int count = slotIndices.length;
    if (count != slotItems.length) {
      reset(db, count, aiIndex);
    }
    for (int slot = 0; slot < count; slot++) {
      int invSlot = slotIndices[slot];
      ItemStack stack = inv.getStackInSlot(invSlot);
      if (stack != null && !inv.canExtractItem(invSlot, stack, side)) {
        stack = null;
      }
      updateSlot(db, slot, aiIndex, stack);
    }
  }

  @Override
  public int extractItem(InventoryDatabaseServer db, ItemEntry entry, int slot, int aiIndex, int count) {
    ISidedInventory inv = ni.getInventoryRecheck();
    int side = ni.getInventorySide();
    int[] slotIndices = inv.getAccessibleSlotsFromSide(side);
    if (slotIndices == null || slot >= slotIndices.length) {
      return 0;
    }
    int invSlot = slotIndices[slot];
    ItemStack stack = inv.getStackInSlot(invSlot);
    if (stack == null || !inv.canExtractItem(invSlot, stack, side)) {
      return 0;
    }
    if (db.lookupItem(stack, entry, false) != entry) {
      return 0;
    }
    int remaining = stack.stackSize;
    if (count > remaining) {
      count = remaining;
    }
    ni.itemExtracted(invSlot, count);
    remaining -= count;
    updateCount(db, slot, aiIndex, entry, remaining);
    return count;
  }

}
