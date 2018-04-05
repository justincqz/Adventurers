package io.github.justincqz.adventurers;

import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.server.v1_12_R1.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

public enum EntityTypes {
  //NAME("Entity name", Entity ID, yourcustomclass.class);
  CUSTOM_NPC("Zombie", 54, CustomNPC.class); //You can add as many as you want.

  EntityTypes(String name, int id, Class<? extends Entity> custom) {
    addToMaps(custom, name, id);
  }

  public static void spawnEntity(Entity entity, Location loc) {
    entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static void addToMaps(Class clazz, String name, int id) {
    ((Map) getPrivateField("c", EntityTypes.class, null))
        .put(name, clazz);
    ((Map) getPrivateField("d", EntityTypes.class, null))
        .put(clazz, name);
    ((Map) getPrivateField("f", EntityTypes.class, null))
        .put(clazz, id);
  }

  public static Object getPrivateField(String fieldName, Class clazz, Object object) {
    Field field;
    Object o = null;

    try {
      field = clazz.getDeclaredField(fieldName);

      field.setAccessible(true);

      o = field.get(object);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }

    return o;
  }
}
