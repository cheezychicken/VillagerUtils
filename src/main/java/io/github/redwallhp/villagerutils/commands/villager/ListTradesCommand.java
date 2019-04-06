package io.github.redwallhp.villagerutils.commands.villager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import io.github.redwallhp.villagerutils.VillagerUtils;
import io.github.redwallhp.villagerutils.commands.AbstractCommand;
import io.github.redwallhp.villagerutils.helpers.ItemHelper;
import io.github.redwallhp.villagerutils.helpers.VillagerHelper;

public class ListTradesCommand extends AbstractCommand {

    public ListTradesCommand(VillagerUtils plugin) {
        super(plugin, "villagerutils.editvillager");
    }

    @Override
    public String getName() {
        return "listtrades";
    }

    @Override
    public String getUsage() {
        return "/villager listtrades";
    }

    @Override
    public boolean action(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Console cannot edit villagers.");
            return false;
        }

        Player player = (Player) sender;
        Villager target = VillagerHelper.getVillagerInLineOfSight(player);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "You're not looking at a villager.");
            return false;
        }

        int index = 1;
        for (MerchantRecipe recipe : target.getRecipes()) {
            listTrade(sender, index, recipe);
            ++index;
        }
        sender.sendMessage(ChatColor.DARK_AQUA + "======== Total trades: " + (index - 1) + " ========");
        return true;
    }

    /**
     * List one trade.
     * 
     * @param index the 1-based index in the list of trades.
     * @param recipe the MerchantRecipe.
     */
    protected void listTrade(CommandSender sender, int index, MerchantRecipe recipe) {
        sender.sendMessage(ChatColor.DARK_AQUA + "---------- Trade #" + index + " ----------");
        int ingredientndex = 1;
        for (ItemStack item : recipe.getIngredients()) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Ingredient #" + ingredientndex + ": " +
                               ChatColor.WHITE + ItemHelper.getItemDescription(item));
        }
        sender.sendMessage(ChatColor.DARK_AQUA + "Result: " + ChatColor.WHITE + ItemHelper.getItemDescription(recipe.getResult()));
        sender.sendMessage(ChatColor.DARK_AQUA + "Uses: " + ChatColor.WHITE + recipe.getUses() + " / " + recipe.getMaxUses());
        sender.sendMessage(ChatColor.DARK_AQUA + "Gives XP: " + ChatColor.WHITE + recipe.hasExperienceReward());
    }

}