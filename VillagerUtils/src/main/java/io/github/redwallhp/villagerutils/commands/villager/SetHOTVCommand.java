package io.github.redwallhp.villagerutils.commands.villager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import io.github.redwallhp.villagerutils.VillagerUtils;
import io.github.redwallhp.villagerutils.commands.VillagerSpecificAbstractCommand;

public class SetHOTVCommand extends VillagerSpecificAbstractCommand implements TabCompleter {

    public SetHOTVCommand(VillagerUtils plugin) {
        super(plugin, "villagerutils.editvillager");
    }

    @Override
    public String getName() {
        return "hotv";
    }

    @Override
    public String getUsage() {
        return "/villager hotv <boolean>";
    }

    @Override
    public boolean action(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Console cannot spawn villagers.");
            return false;
        }
        Player player = (Player) sender;

        Villager villager = getVillagerInLineOfSight(player, "Wandering traders can't be protected against HOTV yet.");
        if (villager == null) {
            return false;
        }

       if (args.length < 1) {
            player.sendMessage(ChatColor.RED + getUsage());
            return false;
        }

        Boolean value = Boolean.parseBoolean(args[0]);
        if (value) {
        	if(plugin.getVillagerMeta().HOTV_MERCHANTS.contains(villager.getUniqueId().toString())) {
        		sender.sendMessage(ChatColor.DARK_AQUA + "This villager already isn't affected by HOTV.");
        	} else {
        		plugin.getVillagerMeta().HOTV_MERCHANTS.add(villager.getUniqueId().toString());
        		sender.sendMessage(ChatColor.DARK_AQUA + "This villager will not be affected by HOTV.");
        	}
        } else {
            plugin.getVillagerMeta().HOTV_MERCHANTS.remove(villager.getUniqueId().toString());
            sender.sendMessage(ChatColor.DARK_AQUA + "This villager will be affected by HOTV.");
        }
        plugin.getVillagerMeta().save();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("false", "true").stream()
            .filter(completion -> completion.startsWith(args[1].toLowerCase()))
            .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

}
