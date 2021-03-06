package mariculture.core.items;

import mariculture.core.Core;
import mariculture.core.helpers.cofh.BlockHelper;
import mariculture.core.lib.CraftingMeta;
import mariculture.core.lib.MachineRenderedMeta;
import mariculture.core.tile.TileAnvil;
import mariculture.core.util.IHasGUI;
import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.Multimap;

public class ItemHammer extends ItemDamageable {
	private ToolMaterial material;
	
	public ItemHammer(ToolMaterial brick) {
		super(brick.getMaxUses());
		this.material = brick;
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		Block block = world.getBlock(x, y, z);
		if(block == Blocks.bed) return false;
		if(BlockHelper.canRotate(block)) {
			int meta = world.getBlockMetadata(x, y, z);
			if(player.isSneaking()) {
				world.setBlockMetadataWithNotify(x, y, z, BlockHelper.rotateVanillaBlockAlt(world, Block.getIdFromBlock(block), meta, x, y, z), 3);
			} else {
				world.setBlockMetadataWithNotify(x, y, z, BlockHelper.rotateVanillaBlock(world, Block.getIdFromBlock(block), meta, x, y, z), 3);
			}
		}
		
		if(block != null) {
			if(block == Core.renderedMachines && world.getBlockMetadata(x, y, z) == MachineRenderedMeta.ANVIL && !player.isSneaking()) return false;
			if(world.getTileEntity(x, y, z) instanceof IHasGUI && !player.isSneaking()) return false;
			if(block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side)))
				return !world.isRemote;
		}
		
		return false;
	}
	
	public String getToolMaterialName() {
		return material.toString();
	}
	
	public int getItemEnchantability() {
		return 10;
	}

	@Override
	public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
		return !(world.getTileEntity(x, y, z) instanceof TileAnvil);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {
		return stack2.getItem() == Core.crafting && stack2.getItemDamage() == CraftingMeta.BURNT_BRICK;
	}
	
	@Override
	public Multimap getItemAttributeModifiers() {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)5.0F, 0));
        return multimap;
    }
}
