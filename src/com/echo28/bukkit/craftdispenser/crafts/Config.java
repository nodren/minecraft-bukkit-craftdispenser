package com.echo28.bukkit.craftdispenser.crafts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
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
				String itemStr = config.getString("craft.item", "Missing crafted-item");
				int[] item = Items.validate(itemStr);
				if (item[0] == -1) {
					System.out.printf("[CraftDispenser] Invalid crafted-item: %s", itemStr);
					return false;
				}
				int amount = config.getInt("craft.amount", 1);
				short damage = (short)config.getInt("craft.damage", 0);
				Byte data = null;
				String dataStr = config.getString("craft.data", "null");
				if (dataStr != null && !dataStr.toLowerCase().equals("null") && !dataStr.toLowerCase().equals("none"))
					data = new Byte((byte)Integer.parseInt(dataStr));
				
				//System.out.printf("crafting %s %d #%d %d %d\n", itemStr, item[0], amount, damage, data);
				
				ItemStack itemStack = new ItemStack(item[0], amount, damage, data);
				
				//System.out.printf("'%s'\n", itemStack);
				
				CraftDispenser.dispenseItems(block, itemStack);
				
				
				int[][] outputItems = parseItemList(config.getStringList("output-items", null));
				if (outputItems != null && outputItems.length == 9) {
					for (int i = 0; i < 9; i++) {
						item = outputItems[i];
						if (item[0] != 0 && item[0] != -1) {
							if (item[3] == -1)
								data = null;
							else
								data = new Byte((byte)item[3]);
							
							inventory.setItem(i, new ItemStack(item[0], item[2], (short)0, data));
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
			return checkVerticalItems(parseItemList(config.getStringList("input-items-vertical", null)));
		}
		if (config.getProperty("input-items") != null) {
			return checkCustomItems(parseItemList(config.getStringList("input-items", null)));
		}
		return false;
	}
}
