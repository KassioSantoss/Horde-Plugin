package brcomkassin.horde;

import brcomkassin.HordeTask;
import brcomkassin.horde.entity.CustomEntityManager;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import java.util.*;

public class HordeManager implements Horde {

    private final Map<UUID, UUID> uuiduuidMap = new HashMap<>();
    public static final Map<UUID, HordeTask> activeTasks = new HashMap<>();
    private static HordeManager hordeManager;
    private final CustomEntityManager customEntityManager = new CustomEntityManager();

    private HordeManager() {
    }

    public static HordeManager getHordeManager() {
        if (hordeManager == null) {
            return hordeManager = new HordeManager();
        }
        return hordeManager;
    }

    @Override
    public void invoke(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();


        if (activeTasks.containsKey(player.getUniqueId())) return;

        double random = Math.random();

        uuiduuidMap.put(player.getUniqueId(), event.getEntity().getUniqueId());

        HordeTask hordeTask = new HordeTask(player);

        hordeTask.hitMob();
        hordeTask.start();
    }

    @Override
    public void target(EntityTargetLivingEntityEvent event) {
        if (!(event.getTarget() instanceof Player)) return;

        Player player = (Player) event.getTarget();

        if (!(uuiduuidMap.containsKey(player.getUniqueId()))) return;

        event.setCancelled(true);

    }

    public void removeTask(UUID uuid) {
        activeTasks.remove(uuid);
    }

}


