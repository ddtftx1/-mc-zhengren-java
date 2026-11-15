package com.zhengren.trollmod.core;

/**
 * 客户端整活效果的 ID 常量表。
 * 服务端事件里请使用这些常量调用 {@code ctx.glitch(GlitchIds.XXX, ticks, power)}，
 * 避免手写字符串写错。
 */
public final class GlitchIds {

    /** 假崩溃界面（点退出会真的退出游戏！） */
    public static final String FAKE_CRASH = "fakecrash";
    /** 假掉线界面 */
    public static final String FAKE_KICK = "fakekick";
    /** 隐藏 HUD */
    public static final String HIDE_HUD = "hidehud";
    /** 视角持续旋转 */
    public static final String SPIN = "spin";
    /** 画面抖动 */
    public static final String SHAKY = "shaky";
    /** 视野疯狂伸缩 */
    public static final String FOV_WILD = "fov_wild";
    /** 视野锁死在 28（像失焦） */
    public static final String FOV_ZOOM = "fov_zoom";
    /** 鼠标灵敏度归零 */
    public static final String SENS_ZERO = "sens_zero";
    /** 鼠标灵敏度拉满 */
    public static final String SENS_MAX = "sens_max";
    /** 反转鼠标 Y 轴 */
    public static final String INVERT_MOUSE = "invert_mouse";
    /** 上下视角反转 */
    public static final String INVERT_PITCH = "invert_pitch";
    /** 锁定快捷栏槽位 */
    public static final String HOTBAR_LOCK = "hotbarlock";
    /** 快捷栏乱跳 */
    public static final String HOTBAR_JUMP = "hotbarjump";
    /** 强制关闭当前界面 */
    public static final String CLOSE_SCREEN = "close_screen";
    /** 黑屏 */
    public static final String BLACKOUT = "blackout";
    /** 跳回标题界面（其实还在游戏里） */
    public static final String MENU_TEASE = "menu_tease";
    /** 闪白 + 巨响 */
    public static final String FLASHBANG = "flashbang";
    /** 一次性剧烈甩视角 */
    public static final String SHAKE_BURST = "shake_burst";
    /** 假内存溢出 */
    public static final String FAKE_OOM = "fake_oom";
    /** 假存档损坏 */
    public static final String FAKE_CORRUPT = "fake_corrupt";
    /** 假死亡提示 */
    public static final String FAKE_DEATH = "fake_death";
    /** 聊天栏被刷屏 */
    public static final String CHAT_SPAM = "chat_spam";
    /** 随机恐怖音效（纯客户端） */
    public static final String RANDOM_SOUND = "random_sound";
    /** 持续眩晕式视角乱转 */
    public static final String NAUSEA_SHADER = "nausea_shader";

    // ================= 第二批：结局级画面 =================

    /** 假永久封禁界面（带申诉码，退出按钮是真的） */
    public static final String FAKE_BAN = "fake_ban";
    /** 假"正在更新，请勿关闭游戏"（进度卡在 99%） */
    public static final String FAKE_UPDATE = "fake_update";
    /** 假"正在重新加载世界数据"（进度卡死） */
    public static final String WORLD_RELOAD = "world_reload";
    /** 假病毒/木马警告 */
    public static final String FAKE_VIRUS = "fake_virus";
    /** 输入反转：前进变后退 */
    public static final String INVERT_CONTROLS = "invert_controls";
    /** 输入冻结：灵敏度归零 + 隐藏 HUD + 视距锁死 */
    public static final String FREEZE_INPUT = "freeze_input";
    /** 画面撕裂：剧烈抖动 + 视野乱跳 */
    public static final String SCREEN_TEAR = "screen_tear";
    /** 海量假聊天刷屏（比 CHAT_SPAM 猛五倍） */
    public static final String MASS_CHAT = "mass_chat";
    /** 长时间硬黑屏 + 心跳 */
    public static final String HARD_BLACKOUT = "hard_blackout";
    /** 假成就弹窗 */
    public static final String FAKE_ACHIEVEMENT = "fake_achievement";
    /** 假自动保存提示 */
    public static final String FAKE_SAVING = "fake_saving";

    public static final String[] ALL = {
            FAKE_CRASH, FAKE_KICK, HIDE_HUD, SPIN, SHAKY, FOV_WILD, FOV_ZOOM, SENS_ZERO,
            SENS_MAX, INVERT_MOUSE, INVERT_PITCH, HOTBAR_LOCK, HOTBAR_JUMP, CLOSE_SCREEN,
            BLACKOUT, MENU_TEASE, FLASHBANG, SHAKE_BURST, FAKE_OOM, FAKE_CORRUPT,
            FAKE_DEATH, CHAT_SPAM, RANDOM_SOUND, NAUSEA_SHADER,
            FAKE_BAN, FAKE_UPDATE, WORLD_RELOAD, FAKE_VIRUS, INVERT_CONTROLS, FREEZE_INPUT,
            SCREEN_TEAR, MASS_CHAT, HARD_BLACKOUT, FAKE_ACHIEVEMENT, FAKE_SAVING
    };

    /**
     * 会**接管整块画面**的整活（占满全屏、吞掉 Esc 与点击）。
     *
     * <p>这一组必须限时并且**绝不能长于十几秒**：它们会把原版的死亡/重生界面一起顶掉，
     * 一旦持续过久，玩家会卡在"已死亡但按不到重生"的死锁里，而服务端仍在照常整他。
     * 服务端在 {@code TrollContext.glitch} 里对它们单独做了时长上限。</p>
     */
    public static final String[] SCREEN_TAKEOVER = {
            FAKE_CRASH, FAKE_KICK, BLACKOUT, HARD_BLACKOUT,
            MENU_TEASE, FAKE_BAN, FAKE_UPDATE, WORLD_RELOAD
    };

    /** 该整活是否会接管整块画面。 */
    public static boolean isScreenTakeover(String id) {
        for (String s : SCREEN_TAKEOVER) {
            if (s.equals(id)) {
                return true;
            }
        }
        return false;
    }

    private GlitchIds() {
    }
}
