package net.gmx.nosefish.fishylib.inventory;

import net.canarymod.api.inventory.Item;

/**
 *
 * @author Stefan Steinheimer (nosefish)
 */
public class ItemInfo {
    public static boolean isStackable(Item item) {
        return ! item.isEnchanted() 
            && ! item.hasDisplayName();
    }
    
    private static boolean areEqual(Object a, Object b)  {
        return (a == null && b == null)
            || (a != null && b != null && a.equals(b));
    }
    
    public static boolean isSameItemType(Item item1, Item item2) {
        return // same id
               (item1.getId() == item2.getId()) 
               // and same damage value
            && (item1.getDamage() == item2.getDamage())
               // and same DataTag:
            && areEqual(item1.getDataTag(), item2.getDataTag());
    }
    
    public static boolean canStack(Item item1, Item item2) {
        return isStackable(item1)
            && isStackable(item2)
            && (isSameItemType(item1, item2));
    }
}
