package com.echo28.bukkit.craftdispenser.crafts;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.echo28.bukkit.craftdispenser.CraftDispenser;
import com.echo28.bukkit.craftdispenser.ItemSpec;



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

	protected boolean checkVerticalItems(ItemSpec[] items)
	{
		if (countAir() != (9 - items.length)) return false;

		if (items.length == 2)
		{
			for (int i = 0; i < 6; i++)
			{
				if (items[0].matchesItemStack(inventory.getItem(i))
				    && items[1].matchesItemStack(inventory.getItem(i + 3)))
				{
					subtractItem(i, items[0].amount);
					subtractItem(i + 3, items[1].amount);
					return true;
				}
			}
			return false;
		}
		else if (items.length == 3)
		{
			for (int i = 0; i < 3; i++)
			{
				if (items[0].matchesItemStack(inventory.getItem(i))
				    && items[1].matchesItemStack(inventory.getItem(i + 3))
				    && items[2].matchesItemStack(inventory.getItem(i + 6)))
				{
					subtractItem(i, items[0].amount);
					subtractItem(i + 3, items[1].amount);
					subtractItem(i + 6, items[2].amount);
					return true;
				}
			}
			return false;
		}

		return false;
	}

	protected boolean checkCustomItems(ItemSpec[] items)
	{
		int[] slotsToSubtract = new int[9];
		for (int i = 0; i < 9; i++)
		{
			ItemSpec item = items[i];

			if (item == null) // This shouldn't happen anymore, but just in case
			return false;

			ItemStack itemStack = inventory.getItem(i);

			if (item.id == 0 && (itemStack == null || itemStack.getTypeId() == 0)) continue;

			if (item.matchesItemStack(itemStack)) slotsToSubtract[i] = item.amount;
			else return false;
		}

		subtractItems(slotsToSubtract);
		return true;
	}

	public void subtractItems(int[] slots)
	{
		for (int slot = 0; slot < 9; slot++)
		{
			subtractItem(slot, slots[slot]);
		}
	}

	public void subtractItem(int slot, int howMuch)
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
		else if (amount == 0) items = null;
		else items.setAmount(amount);

		inventory.setItem(slot, items);
	}

	public void dispenseItems(ItemStack dispenseItem)
	{
		dispenseItems(new ItemStack[]
		{ dispenseItem });
	}

	public void dispenseItems(ItemStack[] dispenseItems)
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

		inventory.clear();
		setContents(dispenseContents);
		for (int i = 0; i < totalItems; i++)
		{
			dispenser.dispense();
		}
		inventory.clear();
		setContents(contents);
	}

	protected ItemStack[] getContents()
	{
		ItemStack[] contents = new ItemStack[9];
		for (int i = 0; i < 9; i++)
		{
			contents[i] = inventory.getItem(i);
		}
		return contents;
	}

	protected void setContents(ItemStack[] contents)
	{
		int i = 0;
		for (ItemStack item : contents)
		{
			if ((item != null) && (item.getType().name().equalsIgnoreCase("AIR")))
			{
				item = null;
			}
			inventory.setItem(i, item);
			i++;
		}
	}
}
