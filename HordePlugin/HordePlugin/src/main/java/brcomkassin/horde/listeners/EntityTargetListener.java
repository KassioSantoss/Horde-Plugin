package brcomkassin.horde.listeners;

import brcomkassin.horde.HordeManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class EntityTargetListener implements Listener {

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent event) {
        HordeManager.getHordeManager().target(event);
    }

}