package com.echo28.bukkit.craftdispenser.crafts;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.echo28.bukkit.craftdispenser.CraftDispenser;


abstract public class Craft
{
	protected CraftDispenser plugin;
	protected Block block = null;
	protected Dispenser dispenser = null;
	protected Inventory inventory;

	public Craft(CraftDispenser plugin, Block block)
	{
		this.plugin = plugin;
		this.block = (Block) block;
		this.dispenser = (Dispenser) this.block.getState();
		this.inventory = this.dispenser.getInventory();
	}

	abstract public boolean make();

	public boolean isAirExcept(Integer[] slots)
	{
		List<Integer> ints = Arrays.asList(slots);

		for (int i = 0; i < 9; i++)
		{
			if (ints.contains(i))
			{
				continue;
			}
			if (inventory.getItem(i).getType() != Material.AIR) { return false; }
		}
		return true;
	}

	public boolean isAir(Integer[] slots)
	{
		return isMaterial(slots, Material.AIR);
	}

	public boolean isMaterial(Integer[] slots, Material mat)
	{
		for (int slot : slots)
		{
			if (inventory.getItem(slot).getType() != mat) { return false; }
		}
		return true;
	}

	public boolean craftUpToDown(Material[] mats)
	{
		if (mats.length == 1) { return inventory.contains(mats[0]); }
		if (mats.length == 2) { return craftUpToDownTwo(mats); }
		if (mats.length != 3) { return false; }
		for (int i = 0; i <= 2; i++)
		{
			if (inventory.getItem(i).getType() == mats[0])
			{
				if (inventory.getItem(i + 3).getType() == mats[1])
				{
					if (inventory.getItem(i + 6).getType() == mats[2])
					{
						Integer[] slots =
						{ i, i + 3, i + 6 };
						if (isAirExcept(slots))
						{
							subtractItem(i);
							subtractItem(i + 3);
							subtractItem(i + 6);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean craftUpToDownTwo(Material[] mats)
	{
		Integer[] topRow =
		{ 0, 1, 2 };
		if (isAir(topRow))
		{
			for (int i = 0; i <= 1; i++)
			{
				if (inventory.getItem(i + 3).getType() == mats[0])
				{
					if (inventory.getItem(i + 6).getType() == mats[1])
					{
						Integer[] slots =
						{ i + 3, i + 6 };
						if (isAirExcept(slots))
						{
							subtractItem(i + 3);
							subtractItem(i + 6);
							return true;
						}
					}
				}
			}
		}
		else
		{
			for (int i = 0; i <= 1; i++)
			{
				if (inventory.getItem(i).getType() == mats[0])
				{
					if (inventory.getItem(i + 3).getType() == mats[1])
					{
						Integer[] slots =
						{ i, i + 3 };
						if (isAirExcept(slots))
						{
							subtractItem(i);
							subtractItem(i + 3);
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public void subtractItems(Integer[] slots)
	{
		for (int slot : slots)
		{
			subtractItem(slot);
		}
	}

	public void subtractItem(int slot)
	{
		ItemStack items;
		items = inventory.getItem(slot);
		if (items.getAmount() == 1)
		{
			items = null;
		}
		else if (items.getAmount() > 1)
		{
			items.setAmount(items.getAmount() - 1);
		}
		inventory.setItem(slot, items);
	}

	public void emptyBucket(int slot)
	{
		ItemStack items;
		items = inventory.getItem(slot);
		if ((items.getType() == Material.LAVA_BUCKET) || (items.getType() == Material.WATER_BUCKET) || (items.getType() == Material.MILK_BUCKET))
		{
			items = new ItemStack(Material.BUCKET, 1);
			inventory.setItem(slot, items);
		}
	}
}
