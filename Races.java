package be.Vilevar.Races;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import be.Vilevar.Races.RacesList.Nain;

public class Races extends JavaPlugin implements Listener{

	public static Races getInstance(){
		return Races.getPlugin(Races.class);
	}
	
	@Override
	public void onEnable(){
		super.onEnable();
		
		File file = new File(this.getDataFolder() + "/");
		if(!file.exists()) {
			file.mkdir();
		}
		
		getServer().getPluginManager().registerEvents(this, this);
		
/*		if(Rituels.enable){
			new Raccourcis();
			Bukkit.getPluginManager().registerEvents(new Rituels(), this);
		}
*/		
		if(Nain.enabled){
			Bukkit.getPluginManager().registerEvents(new Nain(), this);
			Nain.initNain();
		}
	}
}