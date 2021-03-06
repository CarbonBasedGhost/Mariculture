package mariculture.core;

import static mariculture.core.lib.ItemLib.blockSnow;
import static mariculture.core.lib.ItemLib.clay;
import static mariculture.core.lib.ItemLib.dustMagnesium;
import static mariculture.core.lib.ItemLib.dustSalt;
import static mariculture.core.lib.ItemLib.glass;
import static mariculture.core.lib.ItemLib.glassPane;
import static mariculture.core.lib.ItemLib.ice;
import static mariculture.core.lib.ItemLib.ingotMagnesium;
import static mariculture.core.lib.ItemLib.ingotRutile;
import static mariculture.core.lib.ItemLib.limestone;
import static mariculture.core.lib.ItemLib.limestoneSmooth;
import static mariculture.core.lib.ItemLib.obsidian;
import static mariculture.core.lib.ItemLib.salt;
import static mariculture.core.lib.ItemLib.sand;
import static mariculture.core.lib.ItemLib.stone;

import java.util.ArrayList;

import mariculture.api.core.FuelInfo;
import mariculture.api.core.MaricultureHandlers;
import mariculture.api.core.RecipeSmelter;
import mariculture.core.helpers.RecipeHelper;
import mariculture.core.lib.GlassMeta;
import mariculture.core.lib.MaterialsMeta;
import mariculture.core.lib.MetalRates;
import mariculture.core.lib.TransparentMeta;
import mariculture.core.util.Fluids;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipesSmelting {
	public static int iron = 1538;
	public static int gold = 1064;
	public static int tin = 232;
	public static int copper = 1085;
	public static int silver = 962;
	public static int lead = 328;
	public static int magnesium = 650;
	public static int nickel = 1455;
	public static int bronze = 950;
	public static int steel = 1370;
	public static int aluminum = 660;
	public static int titanium = 1662;
	public static int electrum = 1000;
		
	public static void addRecipe(String fluid, int[] volume, Object[] items, int temperature, ItemStack output, int chance) {
		String origFluid = fluid;
		for(int i = 0; i < items.length; i++) {
			if(items[i] != null && volume[i] > 0) {
				Object item = items[i];
				ItemStack stack = null;
				if(item instanceof String) {
					if(OreDictionary.getOres((String)item).size() > 0) {
						stack = OreDictionary.getOres((String) item).get(0);
					}
				} else if(item instanceof ItemStack) {
					stack = (ItemStack) item;
				} else if (item instanceof Item) {
					stack = new ItemStack((Item)item);
				} else if(item instanceof Block) {
					stack = new ItemStack((Block)item);
				}
				
				if(stack != null && FluidRegistry.getFluid(fluid) != null) {
					if(i == 0 || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemHoe) {
	 					RecipeHelper.addMelting(stack, temperature, FluidRegistry.getFluidStack(fluid, volume[i]), output, chance);
					} else {
						RecipeHelper.addMelting(stack, temperature, FluidRegistry.getFluidStack(fluid, volume[i]));
					}
				}
			}
		}
	}

	public static void add() {
		 addFuels();
		 addMetalRecipes();
	}
	
	public static void postAdd() {
		ItemStack sulfur = fetchItem(new String[] { "dustSulfur", "crystalSulfur" });
		ItemStack salt = new ItemStack(Core.materials, 1, MaterialsMeta.DUST_SALT);
		ItemStack silicon = fetchItem(new String[] { "itemSilicon", "dustSiliconDioxide" });
		ItemStack platinum = fetchItem(new String[] { "dustPlatinum", "ingotPlatinum" });
		
		//Copperous Dust
		LinkedMetal[] coppers = new LinkedMetal[] {
				new LinkedMetal("ingotIron", Fluids.iron, 4),
				new LinkedMetal("ingotSilver", Fluids.silver, 7),
				new LinkedMetal("ingotGold", Fluids.gold, 10),
				new LinkedMetal("ingotCobalt", Fluids.cobalt, 15),
				new LinkedMetal("ingotNickel", Fluids.nickel, 8),
				new LinkedMetal("ingotLead", Fluids.lead, 7),
				new LinkedMetal("ingotTin", Fluids.tin, 6)};
		addDust(MaterialsMeta.DUST_COPPEROUS, copper, sulfur, 10, coppers);
		
		//Golden Dust
		LinkedMetal[] golds = new LinkedMetal[] {
				new LinkedMetal("ingotElectrum", Fluids.electrum, 3),
				new LinkedMetal("ingotSilver", Fluids.silver, 7)};
		addDust(MaterialsMeta.DUST_GOLDEN, gold, null, 0, golds);
		
		//Ironic Dust
		LinkedMetal[] irons = new LinkedMetal[] {
				new LinkedMetal("ingotAluminum", Fluids.aluminum, 3),
				new LinkedMetal("ingotTin", Fluids.iron, 8),
				new LinkedMetal("ingotCopper", Fluids.copper, 6)};
		addDust(MaterialsMeta.DUST_IRONIC, iron, silicon, 6, irons);
		
		//Leader Dust
		LinkedMetal[] leads = new LinkedMetal[] {
				new LinkedMetal("ingotSilver", Fluids.silver, 3),
				new LinkedMetal("ingotIron", Fluids.iron, 6),
				new LinkedMetal("ingotCopper", Fluids.copper, 8),
				new LinkedMetal("ingotTin", Fluids.tin, 10)};
		addDust(MaterialsMeta.DUST_LEADER, lead, null, 0, leads);
		
		//Silvery Dust
		LinkedMetal[] silvers = new LinkedMetal[] {
				new LinkedMetal("ingotLead", Fluids.lead, 2),
				new LinkedMetal("ingotElectrum", Fluids.electrum, 4) };
		addDust(MaterialsMeta.DUST_SILVERY, silver, sulfur, 5, silvers);
		
		//Tinnic Dust
		LinkedMetal[] tins = new LinkedMetal[] {
				new LinkedMetal("ingotCopper", Fluids.copper, 3),
				new LinkedMetal("ingotIron", Fluids.iron, 6),
				new LinkedMetal("ingotLead", Fluids.lead, 8)};
		addDust(MaterialsMeta.DUST_TINNIC, tin, sulfur, 7, tins);
		
		addMetal(Fluids.tin, "Tin", tin, new ItemStack(Core.materials, 1, MaterialsMeta.DUST_TINNIC), 10);
		addMetal(Fluids.copper, "Copper", copper, new ItemStack(Core.materials, 1, MaterialsMeta.DUST_COPPEROUS), 10);
		addMetal(Fluids.silver, "Silver", silver, new ItemStack(Core.materials, 1, MaterialsMeta.DUST_SILVERY), 10);
		addMetal(Fluids.lead, "Lead", lead, new ItemStack(Core.materials, 1, MaterialsMeta.DUST_LEADER), 10);
		addMetal(Fluids.nickel, "Nickel", nickel, platinum, 10);
		addMetal(Fluids.bronze, "Bronze", bronze, null, 0);
		addMetal(Fluids.steel, "Steel", steel, null, 0);
		addMetal(Fluids.electrum, "Electrum", electrum, null, 0);
		
		//Gold + Silver = Electrum
		if(OreDictionary.getOres("ingotElectrum").size() > 0 && OreDictionary.getOres("ingotSilver").size() > 0) {
			FluidStack moltenSilver = FluidRegistry.getFluidStack(Fluids.silver, MetalRates.NUGGET);
			FluidStack moltenGold = FluidRegistry.getFluidStack(Fluids.gold, MetalRates.NUGGET);
			FluidStack moltenElectrum = FluidRegistry.getFluidStack(Fluids.electrum, MetalRates.NUGGET * 2);
			RecipeHelper.addFluidAlloy(moltenSilver, moltenGold, moltenElectrum, 1);
			RecipeHelper.addMeltingAlloy(OreDictionary.getOres("ingotSilver").get(0), 
								new ItemStack(Items.gold_ingot), electrum, get(Fluids.electrum, MetalRates.INGOT * 2));
		}
	}
	
	private static class LinkedMetal {
		public String ingot;
		public String fluid;
		public Integer chance;
		
		public LinkedMetal(String ingot, String fluid, Integer chance) {
			this.ingot = ingot;
			this.fluid = fluid;
			this.chance = chance;
		}
	}
	
	private static void addDust(int meta, int temp, ItemStack bonus, int chance, LinkedMetal[] metals) {
		ArrayList<FluidStack> fluids = new ArrayList<FluidStack>();
		ArrayList<Integer> chances = new ArrayList<Integer>();
		
		for(LinkedMetal metal: metals) {
			if(OreDictionary.getOres(metal.ingot).size() > 0 && get(metal.fluid) != null) {
				fluids.add(get(metal.fluid));
				chances.add(metal.chance);
			}
		}
		
		if(fluids.size() > 0) {
			MaricultureHandlers.smelter.addRecipe(new RecipeSmelter(new ItemStack(Core.materials, 1, meta),
					temp, fluids.toArray(new FluidStack[fluids.size()]), chances.toArray(new Integer[chances.size()]), bonus, chance));
		}
	}

	private static ItemStack fetchItem(String[] array) {
		for(String arr: array) {
			if(OreDictionary.getOres(arr).size() > 0)
				return OreDictionary.getOres(arr).get(0);
		}
		
		return null;
	}

	private static void addFuels() {
		RecipeHelper.addFuel("blockCoal", new FuelInfo(2000, 378, 10800));
		RecipeHelper.addFuel(new ItemStack(Items.coal, 1, 0), new FuelInfo(2000, 42, 1200));
		RecipeHelper.addFuel(new ItemStack(Items.coal, 1, 1), new FuelInfo(1600, 32, 900));
		RecipeHelper.addFuel("logWood", new FuelInfo(480, 12, 300));
		RecipeHelper.addFuel("plankWood", new FuelInfo(320, 8, 200));
		RecipeHelper.addFuel("stickWood", new FuelInfo(160, 4, 100));
		RecipeHelper.addFuel(Fluids.natural_gas, new FuelInfo(2000, 35, 1200));
		RecipeHelper.addFuel("gascraft_naturalgas", new FuelInfo(2000, 35, 1000));
		RecipeHelper.addFuel("fuel", new FuelInfo(2000, 35, 1000));
		RecipeHelper.addFuel("pyrotheum", new FuelInfo(2000, 100, 100));
		RecipeHelper.addFuel("coal", new FuelInfo(2000, 40, 300));
		RecipeHelper.addFuel("biofuel", new FuelInfo(1800, 20, 2000));
		RecipeHelper.addFuel("oil", new FuelInfo(1750, 20, 400));
		RecipeHelper.addFuel("lava", new FuelInfo(1500, 20, 360));
		RecipeHelper.addFuel("biomass", new FuelInfo(1500, 10, 1800));
		RecipeHelper.addFuel("bioethanol", new FuelInfo(1500, 10, 1800));
	}
	
	public static void addFullSet(String fluid, Object[] items, int temp, ItemStack output, int chance) {
		addRecipe(fluid, MetalRates.MATERIALS, new Object[] { 
				items[0], items[1], items[2], items[3], items[4] }, temp, output, chance);
		
		addRecipe(fluid, MetalRates.TOOLS, new Object[] { 
				items[5], items[6], items[7], items[8], items[9] }, temp, new ItemStack(Items.stick), 1);
		
		addRecipe(fluid, MetalRates.ARMOR, new Object[] { 
				items[10], items[11], items[12], items[13] }, temp, null, 0);
	}
	
	public static void addMetal(String fluid, String metal, int temp, ItemStack bonus, int chance) {
		addRecipe(fluid, MetalRates.MATERIALS, new Object[] { 
				"ore" + metal, "nugget" + metal, "ingot" + metal, "block" + metal, "dust" + metal }, temp, bonus, chance);
		
		if(OreDictionary.getOres("ingot" + metal).size() > 0) {
			RecipeHelper.addMetalCasting(fluid, metal);
		}
	}

	
	public static void addMetalRecipes() {
		addFullSet(Fluids.iron, new Object[] {
				"oreIron", "nuggetIron", "ingotIron", "blockIron", "dustIron",
				Items.iron_pickaxe, Items.iron_shovel, Items.iron_axe, Items.iron_sword, Items.iron_hoe,
				Items.iron_helmet, Items.iron_chestplate, Items.iron_leggings, Items.iron_boots}, iron, 
				new ItemStack(Core.materials, 1, MaterialsMeta.DUST_IRONIC), 10);
		RecipeHelper.addMetalCasting(Fluids.iron, "Iron");
		
		addFullSet(Fluids.gold, new Object[] {
				"oreGold", "nugetGold", "ingotGold", "blockGold", "dustGold",
				Items.golden_pickaxe, Items.golden_shovel, Items.golden_axe, Items.golden_sword, Items.golden_hoe,
				Items.golden_helmet, Items.golden_chestplate, Items.golden_leggings, Items.golden_boots}, gold, 
				new ItemStack(Core.materials, 1, MaterialsMeta.DUST_GOLDEN), 10);
		RecipeHelper.addMetalCasting(Fluids.gold, "Gold");
		
		addMetal(Fluids.aluminum, "Aluminum", aluminum, new ItemStack(clay), 5);
		addMetal(Fluids.rutile, "Rutile", titanium, limestone, 2);
		addMetal(Fluids.titanium, "Titanium", titanium, limestone, 2);
		addMetal(Fluids.magnesium, "Magnesium", magnesium, new ItemStack(stone), 2);
		addMetal(Fluids.aluminum, "Aluminum", aluminum, new ItemStack(clay), 5);
		
		FluidStack moltenRutile = FluidRegistry.getFluidStack(Fluids.rutile, MetalRates.INGOT);
		FluidStack moltenMagnesium = FluidRegistry.getFluidStack(Fluids.magnesium, MetalRates.INGOT);
		FluidStack moltenTitanium = FluidRegistry.getFluidStack(Fluids.titanium, MetalRates.INGOT);
		RecipeHelper.addFluidAlloy(moltenRutile, moltenMagnesium, moltenTitanium, 6);
		RecipeHelper.addMeltingAlloy(ingotRutile, ingotMagnesium, titanium, get(Fluids.titanium));
		RecipeHelper.addMelting(dustMagnesium, magnesium, get(Fluids.magnesium), salt, 1);
		
		//Gold Back
		RecipeHelper.addMelting(new ItemStack(Blocks.light_weighted_pressure_plate), gold, gold(MetalRates.INGOT * 2));
		RecipeHelper.addMelting(new ItemStack(Items.clock), gold, gold(MetalRates.INGOT * 4), new ItemStack(Items.redstone), 2);
		RecipeHelper.addMelting(new ItemStack(Items.golden_horse_armor), gold, gold(MetalRates.INGOT * 6), new ItemStack(Items.saddle), 4);
		
		//Iron Back
		RecipeHelper.addMelting(new ItemStack(Items.bucket), iron, Fluids.iron, MetalRates.INGOT * 3);
		RecipeHelper.addMelting(new ItemStack(Items.iron_door), iron, Fluids.iron, MetalRates.INGOT * 6);
		RecipeHelper.addMelting(new ItemStack(Blocks.iron_bars), iron, Fluids.iron, (int) (MetalRates.INGOT * 0.25));
		RecipeHelper.addMelting(new ItemStack(Items.shears), iron, Fluids.iron, MetalRates.INGOT * 2);
		RecipeHelper.addMelting(new ItemStack(Blocks.anvil, 1, 0), iron, Fluids.iron, MetalRates.INGOT * 31);
		RecipeHelper.addMelting(new ItemStack(Blocks.anvil, 1, 1), iron, Fluids.iron, MetalRates.INGOT * 22);
		RecipeHelper.addMelting(new ItemStack(Blocks.anvil, 1, 2), iron, Fluids.iron, MetalRates.INGOT * 13);
		RecipeHelper.addMelting(new ItemStack(Blocks.heavy_weighted_pressure_plate), iron, Fluids.iron, MetalRates.INGOT * 2);
		RecipeHelper.addMelting(new ItemStack(Items.compass), iron, iron(MetalRates.INGOT * 4), new ItemStack(Items.redstone), 2);	
		RecipeHelper.addMelting(new ItemStack(Blocks.hopper), iron, iron(MetalRates.INGOT * 5), new ItemStack(Blocks.chest), 2);
		RecipeHelper.addMelting(new ItemStack(Items.flint_and_steel), iron, iron(MetalRates.INGOT));
		RecipeHelper.addMelting(new ItemStack(Items.iron_horse_armor), iron, iron(MetalRates.INGOT * 6), new ItemStack(Items.saddle), 4);
		
		//Glass, Ice, Snow, Plastic, Obisidian
				RecipeHelper.addBlockCasting(get(Fluids.glass, 1000), new ItemStack(glass));
				RecipeHelper.addMelting(new ItemStack(sand), 1000, Fluids.glass, 1000);
				RecipeHelper.addMelting(new ItemStack(glass), 900, Fluids.glass, 1000);
				RecipeHelper.addMelting(new ItemStack(glassPane), 500, Fluids.glass, 375);
				RecipeHelper.addMelting(new ItemStack(ice), 1, "water", 1000);
				RecipeHelper.addMelting(new ItemStack(blockSnow), 1, "water", 1000);
				
				RecipeHelper.addVatItemRecipe(new ItemStack(glass), Fluids.natural_gas, 1000, new ItemStack(Core.transparent, 1, TransparentMeta.PLASTIC), 5);
				if(FluidRegistry.getFluid("bioethanol") != null) {
					RecipeHelper.addVatItemRecipe(new ItemStack(glass), "bioethanol", 2500, new ItemStack(Core.transparent, 1, TransparentMeta.PLASTIC), 10);
				}
				
				RecipeHelper.addFluidAlloyResultItem(get("water", 1000), get("lava", 1000), new ItemStack(obsidian), 10);
				
				//8 Parts Quicklime + 5 Parts Water = Unknown Metal Dust + 3 Parts Water (Takes 10 seconds)
				RecipeHelper.addFluidAlloyResultItemNFluid(get("water", 3000), get(Fluids.quicklime, 4000),get("water", 2000), dustMagnesium, 10);
				RecipeHelper.addMelting(limestone, 825, get(Fluids.quicklime, 900));
				RecipeHelper.addMelting(limestoneSmooth, 825, get(Fluids.quicklime, 1000));
				RecipeHelper.addMelting(dustSalt, 801, get(Fluids.salt, 20));
				RecipeHelper.addMelting(ingotRutile, titanium, get(Fluids.rutile));
				RecipeHelper.addFluidAlloyNItemResultItem(get(Fluids.aluminum, MetalRates.NUGGET), get(Fluids.quicklime, 900), new ItemStack(glass), new ItemStack(Core.glass, 1, GlassMeta.HEAT), 15);
	}
	
	public static FluidStack gold(int vol) {
		return FluidRegistry.getFluidStack(Fluids.gold, vol);
	}
	
	public static FluidStack iron(int vol) {
		return FluidRegistry.getFluidStack(Fluids.iron, vol);
	}
	
	public static FluidStack get(String name, int vol) {
		return FluidRegistry.getFluidStack(name, vol);
	}
	
	public static FluidStack get(String name) {
		return FluidRegistry.getFluidStack(name, MetalRates.INGOT);
	}
}
