package io.github.justincqz.adventurers;

import io.github.justincqz.adventurers.util.NPCMenu;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class Adventurers extends JavaPlugin {

  @Override
  public void onEnable() {
    // TODO Insert logic to be performed when the plugin is enabled
    getLogger().info("onEnable has been invoked!");
  }

  @Override
  public void onDisable() {
    // TODO Insert logic to be performed when the plugin is disabled
    getLogger().info("onDisable has been invoked!");
  }


  @Override
  public void onLoad() {
    CustomNPC.addToMaps();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (command.getName().equalsIgnoreCase("adventurers")
        || command.getName().equalsIgnoreCase("adv")) {
      CustomNPC newNPC = new CustomNPC(((CraftWorld) ((Player) sender).getWorld()).getHandle(),
          ((CraftPlayer) sender).getHandle(), this);
      newNPC.spawnEntity(((Player) sender).getLocation());
      return true;
    }
    return false;
  }
}