package com.echo28.bukkit.craftdispenser.crafts;

import org.bukkit.util.config.Configuration;

import com.echo28.bukkit.craftdispenser.BadItemException;
import com.echo28.bukkit.craftdispenser.CraftDispenser;
import com.echo28.bukkit.craftdispenser.ItemSpec;



public class CraftConfig extends Craft
{
	private String name = "";
	private ItemSpec[] items = null;
	private ItemSpec[] craftItems = null;
	private ItemSpec[] outputItems = null;
	private boolean vertical = false;

	public CraftConfig(CraftDispenser plugin, Configuration config, String name) throws BadItemException
	{
		super(plugin);

		this.name = name;

		try
		{
			loadConfig(config);
		}
		catch (BadItemException e)
		{
			log.severe(e.getMessage() + " in file '" + this.name + "'");
			throw e;
		}
	}

	@Override
	public boolean make()
	{
		if ((vertical && checkVerticalItems(items)) || (!vertical && checkCustomItems(items)))
		{
			// log.info("matched config "+name);

			dispenseItems(ItemSpec.createItemStacks(craftItems));

			if (outputItems != null && outputItems.length == 9)
			{
				for (int i = 0; i < 9; i++)
				{
					ItemSpec item = outputItems[i];
					if (item.id != 0 && item.id != -1)
					{
						inventory.setItem(i, item.createItemStack());
					}
				}
			}

			return true;
		}
		return false;
	}

	private void loadConfig(Configuration config) throws BadItemException
	{
		if (config.getProperty("input-items-vertical") != null)
		{
			vertical = true;
			items = ItemSpec.parseItems(config.getStringList("input-items-vertical", null));

			if (items.length != 2 && items.length != 3) throw new BadItemException(
			        "There must be 2 or 3 items listed under input-items-vertical: not "
			                + Integer.toString(items.length));
		}
		else if (config.getProperty("input-items") != null)
		{
			vertical = false;
			items = ItemSpec.parseItems(config.getStringList("input-items", null));

			if (items.length != 9) throw new BadItemException("There must be 9 items listed under input-items: not "
			                                                  + Integer.toString(items.length));

		}
		else
		{
			throw new BadItemException("Missing input-items: list or input-items-vertical: list");
		}
		craftItems = ItemSpec.parseItems(config.getStringList("craft", null));
		if (craftItems.length == 0) throw new BadItemException("Missing craft: list");
		outputItems = ItemSpec.parseItems(config.getStringList("output-items", null));
	}
}
