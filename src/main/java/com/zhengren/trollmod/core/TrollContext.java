package com.zhengren.trollmod.core;

import com.zhengren.trollmod.net.GlitchPayload;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 事件运行时上下文：事件作者唯一需要打交道的对象。
 * 所有可能出错的 Minecraft API 调用都被封装在这里，事件里尽量只调用这些方法。
 */
public class TrollContext {

    public final MinecraftServer server;
    public final ServerPlayerEntity player;
    public final TrollConfig config;
    public final PlayerTrollState state;
    public final Random rng;
    private final TrollEvent event;

    public TrollContext(MinecraftServer server, ServerPlayerEntity player, TrollConfig config,
                        PlayerTrollState state, Random rng, TrollEvent event) {
        this.server = server;
        this.player = player;
        this.config = config;
        this.state = state;
        this.rng = rng;
        this.event = event;
    }

    public TrollEvent event() {
        return event;
    }

    // ==================================================================
    // 基础查询
    // ==================================================================

    public ServerWorld world() {
        return player.getServerWorld();
    }

    public MinecraftServer server() {
        return server;
    }

    public ServerPlayerEntity player() {
        return player;
    }

    public TrollConfig cfg() {
        return config;
    }

    public String playerName() {
        return player.getName().getString();
    }

    public double x() {
        return player.getX();
    }

    public double y() {
        return player.getY();
    }

    public double z() {
        return player.getZ();
    }

    public BlockPos pos() {
        return player.getBlockPos();
    }

    public Vec3d vec() {
        return player.getPos();
    }

    public long worldTime() {
        return world().getTimeOfDay();
    }

    public long gameTicks() {
        return server.getTicks();
    }

    public double rage() {
        return state.rage;
    }

    public void addRage(double amount) {
        state.rage += amount;
    }

    public Random random() {
        return rng;
    }

    public int ri(int bound) {
        return rng.nextInt(Math.max(1, bound));
    }

    /** [min, max] 闭区间 */
    public int ri(int min, int max) {
        if (max <= min) {
            return min;
        }
        return min + rng.nextInt(max - min + 1);
    }

    public double rd(double min, double max) {
        return min + rng.nextDouble() * (max - min);
    }

    public boolean chance(double p) {
        return rng.nextDouble() < p;
    }

    public float rf(float min, float max) {
        return min + rng.nextFloat() * (max - min);
    }

    public <T> T pick(T[] arr) {
        if (arr.length == 0) {
            return null;
        }
        return arr[rng.nextInt(arr.length)];
    }

    public <T> T pick(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(rng.nextInt(list.size()));
    }

    // ==================================================================
    // 聊天 / 文字
    // ==================================================================

    public void msg(String text) {
        player.sendMessage(Text.literal(text), false);
    }

    public void ok(String text) {
        player.sendMessage(Text.literal("§a" + text), false);
    }

    public void warn(String text) {
        player.sendMessage(Text.literal("§c" + text), false);
    }

    public void sys(String text) {
        player.sendMessage(Text.literal("§7[系统] " + text), false);
    }

    /** 伪造一条其他玩家的聊天信息 */
    public void fakeChat(String sender, String text) {
        player.sendMessage(Text.literal("§f<§b" + sender + "§f> §r" + text), false);
    }

    /** 伪造一条服务器公告 */
    public void fakeBroadcast(String text) {
        player.sendMessage(Text.literal("§8[§c服务器§8] §r" + text), false);
    }

    /** 伪造加入/退出提示 */
    public void fakeJoinLeave(String name, boolean join) {
        player.sendMessage(Text.literal("§e" + name + (join ? " 加入了游戏" : " 离开了游戏")), false);
    }

    public void actionBar(String text) {
        player.networkHandler.sendPacket(new OverlayMessageS2CPacket(Text.literal(text)));
    }

    public void title(String title, String subtitle) {
        title(title, subtitle, 10, 50, 20);
    }

    public void title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.networkHandler.sendPacket(new TitleFadeS2CPacket(fadeIn, stay, fadeOut));
        player.networkHandler.sendPacket(new TitleS2CPacket(Text.literal(title == null ? "" : title)));
        player.networkHandler.sendPacket(new SubtitleS2CPacket(Text.literal(subtitle == null ? "" : subtitle)));
    }

    public void broadcast(String text) {
        server.getPlayerManager().broadcast(Text.literal(text), false);
    }

    public List<ServerPlayerEntity> allPlayers() {
        return new ArrayList<>(server.getPlayerManager().getPlayerList());
    }

    public ServerPlayerEntity randomOtherPlayer() {
        List<ServerPlayerEntity> list = allPlayers();
        list.remove(player);
        return list.isEmpty() ? null : pick(list);
    }

    // ==================================================================
    // 声音与粒子
    // ==================================================================

    public void sound(SoundEvent sound, float volume, float pitch) {
        world().playSound(null, x(), y(), z(), sound, SoundCategory.MASTER, volume, pitch);
    }

    public void sound(SoundEvent sound) {
        sound(sound, 1.0f, 1.0f);
    }

    /** 只发给该玩家（音量拉满的贴脸音效） */
    public void soundSelf(SoundEvent sound, float volume, float pitch) {
        player.playSound(sound, volume, pitch);
    }

    public void soundAt(SoundEvent sound, double px, double py, double pz, float volume, float pitch) {
        world().playSound(null, px, py, pz, sound, SoundCategory.MASTER, volume, pitch);
    }

    public void soundEverywhere(SoundEvent sound, float volume, float pitch) {
        world().playSound(null, x(), y(), z(), sound, SoundCategory.MASTER, volume, pitch);
    }

    public void randomHorrorSound() {
        SoundEvent[] s = {
                SoundEvents.ENTITY_ENDERMAN_SCREAM,
                SoundEvents.ENTITY_GHAST_SCREAM,
                SoundEvents.ENTITY_WITHER_SPAWN,
                SoundEvents.ENTITY_ENDER_DRAGON_GROWL,
                SoundEvents.ENTITY_WARDEN_ROAR,
                SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE,
                SoundEvents.AMBIENT_CAVE.value(),
                SoundEvents.ENTITY_PHANTOM_SWOOP,
                SoundEvents.BLOCK_BELL_RESONATE,
                SoundEvents.ENTITY_WITHER_AMBIENT
        };
        sound(pick(s), 1.0f, rf(0.7f, 1.1f));
    }

    public void particle(ParticleEffect effect, int count) {
        world().spawnParticles(effect, x(), y() + 1.0, z(), count, 0.6, 0.8, 0.6, 0.05);
    }

    public void particleAt(ParticleEffect effect, double px, double py, double pz, int count, double spread) {
        world().spawnParticles(effect, px, py, pz, count, spread, spread, spread, 0.05);
    }

    public void particleAtPlayer(ParticleEffect effect, int count) {
        particleAt(effect, x(), y() + 1, z(), count, 1.0);
    }

    // ==================================================================
    // 移动 / 位置
    // ==================================================================

    public void teleport(double tx, double ty, double tz) {
        player.requestTeleport(tx, ty, tz);
        player.networkHandler.requestTeleport(tx, ty, tz, player.getYaw(), player.getPitch());
    }

    public void teleport(Vec3d v) {
        teleport(v.x, v.y, v.z);
    }

    public void teleportToPlayer(ServerPlayerEntity other) {
        teleport(other.getX(), other.getY(), other.getZ());
    }

    /** 随机水平传送（保持大致高度） */
    public void scatter(int range) {
        double tx = x() + ri(-range, range);
        double tz = z() + ri(-range, range);
        int top = world().getTopY(Heightmap.Type.MOTION_BLOCKING, (int) tx, (int) tz);
        teleport(tx, Math.max(world().getBottomY() + 2, Math.min(top + 1, y() + 20)), tz);
    }

    /** 随机传送到很远的地方 */
    public void scatterFar(int range) {
        double tx = x() + ri(-range, range);
        double tz = z() + ri(-range, range);
        double ty = y() + ri(-8, 25);
        teleport(tx, Math.max(world().getBottomY() + 3, Math.min(ty, 300)), tz);
    }

    public void lift(double height) {
        teleport(x(), Math.min(y() + height, 300), z());
    }

    public void launch(double power) {
        player.setVelocity(player.getVelocity().x, power, player.getVelocity().z);
        player.velocityModified = true;
    }

    public void fling(double dx, double dy, double dz) {
        player.setVelocity(dx, dy, dz);
        player.velocityModified = true;
    }

    public void spin(float degrees) {
        player.setYaw(player.getYaw() + degrees);
        player.setHeadYaw(player.getHeadYaw() + degrees);
    }

    public void setRotation(float yaw, float pitch) {
        player.setYaw(yaw);
        player.setPitch(pitch);
    }

    public void randomRotation() {
        setRotation(rng.nextFloat() * 360f - 180f, rf(-90f, 90f));
    }

    public boolean isOnGround() {
        return player.isOnGround();
    }

    public boolean isFalling() {
        return player.fallDistance > 3.0f;
    }

    public boolean isInLava() {
        return player.isInLava();
    }

    public boolean isInWater() {
        return player.isTouchingWater();
    }

    public boolean isBurning() {
        return player.isOnFire();
    }

    public float health() {
        return player.getHealth();
    }

    // ==================================================================
    // 物品 / 背包
    // ==================================================================

    public PlayerInventory inv() {
        return player.getInventory();
    }

    public ItemStack held() {
        return player.getMainHandStack();
    }

    public void give(Item item, int count) {
        give(new ItemStack(item, count));
    }

    public void give(ItemStack stack) {
        if (!player.giveItemStack(stack)) {
            player.dropItem(stack, false);
        }
    }

    public void giveRandomJunk() {
        Item[] junk = {
                Items.ROTTEN_FLESH, Items.POISONOUS_POTATO, Items.DEAD_BUSH, Items.STICK,
                Items.GRAVEL, Items.FLINT, Items.BONE, Items.STRING, Items.SPIDER_EYE,
                Items.PUFFERFISH, Items.CACTUS, Items.WHEAT_SEEDS, Items.COAL,
                Items.FEATHER, Items.LEATHER, Items.EGG, Items.SNOWBALL, Items.KELP, Items.SEAGRASS
        };
        Item it = pick(junk);
        give(new ItemStack(it, ri(1, 16)));
    }

    /** 随机拿走一个物品 */
    public boolean takeRandomItem() {
        PlayerInventory in = inv();
        for (int attempt = 0; attempt < 12; attempt++) {
            int slot = rng.nextInt(in.size());
            ItemStack s = in.getStack(slot);
            if (!s.isEmpty()) {
                in.setStack(slot, ItemStack.EMPTY);
                return true;
            }
        }
        return false;
    }

    public void clearInventory() {
        inv().clear();
    }

    /** 打乱整个背包 */
    public void shuffleInventory() {
        PlayerInventory in = inv();
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < in.size(); i++) {
            stacks.add(in.getStack(i));
        }
        Collections.shuffle(stacks, rng);
        for (int i = 0; i < in.size(); i++) {
            in.setStack(i, stacks.get(i));
        }
    }

    /** 只打乱快捷栏 9 格 */
    public void scrambleHotbar() {
        PlayerInventory in = inv();
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            stacks.add(in.getStack(i));
        }
        Collections.shuffle(stacks, rng);
        for (int i = 0; i < 9; i++) {
            in.setStack(i, stacks.get(i));
        }
    }

    public void dropHeld() {
        ItemStack s = held();
        if (!s.isEmpty()) {
            player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
            player.dropItem(s, true);
        }
    }

    public void dropRandom() {
        takeRandomItem();
    }

    /** 丢出背包里所有东西 */
    public void dropEverything() {
        inv().dropAll();
    }

    public void selectSlot(int slot) {
        inv().selectedSlot = Math.max(0, Math.min(8, slot));
    }

    public int selectedSlot() {
        return inv().selectedSlot;
    }

    public void randomSelectSlot() {
        selectSlot(rng.nextInt(9));
    }

    /** 随机给手持物品改个名字 */
    public void renameHeld(String name) {
        ItemStack s = held();
        if (!s.isEmpty()) {
            s.setCustomName(Text.literal(name));
        }
    }

    public void renameRandomItem(String name) {
        PlayerInventory in = inv();
        for (int attempt = 0; attempt < 12; attempt++) {
            int slot = rng.nextInt(in.size());
            ItemStack s = in.getStack(slot);
            if (!s.isEmpty()) {
                s.setCustomName(Text.literal(name));
                return;
            }
        }
    }

    public void damageRandomTool(int amount) {
        PlayerInventory in = inv();
        for (int attempt = 0; attempt < 16; attempt++) {
            int slot = rng.nextInt(in.size());
            ItemStack s = in.getStack(slot);
            if (!s.isEmpty() && s.isDamageable()) {
                s.setDamage(Math.min(s.getMaxDamage() - 1, s.getDamage() + amount));
                return;
            }
        }
    }

    public void breakRandomTool() {
        PlayerInventory in = inv();
        for (int attempt = 0; attempt < 16; attempt++) {
            int slot = rng.nextInt(in.size());
            ItemStack s = in.getStack(slot);
            if (!s.isEmpty() && s.isDamageable()) {
                s.setDamage(s.getMaxDamage() - 1);
                return;
            }
        }
    }

    public void enchantHeldRandomly() {
        ItemStack s = held();
        if (!s.isEmpty()) {
            s.addEnchantment(Enchantments.UNBREAKING, ri(1, 3));
        }
    }

    public void curseHeld() {
        ItemStack s = held();
        if (!s.isEmpty() && !s.isEmpty()) {
            s.addEnchantment(Enchantments.VANISHING_CURSE, 1);
        }
    }

    public void swapEquipmentRandomly() {
        EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
        EquipmentSlot a = pick(slots);
        EquipmentSlot b = pick(slots);
        ItemStack sa = player.getEquippedStack(a);
        ItemStack sb = player.getEquippedStack(b);
        player.equipStack(a, sb);
        player.equipStack(b, sa);
    }

    public boolean invEmpty() {
        return inv().isEmpty();
    }

    /** 往背包里塞满某一类垃圾 */
    public void fillWith(Item item, int count) {
        for (int i = 0; i < count; i++) {
            give(new ItemStack(item, 1));
        }
    }

    public void setCooldown(Item item, int ticks) {
        player.getItemCooldownManager().set(item, ticks);
    }

    // ==================================================================
    // 状态 / 生命 / 饥饿
    // ==================================================================

    public void effect(StatusEffect effect, int ticks, int amplifier) {
        player.addStatusEffect(new StatusEffectInstance(effect, ticks, amplifier));
    }

    public void effectRandom(int ticks, int amplifier) {
        StatusEffect[] pool = {
                StatusEffects.NAUSEA, StatusEffects.BLINDNESS, StatusEffects.SLOWNESS,
                StatusEffects.WEAKNESS, StatusEffects.MINING_FATIGUE, StatusEffects.POISON,
                StatusEffects.WITHER, StatusEffects.LEVITATION, StatusEffects.DARKNESS,
                StatusEffects.HUNGER, StatusEffects.UNLUCK, StatusEffects.JUMP_BOOST,
                StatusEffects.SPEED, StatusEffects.INVISIBILITY, StatusEffects.GLOWING
        };
        effect(pick(pool), ticks, amplifier);
    }

    public void clearEffects() {
        player.clearStatusEffects();
    }

    public void removeEffect(StatusEffect effect) {
        player.removeStatusEffect(effect);
    }

    public void damage(float amount) {
        player.damage(world().getDamageSources().generic(), amount);
    }

    public void damage(String kind, float amount) {
        switch (kind) {
            case "magic" -> player.damage(world().getDamageSources().magic(), amount);
            case "fall" -> player.damage(world().getDamageSources().fall(), amount);
            case "fire" -> player.damage(world().getDamageSources().onFire(), amount);
            case "lava" -> player.damage(world().getDamageSources().lava(), amount);
            case "drown" -> player.damage(world().getDamageSources().drown(), amount);
            case "starve" -> player.damage(world().getDamageSources().starve(), amount);
            case "wither" -> player.damage(world().getDamageSources().wither(), amount);
            case "freeze" -> player.damage(world().getDamageSources().freeze(), amount);
            case "cactus" -> player.damage(world().getDamageSources().cactus(), amount);
            case "outofworld" -> player.damage(world().getDamageSources().outOfWorld(), amount);
            case "lightning" -> player.damage(world().getDamageSources().generic(), amount);
            case "flyintowall" -> player.damage(world().getDamageSources().flyIntoWall(), amount);
            default -> player.damage(world().getDamageSources().generic(), amount);
        }
    }

    public void setHealth(float value) {
        player.setHealth(Math.max(0.0f, Math.min(value, player.getMaxHealth())));
    }

    public void heal(float amount) {
        player.heal(amount);
    }

    public void kill() {
        player.kill();
    }

    public void setFire(int seconds) {
        player.setOnFireFor(seconds);
    }

    public void setFireTicks(int ticks) {
        player.setFireTicks(ticks);
    }

    public void extinguish() {
        player.extinguish();
    }

    public void feed(int food) {
        player.getHungerManager().setFoodLevel(Math.max(0, Math.min(20, food)));
    }

    public int foodLevel() {
        return player.getHungerManager().getFoodLevel();
    }

    public void starve() {
        player.getHungerManager().setFoodLevel(0);
        player.getHungerManager().setSaturationLevel(0.0f);
    }

    public void exhaust(float amount) {
        player.getHungerManager().addExhaustion(amount);
    }

    public void saturation(float value) {
        player.getHungerManager().setSaturationLevel(value);
    }

    public void setExperience(int level) {
        player.setExperienceLevel(level);
        player.setExperiencePoints(0);
    }

    public void addExperience(int amount) {
        player.addExperience(amount);
    }

    public void resetFallDistance() {
        player.fallDistance = 0.0f;
    }

    public int air() {
        return player.getAir();
    }

    public void setAir(int value) {
        player.setAir(value);
    }

    // ==================================================================
    // 世界 / 方块
    // ==================================================================

    public BlockState state(Block block) {
        return block.getDefaultState();
    }

    public void setBlock(int dx, int dy, int dz, Block block) {
        BlockPos p = pos().add(dx, dy, dz);
        world().setBlockState(p, block.getDefaultState());
    }

    public void setBlockAt(BlockPos p, Block block) {
        world().setBlockState(p, block.getDefaultState());
    }

    public boolean breakAt(BlockPos p) {
        return world().breakBlock(p, true);
    }

    public void breakUnder() {
        world().breakBlock(pos().down(), true);
    }

    public void placeUnder(Block block) {
        BlockPos p = pos().down();
        if (world().getBlockState(p).isAir()) {
            world().setBlockState(p, block.getDefaultState());
        }
    }

    public void surroundWith(Block block) {
        BlockPos base = pos();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = 0; dy <= 2; dy++) {
                    if (dx == 0 && dz == 0) {
                        continue;
                    }
                    BlockPos p = base.add(dx, dy, dz);
                    if (!world().getBlockState(p).isSolidBlock(world(), p)) {
                        world().setBlockState(p, block.getDefaultState());
                    }
                }
            }
        }
    }

    public void cageWith(Block block) {
        BlockPos base = pos();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy <= 2; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dz == 0) {
                        continue;
                    }
                    world().setBlockState(base.add(dx, dy, dz), block.getDefaultState());
                }
            }
        }
        world().setBlockState(base.up(3), block.getDefaultState());
    }

    public void placeFloor(Block block, int radius) {
        BlockPos base = pos();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                BlockPos p = base.add(dx, -1, dz);
                world().setBlockState(p, block.getDefaultState());
            }
        }
    }

    public BlockPos randomNearbyPos(int radius) {
        return pos().add(ri(-radius, radius), ri(-2, 3), ri(-radius, radius));
    }

    public boolean isAirAt(int dx, int dy, int dz) {
        return world().getBlockState(pos().add(dx, dy, dz)).isAir();
    }

    public void lightningAt(double px, double py, double pz) {
        try {
            LightningEntity bolt = EntityType.LIGHTNING_BOLT.create(world());
            if (bolt != null) {
                bolt.refreshPositionAfterTeleport(px, py, pz);
                world().spawnEntity(bolt);
            }
        } catch (Throwable ignored) {
        }
    }

    public void lightning() {
        lightningAt(x(), y(), z());
    }

    public void lightningNear() {
        lightningAt(x() + ri(-6, 6), y(), z() + ri(-6, 6));
    }

    /** 爆炸（不破坏地形） */
    public void boom(double px, double py, double pz, float power) {
        world().createExplosion(null, px, py, pz, power, World.ExplosionSourceType.NONE);
    }

    public void boomAtPlayer(float power) {
        boom(x(), y(), z(), power);
    }

    /** 爆炸（会破坏地形，需要配置允许） */
    public void boomDestructive(double px, double py, double pz, float power) {
        World.ExplosionSourceType type = config.allowWorldDamage
                ? World.ExplosionSourceType.TNT
                : World.ExplosionSourceType.NONE;
        world().createExplosion(null, px, py, pz, power, type);
    }

    public void timeOfDay(long time) {
        world().setTimeOfDay(time);
    }

    public void setRaining(boolean raining, boolean thundering, int ticks) {
        world().setWeather(raining ? 0 : ticks, ticks, raining, thundering);
    }

    public void thunderstorm(int ticks) {
        setRaining(true, true, ticks);
    }

    public void clearWeather() {
        world().setWeather(6000, 0, false, false);
    }

    // ==================================================================
    // 生物
    // ==================================================================

    public <T extends Entity> T spawn(EntityType<T> type, double px, double py, double pz) {
        try {
            T e = type.create(world());
            if (e == null) {
                return null;
            }
            e.refreshPositionAndAngles(px, py, pz, rng.nextFloat() * 360f, 0f);
            world().spawnEntity(e);
            return e;
        } catch (Throwable t) {
            return null;
        }
    }

    /** 通用生成（配合 TrollPools 的实体数组使用） */
    public Entity spawnAny(EntityType<?> type, double px, double py, double pz) {
        try {
            Entity e = type.create(world());
            if (e == null) {
                return null;
            }
            e.refreshPositionAndAngles(px, py, pz, rng.nextFloat() * 360f, 0f);
            world().spawnEntity(e);
            return e;
        } catch (Throwable t) {
            return null;
        }
    }

    /** 随机刷一只敌对生物（配合 TrollPools.HOSTILE） */
    public Entity spawnHostileNear(EntityType<?> type, int distance) {
        Entity e = spawnAny(type, x() + ri(-distance, distance), y() + ri(0, 2), z() + ri(-distance, distance));
        if (e instanceof MobEntity mob) {
            mob.setTarget(player);
            mob.setPersistent();
        }
        return e;
    }

    /** 随机刷一只敌对生物（配合 TrollPools.HOSTILE） */
    public Entity spawnHostileBehind(EntityType<?> type) {
        double yaw = Math.toRadians(player.getYaw());
        double bx = x() - Math.sin(yaw) * 3.0;
        double bz = z() + Math.cos(yaw) * 3.0;
        Entity e = spawnAny(type, bx, y(), bz);
        if (e instanceof MobEntity mob) {
            mob.setTarget(player);
            mob.setPersistent();
        }
        return e;
    }

    public <T extends Entity> T spawnNear(EntityType<T> type, int radius) {
        return spawn(type, x() + ri(-radius, radius), y() + ri(0, 2), z() + ri(-radius, radius));
    }

    /** 生成一只会追着玩家打的怪 */
    public void spawnAngry(EntityType<? extends MobEntity> type, int distance) {
        MobEntity mob = spawn(type, x() + ri(-distance, distance), y() + ri(0, 2), z() + ri(-distance, distance));
        if (mob != null) {
            mob.setTarget(player);
            mob.setPersistent();
        }
    }

    public void spawnBehind(EntityType<? extends MobEntity> type) {
        double yaw = Math.toRadians(player.getYaw());
        double bx = x() - Math.sin(yaw) * 3.0;
        double bz = z() + Math.cos(yaw) * 3.0;
        MobEntity mob = spawn(type, bx, y(), bz);
        if (mob != null) {
            mob.setTarget(player);
            mob.setPersistent();
        }
    }

    public void giveMobCustomName(Entity entity, String name) {
        if (entity != null) {
            entity.setCustomName(Text.literal(name));
            entity.setCustomNameVisible(true);
        }
    }

    public long nearbyMobCount(int radius) {
        return world().getEntitiesByClass(Entity.class,
                player.getBoundingBox().expand(radius), e -> e instanceof MobEntity).size();
    }

    // ==================================================================
    // 客户端整活（需要客户端安装本模组）
    // ==================================================================

    public void glitch(String id, int ticks) {
        glitch(id, ticks, 1);
    }

    public void glitch(String id, int ticks, int power) {
        if (!config.clientGlitches) {
            return;
        }
        if (GlitchIds.FAKE_KICK.equals(id) && !config.fakeKick) {
            return;
        }
        int scaled = (int) Math.max(5, ticks * config.glitchPower);
        GlitchPayload.send(player, id, scaled, power);
    }

    public boolean clientConnected() {
        return true;
    }

    // ==================================================================
    // 杂项
    // ==================================================================

    public void playHurtSound() {
        soundSelf(SoundEvents.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
    }

    public void playLevelUp() {
        soundSelf(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
    }

    public void flashTotem() {
        particle(ParticleTypes.TOTEM_OF_UNDYING, 40);
        soundSelf(SoundEvents.ITEM_TOTEM_USE, 1.0f, 1.0f);
    }

    public void smokeBurst() {
        particle(ParticleTypes.LARGE_SMOKE, 50);
    }

    public void flameBurst() {
        particle(ParticleTypes.FLAME, 40);
    }

    public void explosionFx() {
        particle(ParticleTypes.EXPLOSION_EMITTER, 1);
        soundSelf(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
    }

    /** 强制关闭当前打开的界面 */
    public void closeScreen() {
        player.closeHandledScreen();
    }

    // ==================================================================
    // 第二批 API：多人 / 社交
    // ==================================================================

    /** 随机另一名在线玩家（单人时返回 null） */
    public ServerPlayerEntity other() {
        return randomOtherPlayer();
    }

    public boolean hasOthers() {
        return allPlayers().size() > 1;
    }

    /** 与另一名玩家交换位置 */
    public void swapWith(ServerPlayerEntity o) {
        if (o == null) {
            return;
        }
        Vec3d mine = player.getPos();
        double ox = o.getX();
        double oy = o.getY();
        double oz = o.getZ();
        o.requestTeleport(mine.x, mine.y, mine.z);
        o.networkHandler.requestTeleport(mine.x, mine.y, mine.z, o.getYaw(), o.getPitch());
        teleport(ox, oy, oz);
    }

    public void teleportOtherTo(ServerPlayerEntity o, double tx, double ty, double tz) {
        if (o == null) {
            return;
        }
        o.requestTeleport(tx, ty, tz);
        o.networkHandler.requestTeleport(tx, ty, tz, o.getYaw(), o.getPitch());
    }

    public void teleportOtherToMe(ServerPlayerEntity o) {
        teleportOtherTo(o, x(), y(), z());
    }

    public void damageOther(ServerPlayerEntity o, float amount) {
        if (o == null) {
            return;
        }
        o.damage(world().getDamageSources().generic(), amount);
    }

    public void lightningAtPlayer(ServerPlayerEntity o) {
        if (o != null) {
            lightningAt(o.getX(), o.getY(), o.getZ());
        }
    }

    public void scatterOther(ServerPlayerEntity o, int range) {
        if (o != null) {
            teleportOtherTo(o, o.getX() + ri(-range, range), o.getY() + ri(-4, 10),
                    o.getZ() + ri(-range, range));
        }
    }

    public void giveOther(ServerPlayerEntity o, Item item, int count) {
        if (o == null) {
            return;
        }
        ItemStack st = new ItemStack(item, count);
        if (!o.giveItemStack(st)) {
            o.dropItem(st, false);
        }
    }

    public void msgToOther(ServerPlayerEntity o, String text) {
        if (o != null) {
            o.sendMessage(Text.literal(text), false);
        }
    }

    public void sendToAll(String text) {
        server.getPlayerManager().broadcast(Text.literal(text), false);
    }

    public void soundToAll(SoundEvent sound, float volume, float pitch) {
        for (ServerPlayerEntity p : allPlayers()) {
            p.getWorld().playSound(null, p.getX(), p.getY(), p.getZ(), sound,
                    SoundCategory.MASTER, volume, pitch);
        }
    }

    public void titleToAll(String title, String subtitle) {
        String t = title == null ? "" : title;
        String s = subtitle == null ? "" : subtitle;
        for (ServerPlayerEntity p : allPlayers()) {
            p.networkHandler.sendPacket(new TitleFadeS2CPacket(10, 50, 20));
            p.networkHandler.sendPacket(new TitleS2CPacket(Text.literal(t)));
            p.networkHandler.sendPacket(new SubtitleS2CPacket(Text.literal(s)));
        }
    }

    public void effectAll(StatusEffect effect, int ticks, int amplifier) {
        for (ServerPlayerEntity p : allPlayers()) {
            p.addStatusEffect(new StatusEffectInstance(effect, ticks, amplifier));
        }
    }

    /** 把半径内所有其他玩家拉到自己身边 */
    public void pullPlayersToMe(double radius) {
        for (ServerPlayerEntity p : world().getPlayers()) {
            if (p == player) {
                continue;
            }
            if (p.squaredDistanceTo(player) <= radius * radius) {
                teleportOtherToMe(p);
            }
        }
    }

    // ==================================================================
    // 第二批 API：骑乘 / 姿态
    // ==================================================================

    /** 强制骑上一只生物 */
    public void mount(EntityType<?> type) {
        Entity e = spawnAny(type, x(), y(), z());
        if (e != null) {
            try {
                player.startRiding(e, true);
            } catch (Throwable ignored) {
            }
        }
    }

    public void dismount() {
        player.stopRiding();
    }

    public void setSneaking(boolean value) {
        player.setSneaking(value);
    }

    public void setSprinting(boolean value) {
        player.setSprinting(value);
    }

    public void setInvisible(boolean value) {
        player.setInvisible(value);
    }

    /** 失重（用飘浮效果模拟） */
    public void noGravity(int ticks) {
        effect(StatusEffects.LEVITATION, ticks, 0);
    }

    /** 冻结：极强缓慢 + 虚弱 */
    public void freeze(int ticks) {
        effect(StatusEffects.SLOWNESS, ticks, 200);
        effect(StatusEffects.WEAKNESS, ticks, 200);
    }

    // ==================================================================
    // 第二批 API：TNT / 下落方块 / 地形
    // ==================================================================

    public void spawnTnt(double px, double py, double pz, int fuse) {
        try {
            TntEntity tnt = new TntEntity(world(), px, py, pz, null);
            tnt.setFuse(Math.max(1, fuse));
            world().spawnEntity(tnt);
        } catch (Throwable ignored) {
        }
    }

    /** 头顶掉一块方块（不破坏地形） */
    public void spawnFallingBlock(Block block) {
        try {
            FallingBlockEntity e = FallingBlockEntity.spawnFromBlock(world(), pos().up(6),
                    block.getDefaultState());
            if (e != null) {
                e.dropItem = false;
            }
        } catch (Throwable ignored) {
        }
    }

    /** 陨石雨：配置允许破坏地形时用 TNT，否则改用铁砧（只砸人不毁地） */
    public void meteorShower(int count, double height) {
        boolean damage = config.allowWorldDamage;
        for (int i = 0; i < count; i++) {
            double ox = x() + ri(-10, 10);
            double oz = z() + ri(-10, 10);
            double oy = y() + height + ri(0, 8);
            if (damage) {
                spawnTnt(ox, oy, oz, ri(35, 70));
            } else {
                try {
                    FallingBlockEntity e = FallingBlockEntity.spawnFromBlock(world(),
                            new BlockPos((int) ox, (int) oy, (int) oz), Blocks.ANVIL.getDefaultState());
                    if (e != null) {
                        e.dropItem = false;
                    }
                } catch (Throwable ignored) {
                }
            }
        }
        sound(pick(TrollPools.ULTRA), 1.0f, 0.8f);
    }

    /** 在玩家脚下挖一个深坑（避开基岩） */
    public void voidPit(int radius, int depth) {
        BlockPos base = pos();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = 0; dy < depth; dy++) {
                    BlockPos p = base.add(dx, -dy, dz);
                    if (p.getY() <= world().getBottomY() + 1) {
                        continue;
                    }
                    BlockState st = world().getBlockState(p);
                    if (st.isAir() || st.getBlock() == Blocks.BEDROCK) {
                        continue;
                    }
                    world().setBlockState(p, Blocks.AIR.getDefaultState());
                }
            }
        }
    }

    /** 脚下铺岩浆（不允许破坏地形时退化为岩浆块） */
    public void lavaPit(int radius) {
        if (!config.allowWorldDamage) {
            placeFloor(Blocks.MAGMA_BLOCK, radius);
            return;
        }
        BlockPos base = pos();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                world().setBlockState(base.add(dx, -1, dz), Blocks.LAVA.getDefaultState());
            }
        }
    }

    /** 随机撒方块 */
    public void scatterBlocks(Block block, int count) {
        for (int i = 0; i < count; i++) {
            BlockPos p = randomNearbyPos(6);
            if (world().getBlockState(p).isAir()) {
                world().setBlockState(p, block.getDefaultState());
            }
        }
    }

    // ==================================================================
    // 第二批 API：时间 / 假 UI
    // ==================================================================

    public void timeSkip(long ticks) {
        world().setTimeOfDay(Math.floorMod(world().getTimeOfDay() + ticks, 24000L));
    }

    public void setWeatherRandom() {
        int r = ri(3);
        if (r == 0) {
            clearWeather();
        } else if (r == 1) {
            setRaining(true, false, 600);
        } else {
            thunderstorm(600);
        }
    }

    /** 假成就弹窗 */
    public void fakeAchievement(String achievementTitle, String desc) {
        playLevelUp();
        title("§e§l成就达成！", "§f" + achievementTitle + "§7 - " + desc);
        msg("§e" + playerName() + " §7获得了成就 §f[" + achievementTitle + "]");
    }

    /** 假死亡提示（不死） */
    public void fakeDeathScreen(String cause) {
        title("§c§l你死了！", "§7" + cause);
        soundSelf(SoundEvents.ENTITY_PLAYER_DEATH, 1.0f, 1.0f);
    }

    /** 假自动保存 */
    public void fakeSaveNotice() {
        actionBar("§7正在保存世界...");
        msg("§7[系统] 世界数据已保存。");
    }

    public String format(String template) {
        return template.replace("%p", playerName());
    }
}
