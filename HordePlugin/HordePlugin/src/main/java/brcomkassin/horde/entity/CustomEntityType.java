package brcomkassin.horde.entity;

import lombok.Getter;
import org.bukkit.entity.EntityType;

@Getter
public enum CustomEntityType {

    ZOMBIE_HORDE(EntityType.ZOMBIE, "Zombie Horde", 20.0);

    private final EntityType entityType;
    private final String name;
    private final double health;

    CustomEntityType(EntityType entityType, String name, double health) {
        this.entityType = entityType;
        this.name = name;
        this.health = health;
    }

}
