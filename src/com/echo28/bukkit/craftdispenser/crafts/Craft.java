package com.echo28.bukkit.craftdispenser.crafts;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.echo28.bukkit.craftdispenser.CraftDispenser;


abstract public class Craft
{
	protected CraftDispenser plugin;
	protected Block block = null;
	protected Dispenser dispenser = null;
	protected Inventory inventory;
	protected final Logger log = Logger.getLogger("minecraft");

	public Craft(CraftDispenser plugin)
	{
		this.plugin = plugin;
	}

	public void load(Block block)
	{
		this.block = block;
		this.dispenser = (Dispenser) this.block.getState();
		this.inventory = this.dispenser.getInventory();
	}

	public boolean make(Block block)
	{
		load(block);
		return make();
	}

	abstract public boolean make();

	public int countAir()
	{
		int airCount = 0;
		for (int i = 0; i < 9; i++)
		{
			if (inventory.getItem(i).getType() == Material.AIR) airCount += 1;
		}
		return airCount;
	}

	protected boolean checkVerticalItems(int[][] items)
	{
		if (countAir() != (9 - items.length)) return false;

		if (items.length == 2)
		{
			for (int i = 0; i < 6; i++)
			{
				if (itemMatchesStack(items[0], inventory.getItem(i)) && itemMatchesStack(items[1], inventory.getItem(i + 3)))
				{
					newSubtractItem(i, items[0][3]);
					newSubtractItem(i + 3, items[1][3]);
					return true;
				}
			}
			return false;
		}
		else if (items.length == 3)
		{
			for (int i = 0; i < 3; i++)
			{
				if (itemMatchesStack(items[0], inventory.getItem(i)) && itemMatchesStack(items[1], inventory.getItem(i + 3))
						&& itemMatchesStack(items[2], inventory.getItem(i + 6)))
				{
					newSubtractItem(i, items[0][3]);
					newSubtractItem(i + 3, items[1][3]);
					newSubtractItem(i + 6, items[2][3]);
					return true;
				}
			}
			return false;
		}

		return false;
	}

	protected boolean checkCustomItems(int[][] items)
	{
		int[] slotsToSubtract = new int[9];
		for (int i = 0; i < 9; i++)
		{
			int[] item = items[i];

			if (item[0] == 0) continue;

			if (itemMatchesStack(item, inventory.getItem(i)))
				slotsToSubtract[i] = item[3];
			else
				return false;
		}

		newSubtractItems(slotsToSubtract);
		return true;
	}

	public void newSubtractItems(int[] slots)
	{
		for (int slot = 0; slot < 9; slot++)
		{
			newSubtractItem(slot, slots[slot]);
		}
	}

	public void newSubtractItem(int slot, int howMuch)
	{
		if (howMuch == 0) return;
		ItemStack items = inventory.getItem(slot);
		int amount = items.getAmount() - howMuch;
		if (amount < 0)
		{
			System.out.printf("%d %d %s", amount, howMuch, items.toString());
			new Exception().printStackTrace();
			items = null;
		}
		else if (amount == 0)
			items = null;
		else
			items.setAmount(amount);

		inventory.setItem(slot, items);
	}

	public void dispenseItems(Block block, ItemStack dispenseItem)
	{
		dispenseItems(block, new ItemStack[]
		{ dispenseItem });
	}

	public void dispenseItems(Block block, ItemStack[] dispenseItems)
	{
		Dispenser cd = (Dispenser) block.getState();
		ItemStack[] contents = cd.getInventory().getContents();
		ItemStack[] dispenseContents = new ItemStack[9];

		int totalItems = 0;
		for (int i = 0; i < dispenseItems.length; i++)
		{
			totalItems += dispenseItems[i].getAmount();
			dispenseContents[i] = dispenseItems[i];
		}

		cd.getInventory().setContents(dispenseContents);
		for (int i = 0; i < totalItems; i++)
			cd.dispense();
		cd.getInventory().setContents(contents);
	}

	public int[][] parseItems(List<String> itemsList)
	{
		int[][] items = new int[itemsList.size()][];

		for (int i = 0; i < itemsList.size(); i++)
		{
			items[i] = parseItem(itemsList.get(i));
		}
		return items;
	}

	private static Pattern itemPattern = Pattern.compile("([a-zA-Z ]+|\\d+)(?:\\s*\\.\\s*(\\d+))?(?:\\s*,\\s*(\\d+))?(?:\\s*\\*\\s*(\\d+))?");

	public int[] parseItem(String itemStr)
	{
		Matcher m = itemPattern.matcher(itemStr);

		int[] ret = new int[]
		{ -1, -1, 0, 1 };

		if (m.matches())
		{
			String type = m.group(1);
			try
			{
				ret[0] = Integer.parseInt(type);
			}
			catch (NumberFormatException e)
			{
				type = type.toLowerCase().replaceAll("\\s+", "");
				Material mat = Material.matchMaterial(type);
				if (mat != null)
				{
					ret[0] = mat.getId();
				}
				else
				{
					log.severe("Fail " + type);
				}
			}
			for (int i = 1; i < 4; i++)
			{
				try
				{
					ret[i] = Integer.parseInt(m.group(i + 1));
				}
				catch (NumberFormatException e)
				{
					// do nothing
				}
			}
		}
		else
		{
			log.severe("Unknown item " + itemStr);
		}

		return ret;
	}

	public static boolean itemMatchesStack(int[] item, ItemStack itemstack)
	{
		if (itemstack.getTypeId() == item[0] && itemstack.getAmount() >= item[3])
		{
			MaterialData matdat = itemstack.getData();
			if (matdat == null || matdat.getData() == item[1]) return true;
		}
		return false;
	}

	public ItemStack createItemStack(int[] item)
	{
		return new ItemStack(item[0], item[3], (short) item[2], item[1] == -1 ? null : new Byte((byte) item[1]));
	}

	public ItemStack[] createItemStacks(int[][] items)
	{
		ItemStack[] itemstacks = new ItemStack[items.length];

		for (int i = 0; i < items.length; i++)
			itemstacks[i] = createItemStack(items[i]);

		return itemstacks;
	}
}
