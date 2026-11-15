# VERIFICATION-3.0.0.md — 独立对抗性验证报告

**验证对象**: zhengren-mod v3.0.0「残酷化」(commit 44d6315, 基线 v2.0.0 = b7ff2a9)
**验证者**: 独立对抗性验证者（第三位）
**方法**: 静态源码解析 + 与 `/tmp/src_backup_pre_harden/` 逐 id / 逐文件回归比对 + 编译校验
**约束**: 只读 `src/`；唯一写入本文件。未跑 clean build（用 `flock /tmp/zr-build.lock bash gradlew compileJava`）。

> 状态：**已完成**（§1-§6 针对 v3.0.0 `44d6315`；§7 为 HEAD 前移到 v3.0.1 `d01d701` 后的补充与勘误）。
> 重要前提：任务给的 `/tmp/src_backup_pre_harden/` **不是 v2 备份**（内含全部 v3 内容），本次额外用 `git show b7ff2a9`（真实 v2.0.0）作基线。详见 §0 与 I-1。

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
| `takeRandomItem()` | 443-445 | v3.0.0 实为 `takeRandomItems(1)` → **不缩放** | **❌ 见 D-1** |
| `dropRandom()` | 538-541 | `takeRandomItems(1 + (int)(brutality()/1.5))` | ✅ |
| **`takeRandomItems(int n)`** | **452-466** | **方法体完全不引用 `brutality()`** | **❌ 见 D-1** |

**D-1（中）**：`core/TrollContext.java:449-451` 的 javadoc 明确写着
```
* 随机拿走 n 个物品（残酷度越高，语义上的"一个"越接近于"一把"）。
```
但 452-466 行实现为 `while (taken < n && attempts < 40)`，**`n` 未乘任何倍率**。
因此 v3.0.0 中，财产类惩罚里唯一真正随残酷度放大的只有 `dropRandom()`（`takeRandomItems(1+brutality()/1.5)`）；
`ctx.takeRandomItem()`（68 处）与 `ctx.takeRandomItems(k)`（**203 处**）拿走的数量都与 `brutality` 无关，合计 **271 处调用点未接残酷度**，且属文档—实现不一致。

> **勘误（v3.0.1 复核）**：本报告初版此处曾把 `takeRandomItem()` 误写成"有缩放"（实际把 `dropRandom()` 的式子串到了它身上）。
> 经 `git show 44d6315:...TrollContext.java` 逐行复核，v3.0.0 的 `takeRandomItem()` 就是 `takeRandomItems(1)`，**未缩放**。
> 真实情况比初版描述**更严重**（271 处而非 203 处）。该缺陷已在 v3.0.1 修复，见 §7。

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

### 6.1 结论摘要

| 验证项 | 结果 |
|---|---|
| §1 事件表 2000 / 唯一 2000 / 零重复 | **通过** |
| §1 逐 id 与基线（备份 + 真实 v2.0.0）0 差异 | **通过** |
| §1 逐文件 `reg(` 数量、desc/run/build 齐全 | **通过** |
| §2 expansion 无 `net.minecraft`、`END_SERVER_TICK` 仅 2 处 | **通过** |
| §2 自造 API / 常量池引用（168 ctx + 40 TrollPools + 36 GlitchIds） | **通过**（缺失 0） |
| §2 编译（真实 compilable） | **通过** |
| §3 第 6 档 OBLIVION、maxTier=6 | **通过** |
| §3 惩罚附加 / 连击 / rage / 重生不赦 | **通过** |
| §3 倍率接入（16 项） | **15 项通过，1 项未接（D-1）** |
| §3 PunishmentPool 28 条 | **通过** |
| §4 配置迁移：v2 默认 → 迁移 | **通过**（真实 class 反射实测） |
| §4 配置迁移：`configVersion=3` → 不改写 | **通过**（真实 class 反射实测） |
| §5 等级分布 32/218/528/518/446/258 | **精确命中** |
| §5 危险 API 硬规则（boom/lavaPit/voidPit/obliterate/buryAlive/kill/FAKE_BAN） | **0 违规**（v2 的 3 类老违规已修） |
| §5 `FAKE_CRASH` 规则 | **1 处真违规（F-2，v2 遗留）+ 17 处口径问题（F-1）** |

### 6.2 缺陷清单（按严重度）

| 编号 | 严重度 | 位置 | 问题 |
|---|---|---|---|
| **D-1** | **中** | `core/TrollContext.java:449-466` | `takeRandomItems(int n)` 未乘 `brutality()`，与自身 javadoc 及任务清单矛盾；`takeRandomItem()` 有缩放，导致直接调用 `ctx.takeRandomItems(k)` 的 **203 处**事件财产惩罚不随残酷度增长 |
| **D-2** | **中** | `core/TrollConfig.java:152-155` | 迁移用 `if (!allowWorldDamage)`，把 v2 用户**明确关闭**的地形破坏强制打开；违反同文件 89-90 行「用户手动改过的设置不会被覆盖」的承诺。实测用例 C/F 复现 |
| **F-2** | **中** | `events/GlitchEvents.java:419` | `glitch_fake_crash_report` (NIGHTMARE) 使用 `FAKE_CRASH`，违反「FAKE_CRASH 只允许 APOCALYPSE」；v2 已如此，v3 重排漏改（同类事件其他都被升到 APOCALYPSE/OBLIVION） |
| **D-3** | 低 | `core/TrollConfig.java:141-144` | `maxTier == 5` 无法区分 v2 默认值与用户自选 5，用户上限被抬到 6（注释已承认） |
| **F-1** | 低 | 17 个 OBLIVION 事件（如 `events/ChaosEvents.java:410`、`mindbreak/MindbreakEvents.java:638`） | `FAKE_CRASH` 出现在比 APOCALYPSE 更高的 OBLIVION 档；按字面规则是违规，按「≥APOCALYPSE」理解可接受 |
| **F-3** | 低 | `expansion/HallucinationEvents.java:982` | `hall_hard_blackout_br` (BRUTAL) 使用 `HARD_BLACKOUT`；规则未定义，v2 亦如此，仅记录 |
| **O-1** | 低 | `core/TrollContext.java:997-1010` | `glitch()` 时长只乘 `config.glitchPower`，**不乘** `brutality()`；客户端画面折磨强度不随残酷度变化 |
| **O-2** | 低 | `core/TrollContext.java:1273-1305`、`:1227` | `voidPit`/`lavaPit` 半径、`spawnTnt` 引信时长未接残酷度（不在任务清单内，但与「爆炸/残酷 ×5」的叙述不一致） |
| **I-1** | 信息 | `/tmp/src_backup_pre_harden/` | **该"残酷化之前的备份"名不副实**：它含 `PunishmentPool.java`、`TrollTier.OBLIVION`、`brutality` 等全部 v3 内容，与当前源码唯一差异是 `TrollConfig` 的 `configVersion`/`migrateIfNeeded()`。它是「v3 未加迁移修复」的快照。用它做 v2 回归会得到误导性的"零差异" |

### 6.3 未能验证 / 验证强度不足的项

1. **未做端到端游戏内运行**。环境无 Fabric runtime，未启动服务端。因此以下只能静态推断：
   - 运行时 `TrollEvents.bootstrap()` 实际注册 2000 条且 `TrollRegistry.duplicates()` 为空（静态层面：2000 个唯一 id + 2000 次 `reg(` + 注册表按 id 去重，已足够强，但非运行时证据）；
   - `AFTER_RESPAWN` 回调在真实服务端的触发与 `PunishmentPool.apply` 的实际效果。已确认 `TrollContext` 中除构造器与 `event()` getter 外**没有任何方法解引用 `event` 字段**，故 `event=null` 的重生上下文不会 NPE（`grep -n '\bevent\b'` 仅 69/73 两行），这是静态上的安全性证据。
   - 未改动共享的 `run/config/zhengren.json`（备份/恢复都未执行，因为不需要）。
2. **「1326 处尾巴注入」无法复现**。事件文件中 `v3.0.0` 标记共 979 处，事件块内带 `v3.0.0`/`残酷` 标记的为 **975 / 2000**。无法确定 1326 的统计口径，故**不确认也不否定**该数字。
3. **「7 位写手重写内容」无法验证**（无作者信息，非代码可判定项）。只能确认：全部 29 个事件文件的 id 与 v2 完全一致，内容层改动没有增删事件。
4. **`brutality` 的"手感"未验证**：只验证了数值乘子存在与量级，未验证 5× 是否会导致服务端/客户端崩溃（例如 `MAX_BOOM=40` 上限外的级联）。抽样的上限保护确实存在：`scaledTicks ≤ 72000`、`scaledRange ≤ 500`、`setFire ≤ 600`、`MAX_BOOM = 40`、`meteorShower ≤ 80`、`takeRandomItems attempts < 40`。
5. **未验证 `.weight()` / `.cooldownSeconds()` / `.once()` / `.when()` 在重排后是否仍合理**（不在任务范围）。

### 6.4 无阻塞缺陷

未发现阻塞级（导致编译失败 / 事件表损坏 / 启动崩溃）缺陷。所有 §1、§2 的核心回归项 0 差异。
最需要 Lead 决策的是 **D-2**（配置迁移会打开地形破坏）与 **D-1**（`takeRandomItems` 未接残酷度）。

---

## 7. 补充与勘误：HEAD 已前移到 v3.0.1（d01d701）

验证期间工作区的 HEAD 从 `44d6315`（v3.0.0）前进到 `d01d701`（**v3.0.1「修复独立验证发现的 2 个中级缺陷」**）。
`git diff 44d6315 d01d701 -- src` 只改了 2 个文件：`core/TrollConfig.java`（+37/-11）与 `core/TrollContext.java`（+9/-3）——
**29 个事件文件与事件表完全未动**，因此 §1/§2/§5 的结论对 v3.0.1 同样成立（已重跑解析复核：2000 / 唯一 2000 / 零重复 / 分布 32-218-528-518-446-258）。

### 7.1 D-1 修复验证（`takeRandomItems` 接上残酷度）

`src/main/java/com/zhengren/trollmod/core/TrollContext.java:450-467`（v3.0.1）
```java
public int takeRandomItems(int n) {
    PlayerInventory in = inv();
    int want = (int) Math.min(in.size(), Math.max(1L, Math.round(n * brutality())));
    int taken = 0, attempts = 0;
    while (taken < want && attempts < 40) { ... }
```
同时 `takeRandomItem()` 改为 `takeRandomItems(1)`，`dropRandom()` 从 `takeRandomItems(1 + (int)(brutality()/1.5))` 改为 `takeRandomItems(1)`，**两处都是为了配合内部缩放、避免二次放大**。

对抗性复核：
- ✅ **无二次放大**：`grep` 全部 271 个事件调用点，参数里**没有任何一个含 `brutality`**；core 内部调用者只有 `takeRandomItem()`→`takeRandomItems(1)`、`dropRandom()`→`takeRandomItems(1)`、`PunishmentPool.java:69`→`takeRandomItems(3)`。
- ✅ **缩放量级合理**（按 `brutalityScale()` 计算 `want`）：`b=1(×0.5) n=1→1, n=3→2`；`b=3(×1.8) n=1→2, n=3→5`；`b=5(×5.0) n=1→5, n=3→15`。
- ✅ **有上限保护**：`min(in.size(), …)`（玩家背包 41 格），且 `attempts < 40` 防死循环。
- ⚠️ 副作用（低）：事件里最大的字面参数是 `takeRandomItems(12)`（10 处为 `ri` 之类动态值之外的最大常量），×5 时 = 60 → 被 `in.size()` 截到 41 → **一次清空整个背包**。属"残酷化"预期内，但比 v3.0.0（固定 12 件）明显更狠，值得 Lead 确认是否符合意图。
- ⚠️ 残留（低）：`want` 上限取 `in.size()`（41），而 `attempts` 上限只有 40，因此当 `want=41` 时最多只能拿到 40 件——1 件的边界误差，无实际影响。

### 7.2 D-2 修复验证（迁移不再无条件打开地形破坏）

`src/main/java/com/zhengren/trollmod/core/TrollConfig.java`（v3.0.1）新增 `looksUntouched` 前置条件：
```java
boolean looksUntouched = maxTier == 5
        && minIntervalSeconds == 20 && maxIntervalSeconds == 75
        && maxEventsPerMinute == 14
        && !allowWorldDamage
        && (disabled == null || disabled.isEmpty())
        && (targets == null || targets.isEmpty());
...
if (looksUntouched && !allowWorldDamage) { allowWorldDamage = true; changed = true; destroyedFlagChanged = true; }
```
注意 `looksUntouched` 在 `maxTier = 6` 改写**之前**求值，用的是原始值（正确）。

用真实 v3.0.1 class 文件（`build/classes/java/main` → `/tmp/mclasses2`）反射调用 `migrateIfNeeded()` 的实测结果：

| 用例 | 输入 | 迁移后 | 判定 |
|---|---|---|---|
| A v2 全默认 | `maxTier=5, 20-75, 14/分, worldDmg=false` | `cv=3 maxTier=6 8-30 40 worldDmg=`**true** | ✅ 预期迁移 |
| B `cv=3 maxTier=3` | 同上 + `configVersion=3` | **完全不变** | ✅ 反向用例通过 |
| C 部分自定义 | `maxTier=4, 45-90, 10/分, worldDmg=false` | `maxTier=4` 保留、间隔保留、**worldDmg=false 保留** | ✅ **D-2 已修复** |
| F 只贴 v2 默认（仅 maxTier=5） | 全默认 | `worldDmg=true` | ✅ 视为未改动，符合新策略 |
| G 自定义黑名单 | 全默认 + `disabled=["trap_x"]` | `maxTier=6`、**worldDmg=false**、黑名单保留 | ✅ 保守 |
| H 自定义白名单 | 全默认 + `targets=["Steve"]` | `maxTier=6`、**worldDmg=false**、白名单保留 | ✅ 保守 |

日志分支也正确：C 走 `"检测到自定义配置，已保留你的设置"`，A 走 `"…允许破坏地形"`，G/H 走 `"…地形破坏开关保持原样"`。

### 7.3 修复后仍存在的残留问题（更新版缺陷清单）

| 编号 | 严重度 | v3.0.1 状态 | 说明 |
|---|---|---|---|
| D-1 | 中 | **已修复** | `takeRandomItems` 已接残酷度，无二次放大；见 7.1 的两个低危副作用 |
| D-2 | 中 | **已修复** | `looksUntouched` 门控生效，自定义配置不再被覆盖 |
| D-2′ | 低（残留） | 未处理 | v2 中**从未改过任何设置**的配置仍会把 `allowWorldDamage` 由 false 抬为 true。这是"碰巧等于旧默认值"的固有歧义，无法在不增加额外标志位的前提下区分；现策略（只有在整份配置完全未动时才开）已是合理折中 |
| D-3 | 低 | 未处理 | `maxTier==5` 仍与 v2 默认不可区分，故意设 5 的用户被抬到 6（代码注释已承认） |
| F-2 | 中 | **未修复** | `events/GlitchEvents.java:419` `glitch_fake_crash_report` (NIGHTMARE) 仍使用 `FAKE_CRASH`；v3.0.1 未触及事件文件（`git diff` 已确认） |
| F-1 / F-3 / O-1 / O-2 | 低 | 未处理 | 见 §5.2 / §6.2 |
| I-1 | 信息 | 未处理 | `/tmp/src_backup_pre_harden/` 仍是误导性的"v3 无迁移版"快照，不是 v2 备份 |

### 7.4 对 v3.0.1 的结论

- **两个中级缺陷均已正确修复**，修复方式经真实 class 反射实测 + 调用点全量搜索验证，未引入二次缩放或新的迁移覆盖问题。
- **未修复的中级问题只剩 F-2**（`glitch_fake_crash_report` 档位与 `FAKE_CRASH` 不匹配），且它是从 v2 继承来的，不影响事件表完整性。
- 事件表、纪律、自造 API、等级分布（§1/§2/§5）在 `44d6315 → d01d701` 之间**完全未变**，原结论继续有效。



---

## 附录：Lead 对缺陷清单的处置（验证后）

### 已修复（在 v3.0.1 中）

| 编号 | 严重度 | 修复内容 |
|---|---|---|
| **D-1** | 中 | `TrollContext.takeRandomItems(int)` 内部按 `brutality()` 缩放目标数量（上限为背包大小）；`dropRandom()` 改回固定传 1，避免与内部缩放重复放大。 |
| **D-2** | 中 | `TrollConfig.migrateIfNeeded()` 改为：只有**整份配置完全是 v2 默认样子**（maxTier=5、间隔 20/75、每分钟 14、地形破坏关、黑名单白名单都空）时才打开 `allowWorldDamage`；有任何一处偏离就只迁移 maxTier/间隔/频率，地形破坏原样保留并在日志说明。 |
| **F-2** | 中 | `events/GlitchEvents.java` 的 `glitch_fake_crash_report` 由 `NIGHTMARE` 升为 `APOCALYPSE`（它调用的 `FAKE_CRASH` 界面点退出会真的退出游戏）。 |
| **O-1** | 低 | `ctx.glitch()` 时长接入残酷度温和放大：`factor = 1 + (brutality-1) * 0.4`，结果上限 6000 tick（避免长时间黑屏锁死屏幕）。 |

**修复后实测（反射直调 `migrateIfNeeded`）：**

```
1 完全未改过(v2默认)   → maxTier=6 8-30 perMin=40 worldDamage=true   （拿到完整残酷化）
2 用户改过 maxTier=3    → maxTier=3 8-30 perMin=40 worldDamage=false  （尊重自定义）
3 有黑名单(说明动过)    → maxTier=6 8-30 perMin=40 worldDamage=false  （保守）
4 已是 v3               → 完全不变
5 未改过但已开破坏      → worldDamage=true
```

**修复后等级分布**：小整 32 / 烦人 218 / 抓狂 528 / 崩溃 517 / 地狱 447 / 绝望 258 = 2000
（F-2 升档使 NIGHTMARE −1、APOCALYPSE +1，总数不变）。

### 已接受 / 不修（附理由）

| 编号 | 严重度 | 处置理由 |
|---|---|---|
| **D-3** | 低 | `maxTier == 5` 确实无法区分「v2 默认」与「用户自选 5」，会把上限抬到 6。但**不迁移的代价更大**——258 个绝望事件将永远不可达，残酷化形同白做。偏差方向是「更狠」，与本次改版意图一致，接受。 |
| **F-1** | 低 | 17 个 OBLIVION 事件使用 `FAKE_CRASH`。规则本意是「**APOCALYPSE 及以上**」（防止在低档位弹出会真退游戏的界面），OBLIVION 高于 APOCALYPSE，**判定为符合本意**。验收口径正式澄清为「≥ APOCALYPSE」。 |
| **F-3** | 低 | `hall_hard_blackout_br`(BRUTAL) 使用 `HARD_BLACKOUT`。该常量此前未定义等级规则，且 v2 起即如此；仅记录，不改动既有编排。 |
| **O-2** | 低 | `voidPit`/`lavaPit` 半径、`spawnTnt` 引信**有意不接残酷度**。半径若随 ×5 放大，一次事件会挖出直径 11 格、深 16 格的坑，破坏性远超发行说明里「爆炸威力 ×5」的承诺。保持半径不变、只放大爆炸威力，是对玩家存档更负责的取舍。 |
| **I-1** | 信息 | 验证者指正正确：`/tmp/src_backup_pre_harden/` 是「**事件文件残酷化之前**」的快照（引擎改动已应用），不是「v2.0.0 之前」。我在 v3.0.0 提交信息中对它的描述不准确。**但它作为「事件文件是否被改动 id」的基线仍然有效**，且验证者额外用真实 v2.0.0（`git show b7ff2a9`）重新比对，结论同样是**逐 id 0 差异**——所以「2000 个事件 id 一个都没变」的结论成立。 |

### Lead 独立复核的补充证据

- `clean build` 通过；源码 mtime `07:22` < 产物 mtime `07:26`，确认 jar 含全部修复
- 字节码反查 `TrollContext.class` 含 `brutalityFactor` → O-1 修复确已编入
- 真实服务端启动（修复前）：`共 2000 个整人事件（小整32 / 烦人218 / 抓狂528 / 崩溃518 / 地狱446 / 绝望258）… 残酷度=5（×5.0）惩罚附加=true 连击=45% 不赦=true`，`惩罚池共 28 种残酷惩罚`，全程 **0 ERROR / 0 Exception**
- RCON 实测 `/zr count`（六档全显示）、`/zr brutality`（5，×5.0）
- 真实服务端启动（迁移修复后）：日志打印 `已把旧版默认配置迁移到 v3 残酷默认值` 且 `最高等级=绝望`

> 一处 Lead 文档瑕疵已自查修正：README 事件总览表的逐分类等级分布列在等级重排后已失真，
> 已用脚本按源码真实数据重新生成全部 29 行，合计与全局分布一致（32/218/528/517/447/258）。
