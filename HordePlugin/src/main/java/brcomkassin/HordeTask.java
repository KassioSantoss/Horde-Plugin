package brcomkassin;

import brcomkassin.horde.entity.CustomEntityManager;
import brcomkassin.horde.entity.CustomEntityType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class HordeTask extends BukkitRunnable {

    private final Player player;
    private final CustomEntityManager customEntityManager = new CustomEntityManager();
    public static final List<LivingEntity> livingEntityList = new ArrayList<>();
    private final Vector impulse = new Vector(0, 0.25, 0);
    private double count = 0;
    private double value = 0;
    private double currentRadius = 0.8;
    private final int particleCount = 15;
    private final List<BlockDisplay> blockDisplayList = new ArrayList<>();

    public HordeTask(Player player) {
        this.player = player;
    }

    public void start() {
        this.runTaskTimer(Main.getPlugin(Main.class), 0, 5);
    }

    @Override
    public void run() {

        count += 0.25;

        if (count <= 1.50) {
            setImpulseInEntities();
            return;
        }

        blockDisplayList.forEach(Entity::remove);
        blockDisplayList.clear();
        livingEntityList.clear();
        count = 0;
        value = 0;
        currentRadius = 0.8;
        cancel();
        player.sendMessage("task principal cancelada");

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

            if (distance == 2) {
                activeAnimation(entity);
            }

        }
    }

    private void activeAnimation(LivingEntity livingEntity) {

        player.sendMessage("activeAnimation chamada");

        for (int i = 0; i < particleCount; i++) {

            double angle = 2 * Math.PI * i / particleCount;
            double x = livingEntity.getLocation().clone().getX() + currentRadius * Math.cos(angle);
            double z = livingEntity.getLocation().clone().getZ() + currentRadius * Math.sin(angle);

            Location spawnLocation = new Location(livingEntity.getWorld(), x,
                    livingEntity.getLocation().clone().getY() + 2, z);

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

                if (value <= 1.5) {

                    for (LivingEntity livingEntity : livingEntityList) {

                        for (int i = 0; i < particleCount; i++) {
                            double angle = 2 * Math.PI * i / particleCount;

                            double x = livingEntity.getLocation().clone().getX() + currentRadius * Math.cos(angle);
                            double z = livingEntity.getLocation().clone().getZ() + currentRadius * Math.sin(angle);

                            Location spawnLocation = new Location(livingEntity.getWorld(), x,
                                    livingEntity.getLocation().clone().getY() - 0.5, z);

                            BlockDisplay blockDisplay = livingEntity.getWorld().spawn(spawnLocation, BlockDisplay.class);

                            Transformation transformation = blockDisplay.getTransformation();

                            transformation.getScale().set(0.8, 2, 0.8);

                            Block blockBelow = livingEntity.getLocation().clone().subtract(0, 1, 0).getBlock();

                            blockDisplay.setBlock(blockBelow.getBlockData());
                            blockDisplay.setShadowStrength(0);

                            blockDisplay.setTransformation(transformation);

                            blockDisplayList.add(blockDisplay);
                        }
                        currentRadius += 0.2;
                    }
                    
                    value += 0.25;
                    return;
                }

                cancel();
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 5);
    }


    public void hitMob() {

        if (livingEntityList.size() <= 3) {
            if (livingEntityList.isEmpty()) {
                Location spawnLocation = player.getLocation().clone().add(0, -2.5, 4);
                LivingEntity livingEntity = customEntityManager.spawnEntity(CustomEntityType.ZOMBIE_HORDE, spawnLocation);
                livingEntity.setCollidable(false);
                livingEntityList.add(livingEntity);
            }

        }

    }
}
