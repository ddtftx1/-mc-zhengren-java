package com.zhengren.trollmod.core;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

/**
 * 安全常量池：所有 Minecraft 常量都集中在这里，事件作者直接 {@code ctx.pick(TrollPools.XXX)} 使用，
 * 避免在事件文件里手写容易写错的 API。
 */
public final class TrollPools {

    private TrollPools() {
    }

    // ================= 音效 =================

    /** 恐怖音效 */
    public static final SoundEvent[] HORROR = {
            SoundEvents.ENTITY_ENDERMAN_SCREAM,
            SoundEvents.ENTITY_GHAST_SCREAM,
            SoundEvents.ENTITY_WITHER_SPAWN,
            SoundEvents.ENTITY_ENDER_DRAGON_GROWL,
            SoundEvents.ENTITY_WARDEN_ROAR,
            SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE,
            SoundEvents.ENTITY_PHANTOM_SWOOP,
            SoundEvents.ENTITY_ZOMBIE_AMBIENT,
            SoundEvents.ENTITY_SPIDER_AMBIENT,
            SoundEvents.AMBIENT_CAVE.value(),
            SoundEvents.BLOCK_BELL_RESONATE,
            SoundEvents.ENTITY_WITHER_AMBIENT,
            SoundEvents.ENTITY_WARDEN_HEARTBEAT,
            SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT
    };

    /** 阴森 / 神经紧张音效 */
    public static final SoundEvent[] CREEPY = {
            SoundEvents.ENTITY_CREEPER_PRIMED,
            SoundEvents.ENTITY_ENDERMAN_STARE,
            SoundEvents.ENTITY_ENDERMAN_TELEPORT,
            SoundEvents.BLOCK_CHEST_OPEN,
            SoundEvents.BLOCK_CHEST_CLOSE,
            SoundEvents.ENTITY_ITEM_BREAK,
            SoundEvents.ENTITY_GHAST_AMBIENT,
            SoundEvents.ENTITY_SILVERFISH_AMBIENT,
            SoundEvents.ENTITY_ENDERMITE_AMBIENT,
            SoundEvents.ENTITY_SHULKER_AMBIENT,
            SoundEvents.ENTITY_VEX_AMBIENT,
            SoundEvents.ENTITY_PHANTOM_AMBIENT,
            SoundEvents.ENTITY_HUSK_AMBIENT,
            SoundEvents.ENTITY_DROWNED_AMBIENT,
            SoundEvents.ENTITY_STRAY_AMBIENT,
            SoundEvents.ENTITY_WOLF_GROWL,
            SoundEvents.ENTITY_HORSE_ANGRY,
            SoundEvents.ENTITY_BAT_AMBIENT
    };

    /** 搞笑 / 气人音效 */
    public static final SoundEvent[] FUNNY = {
            SoundEvents.ENTITY_PLAYER_BURP,
            SoundEvents.ENTITY_VILLAGER_NO,
            SoundEvents.ENTITY_VILLAGER_YES,
            SoundEvents.ENTITY_VILLAGER_HURT,
            SoundEvents.ENTITY_ITEM_PICKUP,
            SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
            SoundEvents.ENTITY_PLAYER_LEVELUP,
            SoundEvents.ENTITY_CAT_AMBIENT,
            SoundEvents.ENTITY_PARROT_AMBIENT,
            SoundEvents.ENTITY_GOAT_SCREAMING_AMBIENT,
            SoundEvents.ENTITY_BEE_LOOP_AGGRESSIVE,
            SoundEvents.UI_BUTTON_CLICK.value(),
            SoundEvents.ENTITY_SLIME_ATTACK,
            SoundEvents.ENTITY_MAGMA_CUBE_SQUISH,
            SoundEvents.BLOCK_ANVIL_LAND,
            SoundEvents.ENTITY_PLAYER_HURT
    };

    /** 巨响 / 爆炸 */
    public static final SoundEvent[] IMPACT = {
            SoundEvents.ENTITY_GENERIC_EXPLODE,
            SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
            SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT,
            SoundEvents.BLOCK_ANVIL_LAND,
            SoundEvents.BLOCK_ANVIL_DESTROY,
            SoundEvents.ENTITY_TNT_PRIMED,
            SoundEvents.ENTITY_FIREWORK_ROCKET_LARGE_BLAST,
            SoundEvents.ENTITY_WITHER_SHOOT,
            SoundEvents.ENTITY_ENDER_DRAGON_DEATH
    };

    // ================= 物品 =================

    /** 垃圾（用来塞满背包） */
    public static final Item[] JUNK = {
            Items.ROTTEN_FLESH, Items.POISONOUS_POTATO, Items.DEAD_BUSH, Items.STICK,
            Items.GRAVEL, Items.FLINT, Items.BONE, Items.STRING, Items.SPIDER_EYE,
            Items.PUFFERFISH, Items.CACTUS, Items.WHEAT_SEEDS, Items.COAL,
            Items.FEATHER, Items.LEATHER, Items.EGG, Items.SNOWBALL, Items.KELP,
            Items.SEAGRASS, Items.SAND, Items.DIRT, Items.COBBLESTONE, Items.PAPER,
            Items.SLIME_BALL, Items.INK_SAC, Items.GUNPOWDER, Items.REDSTONE,
            Items.BOWL, Items.BRICK, Items.SUGAR_CANE, Items.APPLE, Items.BREAD
    };

    /** 好东西（用来勾起贪念再拿走） */
    public static final Item[] LOOT = {
            Items.DIAMOND, Items.IRON_INGOT, Items.GOLD_INGOT, Items.EMERALD,
            Items.GOLDEN_APPLE, Items.BREAD, Items.COOKED_BEEF, Items.ARROW,
            Items.TORCH, Items.SHIELD, Items.IRON_SWORD, Items.BOW,
            Items.LEATHER_CHESTPLATE, Items.ENDER_PEARL, Items.EXPERIENCE_BOTTLE,
            Items.FIREWORK_ROCKET, Items.CAKE, Items.GOLDEN_CARROT, Items.IRON_PICKAXE,
            Items.ANVIL, Items.OBSIDIAN, Items.ELYTRA, Items.TOTEM_OF_UNDYING
    };

    /** 危险物品 */
    public static final Item[] DANGEROUS = {
            Items.TNT, Items.LAVA_BUCKET, Items.FLINT_AND_STEEL, Items.POWDER_SNOW_BUCKET,
            Items.PUFFERFISH, Items.SPIDER_EYE, Items.ROTTEN_FLESH, Items.POISONOUS_POTATO,
            Items.ENDER_PEARL, Items.WITHER_ROSE, Items.MAGMA_CREAM, Items.FIRE_CHARGE,
            Items.CACTUS, Items.SWEET_BERRIES
    };

    // ================= 实体 =================

    /** 常见敌对生物 */
    public static final EntityType<?>[] HOSTILE = {
            EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.CREEPER,
            EntityType.HUSK, EntityType.STRAY, EntityType.DROWNED, EntityType.CAVE_SPIDER,
            EntityType.SILVERFISH, EntityType.ENDERMITE, EntityType.WITCH, EntityType.PILLAGER,
            EntityType.VINDICATOR, EntityType.SLIME, EntityType.MAGMA_CUBE, EntityType.PHANTOM,
            EntityType.ZOMBIFIED_PIGLIN, EntityType.PIGLIN, EntityType.HOGLIN, EntityType.SHULKER
    };

    /** 精英怪（别乱刷）。注意：不含末影龙——在非末地维度生成它会导致服务端异常 */
    public static final EntityType<?>[] BOSS = {
            EntityType.ENDERMAN, EntityType.RAVAGER, EntityType.EVOKER, EntityType.ILLUSIONER,
            EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN, EntityType.BLAZE, EntityType.GHAST,
            EntityType.WARDEN, EntityType.WITHER, EntityType.GIANT
    };

    /** 无害但烦人的生物 */
    public static final EntityType<?>[] ANNOYING_MOBS = {
            EntityType.BAT, EntityType.BEE, EntityType.PARROT, EntityType.CHICKEN,
            EntityType.CAT, EntityType.WOLF, EntityType.GOAT, EntityType.LLAMA,
            EntityType.SQUID, EntityType.SILVERFISH, EntityType.VEX, EntityType.ALLAY,
            EntityType.FROG, EntityType.AXOLOTL, EntityType.TURTLE
    };

    /** 坐骑 / 交通工具（把人甩出去） */
    public static final EntityType<?>[] RIDEABLE = {
            EntityType.HORSE, EntityType.DONKEY, EntityType.MULE, EntityType.PIG,
            EntityType.STRIDER, EntityType.CAMEL, EntityType.LLAMA, EntityType.BOAT,
            EntityType.MINECART, EntityType.TNT
    };

    /** 投射物 */
    public static final EntityType<?>[] PROJECTILE = {
            EntityType.ARROW, EntityType.SPECTRAL_ARROW, EntityType.FIREBALL,
            EntityType.SMALL_FIREBALL, EntityType.DRAGON_FIREBALL, EntityType.SNOWBALL,
            EntityType.EGG, EntityType.ENDER_PEARL, EntityType.EXPERIENCE_BOTTLE,
            EntityType.POTION, EntityType.FIREWORK_ROCKET, EntityType.WITHER_SKULL,
            EntityType.TRIDENT, EntityType.LLAMA_SPIT
    };

    // ================= 方块 =================

    /** 整人常用方块 */
    public static final Block[] TROLL_BLOCKS = {
            Blocks.DIRT, Blocks.COBBLESTONE, Blocks.SAND, Blocks.GRAVEL, Blocks.OAK_PLANKS,
            Blocks.GLASS, Blocks.ICE, Blocks.PACKED_ICE, Blocks.SLIME_BLOCK, Blocks.HONEY_BLOCK,
            Blocks.MAGMA_BLOCK, Blocks.CACTUS, Blocks.SOUL_SAND, Blocks.NETHERRACK,
            Blocks.OBSIDIAN, Blocks.SPONGE, Blocks.TNT, Blocks.COBWEB, Blocks.SCAFFOLDING
    };

    /** 恶心人的方块 */
    public static final Block[] NASTY_BLOCKS = {
            Blocks.SLIME_BLOCK, Blocks.HONEY_BLOCK, Blocks.MAGMA_BLOCK, Blocks.CACTUS,
            Blocks.SOUL_SAND, Blocks.COBWEB, Blocks.TNT, Blocks.SPONGE, Blocks.ICE,
            Blocks.PACKED_ICE, Blocks.POWDER_SNOW, Blocks.SWEET_BERRY_BUSH
    };

    /** 极其过分（用之前想清楚） */
    public static final Block[] EVIL_BLOCKS = {
            Blocks.BEDROCK, Blocks.BARRIER, Blocks.OBSIDIAN, Blocks.LAVA, Blocks.WATER
    };

    // ================= 状态效果 =================

    /** 负面效果 */
    public static final StatusEffect[] BAD_EFFECTS = {
            StatusEffects.NAUSEA, StatusEffects.BLINDNESS, StatusEffects.SLOWNESS,
            StatusEffects.WEAKNESS, StatusEffects.MINING_FATIGUE, StatusEffects.POISON,
            StatusEffects.WITHER, StatusEffects.LEVITATION, StatusEffects.DARKNESS,
            StatusEffects.HUNGER, StatusEffects.UNLUCK, StatusEffects.GLOWING
    };

    /** 奇怪但不算纯粹的坏事 */
    public static final StatusEffect[] WEIRD_EFFECTS = {
            StatusEffects.JUMP_BOOST, StatusEffects.SPEED, StatusEffects.INVISIBILITY,
            StatusEffects.NIGHT_VISION, StatusEffects.WATER_BREATHING,
            StatusEffects.FIRE_RESISTANCE, StatusEffects.REGENERATION,
            StatusEffects.ABSORPTION, StatusEffects.CONDUIT_POWER,
            StatusEffects.DOLPHINS_GRACE, StatusEffects.SLOW_FALLING,
            StatusEffects.STRENGTH, StatusEffects.HASTE, StatusEffects.RESISTANCE
    };

    // ================= 粒子 =================

    /** 恐怖粒子 */
    public static final ParticleEffect[] SPOOKY_PARTICLES = {
            ParticleTypes.SOUL, ParticleTypes.SOUL_FIRE_FLAME, ParticleTypes.SCULK_SOUL,
            ParticleTypes.PORTAL, ParticleTypes.REVERSE_PORTAL, ParticleTypes.SMOKE,
            ParticleTypes.LARGE_SMOKE, ParticleTypes.ASH, ParticleTypes.WHITE_ASH,
            ParticleTypes.DRAGON_BREATH, ParticleTypes.WITCH, ParticleTypes.EFFECT,
            ParticleTypes.DAMAGE_INDICATOR, ParticleTypes.SQUID_INK
    };

    /** 爆炸粒子 */
    public static final ParticleEffect[] BOOM_PARTICLES = {
            ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, ParticleTypes.FLAME,
            ParticleTypes.LAVA, ParticleTypes.FIREWORK, ParticleTypes.FLASH,
            ParticleTypes.ELECTRIC_SPARK, ParticleTypes.CLOUD, ParticleTypes.SONIC_BOOM
    };

    /** 开心 / 嘲讽粒子 */
    public static final ParticleEffect[] TAUNT_PARTICLES = {
            ParticleTypes.HEART, ParticleTypes.HAPPY_VILLAGER, ParticleTypes.ANGRY_VILLAGER,
            ParticleTypes.NOTE, ParticleTypes.ENCHANT, ParticleTypes.CRIT,
            ParticleTypes.TOTEM_OF_UNDYING, ParticleTypes.POOF, ParticleTypes.SNEEZE,
            ParticleTypes.BUBBLE_POP, ParticleTypes.END_ROD
    };

    // ================= 文本 =================

    /** 假玩家名 */
    public static final String[] FAKE_NAMES = {
            "Herobrine", "Notch", "Dinnerbone", "Grumm", "jeb_", "Dream",
            "Technoblade", "Steve", "Alex", "服务器管理员", "系统", "一名玩家",
            "Notch的幽灵", "§k你是谁§r"
    };

    /** 假聊天内容 */
    public static final String[] FAKE_LINES = {
            "哈哈哈哈哈这也太惨了", "别玩了，删游戏吧", "你被盯上了", "这个模组真好玩",
            "我刚刚把你家炸了", "快跑", "它就在你身后", "我已经看着你 30 分钟了",
            "别回头", "服务器将在 3 秒后关闭", "你的存档正在被上传", "Game Over",
            "你今天运气不太好啊", "要帮忙吗？不帮", "我看见你死了 7 次了",
            "这个玩家有点菜", "你的床我拆了", "钻石在岩浆里", "有人在挖你的地基",
            "别挖了，下面什么都没有"
    };

    /** 假服务器公告 */
    public static final String[] FAKE_BROADCASTS = {
            "§c服务器将在 10 秒后重启",
            "§e检测到玩家作弊，正在处理……",
            "§c你的账号已在另一台设备登录",
            "§e管理员正在观察你",
            "§c世界边界正在收缩",
            "§e即将开启 PVP 模式",
            "§c你的存档已被标记为损坏",
            "§e服务器内存不足，正在关闭区块加载"
    };

    /** 假死因 */
    public static final String[] FAKE_DEATHS = {
            "被自己蠢死", "被空气击落", "被内心的恐惧吞噬", "因为手抖撞墙",
            "被一只看不见的苦力怕吓死", "死于社死", "被模组作者诅咒",
            "因为太菜被系统移除", "被自己的影子绊倒", "心跳过速"
    };

    /** 惊恐标题 */
    public static final String[] PANIC_TITLES = {
            "§4§l你身后有东西", "§c§l别动", "§4§l它来了", "§e§l快跑",
            "§c§l你被发现了", "§4§l倒计时开始", "§8§l你听得到吗",
            "§c§l游戏出现异常", "§4§l最后一秒"
    };

    /** 嘲讽语句 */
    public static final String[] TAUNTS = {
            "§7这就是你的极限吗？", "§7才刚开始而已。", "§7别急，还有更狠的。",
            "§7你的表情一定很精彩。", "§7再来一次？", "§7坚持住，快结束了。",
            "§7这次算你运气好。", "§7模组正在观察你的反应。", "§7你确定还要玩下去？",
            "§7下一个更刺激。"
    };

    // ================= 第二批：陷阱 / 机关 / 诱饵 =================

    /** 陷阱与机关方块 */
    public static final Block[] TRAP_BLOCKS = {
            Blocks.STONE_PRESSURE_PLATE, Blocks.OAK_PRESSURE_PLATE, Blocks.TRIPWIRE,
            Blocks.DISPENSER, Blocks.DROPPER, Blocks.PISTON, Blocks.STICKY_PISTON,
            Blocks.OBSERVER, Blocks.TARGET, Blocks.NOTE_BLOCK, Blocks.CAULDRON,
            Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE, Blocks.MAGMA_BLOCK, Blocks.POWDER_SNOW,
            Blocks.SWEET_BERRY_BUSH, Blocks.COBWEB, Blocks.CACTUS, Blocks.FIRE,
            Blocks.SOUL_FIRE, Blocks.HONEY_BLOCK, Blocks.SLIME_BLOCK, Blocks.SCAFFOLDING,
            Blocks.TNT, Blocks.REDSTONE_BLOCK, Blocks.LEVER, Blocks.TRIPWIRE_HOOK
    };

    /** 看起来值钱其实什么都没有的矿（用来骗人） */
    public static final Block[] ORE_TEASE = {
            Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.GOLD_ORE,
            Blocks.DEEPSLATE_GOLD_ORE, Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.ANCIENT_DEBRIS, Blocks.NETHER_GOLD_ORE, Blocks.LAPIS_ORE,
            Blocks.REDSTONE_ORE, Blocks.IRON_ORE, Blocks.COAL_ORE, Blocks.GILDED_BLACKSTONE
    };

    /** 奇怪但无害的物品 */
    public static final Item[] WEIRD_ITEMS = {
            Items.NAME_TAG, Items.LEAD, Items.BUCKET, Items.COMPASS, Items.CLOCK,
            Items.MAP, Items.SPYGLASS, Items.ECHO_SHARD, Items.AMETHYST_SHARD,
            Items.GLOW_INK_SAC, Items.HONEYCOMB, Items.SLIME_BALL, Items.FIREWORK_STAR,
            Items.GOAT_HORN, Items.WRITABLE_BOOK, Items.ARMOR_STAND, Items.PAINTING,
            Items.ITEM_FRAME, Items.FLOWER_POT, Items.BELL, Items.LANTERN,
            Items.SOUL_LANTERN, Items.CAMPFIRE, Items.BRUSH, Items.TORCHFLOWER,
            Items.PITCHER_POD, Items.MUSIC_DISC_CAT, Items.MUSIC_DISC_13
    };

    /** 神秘物品（看起来很厉害） */
    public static final Item[] MYSTERY_ITEMS = {
            Items.WITHER_ROSE, Items.RABBIT_FOOT, Items.PHANTOM_MEMBRANE, Items.GHAST_TEAR,
            Items.ENDER_EYE, Items.DRAGON_BREATH, Items.SHULKER_SHELL, Items.NAUTILUS_SHELL,
            Items.HEART_OF_THE_SEA, Items.ECHO_SHARD, Items.RECOVERY_COMPASS,
            Items.DISC_FRAGMENT_5, Items.SCULK_SENSOR, Items.CONDUIT
    };

    /** 无害动物军团 */
    public static final EntityType<?>[] FARM_ANIMALS = {
            EntityType.COW, EntityType.PIG, EntityType.SHEEP, EntityType.CHICKEN,
            EntityType.RABBIT, EntityType.HORSE, EntityType.DONKEY, EntityType.LLAMA,
            EntityType.CAT, EntityType.WOLF, EntityType.PARROT, EntityType.BEE,
            EntityType.FOX, EntityType.PANDA, EntityType.POLAR_BEAR, EntityType.TURTLE,
            EntityType.AXOLOTL, EntityType.FROG, EntityType.GOAT, EntityType.CAMEL,
            EntityType.SNIFFER, EntityType.OCELOT, EntityType.MULE, EntityType.STRIDER
    };

    /** 极小 / 极烦的生物 */
    public static final EntityType<?>[] TINY_ANNOYING = {
            EntityType.BAT, EntityType.SILVERFISH, EntityType.ENDERMITE, EntityType.VEX,
            EntityType.BEE, EntityType.ALLAY, EntityType.PARROT, EntityType.SQUID,
            EntityType.GLOW_SQUID, EntityType.TADPOLE, EntityType.CHICKEN
    };

    /** 结局级巨响 */
    public static final SoundEvent[] ULTRA = {
            SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
            SoundEvents.ENTITY_GENERIC_EXPLODE,
            SoundEvents.ENTITY_WITHER_SPAWN,
            SoundEvents.ENTITY_ENDER_DRAGON_DEATH,
            SoundEvents.ENTITY_WARDEN_SONIC_BOOM,
            SoundEvents.ENTITY_WARDEN_ROAR,
            SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE,
            SoundEvents.BLOCK_ANVIL_DESTROY,
            SoundEvents.ENTITY_GHAST_SCREAM,
            SoundEvents.BLOCK_END_PORTAL_SPAWN,
            SoundEvents.ENTITY_ENDER_DRAGON_GROWL,
            SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT,
            SoundEvents.ENTITY_WITHER_DEATH,
            SoundEvents.ENTITY_ENDER_DRAGON_FLAP
    };

    /** 环境级恐怖音（很轻，但会让人起鸡皮疙瘩） */
    public static final SoundEvent[] AMBIENT_CREEPY = {
            SoundEvents.AMBIENT_CAVE.value(),
            SoundEvents.AMBIENT_UNDERWATER_LOOP,
            SoundEvents.ENTITY_BAT_AMBIENT,
            SoundEvents.ENTITY_SQUID_AMBIENT,
            SoundEvents.ENTITY_CAT_STRAY_AMBIENT,
            SoundEvents.BLOCK_FIRE_AMBIENT,
            SoundEvents.BLOCK_LAVA_AMBIENT,
            SoundEvents.ENTITY_WOLF_HOWL,
            SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT,
            SoundEvents.ENTITY_DOLPHIN_AMBIENT,
            SoundEvents.ENTITY_ENDERMAN_AMBIENT,
            SoundEvents.BLOCK_WATER_AMBIENT
    };

    // ================= 第二批：文本 =================

    /** 假成就 */
    public static final String[] FAKE_ACHIEVEMENTS = {
            "资深受害者", "被整了一百次", "背包空空如也", "死于自己家", "被床炸飞",
            "见过鬼", "原地升天", "钻石过敏", "挖到基岩", "被自己蠢哭",
            "连续被雷劈三次", "把家建在岩浆上", "把 TNT 当晚饭", "与苦力怕共舞",
            "在虚空里思考人生", "被模组作者针对", "第 100 次死亡", "永远的新手"
    };

    /** 假加载提示 */
    public static final String[] FAKE_TIPS = {
            "提示：被整的时候保持微笑", "提示：这个模组不会删除你的存档",
            "提示：按 /zr off 可以活命", "提示：苦力怕不会真的炸你……吧",
            "提示：你的背包刚刚被洗过牌", "提示：本提示没有任何用处",
            "提示：看着你身后", "提示：不建议在床上睡觉", "提示：基岩真的很硬",
            "提示：模组正在记录你的表情", "提示：不要相信任何一颗钻石",
            "提示：当前难度建议降低"
    };

    /** 假任务 */
    public static final String[] FAKE_QUESTS = {
            "生存 10 分钟不被整", "把背包整理整齐（不可能）", "在基岩棺材里活下来",
            "找到不存在的钻石", "跑赢一只追你的僵尸", "不被雷劈中一次",
            "保持冷静（进度 0%）", "找到一个安全的角落", "相信这个世界", "重新开始"
    };

    /** 假封禁 / 假踢出理由 */
    public static final String[] FAKE_EXCUSES = {
            "检测到你的操作过于熟练", "服务器觉得你需要休息", "你的运气值已用尽",
            "系统随机抽中了你的名字", "有人举报你太强了", "你的键盘敲击频率异常",
            "你触发了隐藏成就：倒霉", "模组作者今天心情不好", "轮到你被整了",
            "这是服务器维护的一部分", "你的角色数据过于正常", "检测到过度自信"
    };

    /** 假管理员发言 */
    public static final String[] FAKE_OPERATOR_LINES = {
            "别玩了，我看着都心疼", "你已经被标记为「高危受害者」",
            "我给你的钻石是假的", "封禁倒计时：3…2…1…（开玩笑的）",
            "你的存档我已经备份了（骗你的）", "再被整一次就送你一组钻石（不会送的）",
            "正在观察你的反应", "请不要试图反抗"
    };

    /** 羞辱性物品名 */
    public static final String[] CURSED_NAMES = {
            "§k你确定要看吗§r", "不值钱的东西", "§c危险品", "别用这个",
            "模组作者的礼物", "假钻石", "上一任主人的遗物", "§e看起来很贵§r",
            "请勿食用", "最终解释权归本模组所有", "§7（已损坏）", "垃圾"
    };

    /** 安全常量数组（防止空指针） */
    public static void init() {
        // 触发类加载即可
    }
}
