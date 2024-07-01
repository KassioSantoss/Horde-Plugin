package brcomkassin.horde;

import brcomkassin.horde.tasks.HordeTask;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;

public class HordeManager implements Horde {

    private final Map<UUID, UUID> uuiduuidMap = new HashMap<>();
    public static final Map<UUID, HordeTask> activeTasks = new HashMap<>();
    private static HordeManager hordeManager;

    private HordeManager() {
    }

    public static HordeManager getHordeManager() {
        if (hordeManager == null) {
            return hordeManager = new HordeManager();
        }
        return hordeManager;
    }

    @Override
    public void invoke(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (activeTasks.containsKey(player.getUniqueId())) return;

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


