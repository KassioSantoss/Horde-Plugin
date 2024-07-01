package brcomkassin.horde;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface Horde {

    void invoke(PlayerInteractEvent event);

    void target(EntityTargetLivingEntityEvent event);

}
