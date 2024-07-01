package brcomkassin.horde.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public interface CustomEntity {
    LivingEntity spawnEntity(CustomEntityType customEntityType , Location location);
}
