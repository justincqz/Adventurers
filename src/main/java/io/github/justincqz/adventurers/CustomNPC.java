package io.github.justincqz.adventurers;

import io.github.justincqz.adventurers.util.NPCMenu;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityTypes;
import net.minecraft.server.v1_12_R1.EntityWolf;
import net.minecraft.server.v1_12_R1.EnumHand;
import net.minecraft.server.v1_12_R1.ItemFood;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CustomNPC extends EntityWolf {

  private NPCMenu menu;
  private Inventory armor_inventory;
  private Plugin plugin;

  public CustomNPC(World world, EntityHuman entityHuman, Plugin plugin) {
    super(world);
    this.plugin = plugin;
    setInitialOwner(entityHuman);
    menu = initialiseMenu(entityHuman, plugin);

  }

  @Override
  public boolean isAngry() {
    return false;
  }

  private NPCMenu initialiseMenu(EntityHuman entityHuman, Plugin plugin) {
    return new NPCMenu("Party Member Menu", 9, new NPCMenu.OptionClickEventHandler() {
      @Override
      public void onOptionClick(NPCMenu.OptionClickEvent event) {
        parseCommands(event.getName());
        event.getPlayer()
            .sendMessage("You have asked " + getName() + " to " + event.getName() + ".");
        event.setWillClose(true);
      }
    }, plugin)
        .setOption(3, new ItemStack(Material.COMPASS, 1), "Wait",
            "Ask " + getName() + " to wait at his current location.")
        .setOption(4, new ItemStack(Material.LAVA_BUCKET, 1), "Kick",
            "Ask " + getName() + " to leave the party.")
        .setOption(5, new ItemStack(Material.BEACON, 1), "Recall",
            "Ask " + getName() + " to return to your position.")
        .setOption(2, new ItemStack(Material.LEASH, 1), "Follow",
            "Ask " + getName() + " to follow you.");
  }

  private void parseCommands(String cmd) {
    switch (cmd) {
      case "Follow":
        this.goalSit.setSitting(false);
        this.bd = false;
        this.navigation.p();
        this.setGoalTarget(null, TargetReason.FORGOT_TARGET, false);
        break;
      case "Wait":
        this.goalSit.setSitting(true);
        this.bd = false;
        this.navigation.p();
        this.setGoalTarget(null, TargetReason.FORGOT_TARGET, false);
        break;
      case "Kick":
        killEntity();
        break;
      case "Recall":
        Location loc = ((EntityHuman) getOwner()).getBukkitEntity().getLocation();
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        break;
    }
  }

  private void setInitialOwner(EntityHuman entityHuman) {
    CraftEventFactory.callEntityTameEvent(this, entityHuman);
    this.c(entityHuman);
    this.navigation.p();
    this.setGoalTarget(null);
    this.goalSit.setSitting(false);
    this.setHealth(this.getMaxHealth());
    this.p(true);
    setCustomName("John");
    setCustomNameVisible(true);
    setTamed(true);
    setOwnerUUID(entityHuman.getUniqueID());
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static void addToMaps() {
    final String ENTITY_NAME = "CustomNPC";
    final int ENTITY_MODEL_ID = 51;
    try {
      MinecraftKey var = new MinecraftKey(ENTITY_NAME);

      EntityTypes.b.a(ENTITY_MODEL_ID, var, CustomNPC.class);

      EntityTypes.d.add(var);

      Field field = net.minecraft.server.v1_12_R1.EntityTypes.class.getDeclaredField("g");
      field.setAccessible(true);
      ((List) field.get(null)).add(ENTITY_MODEL_ID, ENTITY_NAME);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public void spawnEntity(Location loc) {
    this.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    ((CraftWorld) loc.getWorld()).getHandle().addEntity(this);
  }

  @Override
  public boolean a(EntityHuman entityHuman, EnumHand enumHand) {
    net.minecraft.server.v1_12_R1.ItemStack itemstack = entityHuman.b(enumHand);
    if (!itemstack.isEmpty()) {
      if (itemstack.getItem() instanceof ItemFood) {
        return super.a(entityHuman, enumHand);
      }
      // If hand is not empty, do something
    } else {
      menu.open((Player) entityHuman.getBukkitEntity());
    }
    return true;
  }

}
