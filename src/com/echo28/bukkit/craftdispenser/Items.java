package com.echo28.bukkit.craftdispenser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

/**
 *
 * @author Nijiko
 */
public class Items {
	
	public static Logger log = Logger.getLogger("Items");
	
	public static iProperty ItemsiProp = new iProperty("items.db");
	public static HashMap<String, String> items;
	
    public Items() { }
    
    
    /**
     * Setup Items
     */
    public static void setupItems() {
		Map<String, String> mappedItems = null;
		items = new HashMap<String, String>();
	
		try {
			mappedItems = ItemsiProp.returnMap();
		} catch (Exception ex) {
			System.out.println("[CraftDispenser Flatfile] could not open items.db!");
			ex.printStackTrace();
		}
	
		if(mappedItems != null) {
			for (Object item : mappedItems.keySet()) {
				String left = (String)item;
				String right = (String) mappedItems.get(item);
				String id = left.trim();
				String itemName;
				//log.info("Found " + left + "=" + right + " in items.db");
				if(id.matches("[0-9]+") || id.matches("[0-9]+,[0-9]+")) {
					//log.info("matches");
					if(right.contains(",")) {
						String[] synonyms = right.split(",");
						itemName = synonyms[0].toLowerCase().replaceAll("\\s+", "");
						items.put(itemName, id);
						//log.info("Added " + itemName + "=" + id);
						for(int i = 1; i < synonyms.length; i++) {
							itemName = synonyms[i].toLowerCase().replaceAll("\\s+", "");
							items.put(itemName, id);
							//log.info("Added " + itemName + "=" + id);
						}
					} else {
						itemName = right.toLowerCase().replaceAll("\\s+", "");
						items.put(itemName, id);
						//log.info("Added " + itemName + "=" + id);
					}
				} else {
					itemName = left.toLowerCase().replaceAll("\\s+", "");
					id = right.trim();
					items.put(itemName, id);
					//log.info("Added " + itemName + "=" + id);
				}
			}
		}
    }
    
    

    /**
     * Returns the name of the item stored in the hashmap or the item name stored in the items.txt file in the hMod folder.
     *
     * @param id
     * @return
     */
    public static String name(int id, int data) {
		String longKey = Misc.string(id) + "," + Misc.string(data);
		if (items.containsKey(Misc.string(id))) {
			return Misc.camelToPhrase(items.get(Misc.string(id)));
		} else if (data >= 0 && items.containsKey(longKey)) {
			return Misc.camelToPhrase(items.get(longKey));
		}
	
		for (Material item : Material.values()) {
			if (item.getId() == id) {
				return Misc.camelToPhrase(item.toString());
			}
		}
	
		return Misc.camelToPhrase(Misc.string(id));
    }

    /**
     * Checks whether a player has the required amount of items or not.
     *
     * @param player
     * @param itemId
     * @param amount
     * @return true / false
     */
    public static boolean has(Player player, int itemId, int amount) {
		PlayerInventory inventory = player.getInventory();
		ItemStack[] items = inventory.getContents();
		int total = 0;
	
		for (ItemStack item : items) {
		    if ((item != null) && (item.getTypeId() == itemId) && (item.getAmount() > 0)) {
			total += item.getAmount();
		    }
		}
	
		return (total >= amount) ? true : false;
    }

    /**
     * Checks the getArray() for the amount total of items
     *
     * @param itemId The item we are looking for
     * @return integer - Total amount of items
     */
    public static int hasAmount(Player player) {
		PlayerInventory inventory = player.getInventory();
		ItemStack[] items = inventory.getContents();
		int amount = 0;
	
		for (ItemStack item : items) {
		    if ((item != null)) {
			amount += item.getAmount();
		    }
		}
	
		return amount;
    }

    /**
     * Checks the getArray() for the amount total of this item id.
     *
     * @param itemId The item we are looking for
     * @return integer - Total amount of itemId in the array
     */
    public static int hasAmount(Player player, int itemId) {
		PlayerInventory inventory = player.getInventory();
		ItemStack[] items = inventory.getContents();
		int amount = 0;
	
		for (ItemStack item : items) {
		    if ((item != null) && (item.getTypeId() == itemId)) {
			amount += item.getAmount();
		    }
		}
	
		return amount;
    }

    /**
     * Removes the amount of items from a player
     *
     * @param player
     * @param item
     * @param amount
     */
    public static void remove(Player player, int item, int amount) {
		PlayerInventory inventory = player.getInventory();
		//ItemStack[] items = inventory.getContents();
		int counter = amount;
		int leftover = 0;
	
		for (int i = 0; i < 120; i++) {
		    ItemStack current = inventory.getItem(i);
	
		    if (current == null || current.getAmount() <= 0) {
		    	continue;
		    }
	
		    if (current.getTypeId() != item) {
		    	continue;
		    }
	
		    if (current.getAmount() > counter) {
		    	leftover = current.getAmount() - counter;
		    }
	
		    if (leftover != 0) {
				inventory.remove(i);
		
				if (inventory.firstEmpty() == -1) {
				    player.getWorld().dropItem(player.getLocation(), new ItemStack(item, leftover));
				} else {
				    inventory.setItem(inventory.firstEmpty(), new ItemStack(item, leftover));
				}
		
				counter = 0;
				break;
		    } else {
				counter -= current.getAmount();
				inventory.remove(i);
		    }
		}
    }
    
    public static ItemStack createItemStack(int[] item) {
    	return new ItemStack(item[0], item[3], (short)item[2], item[1] == -1? null : new Byte((byte)item[1]));
    }
    public static ItemStack[] createItemStacks(int[][] items) {
    	ItemStack[] itemstacks = new ItemStack[items.length];
    	
    	for (int i = 0; i < items.length; i++)
    		itemstacks[i] = createItemStack(items[i]);
    	
    	return itemstacks;
    }
    
    public static int[][] parseItemList(List<String> itemsList)
	{
		int[][] items = new int[itemsList.size()][];
		
		for (int i = 0; i < itemsList.size(); i++)
			items[i] = parseItem(itemsList.get(i));
		
		return items;
	}
	
	public static boolean itemMatchesStack(int[] item, ItemStack itemstack) {
		if (itemstack.getTypeId() == item[0] && itemstack.getAmount() >= item[3]) {
			MaterialData matdat = itemstack.getData();
			if (matdat == null || matdat.getData() == item[1]) 
				return true;
		}
		return false;
	}

    /**
     * Validate the string for an item
     *
     * @param itemStr
     * @return {id, data, damage, amount}
     */
	private static Pattern itemPattern = Pattern.compile("([a-zA-Z ]+|\\d+)(?:\\s*\\.\\s*(\\d+))?(?:\\s*,\\s*(\\d+))?(?:\\s*\\*\\s*(\\d+))?");
    public static int[] parseItem(String itemStr) {
    	Matcher m = itemPattern.matcher(itemStr);
    	
    	int[] ret = new int[] {-1, -1, 0, 1};
    	
    	if (m.matches()) {
    		String type = m.group(1);
    		try {
    			ret[0] = Integer.parseInt(type);
    		} catch (NumberFormatException e) {
    			type = type.toLowerCase().replaceAll("\\s+", "");
    			String id = items.get(type);
    			try {
    				ret[0] = Integer.parseInt(id);
    			} catch (NumberFormatException e2) {
    				System.out.printf("Fail '%s' '%s'\n", type, id);
    			}
    		}
    		for (int i = 1; i < 4; i++) {
    			try {
    				ret[i] = Integer.parseInt(m.group(i+1));
    			} catch (NumberFormatException e) {
    				// do nothing
    			}
    		}
    		//System.out.printf("Matched '%s': {%d, %d, %d, %d}\n", itemStr, ret[0], ret[1], ret[2], ret[3]);
    	} else {
    		System.out.printf("Unknown item '%s'\n", itemStr);
    	}
    	
    	return ret;
    }

   

    public static boolean validateType(int id, int type) {
		if (type == -1 || type == 0) {
		    return true;
		}
	
		//int itemId = -1;
	
		if (id == 35 || id == 351 || id == 63) {
		    if (type >= 0 && type <= 15) {
			return true;
		    }
		}
	
		if (id == 17) {
		    if (type >= 0 && type <= 2) {
			return true;
		    }
		}
	
		if (id == 91 || id == 86 || id == 67 || id == 53 || id == 77 || id == 71 || id == 64) {
		    if (type >= 0 && type <= 3) {
			return true;
		    }
		}
	
		if (id == 66) {
		    if (type >= 0 && type <= 9) {
			return true;
		    }
		}
	
		if (id == 68) {
		    if (type >= 2 && type <= 5) {
			return true;
		    }
		}
	
		if (id == 263) {
		    if (type == 0 || type == 1) {
			return true;
		    }
		}
	
		if (isDamageable(id)) {
		    return true;
		}
	
		return false;
    }

    public static boolean checkID(int id) {
		for (Material item : Material.values()) {
		    if (item.getId() == id) {
			return true;
		    }
		}
	
		return false;
    }

    public static boolean isDamageable(int id) {
    	//tools (including lighters and fishing poles) and armour
		if(id >= 256 && id <= 259) return true;
		if(id >= 267 && id <= 279) return true;
		if(id >= 283 && id <= 286) return true;
		if(id >= 290 && id <= 294) return true;
		if(id >= 298 && id <= 317) return true;
		if(id == 346) return true;
		return false;
    }
    
    public static boolean isStackable(int id) {
    	// false for tools (including buckets, bow, and lighters, but not fishing poles), food, armour, minecarts, boats, doors, and signs.
		if(id >= 256 && id <= 261) return false;
		if(id >= 267 && id <= 279) return false;
		if(id >= 282 && id <= 286) return false;
		if(id >= 290 && id <= 294) return false;
		if(id >= 297 && id <= 317) return false;
		if(id >= 322 && id <= 330) return false;
		if(id == 319 || id == 320 || id == 349 || id == 350) return false;
		if(id == 333 || id == 335 || id == 343 || id == 342) return false;
		if(id == 354 || id == 2256 || id == 2257) return false;
		return true;
    }
}