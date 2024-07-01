package brcomkassin;

import brcomkassin.horde.entity.CustomEntityManager;
import brcomkassin.horde.entity.CustomEntityType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class HordeTask extends BukkitRunnable {

    private final Player player;
    private final CustomEntityManager customEntityManager = new CustomEntityManager();
    public static final List<LivingEntity> livingEntityList = new ArrayList<>();
    private final Vector impulse = new Vector(0, 0.25, 0);
    private final List<BlockDisplay> blockDisplayList = new ArrayList<>();

    public HordeTask(Player player) {
        this.player = player;
    }

    public void start() {
        this.runTaskTimer(Main.getPlugin(Main.class), 0, 5);
    }

    @Override
    public void run() {

        player.sendMessage("list: " + livingEntityList);
        HordeMath.count += 0.25;

        if (HordeMath.count <= 1.50) {
            setImpulseInEntities();
            return;
        }

        HordeMath.count = 0;
        cancel();
        player.sendMessage("Task principal cancelada");
        player.sendMessage("-------------------------");

    }

    private void setImpulseInEntities() {
        player.sendMessage("setImpulseInEntities chamada");
        double playerY = player.getLocation().clone().getY();

        for (LivingEntity entity : livingEntityList) {
            Location entityLocation = entity.getLocation();
            double distance = playerY - entityLocation.getY();

            if (entityLocation.getY() < playerY) {
                entity.setVelocity(impulse);
            }

            if (distance != 2) return;

            activeAnimation(entity);

        }
    }

    private void activeAnimation(LivingEntity livingEntity) {
        player.sendMessage("activeAnimation chamada");

        for (int i = 0; i < HordeMath.particleCount; i++) {
            double angle = 2 * Math.PI * i / HordeMath.particleCount;
            double x = livingEntity.getLocation().clone().getX() + HordeMath.currentRadius * Math.cos(angle);
            double z = livingEntity.getLocation().clone().getZ() + HordeMath.currentRadius * Math.sin(angle);
            Location spawnLocation = new Location(livingEntity.getWorld(), x - 0.4,
                    livingEntity.getLocation().clone().getY() + 2, z - 0.4);
            BlockDisplay blockDisplay = livingEntity.getWorld().spawn(spawnLocation, BlockDisplay.class);
            Transformation transformation = blockDisplay.getTransformation();
            transformation.getScale().set(0.8, 0.6, 0.8);
            Block blockBelow = livingEntity.getLocation().clone().subtract(0, 1, 0).getBlock();
            blockDisplay.setBlock(blockBelow.getBlockData());
            blockDisplay.setShadowStrength(0);
            blockDisplay.setTransformation(transformation);
            blockDisplayList.add(blockDisplay);
        }

        activeTaskAnimation();
    }

    private void activeTaskAnimation() {
        player.sendMessage("activeTaskAnimation chamada");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (HordeMath.value <= 1) {
                    for (LivingEntity livingEntity : livingEntityList) {
                        for (int i = 0; i < HordeMath.particleCount; i++) {
                            double angle = 2 * Math.PI * i / HordeMath.particleCount;
                            double x = livingEntity.getLocation().clone().getX() + HordeMath.currentRadius * Math.cos(angle);
                            double z = livingEntity.getLocation().clone().getZ() + HordeMath.currentRadius * Math.sin(angle);
                            Location spawnLocation = new Location(livingEntity.getWorld(), x - 0.4,
                                    livingEntity.getLocation().clone().getY() - 0.1, z - 0.4);
                            BlockDisplay blockDisplay = livingEntity.getWorld().spawn(spawnLocation, BlockDisplay.class);
                            Transformation transformation = blockDisplay.getTransformation();
                            transformation.getScale().set(0.8, 0.7, 0.8);
                            Block blockBelow = livingEntity.getLocation().clone().subtract(0, 1, 0).getBlock();
                            blockDisplay.setBlock(blockBelow.getBlockData());
                            blockDisplay.setShadowStrength(0);
                            blockDisplay.setTransformation(transformation);
                            blockDisplayList.add(blockDisplay);
                        }
                        HordeMath.currentRadius += 0.2;
                    }
                    HordeMath.value += 0.25;

                    player.sendMessage("raio: " + HordeMath.currentRadius);
                    player.sendMessage("value: " + HordeMath.value);
                    return;
                }

                resetState();

                cancel();
                player.sendMessage("Animação finalizada.");
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 5);
    }

    private void resetState() {
        HordeMath.currentRadius = 0.8;
        HordeMath.value = 0;
        blockDisplayList.forEach(Entity::remove);
        blockDisplayList.clear();
        livingEntityList.clear();
        player.sendMessage("list limpa: " + livingEntityList);
    }

    public void hitMob() {
        if (livingEntityList.size() < 4) {
            spawnZombies();
        }
    }

    private void spawnZombies() {
        Location[] spawnLocations = {
                player.getLocation().clone().add(0, -2.5, 4),
//                player.getLocation().clone().add(0, -2.5, -4),
//                player.getLocation().clone().add(-4, -2.5, 0),
//                player.getLocation().clone().add(4, -2.5, 0)
        };

        for (Location loc : spawnLocations) {
            if (livingEntityList.size() < 4) {
                LivingEntity entity = customEntityManager.spawnEntity(CustomEntityType.ZOMBIE_HORDE, loc);
                livingEntityList.add(entity);
            }
        }
    }

    public static class HordeMath {
        public static double count = 0;
        public static double value = 0;
        public static double currentRadius = 0.8;
        public static final int particleCount = 15;

    }

}
