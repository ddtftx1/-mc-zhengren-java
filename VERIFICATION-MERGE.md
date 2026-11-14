# 合并结果独立验证报告（VERIFICATION-MERGE）

- **验证者**：`verifier`（对抗性验证，只读）
- **验证对象**：`zhengren-mod`（整人大师 × 精神污染 合并体，MC 1.20.1 / Fabric / Java 17）
- **任务**：task-4
- **结论**：**通过（可交付），但存在 2 个中级缺陷 + 6 个低级/信息级问题，无阻塞性缺陷**
- **验证方式**：独立 `clean build` + 静态审计（源码/资源/字节码）+ **真实启动 Minecraft 1.20.1 服务端**（RCON 实测命令）

> 本报告所有结论均附可复现命令与原始输出。凡未能验证的项目在 §G 明确列出。
> 本次验证**未修改** `src/` 下任何文件；唯一写入的仓库内文件是本报告，另外在 `run/`（已被 `.gitignore` 忽略）创建了运行时验证用目录。

---

## 0. 结论摘要

| 项 | 期望 | 实测 | 判定 |
|---|---|---|---|
| A 构建 | `BUILD SUCCESSFUL` | `BUILD SUCCESSFUL in 1m 44s` | ✅ |
| B 事件总数 | 615 | **615**（静态 + 运行时双重确认） | ✅ |
| B 精神污染事件 | 44 | **44**（静态 + 运行时双重确认） | ✅ |
| B 原 43 个 id | 一个不能少 | 43/43 全部存在 | ✅ |
| B 重复 id | 0 | 0（616 个 `of("…")` 字面量全唯一，含 1 个 javadoc 示例） | ✅ |
| B `.desc()`/`.run()` | 每个事件都有 | 615/615/615 | ✅ |
| C 结构 | 单套调度/单套状态 | 通过（详见 §C） | ✅ |
| D 产物 jar | 资源齐全、intermediary | 通过（详见 §D） | ✅ |
| E 资源自洽 | 0 处不一致 | 0 处（脚本校验 0 error） | ✅ |
| F 功能互通 | 8 条全部成立 | 8/8 成立（含运行时实测） | ✅ |
| G 诚实报告 | 列出未验证项 | 见 §G | ✅ |

**中级缺陷 2 个**：`fabric.mod.json` 事件数字错误（614/43，实际 615/44）；`TrollScheduler.randomEvent` 不遵守黑名单/精神污染开关，与代码注释及 README 承诺不符。

---

## 1. 验证环境（可复现）

```bash
cd /data/data/com.termux/files/home/整人模组工作区/zhengren-mod
export LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_HOME=/usr/lib/jvm/java-17-openjdk-arm64
```

原始输出：

```
PWD=/data/data/com.termux/files/home/整人模组工作区/zhengren-mod
openjdk version "17.0.21.0.3" 2026-10-24
OpenJDK Runtime Environment (build 17.0.21.0.3+1-1-22.04.1-Ubuntu)
distributionUrl=https\://mirrors.cloud.tencent.com/gradle/gradle-8.7-bin.zip
```

- Gradle：wrapper 8.7（`bash gradlew`，**不用** `./gradlew`）
- Loom 1.6.12；MC 1.20.1；yarn `1.20.1+build.10`；loader 0.16.14；Fabric API 0.92.12（`libs/fabric-api/` 本地 53 个模块 jar）
- 源码规模：46 个 `.java`，14610 行

---

## A. 构建（自己跑，不采信他人输出）

**命令**

```bash
flock /tmp/zr-build.lock bash gradlew clean build --no-daemon
```

**原始输出（节选，完整日志见文末附录 A）**

```
> Task :clean
> Task :compileJava
> Task :processResources
> Task :classes
> Task :jar
> Task :sourcesJar
> Task :compileTestJava NO-SOURCE
> Task :test NO-SOURCE
> Task :remapJar
> Task :remapSourcesJar
> Task :assemble
> Task :build

BUILD SUCCESSFUL in 1m 44s
7 actionable tasks: 7 executed
```

**产物**

```
-rw-r--r-- 1 root root 335688 Nov 14 08:58 build/libs/zhengren-1.0.0.jar
-rw-r--r-- 1 root root 191563 Nov 14 08:58 build/libs/zhengren-1.0.0-sources.jar
sha256 a5d4454a216061dd286bd25416fd50664871c52ae6c164082ad0ee4b6bff7a1b
```

✅ **通过**。`clean build` 从零开始、无缓存复用（`7 actionable tasks: 7 executed`），无警告性失败。

---

## B. 事件表完整性（核心）

### B.1 关于「616 vs 615」的澄清（必须算清楚）

```bash
$ grep -rho 'TrollEvent\.of("[^"]*"' src/main/java --include=*.java | wc -l
616
$ grep -rho 'TrollEvent\.of("[^"]*"' src/main/java --include=*.java | sed 's/.*of("//;s/"$//' | sort -u | wc -l
616
$ grep -rc 'TrollEvent\.of(' src/main/java --include=*.java | grep -v 'core/TrollEvent.java' \
    | awk -F: '{s+=$2} END{print "total="s}'
total=615
```

`core/TrollEvent.java` 里那 1 处是 **javadoc 示例**，不是事件：

```bash
$ grep -n -B3 'TrollEvent\.of(' src/main/java/com/zhengren/trollmod/core/TrollEvent.java
7- * 一个整人事件。用 {@link #of(String, String, TrollTier)} 构建。
8- *
9- * <pre>
10: * reg(TrollEvent.of("id", "显示名", TrollTier.ANNOYING)
11- *         .desc("说明")
```

**结论**：`grep -c 'TrollEvent.of('` = 616 = 615 个真实事件 + 1 个注释示例。
另外 616 个字面量 **全部唯一**，说明 javadoc 里的 `"id"` 也不是真实事件 id，即**零重复**。

### B.2 每个事件文件的 of/reg 数量必须一致（防止"构建了但没注册"）

```bash
$ for f in src/main/java/com/zhengren/trollmod/events/*.java \
           src/main/java/com/zhengren/trollmod/mindbreak/MindbreakEvents.java; do
    of=$(grep -c 'TrollEvent\.of(' "$f"); reg=$(grep -o 'TrollEvents\.reg(\|[^A-Za-z_]reg(' "$f" | wc -l)
    printf "%-24s of=%-4s reg=%-4s %s\n" "$(basename $f)" "$of" "$reg" "$([ "$of" = "$reg" ] && echo OK || echo MISMATCH)"
  done
AudioEvents.java         of=31   reg=31   OK
BeastEvents.java         of=47   reg=47   OK
ChaosEvents.java         of=26   reg=26   OK
DoomEvents.java          of=45   reg=45   OK
GlitchEvents.java        of=35   reg=35   OK
ItemEvents.java          of=34   reg=34   OK
LootEvents.java          of=57   reg=57   OK
MindEvents.java          of=48   reg=48   OK
MobEvents.java           of=33   reg=33   OK
NightmareEvents.java     of=46   reg=46   OK
PlayerEvents.java        of=38   reg=38   OK
SocialEvents.java        of=46   reg=46   OK
TrapEvents.java          of=52   reg=52   OK
WorldEvents.java         of=33   reg=33   OK
MindbreakEvents.java     of=44   reg=44   OK
```

原生合计 `31+47+26+45+35+34+57+48+33+46+38+46+52+33 = 571`，精神污染 `44`，合计 **615**。

### B.3 原 mindbreak 43 个 id 逐个比对

```bash
$ grep -o 'add("[a-z_]*"' mindbreak-1.0.0-main/src/main/java/com/mindbreak/chaos/ChaosEvents.java \
    | sed 's/add("//;s/"//' | sort > /tmp/mb_orig.txt ; wc -l < /tmp/mb_orig.txt
43
$ grep -o 'TrollEvent\.of("[^"]*"' src/main/java/com/zhengren/trollmod/mindbreak/MindbreakEvents.java \
    | sed 's/.*of("//;s/"$//' | sort > /tmp/mb_new.txt ; wc -l < /tmp/mb_new.txt
44
$ comm -23 /tmp/mb_orig.txt /tmp/mb_new.txt          # 原有但合并后缺失
（空）
--- count missing: 0
$ comm -13 /tmp/mb_orig.txt /tmp/mb_new.txt          # 合并后新增
relic_gift
--- count extra: 1
```

✅ **43/43 全部保留，无改名、无前缀，仅新增 1 个桥接事件 `relic_gift`。**

### B.4 原事件权重保真度（43 个逐个核对）

脚本比对 `add("id", weight, ...)` 与合并后 `.weight(N)`：

```
orig events: 43  merged events: 44
id                          origW  mergedW  status
chat_spam                     20       20  OK
fake_achievement               8        8  OK
...（43 行全部 OK，此处省略，见附录 B 完整输出）
totem_tease                    5        5  OK

weight mismatches: 0
no-weight events in merged: []
```

✅ **43 个原事件的权重 100% 保持原值。**

### B.5 每个事件都有 `.desc()` 和 `.run()`

逐事件切块解析（不是简单行计数，避免"一个事件两个 desc、另一个没有"的假阳性）：

```bash
$ python3 - <<'EOF'
import re,glob
files=sorted(glob.glob('src/main/java/com/zhengren/trollmod/events/*.java'))+['src/main/java/com/zhengren/trollmod/mindbreak/MindbreakEvents.java']
bad=[];total=0
for f in files:
    s=open(f,encoding='utf-8').read()
    idxs=[m.start() for m in re.finditer(r'TrollEvent\.of\(',s)]
    for i,st in enumerate(idxs):
        en=idxs[i+1] if i+1<len(idxs) else len(s); chunk=s[st:en]
        eid=re.match(r'TrollEvent\.of\("([^"]*)"',chunk).group(1); total+=1
        miss=[k for k in ('desc','run','build') if '.'+k+'(' not in chunk]
        if miss: bad.append((f,eid,miss))
print("total events parsed:",total); print("events missing something:",len(bad))
EOF
total events parsed: 615
events missing something: 0
```

✅ **615/615 事件均含 `.desc(...)` + `.run(...)` + `.build()`。**

### B.6 运行时确认（最强证据）

真实启动 1.20.1 服务端（方法见 §F.0），mod 初始化日志：

```
[09:10:58] [main/INFO] (zhengren) [整人大师] 加载完成：共 615 个整人事件（小整111 / 烦人176 / 抓狂154 / 崩溃110 / 地狱64），其中「精神污染」44 个
```

这一行由 `ZhengrenMod.java:61-64` 打印，数据源是 `TrollRegistry.size()` 与 `MindbreakEvents.size()`，因此：

- `TrollRegistry.size()` = **615** ✅
- `MindbreakEvents.size()` = **44** ✅
- 各等级 111/176/154/110/64，与静态统计完全一致 ✅
- 日志中 **没有** `发现 N 个重复事件 ID` 警告（`grep -c '重复事件 ID' = 0`）→ `TrollRegistry.duplicates()` 为空 ✅

RCON 实测：

```
===== RCON> zr count
§6事件总数：§f615 §7| §a小整 111 §e烦人 176 §6抓狂 154 §c崩溃 110 §4地狱 64

===== RCON> mb stats
【精神污染】状态：开着，强度 2，精神污染事件 44 个，事件总数 615 个 §7(交互回调 开 / 聊天扭曲 开 / 总开关 开)

===== RCON> mb list
【精神污染】共 44 个事件：chat_spam, fake_achievement, fake_death, system_glitch, villager_gossip,
inventory_shuffle, item_steal, inventory_curse, hotbar_swap, drop_item, gift, fake_diamond, xp_blessing,
hunger_drain, force_feed, levitation, effect_roulette, self_hurt, mob_buff, small_teleport, spin,
chicken_invasion, pig_parade, mob_taunt, creeper_apology, enderman_visitor, tnt_stalker, diamond_rain,
weather_chaos, time_chaos, spooky_sound, lightning_tease, shadow_follow, total_chaos, chat_takeover,
neck_crack, anvil_prank, fake_ore, ice_floor, fake_player_chat, fake_friend_join, swap_players,
totem_tease, relic_gift
```

✅ **B 项全部通过。**

---

## C. 结构审计：证明是"真合并"而不是"两套代码并排"

### C.1 不存在第二套 tick 调度

```bash
$ grep -rn '% 20 == 0' src/main/java
src/main/java/com/zhengren/trollmod/client/ClientGlitchManager.java:247:        if (has("random_sound") && mc.world != null && mc.world.getTime() % 20 == 0) {

$ grep -rn 'END_SERVER_TICK' src/main/java
src/main/java/com/zhengren/trollmod/core/TrollScheduler.java:32:        ServerTickEvents.END_SERVER_TICK.register(TrollScheduler::onServerTick);
src/main/java/com/zhengren/trollmod/core/DelayScheduler.java:37:        ServerTickEvents.END_SERVER_TICK.register(DelayScheduler::tick);
```

- `% 20 == 0` 唯一命中在 **客户端** `ClientGlitchManager`（"随机恐怖音效"每 20 客户端 tick 触发一次），该文件**未在本合并中修改**（`git status` 中不出现），属合并前既有代码，与 mindbreak 的 `ChaosManager` 轮询无关。
- `END_SERVER_TICK` 只有 2 处注册，正是要求的 `TrollScheduler` + `DelayScheduler`。
- 原 mindbreak `Callbacks.register()` 里的第 9 个注册 `ServerTickEvents.END_SERVER_TICK.register(ChaosManager::tick)` **没有被移植**（对照 `mindbreak-1.0.0-main/src/main/java/com/mindbreak/callback/Callbacks.java:51`）。

### C.2 不存在第二套类/包

```bash
$ grep -rn 'ChaosManager\|ChaosScheduler' src/main/java
src/main/java/com/zhengren/trollmod/core/DelayScheduler.java:12: * 延迟任务调度器。合并自 mindbreak 的 ChaosScheduler：
src/main/java/com/zhengren/trollmod/item/ModItems.java:38:    /** 五件神器共用的随机源（原 mindbreak 的 {@code ChaosManager.RANDOM}）。 */
src/main/java/com/zhengren/trollmod/item/MysteryMeatItem.java:16: * <p>移植要点：随机分支改用 {@link ModItems#RANDOM}（原 {@code ChaosManager.RANDOM}），

$ grep -rn 'com\.mindbreak' src/main/java
（空 —— NONE）

$ head -1 src/main/java/com/zhengren/trollmod/mindbreak/*.java
package com.zhengren.trollmod.mindbreak;      # MindbreakEvents.java
package com.zhengren.trollmod.mindbreak;      # MindbreakHooks.java
package com.zhengren.trollmod.mindbreak;      # MindbreakUtil.java
package com.zhengren.trollmod.mindbreak;      # Msgs.java
```

- `ChaosManager`/`ChaosScheduler` **仅出现在注释里**（说明来源），没有任何同名类。
- `ChaosEvents` 只剩原生 `com.zhengren.trollmod.events.ChaosEvents`（26 个原生事件），`com.mindbreak.chaos.ChaosEvents` 不存在。
- `com.mindbreak` 包引用 **0 处**；`mindbreak/` 目录 4 个文件包名全部正确。

### C.3 不存在第二套开关/状态

```bash
$ grep -rn 'boolean enabled\|\.enabled *=' src/main/java
src/main/java/com/zhengren/trollmod/command/TrollCommand.java:49:                    c.enabled = true;
src/main/java/com/zhengren/trollmod/command/TrollCommand.java:56:                    c.enabled = false;
src/main/java/com/zhengren/trollmod/core/TrollConfig.java:25:    public boolean enabled = true;
```

只有 `TrollConfig` 一处 `enabled` 字段，两个命令都写同一个 `c`（= `ZhengrenMod.CONFIG`）。
`mindbreakEnabled / mindbreakIntensity / mindbreakHooks / chatMangle` 4 个字段也只在 `TrollConfig.java:59-65` 定义，全部读写点都指向同一实例。

### C.4 物品驱动统一事件表（不是自己的随机列表）

`AbsurdStickItem.java:39-46`：

```java
TrollConfig cfg = ZhengrenMod.CONFIG;
TrollEvent event = TrollScheduler.randomEvent(cfg);
if (event == null) {
    ModItems.msg(serverPlayer, Msgs.pick(ModItems.RANDOM, Msgs.CURSE));
} else {
    TrollScheduler.fire(server, serverPlayer, cfg, TrollScheduler.state(serverPlayer), event);
}
```

对照原实现 `mindbreak-1.0.0-main/src/main/java/com/mindbreak/item/AbsurdStickItem.java:24`：原来是 `ChaosManager.fireRandom(server, player)`（走 mindbreak 自己那套列表），合并后已改为统一调度器。`AbsurdStickItem` 内**没有**任何自建事件数组/随机事件列表（`grep -c 'Item\[\]\|String\[\].*event'` 无命中）。

### C.5 `ctx.later(...)` 真实使用 + `DelayScheduler` 已注册

```bash
$ grep -rn 'ctx\.later(' src/main/java
src/main/java/com/zhengren/trollmod/mindbreak/MindbreakEvents.java:93
src/main/java/com/zhengren/trollmod/mindbreak/MindbreakEvents.java:105
src/main/java/com/zhengren/trollmod/mindbreak/MindbreakEvents.java:151
src/main/java/com/zhengren/trollmod/mindbreak/MindbreakEvents.java:572
src/main/java/com/zhengren/trollmod/mindbreak/MindbreakEvents.java:573
src/main/java/com/zhengren/trollmod/mindbreak/MindbreakEvents.java:574
src/main/java/com/zhengren/trollmod/mindbreak/MindbreakEvents.java:629
src/main/java/com/zhengren/trollmod/mindbreak/MindbreakEvents.java:668
```

（另有 `TrollContext.java:974` 的 javadoc 示例）

```bash
$ grep -n 'DelayScheduler.register()' src/main/java/com/zhengren/trollmod/ZhengrenMod.java
49:        DelayScheduler.register();
```

✅ `ctx.later` 被真实使用 8 次（`fake_death` / `system_glitch` / `item_steal` / `chat_takeover` / `fake_ore` / `fake_friend_join`），`DelayScheduler` 在 `ZhengrenMod.java:49` 注册。
原 mindbreak 的 `ChaosScheduler.schedule(...)` 调用点（`ChaosEvents.java:66,72,112,431-433`）全部已替换为 `ctx.later(...)`，无 `ChaosScheduler` 残留。

✅ **C 项全部通过：单套事件表、单套调度、单套配置、单套命令。**

---

## D. 产物 jar 检查

**命令**

```bash
J=build/libs/zhengren-1.0.0.jar
unzip -l $J
echo "textures/item png : $(unzip -l $J | grep -c 'assets/zhengren/textures/item/.*\.png')"
...
```

**原始输出（统计）**

```
textures/item png : 5
models/item json  : 5
data/zhengren/recipes json : 5
lang json         : 2
data/zhengren/recipe/ (单数) : 0
data/mindbreak    : 0
assets/mindbreak  : 0
com/mindbreak     : 0
total entries     : 91 files
```

**关键条目（`unzip -l` 节选）**

```
     204  assets/zhengren/textures/item/absurd_stick.png
     338  assets/zhengren/textures/item/chaos_pearl.png
     228  assets/zhengren/textures/item/curse_book.png
     331  assets/zhengren/textures/item/mystery_meat.png
     250  assets/zhengren/textures/item/regret_cake.png
     107  assets/zhengren/models/item/absurd_stick.json
     ...
     277  data/zhengren/recipes/absurd_stick.json
     284  data/zhengren/recipes/chaos_pearl.json
     272  data/zhengren/recipes/curse_book.json
     278  data/zhengren/recipes/mystery_meat.json
     271  data/zhengren/recipes/regret_cake.json
     507  assets/zhengren/lang/en_us.json
     505  assets/zhengren/lang/zh_cn.json
     851  fabric.mod.json
  36800  com/zhengren/trollmod/mindbreak/MindbreakEvents.class
  14273  com/zhengren/trollmod/mindbreak/MindbreakHooks.class
   5156  com/zhengren/trollmod/mindbreak/MindbreakUtil.class
   7529  com/zhengren/trollmod/mindbreak/Msgs.class
```

- ✅ `data/zhengren/recipes/` 是**复数**，无 `data/zhengren/recipe/` 单数目录
- ✅ 无 `data/mindbreak` / `assets/mindbreak` / `com/mindbreak` 残留
- ✅ 5 材质 + 5 模型 + 5 配方 + 2 lang
- ✅ 4 个 `mindbreak` 类已打进包（证明代码真在产物里，不是只在源码里）

**intermediary remap 验证**

```bash
$ cat META-INF/MANIFEST.MF | grep Mapping
Fabric-Mapping-Namespace: intermediary
Fabric-Minecraft-Version: 1.20.1
Fabric-Loom-Version: 1.6.12

$ strings com/zhengren/trollmod/item/ModItems.class | grep -c 'net/minecraft/class_'
47
$ strings com/zhengren/trollmod/item/ModItems.class | grep 'net/minecraft/class_' | head -5
net/minecraft/class_1792
net/minecraft/class_4174$class_4175
net/minecraft/class_7706
net/minecraft/class_5321
net/minecraft/class_1792$class_1793

$ strings com/zhengren/trollmod/mindbreak/MindbreakEvents.class | grep -c 'net/minecraft/class_'
91
$ strings com/zhengren/trollmod/item/ModItems.class | grep -o 'field_[0-9]*\|method_[0-9]*' | sort -u | head -5
field_15248
field_41060
field_41061
field_41178
method_10230

$ javap -verbose com/zhengren/trollmod/item/ModItems.class | grep major
  major version: 61          # Java 17
```

✅ **已 remap 到 intermediary（`class_` / `field_` / `method_`），class 版本 61（Java 17），是可供正式客户端加载的产物。**
（说明：`net/minecraft/server/MinecraftServer` 未混淆，1.20.1 中该类本就保留原名，属正常。）

**jar 内 `fabric.mod.json`**

```json
{
  "schemaVersion": 1,
  "id": "zhengren",
  "version": "1.0.0",
  "name": "整人大师 × 精神污染 (Zhengren Master × Mindbreak)",
  "description": "整人大师与精神污染的合并体：614 个整人事件（原 571 个 + 精神污染 43 个，15 大分类、5 档残酷等级）、5 件精神污染神器、交互型整活回调。……",
  "entrypoints": { "main": ["com.zhengren.trollmod.ZhengrenMod"], "client": ["com.zhengren.trollmod.client.ZhengrenClient"] },
  "depends": { "fabricloader": ">=0.15.0", "fabric-api": "*", "minecraft": "~1.20.1", "java": ">=17" }
}
```

- ✅ `id` 仍是 `zhengren`，`version` 已被 `${version}` 正确展开为 `1.0.0`
- ✅ name 反映合并
- ⚠️ **description 的事件数字错误（614 / 43），实际是 615 / 44** → 缺陷 D-1（见 §G）

---

## E. 资源自洽性

### E.1 脚本化交叉校验（model ↔ png ↔ recipe ↔ 命名空间）

```bash
$ python3 - <<'EOF'   # 逐项断言，任何不一致都记 error
...
EOF
ERRORS: 0

$ python3 -c "..."   # 递归解析所有 json
json files: 13 invalid: 0
```

校验内容（全部 0 error）：

| 检查 | 结果 |
|---|---|
| 5 个 model 的 `parent` = `minecraft:item/generated` | ✅ |
| 5 个 model 的 `layer0` = `zhengren:item/<name>` 且与文件名同名 | ✅ |
| 5 个 png 真实存在、PNG magic 正确、尺寸 16×16 | ✅ |
| 5 个 recipe 的 `type` = `minecraft:crafting_shapeless` | ✅ |
| 5 个 recipe 的 `result.item` = `zhengren:<name>` | ✅ |
| **无** 1.21 语法的 `result.id` | ✅（`grep -rn '"id"' data/zhengren/recipes/` 无命中） |
| `ingredients` 是对象数组 `[{"item": ...}]`，无裸字符串 | ✅ |
| 13 个资源 json 全部可被 `json.load` 解析 | ✅ |

### E.2 与 mindbreak 原资源对比（保真度）

```bash
$ for n in absurd_stick curse_book chaos_pearl regret_cake mystery_meat; do
    sha1sum 两边的 png 后比较 ; done
absurd_stick: IDENTICAL (74c6a42287bed4f9a0cb0deb643f63031972f43c)
curse_book: IDENTICAL (d155c0e157bafbf8f82d9ea1947cdc3b2744acc6)
chaos_pearl: IDENTICAL (ec283b68643811783858d95069eac755e5269c8f)
regret_cake: IDENTICAL (55810889c72e3dfd71af195bdd3f274ededdafe3)
mystery_meat: IDENTICAL (fc495b877fa84f37a6f62ae9add6fee7e98f9f0a)
```

配方内容与 mindbreak 原配方逐字段一致，仅做了 1.21→1.20.1 的语法转换：

| | mindbreak 1.21（原） | 合并后 1.20.1 |
|---|---|---|
| 路径 | `data/mindbreak/recipe/`（单数） | `data/zhengren/recipes/`（**复数**） |
| result | `"result": { "id": "mindbreak:absurd_stick" }` | `"result": { "item": "zhengren:absurd_stick" }` |
| ingredients | `[{"item": ...}]`（本来就对） | 保持不变 |

### E.3 lang key 与注册名一致

```bash
$ grep -n 'item.zhengren' src/main/resources/assets/zhengren/lang/zh_cn.json
"item.zhengren.absurd_stick": "离谱棒",
"item.zhengren.curse_book": "嘴臭之书",
"item.zhengren.chaos_pearl": "混乱珍珠",
"item.zhengren.regret_cake": "后悔蛋糕",
"item.zhengren.mystery_meat": "神秘不明肉"
```

与 `ModItems.java:41-62` 的注册名 `absurd_stick / curse_book / chaos_pearl / regret_cake / mystery_meat` 完全一致；`en_us.json` 同 5 个 key 齐备。

### E.4 运行时佐证（配方真的被解析了）

服务端启动日志中**没有任何** `ERROR` 行（只有 5 条离线模式 WARN），即不存在 `Parsing error loading recipe ...`；5 个配方文件全部成功反序列化。

> 附：我一开始怀疑日志里的 `Loaded 7 recipes` 是"只有 7 个配方加载成功"。用 javap 反查 1.20.1 的 `RecipeManager.apply` 后确认：该日志打印的是 `recipes` 外层 map（`Map<RecipeType, Map<Identifier, Recipe>>`）的 size，即**配方类型数**（crafting/smelting/blasting/smoking/campfire_cooking/stonecutting/smithing = 7），不是配方条目数。**不是缺陷**，是我排除掉的误报。

✅ **E 项通过（0 处不一致）。**

---

## F. 功能互通证据（本任务核心命题）

### F.0 运行时验证方法（真实服务端）

```bash
cd zhengren-mod
mkdir -p run && echo "eula=true" > run/eula.txt
# run/server.properties: level-type=minecraft\:flat, online-mode=false, enable-rcon=true, rcon.password=zrverify
flock /tmp/zr-build.lock timeout 400 bash gradlew runServer --no-daemon --console=plain
# 另开一个终端用最小 RCON 客户端（/tmp/rcon.py，纯标准库）执行命令
```

服务端启动成功：

```
[09:10:58] [main/INFO] (zhengren) [整人大师] 加载完成：共 615 个整人事件（…），其中「精神污染」44 个
[09:11:36] [Server thread/INFO] (Minecraft) Done (22.677s)! For help, type "help"
[09:11:36] [Server thread/INFO] (Minecraft) RCON running on 0.0.0.0:25575
```

> 说明：`libs/fabric-api/` 只有 53 个**模块** jar，缺聚合 mod id `fabric-api`，`runServer` 开箱会 `HARD_DEP_NO_CANDIDATE` 失败。为完成运行时验证，我在 `run/mods/` 放了一个只含 `fabric.mod.json`（`id=fabric-api`）的 **stub jar**，未改动 `src/` 与 `build.gradle`。详见 §G 信息项 I-1。

### F.1 精神污染事件进入 `/zr` 统一事件表

```bash
$ grep -n 'MindbreakEvents.register' src/main/java/com/zhengren/trollmod/core/TrollEvents.java
68:        com.zhengren.trollmod.mindbreak.MindbreakEvents.register();
```

- `TrollEvents.java:50-69` 的 `bootstrap()` 在最后一批调用它；`ZhengrenMod.java:47` 调用 `TrollEvents.bootstrap()`。
- `MindbreakEvents.java:55` 的 `CATEGORY = TrollEvents.CATEGORY_MINDBREAK`（`TrollEvents.java:28` = `"精神污染"`），单一来源，无硬编码分叉。
- **运行时**：`/zr list 1` 的分类列表末尾出现 `精神污染`；`/zr info chat_spam` 显示 `分类：精神污染`：

```
===== RCON> zr info chat_spam
§8--- §fchat_spam §8---§7名称：§e[烦人] §f嘴替上身§7分类：精神污染 §7权重：20 §7冷却：30s
§7说明：随机一句阴阳怪气只发给你，精神污染的入门款

===== RCON> zr info relic_gift
§8--- §frelic_gift §8---§7名称：§a[小整] §f神器馈赠§7分类：精神污染 §7权重：6 §7冷却：300s
§7说明：桥接事件：融合整人大师物品系统，随机赠送五件神器之一（ABSURD_STICK / …）
```

✅ **成立。**

### F.2 精神污染事件受统一配置约束（开关 + 强度加权）

```bash
$ sed -n '118,150p' src/main/java/com/zhengren/trollmod/core/TrollScheduler.java
        for (TrollEvent e : TrollRegistry.all()) {
            if (cfg.isDisabled(e.id())) { continue; }
            if (TrollEvents.CATEGORY_MINDBREAK.equals(e.category()) && !cfg.mindbreakEnabled) {
                continue;                                  // ← 开关门控（第 127-129 行）
            }
            ...
            pool.add(e);
            int w = effectiveWeight(e, cfg);               // ← 强度加权（第 147 行）
            weights.add(w); total += w;
```

```java
// TrollScheduler.java:169-174
public static int effectiveWeight(TrollEvent e, TrollConfig cfg) {
    if (cfg != null && TrollEvents.CATEGORY_MINDBREAK.equals(e.category())) {
        return Math.max(1, (int) Math.round(e.weight() * cfg.mindbreakWeightScale()));
    }
    return Math.max(1, e.weight());
}

// TrollConfig.java:149-155
public double mindbreakWeightScale() {
    return switch (mindbreakIntensity) {
        case 1 -> 0.5;
        case 3 -> 2.0;
        default -> 1.0;
    };
}
```

加权是**在抽取时实时计算**（不是注册时烘死），所以 `/zr intensity` 改完立即生效。

**运行时**：`/zr intensity 3` 后 `/mb stats` 立刻读到 3（同一份配置对象）：

```
===== RCON> zr intensity 3
§6精神污染强度：3 §7(等价于 /mindbreak intensity)
===== RCON> mb stats
【精神污染】状态：开着，强度 3，精神污染事件 44 个，事件总数 615 个 …
```

✅ **成立。**（但 `randomEvent` 路径不遵守该门控，见缺陷 D-2。）

### F.3 至少 3 个精神污染事件使用整人大师原生独有能力

```bash
$ grep -n 'ctx\.fakeAchievement\|ctx\.glitch(\|ctx\.title(\|ctx\.meteorShower\|ctx\.spawnFallingBlock\|ctx\.fakeDeathScreen' \
    src/main/java/com/zhengren/trollmod/mindbreak/MindbreakEvents.java
79:  ctx.fakeAchievement("精神状态稳定", "在崩溃边缘保持微笑");
92:  ctx.fakeDeathScreen("§7……开个玩笑。");
103: ctx.glitch(GlitchIds.SHAKY, 60, 2);
104: ctx.title("§c系统错误", "§7正在重新计算你的价值……", 5, 40, 10);
554: ctx.meteorShower(6, 12.0);
555: ctx.glitch(GlitchIds.FAKE_CRASH, 80, 2);
598: ctx.spawnFallingBlock(Blocks.ANVIL);
```

归属事件（切块确认）：

| 事件 id | 使用的原生独有能力 | 行号 |
|---|---|---|
| `fake_achievement` | `ctx.fakeAchievement` | 79 |
| `fake_death` | `ctx.fakeDeathScreen` + `ctx.later` | 92 |
| `system_glitch` | `ctx.glitch` + `ctx.title` | 103-104 |
| `total_chaos` | `ctx.meteorShower` + `ctx.glitch` | 554-555 |
| `anvil_prank` | `ctx.spawnFallingBlock` | 598 |

任务要求「至少 3 个」，实测 **5 个**。且 `GlitchIds.SHAKY` / `GlitchIds.FAKE_CRASH` 都是客户端真实处理的效果：

```bash
$ grep -n 'has("shaky")\|has("fakecrash")' src/main/java/com/zhengren/trollmod/client/ClientGlitchManager.java
234:        if (has("fakecrash") && !(mc.currentScreen instanceof FakeCrashScreen)) {
220:        if (has("shaky")) {
```

✅ **成立（5 ≥ 3）。**

### F.4 `relic_gift` 桥接事件存在，能发五件神器

```bash
$ sed -n '711,730p' src/main/java/com/zhengren/trollmod/mindbreak/MindbreakEvents.java
        TrollEvents.reg(CATEGORY, TrollEvent.of("relic_gift", "神器馈赠", TrollTier.PRANK)
                .desc("桥接事件：融合整人大师物品系统，随机赠送五件神器之一（ABSURD_STICK / CURSE_BOOK / "
                        + "CHAOS_PEARL / REGRET_CAKE / MYSTERY_MEAT）")
                .weight(6)
                .cooldownSeconds(300)
                .run(ctx -> {
                    Item[] relics = { ModItems.ABSURD_STICK, ModItems.CURSE_BOOK, ModItems.CHAOS_PEARL,
                                      ModItems.REGRET_CAKE, ModItems.MYSTERY_MEAT };
                    ItemStack relic = new ItemStack(ctx.pick(relics));
                    ctx.give(relic);
                    ...
                })
                .build());
```

- 五件神器全部来自 `ModItems` 的**真实字段**（`ModItems.java:41-62`，`Registry.register(Registries.ITEM, new Identifier("zhengren", name), ...)`），不是 `Items.STICK` 占位。
- 运行时 `/zr info relic_gift` 正常返回（见 F.1）。

✅ **成立。**

### F.5 物品能触发统一事件表里的随机事件

`AbsurdStickItem.java:39-46`（完整代码见 §C.4）：`TrollScheduler.randomEvent(cfg)` → `TrollScheduler.fire(...)`。
`randomEvent` 的池子是 `TrollRegistry.upTo(cfg.maxTier)`（`TrollScheduler.java:207-213`），即**整张 615 事件表**，因此可能抽到原生事件，也会抽到精神污染事件。

其余 4 件物品的行为与原 mindbreak 实现逐行对照后一致（`ChaosPearlItem` 的 `teleport(world,x,y,z,yaw,pitch)` 等价于原 `ChaosUtil.teleport`；`CurseBookItem`/`RegretCakeItem`/`MysteryMeatItem` 效果数值完全相同），并统一加了 `ModItems.canTroll()` 开关闸门（`ModItems.java:102-109`：`!cfg.enabled || !cfg.mindbreakEnabled` 时提示并拒绝）。

✅ **成立。**

### F.6 交互回调受统一开关约束

```java
// MindbreakHooks.java:102-108 —— 所有回调共用同一个闸门
private static boolean hooksOn(PlayerEntity player) {
    TrollConfig cfg = cfg();
    if (cfg == null || !cfg.enabled || !cfg.mindbreakEnabled || !cfg.mindbreakHooks) {
        return false;
    }
    return cfg.affectCreative || !player.isCreative();
}
```

8 个回调逐一确认调用 `hooksOn`（或等价判断）：

| # | 回调 | 行号 | 开关 |
|---|---|---|---|
| 1 | `PlayerBlockBreakEvents.BEFORE` | 122 | `hooksOn` |
| 2 | `PlayerBlockBreakEvents.AFTER` | 140 | `hooksOn` |
| 3 | `UseBlockCallback` | 158 | `hooksOn` |
| 4 | `AttackEntityCallback` | 193 | `hooksOn` |
| 5 | `ServerMessageEvents.ALLOW_CHAT_MESSAGE` | 217 | `hooksOn` 等价 + `chatMangle` |
| 6 | `ServerLivingEntityEvents.AFTER_DEATH` | 237 | `hooksOn` |
| 7 | `ServerPlayConnectionEvents.JOIN` | 251 | `hooksOn` |
| 8 | `ServerPlayerEvents.AFTER_RESPAWN` | 265 | `hooksOn` |

注册清单在 `MindbreakHooks.java:81-91`，共 8 个，**没有** `END_SERVER_TICK`（对照原 `Callbacks.java:51` 的 `ChaosManager::tick` 已剔除）。

关于 `ServerPlayerEvents.JOIN` 的替代：合并代码注释称 1.20.1 的 fabric-api 0.92.12 没有该事件。我做了独立核实：

```bash
$ unzip -p libs/fabric-api/fabric-entity-events-v1-0.92.12.jar \
    net/fabricmc/fabric/api/entity/event/v1/ServerPlayerEvents.class | strings | grep -i 'join\|AFTER_RESPAWN'
        COPY_FROM
AFTER_RESPAWN
```

确实只有 `COPY_FROM` 和 `AFTER_RESPAWN`，**注释属实**，改用 `ServerPlayConnectionEvents.JOIN` 是正确的等价替换。

✅ **成立。**

### F.7 `/zr` 与 `/mindbreak` 共用同一份状态（无第二套开关）

```java
// TrollCommand.java:173-178
private static TrollConfig cfg() {
    if (ZhengrenMod.CONFIG == null) { ZhengrenMod.CONFIG = TrollConfig.load(); }
    return ZhengrenMod.CONFIG;
}
// MindbreakCommand.java:157-162 —— 完全相同的写法，同一个静态字段
```

- `/zr on|off` → `c.enabled`（`TrollCommand.java:47-60`）
- `/mb toggle` → `c.mindbreakEnabled`（`MindbreakCommand.java:52-62`），**不动** `enabled`
- 两者都 `c.save()` 写同一个 `config/zhengren.json`

**运行时实证**（RCON 依次执行，并直接读配置文件）：

```
===== RCON> zr intensity 3
§6精神污染强度：3 §7(等价于 /mindbreak intensity)
===== RCON> mb stats
【精神污染】状态：开着，强度 3，…          ← /zr 的修改被 /mb 读到
===== RCON> mb toggle
【精神污染】整活已关闭。你以为这就结束了？ §7(共用开关：/zr on|off 控制总开关)
===== RCON> mb stats
【精神污染】状态：关着，强度 3，精神污染事件 44 个，事件总数 615 个 §7(… / 总开关 开)   ← toggle 不影响总开关
```

```
$ cat run/config/zhengren.json
{
  "enabled": true,                  ← 总开关未被 toggle 改动
  ...
  "mindbreakEnabled": false,        ← toggle 生效
  "mindbreakIntensity": 3,          ← /zr intensity 生效
  "mindbreakHooks": true,
  "chatMangle": true
}
```

✅ **成立：单一 `ZhengrenMod.CONFIG`，无第二套开关状态。**

### F.8 原 43 个事件 id 在 `/zr fire <id>` 下可用

- `/zr fire` 的实现是 `TrollRegistry.has(id)` → `TrollScheduler.fireById` → `TrollRegistry.get(id)`（`TrollCommand.java:239-247`、`TrollScheduler.java:197-205`）。
- 43 个 id 全部通过 `TrollEvents.reg(CATEGORY, ...)` 进入 `TrollRegistry`（§B.3 已逐个比对），因此全部 `has(id) == true`。
- **运行时**：`/mb list` 返回全部 44 个 id；`/zr info chat_spam`、`/zr info relic_gift` 均能查到；`/zr fire chat_spam` 在控制台执行时返回的是"控制台请指定玩家"（说明命令已匹配、只是缺玩家参数），而非"找不到事件"：

```
===== RCON> zr fire chat_spam
§c控制台请指定玩家：/zr fire <id> <玩家>
```

✅ **成立。**

### F 小结

| # | 命题 | 判定 | 主要证据 |
|---|---|---|---|
| 1 | 精神污染进入 `/zr` 统一事件表 | ✅ | `TrollEvents.java:68` + 运行时 `zr info chat_spam` 显示分类「精神污染」 |
| 2 | 受统一配置约束（门控+加权） | ✅（`pick` 路径） | `TrollScheduler.java:127-129,147,169-174`；`TrollConfig.java:149-155` |
| 3 | ≥3 个事件用原生独有能力 | ✅（实际 5 个） | `MindbreakEvents.java:79,92,103-104,554-555,598` |
| 4 | `relic_gift` 发五件神器 | ✅ | `MindbreakEvents.java:711-730` + `ModItems.java:41-62` |
| 5 | 物品驱动统一事件表 | ✅ | `AbsurdStickItem.java:39-46` |
| 6 | 交互回调受统一开关约束 | ✅ | `MindbreakHooks.java:102-108` + 8 个回调逐一核对 |
| 7 | `/zr` 与 `/mindbreak` 共用状态 | ✅ | 两个 `cfg()` 同指 `ZhengrenMod.CONFIG` + RCON 实测 + 配置文件 |
| 8 | 原 43 个 id 可用 | ✅ | §B.3 逐个比对 + 运行时 `/mb list` |

---

## G. 缺陷清单（按严重程度）与未验证项

### 阻塞性缺陷

**无。** 构建通过、事件无丢失、资源路径与命名空间全部正确、运行时服务端可正常加载 615/44。

### 中级缺陷

#### D-1 `fabric.mod.json` 的事件数字错误：写 614 / 43，实际 615 / 44

- **文件**：`src/main/resources/fabric.mod.json:6`（jar 内同一份，`unzip -p … fabric.mod.json` 同样内容）
- **现象**：description 写「614 个整人事件（原 571 个 + 精神污染 43 个）」，而实际是 **615 = 571 + 44**（含桥接事件 `relic_gift`）。
- **影响**：该字符串会显示在 Mod Menu / 加载器 UI / 日志里，且与同仓库 `README.md:3,23,32,68,289` 的「615 / 44」自相矛盾；用户会以为少了一个事件。
- **证据**：

```bash
$ grep -n 'description' src/main/resources/fabric.mod.json
6:  "description": "整人大师与精神污染的合并体：614 个整人事件（原 571 个 + 精神污染 43 个，…
$ grep -n '615\|44 个事件' README.md | head -3
3:> 两个整人模组合并成一个：**615 个整人事件** …
32:1. **精神污染事件进入统一事件表** —— … 44 个事件以分类 `精神污染` 注册进 `TrollRegistry`，总数 571 → **615**。
```

- **建议**：改为「615 个整人事件（原 571 个 + 精神污染 44 个）」。

#### D-2 `TrollScheduler.randomEvent(cfg)` 不遵守黑名单 / `mindbreakEnabled`，与代码注释及 README 承诺不符

- **文件**：`src/main/java/com/zhengren/trollmod/core/TrollScheduler.java:207-213`
- **现象**：`randomEvent` 只用 `TrollRegistry.upTo(cfg.maxTier)` 按等级过滤，**完全忽略** `cfg.isDisabled(id)`（黑名单）、`cfg.mindbreakEnabled`、`mindbreakWeightScale()` 加权、事件冷却与 `when(...)` 条件。

```java
public static TrollEvent randomEvent(TrollConfig cfg) {
    List<TrollEvent> pool = TrollRegistry.upTo(cfg.maxTier);   // 仅等级过滤
    if (pool.isEmpty()) { return null; }
    return pool.get(RNG.nextInt(pool.size()));
}
```

- **受影响的调用点（3 处）**：
  1. `item/AbsurdStickItem.java:40` —— 该文件 21-24 行的 javadoc 明确声称「这样 `/zr` 的等级上限、**黑名单**、**强度缩放**以及事件自身的**冷却 / 条件判断**全部生效」，**这句话不成立**（只有等级上限生效）。
  2. `command/TrollCommand.java:254`（`/zr random`）
  3. `command/MindbreakCommand.java:129`（`/mindbreak chaos`）
- **可观察后果**：
  - 玩家把某事件加入 `disabled` 黑名单后，用离谱棒右键 / `/zr random` / `/mb chaos` 仍可能触发该事件。
  - `mindbreakEnabled=false` 时，`/mb chaos` 与 `/zr random` 仍可能抽出精神污染事件，与 `README.md:34`「`mindbreakEnabled=false` 时精神污染事件退出随机调度」及 `README.md:254` 的字段说明冲突（`/zr fire` 手动触发是明确允许的，但 `randomEvent` 属于随机抽取）。
- **证据**：`TrollRegistry.upTo` 只判 `e.tier().level() <= maxLevel`（`TrollRegistry.java:60-68`）；`pick()` 里的黑名单/开关判断（`TrollScheduler.java:124,127`）在 `randomEvent` 中不存在。
- **建议**（仅建议，未改代码）：让 `randomEvent` 复用 `pick()` 的过滤逻辑（或抽一个共用的 `isEligible(e, cfg)`），并把 `AbsurdStickItem` 的 javadoc 改为与实现一致。

### 低级缺陷

#### L-1 `ZhengrenMod.java:46` 注释仍写「43 个精神污染事件」

```bash
$ sed -n '46p' src/main/java/com/zhengren/trollmod/ZhengrenMod.java
        // 事件表：14 个原生分类 + 合并进来的 43 个精神污染事件
```

实际 44 个。仅注释，无功能影响。

#### L-2 两个配方的 `"category": "food"` 不是 1.20.1 `crafting_shapeless` 的合法分类值

- **文件**：`data/zhengren/recipes/mystery_meat.json:3`、`data/zhengren/recipes/regret_cake.json:3`
- **事实**：1.20.1 的 `CraftingRecipeCategory` 只有 `building / redstone / equipment / misc` 四个值（javap 实测）：

```bash
$ javap -p net/minecraft/recipe/book/CraftingRecipeCategory.class | head -8
public final class net.minecraft.recipe.book.CraftingRecipeCategory extends java.lang.Enum<...> {
  public static final ... BUILDING;
  public static final ... REDSTONE;
  public static final ... EQUIPMENT;
  public static final ... MISC;
```

- **后果**：**不会导致配方失效**。1.20.1 用 `CraftingRecipeCategory.CODEC.byId(jsonCategory, MISC)` 读取（`ShapelessRecipe$Serializer` 字节码确认），而 `StringIdentifiable$Codec.byId(String, E)` 实现是 `Objects.requireNonNullElse(byId(id), default)` → 未知值静默回退 `misc`。所以配方正常可用，只是被归到「杂项」而非「食物」分类；严格校验类数据包工具可能报 warning。
- **建议**：改成 `"category": "misc"`（或 `"equipment"` 等合法值），避免依赖静默回退。

#### L-3 `MindbreakHooks.HOOK_CONTEXT` 是一个没有 `.run()` 的 `TrollEvent`

- **文件**：`src/main/java/com/zhengren/trollmod/mindbreak/MindbreakHooks.java:73-76`
- **现象**：

```java
private static final TrollEvent HOOK_CONTEXT = TrollEvent
        .of("mindbreak:hook", "精神污染交互回调", TrollTier.ANNOYING)
        .desc("MindbreakHooks 内部工具上下文，不参与随机调度")
        .build();                      // ← 没有 .run(...)
```

- **影响**：**无功能影响** —— 该对象从未 `TrollEvents.reg(...)` 注册，也从未 `fire`，仅用于构造 `TrollContext`。`TrollEvent.Builder.action` 默认是空 lambda（`TrollEvent.java:110`），因此即使被误 fire 也不会崩。
- 但与本任务「每个事件都必须有 `.desc()` 和 `.run()`」的验收规则字面冲突（§B.5 的 615 个真实事件全部合规，这一个不在注册表内）。
- **建议**：不要用 `TrollEvent` 充当上下文载体（例如给 `TrollContext` 加一个不需要 event 的构造重载）。

#### L-4 `/zr help` 不是有效子命令（裸 `/zr` 才有帮助）

```bash
===== RCON> zr help
Incorrect argument for commandzr help<--[HERE]
===== RCON> zr
§8===== §c整人大师 §8=====§7/zr on §f| §7off …§7/zr intensity <1-3> §f| §7gift …§8已合并精神污染：§7/zr list 可看到全部事件 §8(§7/mindbreak§8、§7/mb§8 亦可用)
```

- 合并新增的两条（`intensity` / `gift`）确实写进了帮助文本（`TrollCommand.java:188`），但帮助只能通过裸 `/zr` 触发。
- **属合并前既有行为**（`git show HEAD:…/TrollCommand.java` 也只有 `.executes(ctx -> help(...))`，无 `literal("help")`），且 README 未文档化 `/zr help`，因此**不是合并引入的缺陷**；仅提示用户敲 `/zr help` 会报语法错。对照 `/mb help` 是合法的（`MindbreakCommand.java:51`）。

#### L-5 `VERIFICATION.md` 已过期，与合并后状态矛盾

```bash
$ sed -n '6p;17p' VERIFICATION.md
**最终规模：571 个整人事件 · 14 大分类 · 36 个 class · jar 270 KB**
| 4 | **事件数量与唯一性** | … | **571 个事件**，**571 个 id，0 重复**（跨 14 个文件校验） |
```

`README.md` 已更新为 615 / 15 分类，但 `VERIFICATION.md` 仍是合并前（571 / 14 分类 / `dist/zhengren-1.0.0.jar`）的内容。交付文档不一致。

#### L-6 `DelayScheduler` 的任务列表在服务器停止时不清空

- **文件**：`core/DelayScheduler.java:20`（`private static final List<Scheduled> TASKS`，无 `ServerLifecycleEvents.SERVER_STOPPED` 清理）
- **影响**：低。同 JVM 内重载世界/重启服务器时，残留任务持有旧的 `ServerPlayerEntity` 引用，可能在新服务器实例上以错误的 tick 基准执行（`due` 基于旧 `server.getTicks()`）。真实生产环境通常一进程一服务器，风险很小。

### 信息级（非缺陷，但影响可复现性 / 需知晓）

#### I-1 `gradlew runServer` / `runClient` 开箱即失败（合并前既有）

- **现象**：

```
[main/WARN] (FabricLoader/Resolution) Mod resolution failed
[HARD_DEP_NO_CANDIDATE zhengren 1.0.0 {depends fabric-api @ [*]}]
[main/ERROR] (FabricLoader) Incompatible mods found!
 - Mod '整人大师 × 精神污染' (zhengren) 1.0.0 requires any version of fabric-api, which is missing!
```

- **原因**：`build.gradle` 用 `fileTree(dir: 'libs/fabric-api')` 直接吃 53 个 **模块** jar，它们各自的 mod id 是 `fabric-api-base` / `fabric-entity-events-v1` …，**没有**聚合 mod id `fabric-api`；而 `fabric.mod.json` 声明 `depends: fabric-api`。
- **性质**：这是合并前就存在的开发环境限制（`git diff HEAD -- build.gradle` 只改了 maven 仓库），**不是发布缺陷** —— 正式使用时用户会安装真正的 Fabric API。
- **我为验证做的处理**：在 `run/mods/` 放了一个仅含 `fabric.mod.json`（`id=fabric-api`, `version=0.92.12+1.20.1`）的 stub jar。`run/` 已被 `.gitignore` 忽略，未触碰 `src/` 与 `build.gradle`。

#### I-2 `Loaded 7 recipes` 是 1.20.1 的误导性日志（已排除，非缺陷）

见 §E.4：该数字是配方**类型**数（7 种），不是配方条目数；且日志无任何 recipe 解析错误。

#### I-3 事件内 `ctx.later` 闭包捕获 `TrollContext`

`item_steal`（`MindbreakEvents.java:151`）、`fake_ore`（`:629`）等把 `ctx` 捕获进延时闭包。若玩家在延时期间下线，闭包仍持有旧的 `ServerPlayerEntity`。`ctx.give/msg/broadcast` 内部对离线玩家的行为未在真实场景验证（见 §G 未验证项）。风险低。

### 未能验证的项目（诚实声明）

1. **没有用真实玩家客户端跑事件**：服务端虽真实启动、RCON 实测了命令与注册表，但**没有第二个客户端/玩家账号连入**，因此：
   - 615 个事件的**实际游戏内表现**（伤害数值、方块变化、掉落物）未在真人身上验证；
   - 客户端整活（`ctx.glitch` 的假崩溃/黑屏/抖动、假成就弹窗、标题、陨石雨）**没有在真实客户端渲染验证**；
   - 交互回调（挖方块被拒 5%、方块变身 4%、床拒绝 15%、聊天扭曲 18% 等）**未实测触发概率**，仅验证了代码路径与开关约束；
   - `swap_players` / `fake_friend_join` 等需要 ≥2 玩家的事件未实测。
2. **未把 remap 后的 `zhengren-1.0.0.jar` 装进真实客户端/服务端启动**：运行时验证用的是 Loom dev classpath（`build/classes` + `build/resources/main`）。jar 只做了结构与 intermediary 字节码反查（§D），未做"装进真实 Fabric 实例加载"的端到端验证。
3. **未在真正的 Fabric API 聚合 mod 下测试**：用的是 `libs/fabric-api/` 53 个模块 jar + stub（I-1）。
4. **未验证 `/zr intensity` 对实际抽取概率的数值影响**：代码路径（`effectiveWeight` 实时加权）与命令回显已确认，但"强度 1/2/3 下精神污染事件的实际出现频率比 ≈ 0.5 : 1 : 2"需要长跑统计，未做。
5. **未验证存档/配置迁移**：老 `config/zhengren.json`（无 4 个新字段）升级时是否按默认值 `true/2/true/true` 补齐 —— 依据 Gson 使用无参构造器（字段初始值会生效）推断可行，但**未实测旧配置升级**。
6. **未做压力/长稳测试**：`DelayScheduler` 在大量延时任务下的表现、`STATES` map 是否会随玩家进出无限增长（`TrollScheduler.STATES` 无清理逻辑，属既有设计）未验证。
7. **未验证 en_us 的脏话移除是否彻底**：task-3 提到"TAUNTS 里有一条脏话已在移植时移除"，我只确认了 `Msgs.java:64-76` 的 11 条 TAUNTS 均为无脏话文案，未逐字与 mindbreak 原始 `Msgs.java` 做全量 diff。

---

## 附录 A：`clean build` 完整输出

```
To honour the JVM settings for this build a single-use Daemon process will be forked.
Daemon will be stopped at the end of the build

> Configure project :
Fabric Loom: 1.6.12

> Task :clean
> Task :compileJava
> Task :processResources
> Task :classes
> Task :jar
> Task :sourcesJar
> Task :compileTestJava NO-SOURCE
> Task :processTestResources NO-SOURCE
> Task :testClasses UP-TO-DATE
> Task :test NO-SOURCE
> Task :validateAccessWidener NO-SOURCE
> Task :check UP-TO-DATE
> Task :remapJar
> Task :remapSourcesJar
> Task :assemble
> Task :build

BUILD SUCCESSFUL in 1m 44s
7 actionable tasks: 7 executed
```

## 附录 B：43 个原事件权重保真度完整输出

```
orig events: 43  merged events: 44

id                          origW  mergedW  status
chat_spam                     20       20  OK
fake_achievement               8        8  OK
fake_death                     4        4  OK
system_glitch                  6        6  OK
villager_gossip                6        6  OK
inventory_shuffle             10       10  OK
item_steal                     7        7  OK
inventory_curse                5        5  OK
hotbar_swap                    6        6  OK
drop_item                      4        4  OK
gift                           9        9  OK
fake_diamond                   8        8  OK
xp_blessing                    6        6  OK
hunger_drain                   8        8  OK
force_feed                     4        4  OK
levitation                     8        8  OK
effect_roulette                7        7  OK
self_hurt                      5        5  OK
mob_buff                       6        6  OK
small_teleport                 8        8  OK
spin                           5        5  OK
chicken_invasion               7        7  OK
pig_parade                     5        5  OK
mob_taunt                      9        9  OK
creeper_apology                5        5  OK
enderman_visitor               4        4  OK
tnt_stalker                    4        4  OK
diamond_rain                   5        5  OK
weather_chaos                  4        4  OK
time_chaos                     4        4  OK
spooky_sound                   8        8  OK
lightning_tease                4        4  OK
shadow_follow                  4        4  OK
total_chaos                    2        2  OK
chat_takeover                  3        3  OK
neck_crack                     6        6  OK
anvil_prank                    4        4  OK
fake_ore                       6        6  OK
ice_floor                      5        5  OK
fake_player_chat               6        6  OK
fake_friend_join               4        4  OK
swap_players                   4        4  OK
totem_tease                    5        5  OK

weight mismatches: 0
no-weight events in merged: []
```

## 附录 C：README 事件表 vs 源码实测（逐分类核对）

```
OK  AudioEvents.java       count= 31 tiers(PRANK/ANNOY/BRUTAL/NIGHT/APOC)=[8, 10, 6, 5, 2]  readme=[8, 10, 6, 5, 2]
OK  BeastEvents.java       count= 47 tiers=[9, 11, 13, 9, 5]   readme=[9, 11, 13, 9, 5]
OK  ChaosEvents.java       count= 26 tiers=[2, 3, 8, 8, 5]     readme=[2, 3, 8, 8, 5]
OK  DoomEvents.java        count= 45 tiers=[3, 5, 13, 12, 12]  readme=[3, 5, 13, 12, 12]
OK  GlitchEvents.java      count= 35 tiers=[5, 10, 10, 6, 4]   readme=[5, 10, 10, 6, 4]
OK  ItemEvents.java        count= 34 tiers=[7, 12, 8, 5, 2]    readme=[7, 12, 8, 5, 2]
OK  LootEvents.java        count= 57 tiers=[12, 13, 15, 12, 5] readme=[12, 13, 15, 12, 5]
OK  MindEvents.java        count= 48 tiers=[10, 16, 11, 8, 3]  readme=[10, 16, 11, 8, 3]
OK  MobEvents.java         count= 33 tiers=[5, 9, 8, 7, 4]     readme=[5, 9, 8, 7, 4]
OK  NightmareEvents.java   count= 46 tiers=[6, 10, 12, 12, 6]  readme=[6, 10, 12, 12, 6]
OK  PlayerEvents.java      count= 38 tiers=[4, 16, 11, 5, 2]   readme=[4, 16, 11, 5, 2]
OK  SocialEvents.java      count= 46 tiers=[12, 13, 11, 6, 4]  readme=[12, 13, 11, 6, 4]
OK  TrapEvents.java        count= 52 tiers=[13, 14, 12, 8, 5]  readme=[13, 14, 12, 8, 5]
OK  WorldEvents.java       count= 33 tiers=[6, 10, 7, 6, 4]    readme=[6, 10, 7, 6, 4]
OK  MindbreakEvents.java   count= 44 tiers=[9, 24, 9, 1, 1]    readme=[9, 24, 9, 1, 1]
TOTAL tiers: [111, 176, 154, 110, 64] = 615
README total claim: [111, 176, 154, 110, 64] = 615
```

## 附录 D：运行时验证的临时产物（均不属交付物）

| 路径 | 用途 | 是否影响仓库 |
|---|---|---|
| `run/`（8.5 MB） | 服务端冒烟测试（eula / server.properties / world / config） | `.gitignore` 已忽略 `run/`，`git status` 干净 |
| `run/mods/fabric-api-stub.jar` | 补 `fabric-api` 聚合 mod id（见 I-1） | 同上 |
| `run/config/zhengren.json` | RCON 命令生成的运行时配置，用于证明共用状态 | 同上 |
| `/tmp/rcon.py`、`/tmp/zr-runserver*.log`、`/tmp/zr_ids_all.txt` 等 | RCON 客户端与日志证据 | 仓库外 |
| `build/libs/zhengren-1.0.0.jar` | `clean build` 产物（§A/§D 的证据） | `.gitignore` 已忽略 `build/` |

---

**最终判定：合并结果通过验收。** 615/44 事件、单套系统、资源与 jar 完全自洽、8 条互通命题全部成立；2 个中级缺陷（D-1 数字错误、D-2 `randomEvent` 过滤缺失）建议在发布前修复，其余为低风险或文档一致性问题。

---

## 附录 E：Lead 修复记录（验证后）

验证者报出的 2 个中级缺陷与 3 个值得修的低级问题，已在验证完成后由 Lead 修复，并重新跑通 `clean build`。

| 编号 | 问题 | 修复内容 | 复核证据 |
|---|---|---|---|
| **D-1** | `fabric.mod.json` 写「614 个（571+43）」 | 改为「615 个（原 571 个 + 精神污染 44 个）」 | `unzip -p build/libs/zhengren-1.0.0.jar fabric.mod.json \| grep 615` → 命中 |
| **D-2** | `TrollScheduler.randomEvent()` 只按等级过滤，忽略黑名单 / `mindbreakEnabled` / 强度加权 | 新增 `randomEventFiltered(TrollConfig)`：过滤黑名单 + 精神污染开关 + 等级，并按 `effectiveWeight()` 加权；`AbsurdStickItem`、`/zr random`、`/mindbreak chaos` 三处调用点全部切过去 | `grep -rn randomEventFiltered src/main/java` → 3 处调用点 + 定义；`unzip -p ... TrollScheduler.class \| strings \| grep randomEventFiltered` → 命中 |
| 低级 | `ZhengrenMod.java:46` 注释写「43 个精神污染事件」 | 改为 44 | `grep -n '44 个精神污染事件'` → 命中 |
| 低级 | `regret_cake` / `mystery_meat` 配方 `"category":"food"` 在 1.20.1 非法（仅 building/redstone/equipment/misc） | 两处改为 `"misc"` | `grep -h category data/zhengren/recipes/*.json \| sort \| uniq -c` → 5 个全为 misc |
| 低级 | `DelayScheduler` 停止服务器时不清空任务列表 | 新增 `clear()`，并注册 `ServerLifecycleEvents.SERVER_STOPPED` 调用 | 源码 `DelayScheduler.register()` 含 SERVER_STOPPED 注册 |

**修复后的最终构建（Lead 执行）：**

```bash
cd zhengren-mod
export LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_HOME=/usr/lib/jvm/java-17-openjdk-arm64
flock /tmp/zr-build.lock bash gradlew clean build --no-daemon
# → BUILD SUCCESSFUL in 2m 1s / 7 actionable tasks: 7 executed
```

产物复核：

```
build/libs/zhengren-1.0.0.jar   336174 bytes
Fabric-Mapping-Namespace: intermediary   （已 remap，可供正式客户端加载）
class major version: 0x3d = 61          （Java 17 字节码）
data/zhengren/recipes/ 复数 5 个；assets/zhengren/{textures,models}/item 各 5 个；lang 2 个
无 data/mindbreak 或 assets/mindbreak 残留
```

> 未修的低级项：`MindbreakHooks` 里那个未注册、未执行的 `TrollEvent` 上下文对象（仅用作复用 `TrollContext` 的载体，`.run()` 走默认空实现，无副作用）；`/zr help` 的既存拼写问题；`gradlew runServer` 开箱需补 `fabric-api` 聚合 mod id（合并前既有，不影响发布产物）。

---

## 附录 F：修复后的端到端运行时复测（Lead 执行）

修复后重新 `clean build`（BUILD SUCCESSFUL in 2m 1s），并用 `gradlew runServer` 真实启动 MC 1.20.1 服务端，
通过 RCON（`server-port=25577` / `rcon.port=25575`）实测命令。**全程 0 条 ERROR / Exception**。

### F-1 启动日志（事件表规模）

```
[09:28:13] [main/INFO] (zhengren) [整人大师] 加载完成：共 615 个整人事件
          （小整111 / 烦人176 / 抓狂154 / 崩溃110 / 地狱64），其中「精神污染」44 个
[09:28:48] [Server thread/INFO] (Minecraft) Done (21.703s)! For help, type "help"
[09:28:48] [Server thread/INFO] (Minecraft) RCON running on 0.0.0.0:25575
```

无 `发现 N 个重复事件 ID` 警告 → 615 个 id 全部唯一。

### F-2 两套命令共用同一份状态（互通核心命题，运行时实证）

```
>>> zr count
§6事件总数：§f615 §7| §a小整 111 §e烦人 176 §6抓狂 154 §c崩溃 110 §4地狱 64

>>> mb stats
【精神污染】状态：开着，强度 2，精神污染事件 44 个，事件总数 615 个
    §7(交互回调 开 / 聊天扭曲 开 / 总开关 开)

>>> zr intensity 3
§6精神污染强度：3 §7(等价于 /mindbreak intensity)

>>> mb stats          ← 用 /mb 读 /zr 刚改的值
【精神污染】状态：开着，强度 3，精神污染事件 44 个，事件总数 615 个
```

```
>>> mb toggle
【精神污染】整活已关闭。…… §7(共用开关：/zr on|off 控制总开关)
```

落盘配置（`run/config/zhengren.json`）实测：

```json
{"enabled": true, "mindbreakEnabled": false, "mindbreakIntensity": 3,
 "mindbreakHooks": true, "chatMangle": true}
```

→ `/mb toggle` 只翻 `mindbreakEnabled`，`enabled` 总开关保持 `true`；`/zr intensity` 与 `/mb stats`
读写同一字段。**不存在第二套状态**（这正是 D-2 类缺陷的反面证据）。

### F-3 精神污染事件已进入统一事件表

```
>>> mb list
【精神污染】共 44 个事件：chat_spam, fake_achievement, ..., totem_tease, relic_gift

>>> zr info relic_gift
§8--- §frelic_gift §8---§7名称：§a[小整] §f神器馈赠§7分类：精神污染 §7权重：6 §7冷却：300s
§7说明：桥接事件：融合整人大师物品系统，随机赠送五件神器之一（ABSURD_STICK / ...）

>>> zr info total_chaos
§8--- §ftotal_chaos §8---§7名称：§4§l[地狱] §f总混沌§7分类：精神污染 §7权重：2 §7冷却：300s
§7说明：融合整人大师独有 meteorShower + glitch，并直接调用统一调度器再触发两个随机事件

>>> zr list 21
§8===== 事件列表 §7(第 21/21 页，共 615 个) §8=====
... §7分类：世界与地形 / ... / 末日与毁灭 / 精神污染
```

→ 精神污染事件可被 `/zr info` 查询、被 `/zr list` 分页列出（第 21 页恰为精神污染事件），
分类表共 **15 类**（14 原生 + 精神污染）。

### F-4 其它

```
>>> zr reload      → §a配置已重载。
>>> zr count       → 615（重载后事件表不变）
```

**结论：修复后的构建在真实服务端上启动干净、事件表规模正确、两套命令状态完全互通、精神污染事件已融入统一事件表。**
