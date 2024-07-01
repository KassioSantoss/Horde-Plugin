package brcomkassin.horde.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class CustomEntityManager implements CustomEntity {

    @Override
    public LivingEntity spawnEntity(CustomEntityType customEntityType, Location location) {
        Entity entity = location.getWorld().spawnEntity(location, customEntityType.getEntityType());

        if (!(entity instanceof LivingEntity)) return null;

        LivingEntity livingEntity = (LivingEntity) entity;
        livingEntity.setCustomNameVisible(true);
        livingEntity.setCustomName(customEntityType.getName());
        livingEntity.setHealth(customEntityType.getHealth());

        return livingEntity;
    }

}
