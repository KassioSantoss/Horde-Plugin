package brcomkassin.horde.tasks;

import brcomkassin.Main;
import brcomkassin.horde.entity.CustomEntityManager;
import brcomkassin.horde.entity.CustomEntityType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HordeTask extends BukkitRunnable {

    private final Player player;
    private final CustomEntityManager customEntityManager = new CustomEntityManager();
    public static final List<LivingEntity> livingEntityList = new ArrayList<>();
    private final Vector impulse = new Vector(0, 0.25, 0);
    private final List<BlockDisplay> blockDisplayList = new ArrayList<>();
    private final Map<LivingEntity, Double> entityCount = new HashMap<>();
    private final Map<LivingEntity, Double> entityValue = new HashMap<>();
    private final Map<LivingEntity, Double> entityCurrentRadius = new HashMap<>();

    public HordeTask(Player player) {
        this.player = player;
    }

    public void start() {
        this.runTaskTimer(Main.getPlugin(Main.class), 0, 5);
    }

    @Override
    public void run() {
        for (LivingEntity entity : livingEntityList) {
            double count = entityCount.getOrDefault(entity, 0.0) + 0.25;
            entityCount.put(entity, count);

            if (count <= 1.50) {
                setImpulseInEntities(entity);
            } else {
                entityCount.put(entity, 0.0);
                livingEntityList.clear();
                cancel();
            }
        }
    }

    private void setImpulseInEntities(LivingEntity entity) {
        double playerY = player.getLocation().clone().getY();
        Location entityLocation = entity.getLocation();
        double distance = playerY - entityLocation.getY();

        if (entityLocation.getY() < playerY) {
            entity.setVelocity(impulse);
        }

        if (distance == 2) {
            activeAnimation(entity);
        }
    }

    private void activeAnimation(LivingEntity livingEntity) {
        for (int i = 0; i < 15; i++) {
            double angle = 2 * Math.PI * i / 15;
            double x = livingEntity.getLocation().clone().getX() + entityCurrentRadius.getOrDefault(livingEntity, 0.8) * Math.cos(angle);
            double z = livingEntity.getLocation().clone().getZ() + entityCurrentRadius.getOrDefault(livingEntity, 0.8) * Math.sin(angle);
            Location spawnLocation = new Location(livingEntity.getWorld(), x - 0.4,
                    livingEntity.getLocation().clone().getY() + 2, z - 0.4);
            BlockDisplay blockDisplay = livingEntity.getWorld().spawn(spawnLocation, BlockDisplay.class);
            Transformation transformation = blockDisplay.getTransformation();
            transformation.getScale().set(0.8, 0.5, 0.8);
            Block blockBelow = livingEntity.getLocation().clone().subtract(0, 1, 0).getBlock();
            blockDisplay.setBlock(blockBelow.getBlockData());
            blockDisplay.setShadowStrength(0);
            blockDisplay.setTransformation(transformation);
            blockDisplayList.add(blockDisplay);
        }

        activeTaskAnimation(livingEntity);
    }

    private void activeTaskAnimation(LivingEntity livingEntity) {


        new BukkitRunnable() {
            @Override
            public void run() {
                double value = entityValue.getOrDefault(livingEntity, 0.0) + 0.25;
                entityValue.put(livingEntity, value);


                if (value <= 1) {
                    for (int i = 0; i < 15; i++) {
                        double angle = 2 * Math.PI * i / 15;
                        double x = livingEntity.getLocation().clone().getX() + entityCurrentRadius.getOrDefault(livingEntity, 0.8) * Math.cos(angle);
                        double z = livingEntity.getLocation().clone().getZ() + entityCurrentRadius.getOrDefault(livingEntity, 0.8) * Math.sin(angle);
                        Location spawnLocation = new Location(livingEntity.getWorld(), x - 0.4,
                                livingEntity.getLocation().clone().getY(), z - 0.4);
                        BlockDisplay blockDisplay = livingEntity.getWorld().spawn(spawnLocation, BlockDisplay.class);
                        Transformation transformation = blockDisplay.getTransformation();
                        transformation.getScale().set(0.8, 0.9, 0.8);
                        Block blockBelow = livingEntity.getLocation().clone().subtract(0, 1, 0).getBlock();
                        blockDisplay.setBlock(blockBelow.getBlockData());
                        blockDisplay.setShadowStrength(0);
                        blockDisplay.setTransformation(transformation);
                        blockDisplayList.add(blockDisplay);
                    }
                    double currentRadius = entityCurrentRadius.getOrDefault(livingEntity, 0.8) + 0.2;
                    entityCurrentRadius.put(livingEntity, currentRadius);
                } else {
                    resetState(livingEntity);
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 5);
    }

    private void resetState(LivingEntity livingEntity) {
        entityCurrentRadius.put(livingEntity, 0.8);
        entityValue.put(livingEntity, 0.0);
        blockDisplayList.forEach(Entity::remove);
        blockDisplayList.clear();
    }

    public void hitMob() {

        if (livingEntityList.size() < 4) {
            spawnZombies();
        }
    }

    private void spawnZombies() {

        Location[] spawnLocations = {
                player.getLocation().clone().add(0, -2.5, 4),
                player.getLocation().clone().add(0, -2.5, -4),
                player.getLocation().clone().add(-4, -2.5, 0),
                player.getLocation().clone().add(4, -2.5, 0)
        };

        for (Location loc : spawnLocations) {
            if (livingEntityList.size() < 4) {
                LivingEntity entity = customEntityManager.spawnEntity(CustomEntityType.ZOMBIE_HORDE, loc);
                livingEntityList.add(entity);
                entityCount.put(entity, 0.0);
                entityValue.put(entity, 0.0);
                entityCurrentRadius.put(entity, 0.8);
            }
        }
    }
}