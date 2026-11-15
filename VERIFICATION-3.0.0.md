# VERIFICATION-3.0.0.md — 独立对抗性验证报告

**验证对象**: zhengren-mod v3.0.0「残酷化」(commit 44d6315, 基线 v2.0.0 = b7ff2a9)
**验证者**: 独立对抗性验证者（第三位）
**方法**: 静态源码解析 + 与 `/tmp/src_backup_pre_harden/` 逐 id / 逐文件回归比对 + 编译校验
**约束**: 只读 `src/`；唯一写入本文件。未跑 clean build（用 `flock /tmp/zr-build.lock bash gradlew compileJava`）。

> 状态：进行中，逐节追加。

---

## 0. 验证环境与方法

- 工作目录：`/data/data/com.termux/files/home/整人模组工作区/zhengren-mod`
- 环境：`LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_HOME=/usr/lib/jvm/java-17-openjdk-arm64`
- 编译：`flock /tmp/zr-build.lock bash gradlew compileJava` → `BUILD SUCCESSFUL`（**未跑 clean build**，遵守约束）
- 只读 `src/`；唯一写入文件为本报告。
- 基线来源（重要，见 I-1）：
  - `git show b7ff2a9` 导出的**真实 v2.0.0 源码树** `/tmp/v2tree`（60 个 java 文件，无 `PunishmentPool.java`）→ 本次回归的主基线；
  - 任务提供的 `/tmp/src_backup_pre_harden/`（61 个 java 文件）→ 实测**不是** v2，见 I-1。
- 静态解析：Python 正则 + 按 `TrollEvent.of(` 切块（2000 块全部解析成功，无 unparsed head）。
- 动态验证：用 Gradle init script 导出 `compileClasspath`，把真实编译产物拷到 ASCII 路径后用反射调用真实 `TrollConfig.migrateIfNeeded()`（见 §4）。
- 环境陷阱记录：项目路径含中文 `整人模组工作区`，在未设 UTF-8 的 JVM 下 `javac -cp <项目路径>` 会报 `package ... does not exist` / `class not found`；须先 `cp` 到 ASCII 路径。

---

## 1. 事件表回归

**结论：通过（无差异）。**

解析范围：`events/*.java`(14) + `expansion/*.java`(14) + `mindbreak/MindbreakEvents.java`(1)，正则 `TrollEvent\.of\("([a-z0-9_]+)"`。

| 指标 | v3.0.0 | v2 备份 | 差异 |
|---|---|---|---|
| 事件 id 出现总次数 | **2000** | 2000 | 0 |
| 唯一 id 数 | **2000** | 2000 | 0 |
| 重复 id | **0** | 0 | 0 |
| `TrollEvents.reg(` 总数 | **2000** | 2000 | 0 |
| 逐文件 `reg(` 数量不一致的文件 | **0** | — | 0 |
| id→文件 映射发生移动的 id | **0** | — | 0 |
| 仅在 v3 存在的 id | **0** | | |
| 仅在 v2 存在的 id | **0** | | |

逐文件 `of=` 数量（v3 与备份完全一致）：

```
events/AudioEvents 31, BeastEvents 47, ChaosEvents 26, DoomEvents 45, GlitchEvents 35,
ItemEvents 34, LootEvents 57, MindEvents 48, MobEvents 33, NightmareEvents 46,
PlayerEvents 38, SocialEvents 46, TrapEvents 52, WorldEvents 33   → 小计 571
expansion/ 13 个 99 + FestivalEvents 98                            → 小计 1385
mindbreak/MindbreakEvents 44                                       → 小计 44
合计 2000
```
> `expansion/FestivalEvents.java = 98`（其余 expansion 均 99）与任务给定基线一致。

**两个统计陷阱均已正确处理：**
1. `core/TrollEvent.java:10` javadoc 内的 `reg(TrollEvent.of("id", ...` 示例 — 不在解析范围内（未计入）。
2. `mindbreak/MindbreakHooks.java:74` 的伪事件 `"mindbreak:hook"` 跨行书写（`TrollEvent\n .of("mindbreak:hook"`），且 id 含 `:`，两种写法都会被排除。原始 `grep -rn 'TrollEvent\.of('` 得到 2001（2000 真实 + 1 javadoc），据此可确认伪事件确为跨行、未混入统计。

**`.desc(` / `.run(` / `.build()` 完备性**：逐文件计数与 `of=` 严格相等，全库合计各 **2000**，无缺项。

---

## 2. 纪律与自造 API

**结论：通过（无自造 API、无越界 API、编译通过）。**

### 2.1 编译证据
`flock /tmp/zr-build.lock bash gradlew compileJava` → `BUILD SUCCESSFUL`（`Task :compileJava UP-TO-DATE`）。
`UP-TO-DATE` 单独不足以证明，故进一步比对时间戳：`build/classes/...` 最新 class = `1794711942`（FestivalEvents.class），
`src/main/java` 最新源文件 = `1794711527`（TrollCommand.java）。**class 比全部源码新约 415s**，即上一次成功编译已覆盖当前全部源码 →
当前源码可编译，不存在拼写/签名/符号错误。共 64 个 class（61 个 java 文件 + 内部类）。

### 2.2 越界 API
| 检查 | 结果 |
|---|---|
| `grep -rn 'net\.minecraft' src/main/java/.../expansion/` | **0** ✅ |
| `grep -rn 'END_SERVER_TICK' src/main/java/` | 仅 **2** 处：`core/TrollScheduler.java:35`、`core/DelayScheduler.java:37` ✅ |

### 2.3 `ctx.*` 自造 API 审计
对 29 个事件文件（events 14 + expansion 14 + MindbreakEvents 1）提取**全部** `ctx.<name>` 引用（含方法调用与字段读取），
去重后与 `core/TrollContext.java` 的 public 方法 / public 字段全集比对：

- 出现的 `ctx.*` 方法名：**168** 个；`ctx.<name>` 字段读取：0 个未匹配
- `TrollContext` public 方法：184 个
- **不存在的 `ctx.*` 成员：0** ✅

历史真实事故的四类签名逐一复查，**全部为 0 命中**：

| 历史事故 | 检查 | 结果 |
|---|---|---|
| `ctx.invertAccept()` | 全库字面搜索 | 0 ✅ |
| `ctx.summonAnnoying()` | 全库字面搜索 | 0 ✅ |
| `ctx.msg(String, boolean)` | `TrollContext.java:204` 只有 `msg(String)` 单参重载；全库 `ctx.msg(...,true/false)` 正则 | 0 ✅ |
| `ctx.soundSelf(SoundEvent[], …)` | `TrollContext.java:276` 只有 `soundSelf(SoundEvent,float,float)`；全库搜索数组字面量实参 | 0 ✅ |

### 2.4 重载元数（arity）交叉校验
对每个 `ctx.<name>(` 调用做顶层实参计数，与 `TrollContext` 中同名方法的合法 arity 集合比对。
多 arity 方法：`ri[1,2]`、`title[2,5]`、`sound[1,3]`、`teleport[1,3]`、`give[1,2]`、`damage[1,2]`、`glitch[2,3]`。
仅 2 处报告为异常，人工复核后**均为 javadoc 注释内的 `{@code ctx.later(...)}` 示例**（`events/NightmareEvents.java:14`、`mindbreak/MindbreakEvents.java:49`），非真实调用。**真实 arity 违规 0**。

### 2.5 常量池引用
| 检查 | 结果 |
|---|---|
| `TrollPools` public 常量 | 40 个 |
| 事件文件中 `TrollPools.*` 引用 | 全部存在；唯一"未命中"是 `events/AudioEvents.java:26` 注释里的 `TrollPools.java` 文件名，非引用 ✅ |
| `GlitchIds` public 常量 | 36 个 |
| 事件文件中 `GlitchIds.*` 引用 | 35 个，**全部存在，0 缺失** ✅ |

---

## 3. 残酷化接入核查

**结论：主干全部接上；发现 1 处「文档声称缩放但代码未缩放」。**

### 3.1 第 6 档

`core/TrollTier.java:14-15`
```java
APOCALYPSE(5, "地狱", "Apocalypse"),
OBLIVION(6, "绝望", "Oblivion");
```
`core/TrollTier.java:59-63` 新增 `default:` 分支返回 `§0§l§k||§r§4§l[绝望]§r§0§l§k||`（原 v2 是 `case APOCALYPSE`）。与 v2 diff 确认新增了 OBLIVION 一档 ✅
上限：`TrollConfig.java:27 maxTier = 6`；`:190-191` clamp 到 6（v2 是 clamp 到 5）✅

### 3.2 TrollContext 倍率（逐项）

| 入口 | 行号 | 代码 | 结果 |
|---|---|---|---|
| `damage(float)` | 672-674 | `player.damage(..., (float)(amount * brutality()))` | ✅ |
| `damage(String,float)` | 676-677 | `float a = (float)(amount * brutality());` 各 case 均用 `a` | ✅ |
| `damageOther` | 1107-1109 | `o.damage(..., (float)(amount * brutality()))` | ✅ |
| `effect` | 647-651 | `int t = scaledTicks(ticks); int a = amplifier + brutalityAmp();` | ✅ 时长× + 等级+ |
| `effectAll` | 1161-1165 | `int t = scaledTicks(ticks); int a = Math.min(255, amplifier + brutalityAmp());` | ✅ |
| `boom` | 868-870 | `Math.min(MAX_BOOM, power * brutality())`（MAX_BOOM=40） | ✅ |
| `boomAtPlayer` | 872-874 | 转调 `boom` | ✅ |
| `boomDestructive` | 877-883 | `Math.min(MAX_BOOM, power * brutality())` + `allowWorldDamage` 决定 TNT/NONE | ✅ |
| `setFire` | 708-710 | `(int) Math.min(600, Math.max(1, seconds * brutality()))` | ✅ |
| `setFireTicks` | 712-714 | `scaledTicks(ticks)` | ✅ |
| `scatter` | 339-347 | `int r = scaledRange(range)` | ✅ |
| `scatterFar` | 347-355 | `scaledRange(range)` + `ty = y() + ri(-8,25)*brutality()` | ✅ |
| `launch` | 359-362 | `power * brutality()` | ✅ |
| `fling` | 364-368 | `dx*s, dy*s, dz*s` | ✅ |
| `lift` | 356-358 | `y() + height * brutality()` | ✅ |
| `heal` | 699-702 | `player.heal((float)(amount / brutality()))` — 除法 ✅ |
| `meteorShower` | 1249-1252 | `n = min(80, count * brutality())` | ✅ |
| `takeRandomItem()` | 443-445 | `takeRandomItems(1 + (int)(brutality()/1.5))` | ✅ |
| `dropRandom()` | 538-541 | `takeRandomItems(1 + (int)(brutality()/1.5))` | ✅ |
| **`takeRandomItems(int n)`** | **452-466** | **方法体完全不引用 `brutality()`** | **❌ 见 D-1** |

**D-1（中）**：`core/TrollContext.java:449-451` 的 javadoc 明确写着
```
* 随机拿走 n 个物品（残酷度越高，语义上的"一个"越接近于"一把"）。
```
但 452-466 行实现为 `while (taken < n && attempts < 40)`，**`n` 未乘任何倍率**。结果：`ctx.takeRandomItem()`（68 处）确实随残酷度放大，而事件里直接写的 `ctx.takeRandomItems(k)`（**203 处**）拿走的数量与 `brutality` 完全无关。这与任务清单里"`takeRandomItems` 是否乘上倍率"的期望不符，且属文档—实现不一致。

### 3.3 TrollScheduler 三件事（均接上）

`core/TrollScheduler.java`

- **惩罚附加** `:221-231`
```java
boolean rider = cfg.punishmentRider
        && event.tier().level() <= 2
        && RNG.nextInt(100) < cfg.punishmentChance;
if (rider) { PunishmentPool.apply(ctx); ... }
```
  `tier <= 2`（小整/烦人）✅、`punishmentRider` ✅、`punishmentChance` ✅
- **连击** `:236-248`
```java
if (chainDepth < cfg.maxChainDepth && RNG.nextInt(100) < cfg.chainChance) {
    TrollEvent next = pick(...);
    if (next != null) { chainDepth++; try { ...; fire(server, player, cfg, st, next); } finally { chainDepth--; } }
}
```
  `chainChance` ✅、`maxChainDepth` ✅、`finally` 保证深度回退、最多 4 连（depth 0..3）不会无限递归 ✅
- **rage 增长** `:213` `st.rage += event.tier().level() * 2.0 + (cfg.brutality - 1);`
  同时 `nextInterval` 从 v2 的 `min(0.65, rage/400)` 收紧到 `min(0.92, rage/250)`，下限 20 tick ✅
- **重生不赦** `:36-52`
```java
ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
    TrollConfig cfg = ZhengrenMod.CONFIG;
    if (cfg == null || !cfg.enabled || !cfg.noMercy) return;
    ... PunishmentPool.apply(ctx); newPlayer.sendMessage(... "§0§l[不赦]..." ...);
});
```
  `AFTER_RESPAWN` ✅、`noMercy` 开关 ✅、异常包裹 ✅

### 3.4 TrollConfig 残酷化字段与倍率

`core/TrollConfig.java`
- `:27 maxTier = 6`；`:56 brutality = 5`；`:59 punishmentRider = true`；`:61 punishmentChance = 100`；`:63 chainChance = 45`；`:65 maxChainDepth = 3`；`:67 noMercy = true`；`:95 configVersion = 0` ✅（**初始值确为 0，不是 3**）
- `:245-253 brutalityScale()`
```java
case 1 -> 0.5;  case 2 -> 1.0;  case 3 -> 1.8;  case 4 -> 3.0;  default -> 5.0;
```
  五档映射与文档一致 ✅；`brutalityAmplifierBonus() :256-263` = 0/0/1/2/3 ✅
- `:135-165 migrateIfNeeded()`：`:136 if (configVersion >= 3) return;` 短路 ✅；`:160 configVersion = 3;`
- `:187-237 reindex()` 对 brutality/punishmentChance/chainChance/maxChainDepth 全部做了范围 clamp ✅

### 3.5 PunishmentPool 条目数

`core/PunishmentPool.java`：`private static void add(...)` 调用 **28 次**（`grep -c '^\s*add('` = 28），与"28 种惩罚"一致 ✅。
分类：肉体 8 / 装备财产 6 / 禁锢 5 / 生物爆炸 5 / 心理 3 / 终结技 1。
`apply()` `:187-201` 在 `brutality < 4` 时会 `limit = size()-1` 排除最后一个终结技（`obliterate`），逻辑自洽 ✅

---

## 4. 配置迁移（真机逻辑验证）

**结论：主用例通过；发现 1 处「不可区分即覆盖」的副作用（D-2）。**

方法与证据强度：**不是纯读代码**。我用 Gradle init script（`-I /tmp/cp.gradle`，不修改 `build.gradle`）导出 `sourceSets.main.compileClasspath`，
把 `build/classes/java/main` 拷到 ASCII 路径（项目路径含中文会让 JVM 的 javap/java 找不到文件），再用真实编译产物 `core/TrollConfig` 跑反射调用
`migrateIfNeeded()`（`javac -proc:none` + `java`）。这测的是**真实的 class 文件**，包含了真实的 Gson 反序列化行为。

> 注：分类路径含中文（`整人模组工作区`）在未设 UTF-8 的情况下会让 `javac -cp <项目路径>` 报 "package does not exist" / "class not found"，这是环境陷阱，不是项目缺陷。

### 4.1 用例 A：v2 默认配置（无 `configVersion`）→ 应迁移

输入 JSON：`{"maxTier":5,"minIntervalSeconds":20,"maxIntervalSeconds":75,"maxEventsPerMinute":14,"allowWorldDamage":false}`
```
before: configVersion=0 maxTier=5 interval=20-75 maxPerMin=14 allowWorldDamage=false brutality=5
[INFO] [整人大师] 已把旧版默认配置迁移到 v3 残酷默认值（maxTier=6 / 间隔 8-30s / 每分钟 40 / 允许破坏地形）
after : configVersion=3 maxTier=6 interval=8-30 maxPerMin=40 allowWorldDamage=true  brutality=5
```
✅ 迁移生效；`configVersion` 缺字段确实保留初始值 0（Gson 调用了隐式无参构造，见 `TrollConfig` 无显式构造器）。
v2 真实默认值已用 `git show b7ff2a9` 复核：`maxTier=5 / 20-75 / 14 / allowWorldDamage=false`（`/tmp/v2tree/.../TrollConfig.java:27,29,31,38,46`）——迁移触发条件与 v2 默认值**逐项精确对应** ✅

### 4.2 用例 B：反向用例 `configVersion=3` + `maxTier=3`（模拟用户自定义）→ 不应改写
```
before: configVersion=3 maxTier=3 interval=20-75 maxPerMin=14 allowWorldDamage=false brutality=5
after : configVersion=3 maxTier=3 interval=20-75 maxPerMin=14 allowWorldDamage=false brutality=5
unchanged=true
```
✅ 完全未被改写（`>= 3` 短路）。

### 4.3 其他用例
| 用例 | 输入 | 输出 | 判定 |
|---|---|---|---|
| C 部分自定义 v2（maxTier=4, 间隔 45-90, 每分钟 10） | configVersion 缺 | maxTier=4 保留 ✅、间隔保留 ✅、`allowWorldDamage` false→**true** ❌、configVersion=3 | 见 D-2 |
| D 全新 v3 默认 JSON | maxTier=6/8-30/40/true | 值不变，仅 configVersion 0→3 | ✅ 无害 |
| E 全新对象 `new TrollConfig()` | — | 值不变，configVersion 0→3 | ✅ 无害 |
| F v2 中 maxTier=5（用户故意选 5） | 同上 | maxTier 5→6 | ⚠️ 不可区分，见 D-3 |

### D-2（中）：`allowWorldDamage=false` 的既有配置被强制打开
`core/TrollConfig.java:152-155`
```java
// 地形破坏：旧默认 false → 新默认 true
if (!allowWorldDamage) {
    allowWorldDamage = true;
    changed = true;
}
```
v2 的默认就是 `false`，代码**无法区分"从没改过"与"用户明确关掉了地形破坏"**。实测用例 C/F 证明：只要用户在 v2 里关着地形破坏，升级后一律被打开。
这与 `:89-90` "因此用户手动改过的设置不会被覆盖"的承诺直接矛盾（其他三处用 `==旧默认值` 判定，只有这一处用 `!当前值` 判定）。
对不想要地形破坏的服主属于**破坏性升级**（会开始出现 `boomDestructive`/`lavaPit` 真实地形破坏）。

### D-3（低，已知歧义）：故意把 `maxTier` 设为 5 的 v2 用户会被抬到 6
`maxTier == 5` 与 v2 默认不可区分，用户主观设的上限被抬高。代码注释已承认这是取舍，且 v2 里 5 也是上限，影响有限。

---

## 5. 等级分布与危险 API

**结论：分布精确命中；硬性危险 API 规则 0 违规；`FAKE_CRASH` 有 1 处真违规（继承自 v2，未被 v3 修掉）。**

### 5.1 等级分布 —— 精确命中

方法：按 `TrollEvents.reg(TrollEvent.of(...)` 切块（2000 块全部解析成功），从第 3 个实参取 `TrollTier`。

| 档位 | 期望 | 实测 | 一致 |
|---|---|---|---|
| PRANK 小整 (1) | 32 | **32** | ✅ |
| ANNOYING 烦人 (2) | 218 | **218** | ✅ |
| BRUTAL 抓狂 (3) | 528 | **528** | ✅ |
| NIGHTMARE 崩溃 (4) | 518 | **518** | ✅ |
| APOCALYPSE 地狱 (5) | 446 | **446** | ✅ |
| OBLIVION 绝望 (6) | 258 | **258** | ✅ |
| **合计** | 2000 | **2000** | ✅ |

「等级脚本重排」的规模：与真实 v2.0.0 逐 id 比对，**1764 个事件改了档位，全部是升档，0 个降档**。
转换矩阵：`ANNOYING→BRUTAL 298`、`BRUTAL→APOCALYPSE 269`、`ANNOYING→NIGHTMARE 256`、`BRUTAL→NIGHTMARE 215`、
`PRANK→BRUTAL 183`、`PRANK→ANNOYING 148`、`NIGHTMARE→APOCALYPSE 137`、`NIGHTMARE→OBLIVION 136`、`APOCALYPSE→OBLIVION 122`。
id 集合本身 0 变化（见 §1），所以重排只动档位、不动事件身份。

### 5.2 危险 API 规则校验（v3）

规则与结果：

| 规则 | 允许档位 | v3 违规 | 说明 |
|---|---|---|---|
| `ctx.boomDestructive` | ≥ BRUTAL | **0** | 用量 OBLIVION 22 / APOCALYPSE 9 / NIGHTMARE 2 / BRUTAL 2 ✅ |
| `ctx.lavaPit` | ≥ BRUTAL | **0** | OBLIVION 19 / APOCALYPSE 17 / NIGHTMARE 64 / BRUTAL 2 ✅ |
| `ctx.voidPit` | ≥ BRUTAL | **0** | OBLIVION 75 / APOCALYPSE 70 / NIGHTMARE 7 / BRUTAL 1 ✅ |
| `ctx.obliterate` | ≥ APOCALYPSE | **0** | 全部在 OBLIVION（220 处）✅ |
| `ctx.buryAlive` | ≥ APOCALYPSE | **0** | OBLIVION 97 / APOCALYPSE 58 ✅ |
| `ctx.kill` | ≥ APOCALYPSE | **0** | APOCALYPSE 3 / OBLIVION 2 ✅ |
| `GlitchIds.FAKE_BAN` | ≥ NIGHTMARE | **0** | OBLIVION 15 / APOCALYPSE 7 / NIGHTMARE 3 ✅ |
| `GlitchIds.FAKE_KICK` | ≥ NIGHTMARE | **0** | ✅ |
| `GlitchIds.FAKE_CRASH` | = APOCALYPSE | **18**（17 在 OBLIVION、**1 在 NIGHTMARE**） | 见 F-1 / F-2 |

**v3 相比 v2 实际修掉了 3 类老违规**（这些是「等级脚本重排」的正向收益）：

| 事件 id | v2 档位 | v2 违规 | v3 档位 | v3 |
|---|---|---|---|---|
| `trap_void_pit_shallow` | ANNOYING (2) | 用了 `voidPit` | **NIGHTMARE (4)** | 已合规 |
| `trap_lava_pit_small` | ANNOYING (2) | 用了 `lavaPit` | **BRUTAL (3)** | 已合规 |
| `social_fake_ban_ui` | BRUTAL (3) | 用了 `FAKE_BAN` | **NIGHTMARE (4)** | 已合规 |
| `mind_fake_kick_tease` | BRUTAL (3) | 用了 `FAKE_KICK` | NIGHTMARE (4) | 已合规 |
| `nightmare_hard_blackout` | BRUTAL (3) | 用了 `HARD_BLACKOUT` | NIGHTMARE (4) | 已合规 |
| `chaos_fake_crash_siege` | NIGHTMARE (4) | 用了 `FAKE_CRASH` | **APOCALYPSE (5)** | 已合规 |
| `nightmare_triple_meltdown` / `mind_fake_crash` | NIGHTMARE (4) | 用了 `FAKE_CRASH` | **APOCALYPSE (5)** | 已合规 |

> v2 基线（`/tmp/v2tree`）共 13 处危险 API 违规，v3 只剩 1 处真违规（下方 F-2）。

### F-1（低，规则口径）`FAKE_CRASH` 在 OBLIVION 档出现 17 次

`GlitchIds.FAKE_CRASH` 被 17 个 **OBLIVION (6)** 事件使用，例如：
- `src/main/java/com/zhengren/trollmod/events/ChaosEvents.java:410` `chaos_ultimate_combo` (OBLIVION) → `ctx.glitch(GlitchIds.FAKE_CRASH, 80, 3);`
- `src/main/java/com/zhengren/trollmod/expansion/HallucinationEvents.java:1399` `hall_fake_crash_apoc` (OBLIVION)
- `src/main/java/com/zhengren/trollmod/mindbreak/MindbreakEvents.java:638` `total_chaos` (OBLIVION)

若规则按字面「只允许 APOCALYPSE」理解则是违规；若按「不得低于 APOCALYPSE」理解则 OBLIVION 更高、可以接受。
考虑到 OBLIVION 是 v3 新增的最高档（比 APOCALYPSE 更狠），且 v2 里对应事件本来就在 NIGHTMARE，判为**规则口径问题**而非安全回归。

### F-2（中，真违规，v2 遗留）`glitch_fake_crash_report` 在 NIGHTMARE 档使用 `FAKE_CRASH`

- 文件:行号：`src/main/java/com/zhengren/trollmod/events/GlitchEvents.java:419`
- id：`glitch_fake_crash_report`，等级：`TrollTier.NIGHTMARE` (4)
- 代码：`ctx.glitch(GlitchIds.FAKE_CRASH, 100, 2);`
- **v2.0.0 同样如此**（`/tmp/v2tree/.../GlitchEvents.java:328`，NIGHTMARE + `FAKE_CRASH`）→ 不是 v3 引入的回归，是**v3 重排时漏改**的遗留项。
- 其余同类 `FAKE_CRASH` 事件都被升到了 APOCALYPSE/OBLIVION，只有这一个停在 NIGHTMARE，属明显不一致。

### F-3（低，观察项）`HARD_BLACKOUT` 在 BRUTAL 档

- `src/main/java/com/zhengren/trollmod/expansion/HallucinationEvents.java:982`，id `hall_hard_blackout_br`，`BRUTAL` (3) → `ctx.glitch(GlitchIds.HARD_BLACKOUT, 20 * 15, 3);`
- 该规则不在任务给定清单内（`HARD_BLACKOUT` 属强制黑屏客户端整活），且 v2 也是这样（`/tmp/v2tree/.../HallucinationEvents.java:876`），仅作记录。

---

## 6. 诚实报告：未验证项与缺陷清单

*(待填)*
