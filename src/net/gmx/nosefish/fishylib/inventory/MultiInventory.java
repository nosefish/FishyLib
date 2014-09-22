package net.gmx.nosefish.fishylib.inventory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;

/**
 * 
 * @author Stefan Steinheimer (nosefish)
 */
public class MultiInventory {
  
    protected final HashSet<WeakReference<Inventory>> inventories, changed, stale;

    
    /**
     * Constructor
     */
    public MultiInventory() {
        this.inventories = new HashSet<>();
        this.changed = new HashSet<>();
        this.stale = new HashSet<>();
    }
    
    /**
     * Removes Inventories that have been marked as stale. Inventories are
     * marked as stale when they are found to have been unloaded
     * they are no longer in memory.
     */
    protected void removeStaleEntries() {
        if (! stale.isEmpty()) {
            inventories.removeAll(stale);
            stale.clear();
        }
    }
    
    protected Inventory getCheckStale(WeakReference<Inventory> ref) {
        Inventory inv = ref.get();
        if (inv == null) {
            stale.add(ref);
            return null;
        }
        return inv;
    }
    
    /**
     * Adds and Inventory to be used as part of the MultiInventory
     * @param inventory
     * @return 
     */
    public MultiInventory add(Inventory inventory) {
        WeakReference<Inventory> ref = new WeakReference<>(inventory);
        inventories.add(ref);
        return this;
    }
    
    /**
     * Removes an inventory from the MultiInventory.
     * @param inventory
     * @return 
     */
    public MultiInventory remove(Inventory inventory) {
        for (WeakReference<Inventory> ref : inventories) {
            Inventory inv = getCheckStale(ref);
            if (inv != null && inv.equals(inventory)) {
                stale.add(ref);
            }
        }
        removeStaleEntries();
        return this;
    }
    
    /**
    * Stores the item in the Inventory. 
    * Does not respect stack-before-new over multiple inventories yet.
     * @param item the item to store. Its amount will be updated.
     * @return true if all items have been stored, false otherwise
    */
    public boolean store(Item item) {
        boolean allStored = false;
        for (WeakReference<Inventory> ref : inventories) {
            Inventory inv = getCheckStale(ref);
            if (inv == null) {
                continue;
            }
            
            // try to store in this inventory
            int amount = item.getAmount();
            allStored = inv.insertItem(item);
            if (item.getAmount() < amount) {
                // something has been stored, the inventory must be updated
                changed.add(ref);
            }
            if (allStored) {
                break;
            }
        }
        removeStaleEntries();
        return allStored;
    }
    
    /**
     * Takes items out of the inventory.
     * @param itemType type of the items to take
     * @param amount amount to take
     * @return a list of item stacks taken out of the inventory
     */
    public List<Item> takeOut(ItemType itemType, int amount) {
        int amountLeftToTake = amount;
        List<Item> toReturn = new ArrayList<>();
        for (WeakReference<Inventory> ref : inventories) {
            
            // check if this inventory still exists
            Inventory inv = getCheckStale(ref);
            if (inv == null) {
                continue;
            }
            
            // get all the stacks we can find, until we have enough
            // or the inventory is empty
            Item taken = null;
            do {
                taken = inv.getItem(itemType, amountLeftToTake);
                if (taken != null) {
                    // got something
                    amountLeftToTake -= taken.getAmount();
                    toReturn.add(taken.clone());
                    changed.add(ref);
                }
            } while (taken != null && amountLeftToTake > 0);
            if (amountLeftToTake <= 0) {
                break;
            }
        }
        removeStaleEntries();
        return toReturn;
    }
    
    /**
     * Checks if the inventory contains an item of the specified type.
     * @param itemType
     * @return true if an item was found, false otherwise
     */
    public boolean contains(ItemType itemType) {
        boolean found = false;
        for (WeakReference<Inventory> ref : inventories) {
            
            // check if this inventory still exists
            Inventory inv = getCheckStale(ref);
            if (inv == null) {
                continue;
            }
            
            // check if it contains the item
            if (inv.hasItem(itemType)) {
                found = true;
                break;
            }
        }
        removeStaleEntries();
        return found;
    }
    
    public MultiInventory update() {
        for (WeakReference<Inventory> ref : changed) {
            Inventory inv = getCheckStale(ref);
            if (inv != null) {
                inv.update();
            }
        }
        removeStaleEntries();
        return this;
    }
}
