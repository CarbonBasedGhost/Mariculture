package mariculture.fishery.fish;

import static mariculture.api.core.Environment.Salinity.SALINE;
import static mariculture.core.lib.ItemLib.dropletRegen;
import static mariculture.core.lib.ItemLib.dropletWater;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mariculture.api.core.Environment.Height;
import mariculture.api.core.Environment.Salinity;
import mariculture.api.core.Environment.Time;
import mariculture.api.fishery.CachedCoords;
import mariculture.api.fishery.RodType;
import mariculture.api.fishery.fish.FishSpecies;
import mariculture.core.helpers.cofh.InventoryHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class FishAngel extends FishSpecies {
	public FishAngel(int id) {
		super(id);
	}
	
	@Override
	public int[] setSuitableTemperature() {
		return new int[] { 19, 27 };
	}
	
	@Override
	public Salinity[] setSuitableSalinity() {
		return new Salinity[] { SALINE };
	}

	@Override
	public boolean isDominant() {
		return false;
	}

	@Override
	public int getLifeSpan() {
		return 10;
	}

	@Override
	public int getFertility() {
		return 500;
	}

	@Override
	public int getWaterRequired() {
		return 30;
	}

	@Override
	public void addFishProducts() {
		addProduct(dropletWater, 1.5D);
		addProduct(dropletRegen, 2.0D);
	}

	@Override
	public double getFishOilVolume() {
		return 1.6D;
	}

	@Override
	public ItemStack onRightClick(World world, EntityPlayer player, ItemStack stack, Random rand) {
		if (!world.isRemote) {
			List list = world.getEntitiesWithinAABB(EntityItem.class, player.boundingBox.expand(64D, 64D, 64D));
			for (Object i : list) {
				((EntityItem) i).setPosition(player.posX, player.posY, player.posZ);
			}
		}
		
		return stack;
	}

	@Override
	public void affectWorld(World world, int x, int y, int z, ArrayList<CachedCoords> coords) {
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(x, y - 1, z);
			if(tile instanceof IInventory) {
				List list = world.getEntitiesWithinAABB(EntityItem.class, Blocks.stone.getCollisionBoundingBoxFromPool(world, x, y - 1, z).expand(16D, 16D, 16D));
				for (Object i : list) {
					EntityItem item = (EntityItem) i;
					ItemStack stack = item.getEntityItem();
					ItemStack newStack = InventoryHelper.insertItemStackIntoInventory((IInventory)tile, stack, ForgeDirection.DOWN.ordinal());
					if(newStack == null) item.setDead();
					else item.setEntityItemStack(newStack);
				}
			}
		}
	}

	@Override
	public void affectLiving(EntityLivingBase entity) {
		if(entity.getHealth() < 1) entity.heal(1);
	}

	@Override
	public boolean canWork(int time) {
		return !Time.isMidnight(time);
	}

	@Override
	public RodType getRodNeeded() {
		return RodType.GOOD;
	}

	@Override
	public double getCatchChance(int height, int time) {
		return Time.isDawn(time)? 45D: Time.isNoon(time)? 15D: 0D;
	}

	@Override
	public double getCaughtAliveChance(int height, int time) {
		return Time.isDawn(time) && Height.isShallows(height)? 5D: 0D;
	}
}
