package brcomkassin.horde;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public interface Horde {

    void invoke(EntityDamageByEntityEvent event);
    void target(EntityTargetLivingEntityEvent event);

}
