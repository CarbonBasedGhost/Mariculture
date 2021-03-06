package mariculture;

import java.io.File;

import mariculture.core.CommonProxy;
import mariculture.core.Config;
import mariculture.core.lib.Modules;
import mariculture.core.lib.Modules.Module;
import mariculture.core.network.PacketPipeline;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = "Mariculture", name = "Mariculture", dependencies="after:Enchiridion;after:TConstruct;after:Thaumcraft;after:AWWayofTime")
public class Mariculture {
	public static final PacketPipeline packets = new PacketPipeline();
	public static final String modid = "mariculture";

	@SidedProxy(clientSide = "mariculture.core.ClientProxy", serverSide = "mariculture.core.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Instance("Mariculture")
	public static Mariculture instance = new Mariculture();
	
	//Root folder
	public static File root;
	
	//Modules
	public static Modules modules = new Modules();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		root = event.getModConfigurationDirectory();
		Config.setup(root + "/mariculture/");	
		for(Module module: Modules.modules) {
			module.preInit();
		}

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		packets.init();
		for(Module module: Modules.modules) {
			module.init();
		}
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		packets.postInit();
		for(Module module: Modules.modules) {
			module.postInit();
		}
		
		proxy.setupClient();
	}
}