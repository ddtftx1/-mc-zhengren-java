# 整人大师 —— 事件编写契约（必须严格遵守）

> 目标：往 `zhengren-mod` 里塞进**尽可能多、尽可能狠**的整人事件。
> 每个作者只负责**一个文件**，互不冲突。

---

## 0. 硬性规则（违反会导致整个工程编译失败）

1. **只允许修改你被分配到的那一个文件**：`src/main/java/com/zhengren/trollmod/events/<Xxx>Events.java`。
   绝对不要改 `core/`、`client/`、`command/`、`net/`、`build.gradle`、其他作者的 events 文件。
2. **不要运行 gradle / gradlew / 任何构建命令**（Lead 统一构建，并发构建会互相破坏）。
3. 不要写文件、不要联网、不要 `System.exit`、不要反射、不要 `Thread`、不要 `while(true)`。
4. 不要删除玩家存档/世界文件，不要调用任何操作系统命令。所有整活必须发生在**游戏内**。
5. 事件 ID 全模组唯一，格式 `小写前缀_小写名字`，例如 `world_sinkhole`。
6. 每个 `run(ctx -> {...})` 里不要抛异常（框架有 try/catch，但别依赖它）。
7. **必须保留** 类名、包名、`public static void register()` 方法签名和 `TrollEvents.category("...")` 调用。
8. 文件必须是 **UTF-8 无 BOM**，Java 17 语法（可以用 `var`、`switch` 表达式、`instanceof` 模式匹配）。
9. 中文文案可以随便写，越毒舌越好，但不要涉及现实世界的真实人身攻击、种族/性别歧视等。

---

## 1. 文件模板

```java
package com.zhengren.trollmod.events;

import com.zhengren.trollmod.core.GlitchIds;      // 只在需要客户端整活时 import
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;     // 只在需要常量池时 import
import com.zhengren.trollmod.core.TrollTier;
// 需要时再 import net.minecraft.*，但强烈建议不要（见第 4 节）

/**
 * 【分类名】分类事件。
 * 负责整活：一句话描述。
 */
public final class XxxEvents {

    private XxxEvents() {
    }

    public static void register() {
        TrollEvents.category("分类名");

        TrollEvents.reg(TrollEvent.of("xxx_事件id", "中文显示名", TrollTier.ANNOYING)
                .desc("一句话说明（会出现在 /zr info 里）")
                .weight(100)              // 权重，越大越常出现。默认 100
                .cooldownSeconds(60)      // 同一玩家两次触发的最小间隔（秒）。默认 45
                .once()                   // 可选：整局只触发一次
                .when(ctx -> !ctx.invEmpty())  // 可选：触发条件
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 1.0f);
                    ctx.msg("§7你听见了什么。");
                })
                .build());

        // ……继续写更多事件
    }
}
```

### 残酷等级怎么选

| 等级 | 含义 | 例子 |
|---|---|---|
| `TrollTier.PRANK` | 小整，笑一下就好 | 假音效、假聊天、给个土豆 |
| `TrollTier.ANNOYING` | 烦人，会皱眉 | 打乱背包、偷走物品、强制效果 |
| `TrollTier.BRUTAL` | 抓狂，会骂人 | 刷怪围殴、传送到远处、装备消失 |
| `TrollTier.NIGHTMARE` | 崩溃，会砸键盘 | 假崩溃界面、闪电劈脸、掉进岩浆 |
| `TrollTier.APOCALYPSE` | 地狱，会退游戏 | 极度过分的组合技、直接击杀 |

**要求：每个分类里 5 个等级都要有，别全是 PRANK。高阶事件权重给小一点（20~60）。**

---

## 2. `ctx` 可用方法清单（优先只用这些！）

### 查询
`ctx.player()` `ctx.world()` `ctx.server()` `ctx.cfg()` `ctx.playerName()` `ctx.pos()`
`ctx.x()` `ctx.y()` `ctx.z()` `ctx.vec()` `ctx.worldTime()` `ctx.gameTicks()` `ctx.rage()`
`ctx.addRage(double)` `ctx.ri(int)` `ctx.ri(int min,int max)` `ctx.rd(double,double)`
`ctx.rf(float,float)` `ctx.chance(double)` `ctx.pick(T[])` `ctx.pick(List<T>)`
`ctx.isOnGround()` `ctx.isFalling()` `ctx.isInLava()` `ctx.isInWater()` `ctx.isBurning()`
`ctx.health()` `ctx.foodLevel()` `ctx.invEmpty()` `ctx.allPlayers()` `ctx.randomOtherPlayer()`

### 聊天 / 文字
`ctx.msg(s)` 白字 · `ctx.ok(s)` 绿 · `ctx.warn(s)` 红 · `ctx.sys(s)` 灰色[系统]
`ctx.fakeChat(名字, 内容)` · `ctx.fakeBroadcast(内容)` · `ctx.fakeJoinLeave(名字, true/false)`
`ctx.actionBar(s)` · `ctx.title(主标题, 副标题)` · `ctx.title(t, sub, in, stay, out)` · `ctx.broadcast(s)`

### 声音 / 粒子
`ctx.sound(SoundEvent, vol, pitch)` · `ctx.sound(SoundEvent)` · `ctx.soundSelf(...)`
`ctx.soundAt(s, x, y, z, vol, pitch)` · `ctx.randomHorrorSound()`
`ctx.particle(ParticleEffect, count)` · `ctx.particleAt(e, x,y,z, count, spread)` · `ctx.particleAtPlayer(e, count)`

### 移动
`ctx.teleport(x,y,z)` · `ctx.teleportToPlayer(p)` · `ctx.scatter(range)` · `ctx.scatterFar(range)`
`ctx.lift(height)` · `ctx.launch(power)` · `ctx.fling(dx,dy,dz)` · `ctx.spin(度数)`
`ctx.setRotation(yaw,pitch)` · `ctx.randomRotation()`

### 背包 / 物品
`ctx.inv()` · `ctx.held()` · `ctx.give(Item,int)` · `ctx.give(ItemStack)` · `ctx.giveRandomJunk()`
`ctx.takeRandomItem()` · `ctx.clearInventory()` · `ctx.shuffleInventory()` · `ctx.scrambleHotbar()`
`ctx.dropHeld()` · `ctx.dropRandom()` · `ctx.dropEverything()` · `ctx.selectSlot(i)` · `ctx.selectedSlot()`
`ctx.randomSelectSlot()` · `ctx.renameHeld(name)` · `ctx.renameRandomItem(name)`
`ctx.damageRandomTool(int)` · `ctx.breakRandomTool()` · `ctx.enchantHeldRandomly()` · `ctx.curseHeld()`
`ctx.swapEquipmentRandomly()` · `ctx.fillWith(Item,count)` · `ctx.setCooldown(Item,ticks)`

### 状态 / 生命
`ctx.effect(StatusEffect, ticks, amplifier)` · `ctx.effectRandom(ticks, amp)` · `ctx.clearEffects()`
`ctx.removeEffect(e)` · `ctx.damage(amount)` · `ctx.damage("magic"/"fall"/"fire"/"lava"/"drown"/"starve"/"wither"/"freeze"/"outofworld", amount)`
`ctx.setHealth(f)` · `ctx.heal(f)` · `ctx.kill()` · `ctx.setFire(秒)` · `ctx.extinguish()`
`ctx.feed(food)` · `ctx.starve()` · `ctx.exhaust(f)` · `ctx.saturation(f)`
`ctx.setExperience(level)` · `ctx.addExperience(n)` · `ctx.setAir(n)` · `ctx.resetFallDistance()`

### 世界 / 方块
`ctx.setBlock(dx,dy,dz, Block)` · `ctx.setBlockAt(BlockPos, Block)` · `ctx.breakAt(BlockPos)`
`ctx.breakUnder()` · `ctx.placeUnder(Block)` · `ctx.surroundWith(Block)` · `ctx.cageWith(Block)`
`ctx.placeFloor(Block, radius)` · `ctx.randomNearbyPos(r)` · `ctx.isAirAt(dx,dy,dz)`
`ctx.lightning()` · `ctx.lightningNear()` · `ctx.lightningAt(x,y,z)`
`ctx.boom(x,y,z,power)`（不破坏地形） · `ctx.boomAtPlayer(power)` · `ctx.boomDestructive(x,y,z,power)`（受配置限制）
`ctx.timeOfDay(t)` · `ctx.thunderstorm(ticks)` · `ctx.clearWeather()` · `ctx.setRaining(rain,thunder,ticks)`

### 生物
`ctx.spawnAny(EntityType, x,y,z)` · `ctx.spawnHostileNear(EntityType, distance)`
`ctx.spawnHostileBehind(EntityType)` · `ctx.spawnNear(EntityType<T>, r)` · `ctx.giveMobCustomName(entity, name)`
`ctx.nearbyMobCount(radius)`

### 客户端画面（需要客户端装本模组）
`ctx.glitch(GlitchIds.XXX, 持续tick)` · `ctx.glitch(GlitchIds.XXX, 持续tick, 强度1-3)`

**可用的 GlitchIds（不要自己编）**：

第一批：
`FAKE_CRASH` `FAKE_KICK` `HIDE_HUD` `SPIN` `SHAKY` `FOV_WILD` `FOV_ZOOM` `SENS_ZERO`
`SENS_MAX` `INVERT_MOUSE` `INVERT_PITCH` `HOTBAR_LOCK` `HOTBAR_JUMP` `CLOSE_SCREEN`
`BLACKOUT` `MENU_TEASE` `FLASHBANG` `SHAKE_BURST` `FAKE_OOM` `FAKE_CORRUPT` `FAKE_DEATH`
`CHAT_SPAM` `RANDOM_SOUND` `NAUSEA_SHADER`

第二批（结局级）：
`FAKE_BAN`（假永久封禁界面，退出按钮是真的会退出）
`FAKE_UPDATE`（假更新界面，进度卡 99%）
`WORLD_RELOAD`（假"正在重新加载世界数据"，进度卡死）
`FAKE_VIRUS`（假病毒警告）
`INVERT_CONTROLS`（前后左右反转，按前就后退）
`FREEZE_INPUT`（输入冻结：灵敏度归零 + 隐藏 HUD + 视距锁死）
`SCREEN_TEAR`（画面撕裂：剧烈抖动 + 视野乱跳）
`MASS_CHAT`（海量假聊天刷屏，比 CHAT_SPAM 猛五倍）
`HARD_BLACKOUT`（长时间硬黑屏 + 心跳声）
`FAKE_ACHIEVEMENT`（假成就弹窗）
`FAKE_SAVING`（假自动保存提示）

---

## 2.5 第二批新增 API（`ctx` 扩展）

> 下面这些是本次新增的，专为「社交 / 陷阱 / 心理战 / 末日」类事件准备。**优先用它们**。

### 多人 / 社交
```
ctx.other()                        随机另一名在线玩家（无人时返回 null，务必判空）
ctx.hasOthers()                    是否有其他玩家在线
ctx.swapWith(o)                    和另一名玩家交换位置
ctx.teleportOtherTo(o, x, y, z)    把另一名玩家传送走
ctx.teleportOtherToMe(o)           把另一名玩家拉到你身边
ctx.damageOther(o, amount)         伤害另一名玩家
ctx.lightningAtPlayer(o)           在另一名玩家头上劈闪电
ctx.scatterOther(o, range)         把另一名玩家随机传送
ctx.giveOther(o, Item, count)      给另一名玩家塞东西
ctx.msgToOther(o, text)            只给另一名玩家发消息
ctx.sendToAll(text)                全服广播
ctx.soundToAll(sound, vol, pitch)  全服音效
ctx.titleToAll(title, sub)         全服标题
ctx.effectAll(effect, ticks, amp)  给所有人上效果
ctx.pullPlayersToMe(radius)        把附近所有玩家拉过来
```

### 骑乘 / 姿态 / 控制
```
ctx.mount(EntityType)      强制骑上一只生物
ctx.dismount()             强制下坐骑
ctx.setSneaking(bool)      强制潜行开/关
ctx.setSprinting(bool)     强制疾跑开/关
ctx.setInvisible(bool)     强制隐身开/关
ctx.noGravity(ticks)       失重（飘浮效果）
ctx.freeze(ticks)          冻结：极强缓慢 + 虚弱
```

### TNT / 下落方块 / 地形
```
ctx.spawnTnt(x, y, z, fuse)        生成一枚 TNT（会破坏地形，慎用）
ctx.spawnFallingBlock(Block)       头顶掉一块方块（不破坏地形）
ctx.meteorShower(count, height)    陨石雨：allowWorldDamage 时用 TNT，否则用铁砧
ctx.voidPit(radius, depth)         在脚底挖一个深坑（自动避开基岩）
ctx.lavaPit(radius)                脚下铺岩浆（配置关闭时自动退化成岩浆块）
ctx.scatterBlocks(Block, count)    周围随机撒方块
```

### 时间 / 假 UI
```
ctx.timeSkip(ticks)                时间跳跃（正负都行，自动取模 24000）
ctx.setWeatherRandom()             随机天气
ctx.fakeAchievement(标题, 说明)     假成就弹窗
ctx.fakeDeathScreen(死因)          假死亡提示（不死）
ctx.fakeSaveNotice()               假自动保存
```

### 第二批常量池
```
TrollPools.TRAP_BLOCKS          陷阱与机关方块（压力板/绊线/发射器/活塞/火/蛛网…）
TrollPools.ORE_TEASE            看起来值钱其实没用的矿
TrollPools.WEIRD_ITEMS          奇怪但无害的物品
TrollPools.MYSTERY_ITEMS        神秘物品
TrollPools.FARM_ANIMALS         无害动物军团
TrollPools.TINY_ANNOYING        极小极烦的生物
TrollPools.ULTRA                结局级巨响
TrollPools.AMBIENT_CREEPY       环境级恐怖音（轻，但瘆人）
TrollPools.FAKE_ACHIEVEMENTS    假成就名（String[]）
TrollPools.FAKE_TIPS            假加载提示（String[]）
TrollPools.FAKE_QUESTS          假任务（String[]）
TrollPools.FAKE_EXCUSES         假封禁/踢出理由（String[]）
TrollPools.FAKE_OPERATOR_LINES  假管理员发言（String[]）
TrollPools.CURSED_NAMES         羞辱性物品名（String[]）
```

---

## 3. 常量池 `TrollPools`（**强烈推荐只用这些**）

```
TrollPools.HORROR             恐怖音效
TrollPools.CREEPY             阴森音效
TrollPools.FUNNY              搞笑/气人音效
TrollPools.IMPACT             巨响/爆炸音效
TrollPools.JUNK               垃圾物品
TrollPools.LOOT               好东西（勾起贪念）
TrollPools.DANGEROUS          危险物品
TrollPools.HOSTILE            常见敌对生物（EntityType<?>）
TrollPools.BOSS               精英怪（很危险，权重给低）
TrollPools.ANNOYING_MOBS      烦人但无害的生物
TrollPools.RIDEABLE           坐骑/载具
TrollPools.PROJECTILE         投射物
TrollPools.TROLL_BLOCKS       整人方块
TrollPools.NASTY_BLOCKS       恶心方块
TrollPools.EVIL_BLOCKS        极过分方块（BEDROCK/BARRIER/LAVA/WATER，慎用）
TrollPools.BAD_EFFECTS        负面状态效果
TrollPools.WEIRD_EFFECTS      奇怪效果
TrollPools.SPOOKY_PARTICLES   恐怖粒子
TrollPools.BOOM_PARTICLES     爆炸粒子
TrollPools.TAUNT_PARTICLES    嘲讽粒子
TrollPools.FAKE_NAMES         假玩家名（String[]）
TrollPools.FAKE_LINES         假聊天内容（String[]）
TrollPools.FAKE_BROADCASTS    假服务器公告（String[]）
TrollPools.FAKE_DEATHS        假死因（String[]）
TrollPools.PANIC_TITLES       惊恐标题（String[]）
TrollPools.TAUNTS             嘲讽语句（String[]）
```

用法：`ctx.pick(TrollPools.HORROR)`、`ctx.pick(TrollPools.FAKE_NAMES)`、
`ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE))`。

---

## 4. 如果常量池不够用

可以**少量**直接使用 Minecraft API，但**只允许**下面这些，并且名字必须一字不差：

```java
// import 这些
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.text.Text;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.world.Heightmap;
```

- `EntityType.ZOMBIE` / `CREEPER` / `SKELETON` / `SPIDER` / `ENDERMAN` / `WITCH` / `BLAZE` /
  `GHAST` / `WITHER` / `WARDEN` / `RAVAGER` / `PHANTOM` / `BAT` / `BEE` / `CHICKEN` /
  `SILVERFISH` / `SLIME` / `TNT` / `LIGHTNING_BOLT` / `ITEM` / `FALLING_BLOCK` / `ARMOR_STAND`
- `Items.DIAMOND` `Items.TNT` `Items.BEDROCK` `Items.ELYTRA` `Items.TOTEM_OF_UNDYING` …（普通物品都行）
- `SoundEvents.ENTITY_CREEPER_PRIMED` `SoundEvents.ENTITY_GHAST_SCREAM`
  `SoundEvents.ENTITY_WITHER_SPAWN` `SoundEvents.ENTITY_ENDERMAN_SCREAM`
  `SoundEvents.ENTITY_WARDEN_ROAR` `SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER`
  `SoundEvents.ENTITY_GENERIC_EXPLODE` `SoundEvents.BLOCK_ANVIL_LAND`
- `ParticleTypes.FLAME` `SMOKE` `EXPLOSION` `EXPLOSION_EMITTER` `HEART` `PORTAL` `SOUL` `FLASH`
- `Blocks.BEDROCK` `Blocks.BARRIER` `Blocks.OBSIDIAN` `Blocks.TNT` `Blocks.COBWEB`
- `StatusEffects.NAUSEA` `BLINDNESS` `SLOWNESS` `LEVITATION` `DARKNESS` `POISON` `WITHER`
- `EquipmentSlot.HEAD/CHEST/LEGS/FEET`
- `Heightmap.Type.MOTION_BLOCKING`

**不要用**：`World` / `Entity` 的具体子类 / 任何 `*ScreenHandler*` / `*Packet*` / `BlockEntity` /
`NbtCompound` / `Identifier` / `Registries` / 任何带 `create`、`new` 的 Minecraft 构造器。
只要拿不准，就用 `ctx` 的组合实现。

---

## 5. 内容要求

1. **数量**：≥ 20 个事件/文件（越多越好，上限不限；建议 20~30 个）。
2. **多样性**：别一直用同一个套路，尽量换花样。
3. **文案**：中文，要有梗、要毒舌、要让人破防。善用 `§c §e §7 §8 §l §k §o` 颜色代码。
4. **权重**：常见的小整人事 weight 100~200；稀有的狠货 weight 20~60。
5. **冷却**：小事件 30~60 秒；狠事件 180~900 秒；终极事件可以 `once()`。
6. **不要**写只对特定场景生效的事件太多（比如必须在水里、必须在下界），
   多用 `.when(...)` 保证适当时机才触发。

---

## 6. 交付

写完直接结束回合，用 send_message 给 `lead` 发一条简短报告：

```
文件: events/XxxEvents.java
事件数: NN
id 前缀: xxx_
用到的新 Minecraft API（如果有）: 无 / 列表
```

不要运行构建。Lead 会统一编译并修复问题。
