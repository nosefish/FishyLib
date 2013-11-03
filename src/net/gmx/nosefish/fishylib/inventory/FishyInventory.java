package net.gmx.nosefish.fishylib.inventory;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.api.nbt.CompoundTag;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.TileEntity;
import net.gmx.nosefish.fishylib.worldmath.FishyLocationInt;
import net.gmx.nosefish.fishylib.worldmath.FishyVectorInt;
import net.gmx.nosefish.fishylib.worldmath.FishyWorld;

public class FishyInventory {
	private List<Inventory> storage = new LinkedList<Inventory>();
	private Set<Inventory> changedInventories = new HashSet<Inventory>(8);
	private Set<FishyLocationInt> positions = new HashSet<FishyLocationInt>(8);
	private Set<Integer> allowedBlocks = new TreeSet<Integer>();


	/** There must be at least one type of storage block allowed, or you won't be able
	 *  to add storage blocks.
	 * 
	 * @param type Block.Type to allow as storage. Invalid types will be ignored.
	 */
	public void addAllowedStorageBlockType(BlockType type) {
		allowedBlocks.add(new Integer(type.getId()));
	}

	/** There must be at least one type of storage block allowed, or you won't be able
	 *  to add storage blocks.
	 * 
	 * @param type block id to allow as storage. Invalid types will be ignored.
	 */
	public void addAllowedStorageBlockType(int type) {
		allowedBlocks.add(type);
	}

	/** There must be at least one type of storage block allowed, or you won't be able
	 *  to add storage blocks.
	 * 
	 * @param type block ids to allow as storage. Invalid types will be ignored.
	 */
	public void addAllowedStorageBlockIds(Collection<Integer> type) {
		allowedBlocks.addAll(type);
	}

	/**
	 * Stores an Item.
	 * 
	 * @param item The Item to store. The amount gets reduced by the amount stored. Handle (item.getAmount < 1) !
	 * @return true if all items in the stack have been stored, false otherwise.
	 */
	public boolean storeItem(Item item){
		boolean allStored = false;
		for (Inventory inventory : storage) {
			if (item.getAmount() > 0) {
				int amountBefore=item.getAmount();
				allStored = storeItem(inventory, item);
				if (item.getAmount() != amountBefore) {
					changedInventories.add(inventory);
				}
			} else {
				allStored = true;
			}
			if (allStored) break;
		}
		return allStored;
	}


	public static boolean storeItem(Inventory inv, Item item) {
		// determine maximum stack size for this item type
		int maxStackSize = maxStackSize(item);
		// the contents array gives us more control than the Inventory methods
		Item[] contents = inv.getContents();
		// stack if possible
		if (maxStackSize > 1) {
			for (int slot = 0; slot < contents.length; slot ++) {
				if (contents[slot] == null) continue;
				// there's something in this slot
				int freeSpace = maxStackSize - contents[slot].getAmount(); 
				if (freeSpace > 0
						&& contents[slot].getId() == item.getId()
						&& contents[slot].getDamage() == item.getDamage()
						&& (contents[slot].getDataTag() == null
						||contents[slot].getDataTag().equals(item.getDataTag()))) {
					int storeAmount = (item.getAmount() > freeSpace) ? freeSpace : item.getAmount();
					contents[slot].setAmount(contents[slot].getAmount() + storeAmount);
					item.setAmount(item.getAmount() - storeAmount);
					if (item.getAmount() < 1) {
						return true;
					}
				}
			}
		}
		// we've tried stacking but still have some items left in the stack
		// do we have an empty slot?
		int freeSlot = inv.getEmptySlot();
		if (freeSlot < 0) return false;
		Item itemClone = item.clone();
		itemClone.setSlot(freeSlot);
		inv.addItem(itemClone);
		item.setAmount(0);
		return true;
	}

	public Item fetchItem(ItemType type, int amount){
		return fetchItem(type.getId(), type.getData(), amount);
	}

	/**
	 * Takes a stack of items out of storage
	 * @param id
	 * @param datavalue
	 * @param amount The desired amount. 
	 *                      The amount in the returned Item may be less than this
	 *                      if there weren't enough items of this type in storage
	 *                      or if the desired amount exceeds maximum stack size for
	 *                      this item type. 
	 * @return The Item taken from storage, or null if no suitable item was found in this storage.
	 */
	public Item fetchItem(int id, int datavalue, int amount){
		int amountRequested = (amount < 0) ? Integer.MAX_VALUE : amount;
		Item retItem = null;
		int maxRetAmount = 0;
		for (Inventory inventory : storage) {
			// the contents array gives us more control than the Inventory methods
			Item[] contents = inventory.getContents();
			for (int slot=0; slot < contents.length; slot++) {
				if (matchItem(contents[slot], id, datavalue)) {
					if (retItem == null) {
						// prepare a new Item with the right properties
						retItem = contents[slot].clone();
						retItem.setAmount(0);
						maxRetAmount = Math.min(FishyInventory.maxStackSize(retItem), amountRequested);
					}
					if (areItemsEqualIgnoreSlotAndAmount(retItem, contents[slot])) {
						// take items from this slot
						int fetchAmount = Math.min(maxRetAmount - retItem.getAmount(), contents[slot].getAmount());
						retItem.setAmount(retItem.getAmount() + fetchAmount);
						contents[slot].setAmount(contents[slot].getAmount() - fetchAmount);
						if (contents[slot].getAmount() < 1) {
							// we've emptied the slot
							contents[slot] = null;
							inventory.removeItem(slot);
						}
						changedInventories.add(inventory);
					}
					// Paranoid? Better safe than sorry!
					if (retItem.getAmount() < 1) {
						retItem = null;
					}
				}
			}
			if (retItem != null && retItem.getAmount() >= maxRetAmount) break;
		}
		return retItem;
	}

	public boolean containsItem(ItemType type, int minAmount) {
		return containsItem(type.getId(), type.getData(),  minAmount);
	}

	/** Checks if the storage contains at least the specified amount of the specified item
	 * 
	 * @param id Item Id
	 * @param datavalue data value to look for, -1 matches all
	 * @param minAmount must be greater than 0
	 * @return true if the specified amount was found
	 */
	public boolean containsItem(int id, int datavalue, int minAmount) {
		// we always have 0 or negative amounts of anything
		if (minAmount < 1) return true;
		// search inventory
		int count = 0;
		for (Inventory inventory : this.storage) {
			Item[] contents = inventory.getContents();
			for (int slot = 0; slot < contents.length; slot++) {
				if (contents[slot] != null
						&& matchItem(contents[slot], id, datavalue)) {
					count += contents[slot].getAmount();
					if (count >= minAmount) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Transfers all items in the source inventory into the storage.
	 * @param source The inventory from which to take the items
	 * @return true if all items have been stored
	 */
	public boolean storeAllItems(Inventory source) {
		Item[] contents = source.getContents();
		boolean allStored = true;
		for (int slot = 0; slot < contents.length; slot++) {
			if (contents[slot] == null) {
				continue;
			}
			if (! this.storeItem(contents[slot])) {
				allStored = false;
			}
			if (contents[slot].getAmount() < 1) {
				contents[slot] = null;
				source.removeItem(slot);
			}
		}
		return allStored;
	}

	public boolean storeAllItems(Inventory source, int id, int datavalue, int amount) {
		int remainingAmount = (amount < 0)? Integer.MAX_VALUE : amount;
		Item[] contents = source.getContents();
		for (int slot = 0; slot < contents.length; slot++) {
			if (remainingAmount < 1) {
				break;
			}
			if (contents[slot] == null || ! matchItem(contents[slot], id, datavalue)) {
				continue;
			}
			Item toStore = contents[slot].clone();
			int amountBefore = toStore.getAmount();
			if (amountBefore > remainingAmount) {
				// store only the remaining amount
				toStore.setAmount(remainingAmount);
				amountBefore = remainingAmount;
			}
			this.storeItem(toStore);
			int amountStored = amountBefore - toStore.getAmount();
			remainingAmount -= amountStored;
			contents[slot].setAmount(contents[slot].getAmount() - amountStored);
			if (contents[slot].getAmount() < 1) {
				contents[slot] = null;
				source.removeItem(slot);
			}
		}
		return remainingAmount < 1;
	}


	public boolean fetchAllItems(Inventory target) {
		Item item = null;
		// TODO: put these back to where they came from
		List <Item> couldNotTransfer = new LinkedList<Item>();
		while (true) {
			item = fetchItem(-1, -1, -1);
			if (item == null) {
				break;
			}
			// put the item into the target inventory
			boolean stored = FishyInventory.storeItem(target, item);
			if (! stored) {
				couldNotTransfer.add(item);
			}
		}
		if  (couldNotTransfer.isEmpty()){
			return true;
		} else {
			// put everything that doesn't fit in the target back into storage
			for (Item i:couldNotTransfer) {
				storeItem(i);
			}
		}
		return false;
	}

	public boolean fetchAllItems(Inventory target, int id, int datavalue, int amount) {
		// negative amount: no limit
		int amountRemaining = (amount < 0) ? Integer.MAX_VALUE: amount;
		while (amountRemaining > 0) {
			Item item = fetchItem(id, datavalue, amountRemaining);
			if (item == null) {
				//storage is empty
				return false;
			}
			int amountBefore = item.getAmount();
			boolean targetFull = ! FishyInventory.storeItem(target, item);
			if (targetFull) {
				return false;
			}
			int amountStored = amountBefore - item.getAmount();
			amountRemaining -= amountStored; 
		}
		return true;
	}


	/**
	 * Adds storage block at wbv to be used as storage space.
	 * @param wbv
	 * @return true if the block was added, false if wbv does not point to a suitable storage block
	 */
	public boolean addStorageBlock(FishyLocationInt wbv) {
		if (! FishyWorld.isBlockLoaded(wbv)) return false;
		World world = wbv.getWorld().getWorldIfLoaded();
		TileEntity cBlock = world.getTileEntityAt(wbv.getIntX(), wbv.getIntY(), wbv.getIntZ());
		if (cBlock == null) return false;
		if (! allowedBlocks.contains(cBlock.getBlock().getType())) return false;
		if (cBlock instanceof Inventory) {
			FishyLocationInt cBlockPos = new FishyLocationInt(wbv.getWorld(), cBlock.getX(), cBlock.getY(), cBlock.getZ());;
			if (positions.add(cBlockPos)) {
				storage.add((Inventory) cBlock);
			}
			return true;
		}
		return false;
	}


	/**
	 * Adds all storage blocks in a 7x7x7 cube centered around wbv to be used as storage space.
	 * @param wbv
	 * @return true, if at least one storage block was added, false if no suitable block was found
	 */
	public boolean addNearbyStorageBlocks(FishyLocationInt wbv) {
		boolean addedAtLeastOne = false;
		for (int x = -3; x <= 3; x++) {
			for (int y = -3; y <= 3; y++) {
				for (int z = -3; z <= 3; z++) {
					FishyLocationInt cur = new FishyLocationInt(
							wbv.getWorld(),
							wbv.addIntVector(new FishyVectorInt(x, y, z)));
					addedAtLeastOne = addStorageBlock(cur) || addedAtLeastOne;
				}
			}
		}
		return addedAtLeastOne;
	}

	/**
	 * Adds all storage blocks in a 5x5x2 cuboid centered around wbv,
	 * as well as those located 2 and 3 blocks directly above to be used as storage space.
	 * Used for [Deposit]/[Collect].
	 * 
	 * @param wbv
	 * @return true, if at least one storage block was added, false if no suitable block was found
	 */
	public boolean addNearbyRailStorageBlocks(FishyLocationInt wbv) {
		boolean addedAtLeastOne = false;
		// Add Chests around the block
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				for (int y = -1; y <= 0; y++) {
					FishyLocationInt cur = 
							new FishyLocationInt(
									wbv.getWorld(),
									wbv.addIntVector(new FishyVectorInt(x, y, z)));
					addedAtLeastOne = addStorageBlock(cur) || addedAtLeastOne;
				}
			}
		}
		// Also allow the rail system to be under the floor stocking chests directly above.
		// Not in the loop so that the chest cannot be offset on X or Z, only Y.
		addedAtLeastOne = addStorageBlock(
				new FishyLocationInt(wbv.getWorld(), wbv.addIntVector(new FishyVectorInt(0, 2, 0))))
				|| addedAtLeastOne;
		addedAtLeastOne = addStorageBlock(
				new FishyLocationInt(wbv.getWorld(), wbv.addIntVector(new FishyVectorInt(0, 3, 0))))
				|| addedAtLeastOne;
		return addedAtLeastOne;
	}

	/**
	 * Sends changes to players. Call when you're done storing and fetching for this tick. Don't forget!
	 */
	public void update() {
		for (Inventory inventory : changedInventories) {
			inventory.update();
		}
		changedInventories.clear();
	}

	private boolean matchItem(Item item, int id, int dataValue) {
		return (item != null
				&& item.getAmount() > 0
				&& (id < 0 || item.getId() == id)
				&& (dataValue < 0 || item.getDamage() == dataValue));
	}

	public static int maxStackSize(Item item) {
		int maxStackSize = item.getMaxAmount();
		//            if (InventoryListener.allowMinecartStacking
				//                            && (item.getType().equals(ItemType.Minecart)
		//                                            || item.getType().equals(ItemType.StorageMinecart)
		//                                            || item.getType().equals(ItemType.PoweredMinecart)
		//                                            || item.getType().equals(ItemType.MinecartTNT)
		//                                            || item.getType().equals(ItemType.MinecartHopper)
		//                                            )) {
		//                    maxStackSize = 64;
		//            }
		//            if (item.getEnchantments() != null && ! InventoryListener.allowEnchantableItemStacking) {
		//                    maxStackSize = 1;
		//            }
		return maxStackSize;
	}

	public static boolean areItemsEqualIgnoreSlotAndAmount(Item item1, Item item2) {
		if (item1 == item2) return true;
		if (item1 == null || item2 == null) return false;
		if (item1.getId() != item2.getId()) return false;
		if (item1.getDamage() != item2.getDamage()) return false;
		CompoundTag tag1 = item1.getDataTag();
		CompoundTag tag2 = item2.getDataTag();
		if (tag1 == tag2) return true;
		if (tag1 == null || tag2 == null) return false;
		return tag1.equals(tag2);
	}
}
