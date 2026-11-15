package com.zhengren.trollmod.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * 客户端整活管理器：接收服务端下发的 glitch id，把玩家的画面/操作彻底搞乱。
 * 所有效果都会在持续时间内自动复原。
 */
public final class ClientGlitchManager {

    public static final Map<String, Integer> ACTIVE = new LinkedHashMap<>();
    public static final Map<String, Integer> POWER = new LinkedHashMap<>();

    private static final Random RNG = new Random();
    private static final String[] FAKE_NAMES = {
            "Herobrine", "Notch", "Dinnerbone", "Steve", "Alex", "jeb_", "Grumm",
            "Dinnerbone", "§k你是傻逼§r", "系统", "Dream", "一名玩家"
    };
    private static final String[] FAKE_LINES = {
            "哈哈哈哈哈", "这也太惨了吧", "别玩了，删游戏吧", "你被盯上了",
            "这个模组真好玩", "我刚刚把你家炸了", "快跑", "它就在你身后",
            "§c你确定要继续吗？", "我已经看着你 30 分钟了", "别回头",
            "服务器将在 3 秒后关闭", "你的存档正在被上传", "Game Over",
            "这波啊，这波是心态爆炸", "笑死，根本停不下来", "你怎么还活着",
            "直接退游吧兄弟", "我已经截图了", "下一个就是你"
    };

    private static final String[] FAKE_ACHIEVEMENTS = {
            "资深受害者", "背包空空如也", "死于自己家", "见过鬼", "原地升天",
            "钻石过敏", "把 TNT 当晚饭", "与苦力怕共舞", "被模组作者针对", "永远的新手"
    };

    private static boolean saved = false;
    private static boolean oldHudHidden = false;
    private static int oldFov = 70;
    private static double oldSens = 0.5;
    private static int lockedSlot = 0;
    private static Screen ownScreen = null;

    /**
     * 会接管整块画面的整活：它们占满全屏、吞掉 Esc 与点击。
     *
     * <p>这一组必须受"硬上限"保护 —— 它们会把原版的死亡/重生界面一起顶掉。</p>
     */
    private static final java.util.Set<String> SCREEN_GLITCHES = java.util.Set.of(
            "fakecrash", "fakekick", "blackout", "hard_blackout",
            "menu_tease", "fake_ban", "fake_update", "world_reload");

    /**
     * 画面接管类整活的硬上限（tick）。无论服务端怎么续期，连续占用到这个时长就强制释放。
     * 这是防"永远卡在黑屏点不动"死锁的最后一道保险：300 tick = 15 秒。
     */
    private static final int MAX_SCREEN_HOLD = 300;

    /** 画面接管类整活已连续占用的 tick 数。 */
    private static int screenHold = 0;

    private ClientGlitchManager() {
    }

    // ------------------------------------------------------------------

    public static boolean has(String id) {
        return ACTIVE.containsKey(id);
    }

    private static int power(String id) {
        Integer p = POWER.get(id);
        return p == null ? 1 : Math.max(1, p);
    }

    public static void clear(String id) {
        ACTIVE.remove(id);
        POWER.remove(id);
    }

    public static void trigger(String id, int durationTicks, int power) {
        int dur = Math.max(5, durationTicks);
        ACTIVE.merge(id, dur, Math::max);
        POWER.merge(id, Math.max(1, power), Math::max);
        saveOnce();

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) {
            return;
        }
        // 死亡时不允许"画面接管"类整活抢屏：否则会把原版的死亡/重生界面顶掉，
        // 玩家永远按不到重生按钮（曾经的真实死锁）。
        if (SCREEN_GLITCHES.contains(id)
                && (mc.player.getHealth() <= 0.0f || mc.currentScreen instanceof DeathScreen)) {
            clear(id);
            return;
        }
        if ("fakecrash".equals(id)) {
            ownScreen = new FakeCrashScreen();
            mc.setScreen(ownScreen);
        } else if ("fakekick".equals(id)) {
            ownScreen = new FakeKickScreen();
            mc.setScreen(ownScreen);
        } else if ("blackout".equals(id)) {
            ownScreen = new BlackoutScreen();
            mc.setScreen(ownScreen);
        } else if ("menu_tease".equals(id)) {
            ownScreen = new TitleScreen();
            mc.setScreen(ownScreen);
        } else if ("close_screen".equals(id)) {
            mc.setScreen(null);
            clear(id);
        } else if ("flashbang".equals(id)) {
            mc.inGameHud.setTitleTicks(0, 20, 10);
            mc.inGameHud.setTitle(Text.literal("§f§l!"));
            mc.inGameHud.setSubtitle(Text.literal("§7别慌"));
            mc.player.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
            clear(id);
        } else if ("fake_oom".equals(id)) {
            spamChat(mc, "§c[警告] java.lang.OutOfMemoryError: Java heap space");
            spamChat(mc, "§c[警告] 内存不足，正在强制清理区块...");
            mc.inGameHud.setTitleTicks(0, 60, 20);
            mc.inGameHud.setTitle(Text.literal("§4§l内存溢出"));
            mc.inGameHud.setSubtitle(Text.literal("§c正在释放内存..."));
        } else if ("fake_corrupt".equals(id)) {
            mc.inGameHud.setTitleTicks(0, 80, 20);
            mc.inGameHud.setTitle(Text.literal("§4§l世界数据已损坏"));
            mc.inGameHud.setSubtitle(Text.literal("§c区块文件校验失败 (chunk -9999, -9999)"));
        } else if ("fake_death".equals(id)) {
            mc.inGameHud.setTitleTicks(0, 60, 20);
            mc.inGameHud.setTitle(Text.literal("§c你死了！"));
            mc.inGameHud.setSubtitle(Text.literal("§7死因：§f被自己蠢死"));
            mc.player.playSound(SoundEvents.ENTITY_PLAYER_DEATH, 1.0f, 1.0f);
        } else if ("fake_ban".equals(id)) {
            ownScreen = new FakeBanScreen();
            mc.setScreen(ownScreen);
        } else if ("fake_update".equals(id)) {
            ownScreen = new FakeUpdatingScreen("正在下载资源文件", "已下载 99%，请不要关闭游戏");
            mc.setScreen(ownScreen);
        } else if ("world_reload".equals(id)) {
            ownScreen = new FakeUpdatingScreen("正在重新加载世界数据", "区块校验中…… 99%");
            mc.setScreen(ownScreen);
        } else if ("hard_blackout".equals(id)) {
            ownScreen = new BlackoutScreen();
            mc.setScreen(ownScreen);
            mc.player.playSound(SoundEvents.ENTITY_WARDEN_HEARTBEAT, 1.0f, 0.7f);
        } else if ("fake_virus".equals(id)) {
            spamChat(mc, "§4[安全警告] 检测到可疑模组：zhengren.jar");
            spamChat(mc, "§4[安全警告] 正在隔离…… 隔离失败");
            spamChat(mc, "§4[安全警告] 你的世界正在被加密");
            mc.inGameHud.setTitleTicks(0, 70, 20);
            mc.inGameHud.setTitle(Text.literal("§4§l检测到病毒"));
            mc.inGameHud.setSubtitle(Text.literal("§c别慌，是骗你的"));
            mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 1.0f, 0.5f);
        } else if ("fake_achievement".equals(id)) {
            mc.inGameHud.setTitleTicks(0, 60, 20);
            mc.inGameHud.setTitle(Text.literal("§e§l成就达成！"));
            mc.inGameHud.setSubtitle(Text.literal("§f"
                    + FAKE_ACHIEVEMENTS[RNG.nextInt(FAKE_ACHIEVEMENTS.length)]));
            mc.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        } else if ("fake_saving".equals(id)) {
            spamChat(mc, "§7[系统] 正在保存世界数据...");
            spamChat(mc, "§7[系统] 保存完成");
        }
        if ("shake_burst".equals(id)) {
            burst(mc, 12 + power * 3);
            clear(id);
        }
    }

    private static void burst(MinecraftClient mc, int iterations) {
        ClientPlayerEntity p = mc.player;
        if (p == null) {
            return;
        }
        for (int i = 0; i < iterations; i++) {
            p.setYaw(p.getYaw() + (RNG.nextFloat() * 40f - 20f));
            p.setPitch(Math.max(-90f, Math.min(90f, p.getPitch() + (RNG.nextFloat() * 30f - 15f))));
        }
    }

    private static void spamChat(MinecraftClient mc, String line) {
        mc.inGameHud.getChatHud().addMessage(Text.literal(line));
    }

    // ------------------------------------------------------------------

    public static void tick(MinecraftClient mc) {
        if (ACTIVE.isEmpty()) {
            if (saved) {
                restore(mc);
            }
            return;
        }
        ACTIVE.replaceAll((k, v) -> v - 1);
        ACTIVE.entrySet().removeIf(e -> e.getValue() <= 0);
        POWER.keySet().removeIf(k -> !ACTIVE.containsKey(k));
        if (ACTIVE.isEmpty()) {
            restore(mc);
            return;
        }
        try {
            apply(mc);
        } catch (Throwable ignored) {
        }
    }

    private static void apply(MinecraftClient mc) {
        ClientPlayerEntity p = mc.player;
        if (p == null) {
            ACTIVE.clear();
            restore(mc);
            return;
        }

        // ==============================================================
        // 安全阀 1：玩家死亡时绝不接管画面
        // --------------------------------------------------------------
        // 曾经的真实死锁：玩家死亡 -> 客户端弹出重生界面 -> 下一 tick 被
        // setScreen(BlackoutScreen) 顶掉 -> 永远按不到"重生"按钮；
        // 而服务端以为玩家还在线，继续刷事件、继续下发黑屏，形成死锁。
        // ==============================================================
        if (p.getHealth() <= 0.0f || mc.currentScreen instanceof DeathScreen) {
            releaseScreenGlitches(mc);
            return;
        }

        // ==============================================================
        // 安全阀 2：画面接管类整活的硬上限
        // --------------------------------------------------------------
        // 服务端可能不断续期（ACTIVE.merge 取 max 而不是覆盖），一旦连续占用
        // 超过 MAX_SCREEN_HOLD，就强制释放，保证玩家一定能拿回控制权。
        // ==============================================================
        if (isHoldingScreen()) {
            screenHold++;
            if (screenHold > MAX_SCREEN_HOLD) {
                releaseScreenGlitches(mc);
                return;
            }
        } else {
            screenHold = 0;
        }

        if (has("hidehud")) {
            mc.options.hudHidden = true;
        }
        if (has("sens_zero")) {
            mc.options.getMouseSensitivity().setValue(0.0);
        }
        if (has("sens_max")) {
            mc.options.getMouseSensitivity().setValue(1.0);
        }
        if (has("invert_mouse")) {
            // 原版 GameOptions.invertYMouse 在 1.20.1 是 private，无法直接改；
            // 改为每 tick 上下颠倒，达到"鼠标彻底疯了"的效果
            p.setPitch(-p.getPitch());
        }
        if (has("fov_wild") && mc.world != null && mc.world.getTime() % 6 == 0) {
            mc.options.getFov().setValue(25 + RNG.nextInt(95));
        }
        if (has("fov_zoom")) {
            mc.options.getFov().setValue(28);
        }
        if (has("spin")) {
            p.setYaw(p.getYaw() + 7.0f * power("spin"));
        }
        if (has("shaky")) {
            p.setYaw(p.getYaw() + (RNG.nextFloat() * 6f - 3f) * power("shaky"));
            p.setPitch(Math.max(-90f, Math.min(90f,
                    p.getPitch() + (RNG.nextFloat() * 5f - 2.5f) * power("shaky"))));
        }
        if (has("invert_pitch")) {
            p.setPitch(-p.getPitch());
        }
        if (has("hotbarlock")) {
            p.getInventory().selectedSlot = lockedSlot;
        }
        if (has("hotbarjump") && mc.world != null && mc.world.getTime() % 5 == 0) {
            p.getInventory().selectedSlot = RNG.nextInt(9);
        }
        if (has("fakecrash") && !(mc.currentScreen instanceof FakeCrashScreen)) {
            mc.setScreen(new FakeCrashScreen());
        }
        if (has("fakekick") && !(mc.currentScreen instanceof FakeKickScreen)) {
            mc.setScreen(new FakeKickScreen());
        }
        if (has("blackout") && !(mc.currentScreen instanceof BlackoutScreen)) {
            mc.setScreen(new BlackoutScreen());
        }
        if (has("chat_spam") && mc.world != null && mc.world.getTime() % 25 == 0) {
            spamChat(mc, "§f<§b" + FAKE_NAMES[RNG.nextInt(FAKE_NAMES.length)] + "§f> §r"
                    + FAKE_LINES[RNG.nextInt(FAKE_LINES.length)]);
        }
        if (has("random_sound") && mc.world != null && mc.world.getTime() % 20 == 0) {
            p.playSound(randomSound(), 1.0f, 0.8f + RNG.nextFloat() * 0.5f);
        }
        if (has("nausea_shader") && mc.world != null && mc.world.getTime() % 40 == 0) {
            p.setYaw(p.getYaw() + RNG.nextFloat() * 360f);
        }
        if (has("invert_controls")) {
            Vec3d v = p.getVelocity();
            p.setVelocity(-v.x, v.y, -v.z);
        }
        if (has("freeze_input")) {
            mc.options.getMouseSensitivity().setValue(0.0);
            mc.options.getFov().setValue(30);
            mc.options.hudHidden = true;
        }
        if (has("screen_tear")) {
            p.setYaw(p.getYaw() + (RNG.nextFloat() * 30f - 15f) * power("screen_tear"));
            p.setPitch(Math.max(-90f, Math.min(90f,
                    p.getPitch() + (RNG.nextFloat() * 24f - 12f))));
            if (mc.world != null && mc.world.getTime() % 4 == 0) {
                mc.options.getFov().setValue(30 + RNG.nextInt(100));
            }
        }
        if (has("mass_chat") && mc.world != null && mc.world.getTime() % 5 == 0) {
            spamChat(mc, "§f<§b" + FAKE_NAMES[RNG.nextInt(FAKE_NAMES.length)] + "§f> §r"
                    + FAKE_LINES[RNG.nextInt(FAKE_LINES.length)]);
        }
        if (has("fake_ban") && !(mc.currentScreen instanceof FakeBanScreen)) {
            mc.setScreen(new FakeBanScreen());
        }
        if (has("fake_update") && !(mc.currentScreen instanceof FakeUpdatingScreen)) {
            mc.setScreen(new FakeUpdatingScreen("正在下载资源文件", "已下载 99%，请不要关闭游戏"));
        }
        if (has("world_reload") && !(mc.currentScreen instanceof FakeUpdatingScreen)) {
            mc.setScreen(new FakeUpdatingScreen("正在重新加载世界数据", "区块校验中…… 99%"));
        }
        if (has("hard_blackout") && !(mc.currentScreen instanceof BlackoutScreen)) {
            mc.setScreen(new BlackoutScreen());
        }
    }

    private static net.minecraft.sound.SoundEvent randomSound() {
        net.minecraft.sound.SoundEvent[] pool = {
                SoundEvents.ENTITY_CREEPER_PRIMED,
                SoundEvents.ENTITY_ENDERMAN_SCREAM,
                SoundEvents.ENTITY_GHAST_SCREAM,
                SoundEvents.ENTITY_WITHER_SPAWN,
                SoundEvents.ENTITY_WARDEN_ROAR,
                SoundEvents.BLOCK_CHEST_OPEN,
                SoundEvents.ENTITY_ITEM_BREAK,
                SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE,
                SoundEvents.ENTITY_PHANTOM_SWOOP,
                SoundEvents.ENTITY_ENDER_DRAGON_GROWL,
                SoundEvents.ENTITY_PLAYER_BURP,
                SoundEvents.ENTITY_VILLAGER_NO
        };
        return pool[RNG.nextInt(pool.length)];
    }

    // ------------------------------------------------------------------

    private static void saveOnce() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (saved || mc.options == null) {
            return;
        }
        saved = true;
        oldHudHidden = mc.options.hudHidden;
        oldFov = mc.options.getFov().getValue();
        oldSens = mc.options.getMouseSensitivity().getValue();
        lockedSlot = mc.player == null ? 0 : mc.player.getInventory().selectedSlot;
    }

    /** 当前是否有"画面接管"类整活在起作用。 */
    private static boolean isHoldingScreen() {
        for (String id : SCREEN_GLITCHES) {
            if (ACTIVE.containsKey(id)) {
                return true;
            }
        }
        return false;
    }

    /** 是否是本模组自己弹出来的整活界面（只有这些才允许我们关掉）。 */
    private static boolean isOwnScreen(Screen s) {
        return s instanceof FakeCrashScreen || s instanceof FakeKickScreen || s instanceof BlackoutScreen
                || s instanceof TitleScreen || s instanceof FakeBanScreen
                || s instanceof FakeUpdatingScreen;
    }

    /**
     * 立刻清掉所有"画面接管"类整活，并把屏幕还给玩家。其他整活（抖动、假聊天等）不受影响。
     *
     * <p>这是死锁保险：死亡时、以及连续占用超过 {@link #MAX_SCREEN_HOLD} 时会调用。</p>
     */
    private static void releaseScreenGlitches(MinecraftClient mc) {
        for (String id : SCREEN_GLITCHES) {
            ACTIVE.remove(id);
            POWER.remove(id);
        }
        if (isOwnScreen(mc.currentScreen)) {
            mc.setScreen(null);
        }
        screenHold = 0;
    }

    public static void restore(MinecraftClient mc) {
        // 先把屏幕还给玩家（即使从未 saveOnce 过，也不能让别人替我们留着一个打不开的界面）
        if (isOwnScreen(mc.currentScreen)) {
            mc.setScreen(null);
        }
        screenHold = 0;
        // 只有确实保存过原始设置时才回写，否则会把玩家的真实设置覆盖成默认值
        if (saved && mc.options != null) {
            try {
                mc.options.hudHidden = oldHudHidden;
                mc.options.getFov().setValue(oldFov);
                mc.options.getMouseSensitivity().setValue(oldSens);
            } catch (Throwable ignored) {
            }
        }
        ownedScreen(null);
        saved = false;
    }

    private static void ownedScreen(Screen s) {
        ownScreen = s;
    }

    public static void reset(MinecraftClient mc) {
        ACTIVE.clear();
        POWER.clear();
        screenHold = 0;
        restore(mc);
    }
}
