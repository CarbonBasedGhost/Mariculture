package mariculture.plugins.enchiridion;

import mariculture.core.helpers.NBTHelper;
import mariculture.core.helpers.SpawnItemHelper;
import mariculture.plugins.PluginEnchiridion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class BookSpawnHelper {
	//Reads the nbt of a player and sets if this book has been collected or not
	public static void spawn(EntityPlayer player, int meta) {
		if(!NBTHelper.getPlayerData(player).hasKey("BookCollected" + meta)) {
			NBTHelper.getPlayerData(player).setBoolean("BookCollected" + meta, true);
			ItemStack book = new ItemStack(PluginEnchiridion.guides, 1, meta);
			if(!player.worldObj.isRemote) {
				SpawnItemHelper.spawnItem(player.worldObj, (int)player.posX, (int)player.posY + 1, (int)player.posZ, book);
			}
		}
	}
}
