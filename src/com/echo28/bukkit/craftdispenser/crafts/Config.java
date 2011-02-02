package com.echo28.bukkit.craftdispenser.crafts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.util.config.Configuration;

import com.echo28.bukkit.craftdispenser.CraftDispenser;
import com.echo28.bukkit.craftdispenser.Items;


public class Config extends Craft
{
	private List<Configuration> configs = new ArrayList<Configuration>();

	public Config(CraftDispenser plugin, Block block)
	{
		super(plugin, block);

		loadCustom();
	}

	private void loadCustom()
	{
		for (File file : plugin.getDataFolder().listFiles())
		{
			if (file.getName().equalsIgnoreCase("config.yml"))
				continue;
			
			Configuration config = new Configuration(file);
			config.load();
			configs.add(config);
		}
	}

	@Override
	public boolean make()
	{
		for (Configuration config : configs)
		{
			if (checkConfig(config))
			{
				int[][] items = Items.parseItemList(config.getStringList("craft", null));
				
				CraftDispenser.dispenseItems(block, Items.createItemStacks(items));
				
				
				int[][] outputItems = Items.parseItemList(config.getStringList("output-items", null));
				if (outputItems != null && outputItems.length == 9) {
					for (int i = 0; i < 9; i++) {
						int[] item = outputItems[i];
						if (item[0] != 0 && item[0] != -1) {
							inventory.setItem(i, Items.createItemStack(item));
						}
					}
				}
				
				return true;
			}
		}
		return false;
	}

	private boolean checkConfig(Configuration config)
	{
		if (config.getProperty("input-items-vertical") != null) {
			return checkVerticalItems(Items.parseItemList(config.getStringList("input-items-vertical", null)));
		}
		if (config.getProperty("input-items") != null) {
			return checkCustomItems(Items.parseItemList(config.getStringList("input-items", null)));
		}
		return false;
	}
}
