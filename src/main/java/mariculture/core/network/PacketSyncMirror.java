package mariculture.core.network;

import mariculture.api.core.MaricultureHandlers;
import mariculture.magic.MirrorHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;

public class PacketSyncMirror extends PacketNBT {
	public PacketSyncMirror() {}
	public PacketSyncMirror(ItemStack[] inventory) {
		nbt = new NBTTagCompound();
		nbt.setInteger("length", inventory.length);
		
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inventory.length; i++) {
			ItemStack stack = inventory[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				tag.setBoolean("NULLItemStack", false);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			} else {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				tag.setBoolean("NULLItemStack", true);
				itemList.appendTag(tag);
			}
		}
		
		nbt.setTag("Inventory", itemList);
	}
	
	@Override
	public void handle(Side side, EntityPlayer player) {
		World world = player.worldObj;
		int length = nbt.getInteger("length");
		
		ItemStack[] inventory = MaricultureHandlers.mirror.getMirrorContents(player);
		NBTTagList tagList = nbt.getTagList("Inventory", 10);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if(tag.getBoolean("NULLItemStack") == true) {
				inventory[slot] = null;
			} else if (slot >= 0 && slot < inventory.length) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		
		MirrorHelper.saveClient(player, inventory);
	}
}
