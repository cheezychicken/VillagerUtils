package io.github.redwallhp.villagerutils.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import io.github.redwallhp.villagerutils.VillagerUtils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

public class HOTVstopper implements Listener {
	
	HashMap<UUID, Integer> HOTVremaining = new HashMap<UUID,Integer>();
	HashMap<UUID, Integer> HOTVlevel = new HashMap<UUID,Integer>();
	
	private final VillagerUtils plugin;
		
	public HOTVstopper() {
	plugin = VillagerUtils.instance;
	plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
	}
	
	/**
	 * When a player interacts with a villager check if they have HOTV, and if so, remove it and save it.
	 */
	@EventHandler
	public void interactVillager(PlayerInteractEntityEvent event) {

        if (event.getRightClicked() instanceof Villager){
        	
        Player p = event.getPlayer();
        Villager villager = (Villager) event.getRightClicked();
        String villagerID = villager.getUniqueId().toString();
        
        	if (plugin.getVillagerMeta().HOTV_MERCHANTS.contains(villagerID) 
        			& p.hasPotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE) ) {
        	HOTVremaining.put(p.getUniqueId(), p.getPotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE).getDuration());
        	HOTVlevel.put(p.getUniqueId(), p.getPotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE).getAmplifier());
            p.removePotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE);
        	}
        }
   }
	
	@EventHandler
	public void onStopTrading(InventoryCloseEvent event) {

		if(event.getInventory().getType() == InventoryType.MERCHANT) {
			HumanEntity p = event.getPlayer();
			if(HOTVremaining.containsKey(p.getUniqueId())) {
				Integer timeleft = HOTVremaining.get(p.getUniqueId());
				Integer level = HOTVlevel.get(p.getUniqueId());
				
				PotionEffect HOTVeffect = new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, timeleft, level);
				HOTVeffect.apply(p);
				
				//clean up
				HOTVremaining.remove(p.getUniqueId());
				HOTVlevel.remove(p.getUniqueId());
			}
		}
		
	}
}
