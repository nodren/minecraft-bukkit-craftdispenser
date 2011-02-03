package com.echo28.bukkit.craftdispenser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;



public class ItemSpec {
	public int id = 0;
	public byte data = -1;
	public short damage = 0;
	public int amount = 1;

	public ItemSpec() {}

	public ItemSpec(int id) {
		this.id = id;
	}

	public ItemSpec(int id, byte data) {
		this.id = id;
		this.data = data;
	}

	public ItemSpec(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}


	public ItemStack createItemStack() {
		return new ItemStack(id, amount, (short) damage, data == -1 ? null : new Byte(data));
	}

	public static ItemStack[] createItemStacks(ItemSpec[] items) {
		ItemStack[] itemstacks = new ItemStack[items.length];

		for (int i = 0; i < items.length; i++)
			itemstacks[i] = items[i].createItemStack();

		return itemstacks;
	}

	public static ItemSpec[] parseItems(List<String> itemsList) throws BadItemException {
		ItemSpec[] items = new ItemSpec[itemsList.size()];

		for (int i = 0; i < itemsList.size(); i++)
			items[i] = parseItem(itemsList.get(i));

		return items;
	}

	public boolean matchesItemStack(ItemStack itemstack) {
		if (itemstack.getTypeId() == id && itemstack.getAmount() >= amount) {
			MaterialData matdat = itemstack.getData();
			if (matdat == null || matdat.getData() == data)
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
	private static Pattern itemPattern = Pattern
	        .compile("([a-zA-Z ]+|\\d+)(?:\\s*\\.\\s*(\\d+))?(?:\\s*,\\s*(\\d+))?(?:\\s*\\*\\s*(\\d+))?");

	public static ItemSpec parseItem(String itemStr) throws BadItemException {
		ItemSpec ret = new ItemSpec();

		if (itemStr.equals(""))
			return ret; // return empty, or air, for an empty string

		Matcher m = itemPattern.matcher(itemStr);

		if (m.matches()) {
			String type = m.group(1).trim();

			Material mat = Material.matchMaterial(type);
			if (mat != null)
				ret.id = mat.getId();
			else {
				throw new BadItemException("Unknown material '" + type + "'");
			}

			try {
				ret.data = Byte.parseByte(m.group(2));
			}
			catch (NumberFormatException e) {}
			try {
				ret.damage = Short.parseShort(m.group(3));
			}
			catch (NumberFormatException e) {}
			try {
				ret.amount = Integer.parseInt(m.group(4));
			}
			catch (NumberFormatException e) {}
			
			return ret;
		}
		else {
			throw new BadItemException("Unknown item '" + itemStr + "'");
		}
	}
}
