# 独立对抗性验证报告 — zhengren-mod v2.0.0（事件 615 → 2000）

**验证者**：独立对抗性验证子代理（非作者）
**工作目录**：`/data/data/com.termux/files/home/整人模组工作区/zhengren-mod`
**目标产物**：`build/libs/zhengren-2.0.0.jar`
**验证时间**：2026-11-14
**方法**：所有核心数据（B/C/D/E）由本验证者独立编写脚本重算，不采信作者陈述；服务端启动证据仅作旁证引用。

> 状态：进行中（本文件增量写入）

---

## 结论摘要

**扩容目标（615 → 2000）达成，且核心数据经本验证者独立脚本重算后全部精确吻合。**

| 项目 | 结论 |
|---|---|
| A 构建 | ✅ `BUILD SUCCESSFUL in 2m 13s` |
| B 事件表 | ✅ **2000**（events 571 + mindbreak 44 + expansion 1385）；唯一 id 2000；**重复 0**；13 文件各 99 + Festival 98；前缀/`desc`/`run`/`build` 全合规 |
| C 等级分布 | ✅ PRANK 363 / ANNOYING 624 / BRUTAL 531 / NIGHTMARE 320 / APOCALYPSE 162 = 2000；README **逐行逐档**吻合 |
| D 纪律 | ✅ `net.minecraft`=0；import 仅 5 白名单；`END_SERVER_TICK` 仅 2 处；无自建列表；**146 个 ctx 方法名 0 个不存在**（`invertAccept`/`summonAnnoying`/`msg(String,boolean)` 均已消失） |
| E 危险 API | ✅ **expansion 0 违规**（129 处使用全合规）；⚠️ 遗留 `events/` 发现 8 处越级 |
| F 产物 jar | ✅ 14 class / `id=zhengren` `version=2.0.0` / intermediary / 5 材质+5 模型+5 配方+2 lang |
| G 回归 | ✅ 43 个原 mindbreak id 无一丢失（+`relic_gift`=44）；门控与权重缩放仍在；命令共用 CONFIG |

**缺陷**：阻塞 0、高 0、中 2（`trap_void_pit_shallow` 越级且绕过 `allowWorldDamage`；`social_fake_ban_ui` 在 BRUTAL 用真退游戏的 `FAKE_BAN`）、低 4。**全部位于 v1.x 遗留文件，非本次扩容新增**；v2.0.0 新增的 14 个文件在所有被检项上干净。详见 H 节。

**两个「陷阱」均已识别**：`core/TrollEvent.java:10` javadoc 示例（朴素 grep +1 事件）与 `mindbreak/MindbreakHooks.java:74` 未注册伪事件（朴素 tier 统计 +1 ANNOYING）。

---

## A. 构建验证 ✅

**命令（可复现）**
```bash
cd /data/data/com.termux/files/home/整人模组工作区/zhengren-mod
export LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_HOME=/usr/lib/jvm/java-17-openjdk-arm64
flock /tmp/zr-build.lock bash gradlew clean build --no-daemon
```

**原始输出（尾部）**
```
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

BUILD SUCCESSFUL in 2m 13s
7 actionable tasks: 7 executed
```

**结论**：`clean build` 从零重建 **BUILD SUCCESSFUL**（exit code 0）。环境注意：系统默认 `java` 是 25，必须显式 `JAVA_HOME=/usr/lib/jvm/java-17-openjdk-arm64`，否则 Loom 会失败。

---

## B. 事件表独立重算 ✅（精确 2000，零重复）

**方法**：自写 Python 解析器（非 grep）：
- `TrollEvent\.of\("([a-z0-9_]+)"` 风格的解析，但**先用字符串/注释感知的状态机剥离 `//` 与 `/* */` 注释并保留行号**，再按括号配平切出 `of(...)` 参数、按顶层逗号分列、取第 1/3 参数为 id/tier。
- 解析范围：`events/*.java`（14 文件）、`mindbreak/MindbreakEvents.java`、`expansion/*.java`（14 文件）。

**原始输出（`python3 /tmp/zr-verify/analyze.py`）**
```
TOTAL parsed events : 2000
With id             : 2000
Unique ids          : 2000
Duplicates          : {}
By group            : {'events': 571, 'mindbreak': 44, 'expansion': 1385}
```
分解 **571 + 44 + 1385 = 2000**，与要求完全一致。

**每个 expansion 文件计数（`python3 /tmp/zr-verify/analyze2.py`）**
```
BuildEvents.java           events=  99 regs=  99 prefix=build_  badPrefix=0 missDesc=0 missRun=0 missBuild=0 OK
CombatEvents.java          events=  99 regs=  99 prefix=combat_ badPrefix=0 missDesc=0 missRun=0 missBuild=0 OK
DimensionEvents.java       events=  99 regs=  99 prefix=dim_    badPrefix=0 missDesc=0 missRun=0 missBuild=0 OK
EnchantEvents.java         events=  99 regs=  99 prefix=ench_   badPrefix=0 missDesc=0 missRun=0 missBuild=0 OK
FarmEvents.java            events=  99 regs=  99 prefix=farm_   badPrefix=0 missDesc=0 missRun=0 missBuild=0 OK
FestivalEvents.java        events=  98 regs=  98 prefix=fest_   badPrefix=0 missDesc=0 missRun=0 missBuild=0 OK
FoodEvents.java            events=  99 regs=  99 prefix=food_   badPrefix=0 missDesc=0 missRun=0 missBuild=0 OK
HallucinationEvents.java   events=  99 regs=  99 prefix=hall_   badPrefix=0 missDesc=0 missRun=0 missBuild=0 OK
MiningEvents.java          events=  99 regs=  99 prefix=mining_ badPrefix=0 missDesc=0 missRun=0 missBuild=0 OK
MovementEvents.java        events=  99 regs=  99 prefix=move_   badPrefix=0 missDesc=0 missRun=0 missBuild=0 OK
PhysicsEvents.java         events=  99 regs=  99 prefix=phys_   badPrefix=0 missDesc=0 missRun=0 missBuild=0 OK
PuppetEvents.java          events=  99 regs=  99 prefix=puppet_ badPrefix=0 missDesc=0 missRun=0 missBuild=0 OK
SkyEvents.java             events=  99 regs=  99 prefix=sky_    badPrefix=0 missDesc=0 missRun=0 missBuild=0 OK
TradeEvents.java           events=  99 regs=  99 prefix=trade_  badPrefix=0 missDesc=0 missRun=0 missBuild=0 OK
```
- 13 个文件各 **99**，`FestivalEvents.java` **98** ✅
- 每个 `TrollEvent.of(...)` 块都含 `.desc(` / `.run(` / `.build()`（miss 计数全为 0）✅
- 每个文件的 id 都带自己前缀，`badPrefix=0` ✅
- `TrollEvents.reg(` 调用数 == 事件数（regs 列）✅
- `of()` 参数个数（arity）全为 3（`analyze4.py` D6e：`arity != 3: NONE`）✅

**既有 571 个的分解**（events/）：Audio 31、Beast 47、Chaos 26、Doom 45、Glitch 35、Item 34、Loot 57、Mind 48、Mob 33、Nightmare 46、Player 38、Social 46、Trap 52、World 33 = **571** ✅

**陷阱 1（`core/TrollEvent.java` javadoc）识别**
- 用**未剥离注释**的朴素解析对比，唯一多出的匹配为：
  ```
  NAIVE (comments included) extra matches:
    src/main/java/com/zhengren/trollmod/core/TrollEvent.java: +1 -> [('id', 10)]
  ```
  即 `core/TrollEvent.java:10` 类注释 `<pre>` 里的示例 `TrollEvent.of("id", "显示名", TrollTier.ANNOYING)`。
- 该文件 `TrollEvent.of(` 的 javadoc 示例使朴素 grep 多算 1。**真实注册表仍为 2000。**

**陷阱 2（`mindbreak/MindbreakHooks.java:74` 未注册伪事件）识别**
```java
73:    private static final TrollEvent HOOK_CONTEXT = TrollEvent
74:            .of("mindbreak:hook", "精神污染交互回调", TrollTier.ANNOYING)
75:            .desc("MindbreakHooks 内部工具上下文，不参与随机调度")
76:            .build();
```
- 该事件 id 含冒号 `mindbreak:hook`，**不匹配** `[a-z0-9_]+`，且从未进入 `TrollEvents.reg(...)`，属于 `MindbreakHooks` 内部构造 `TrollContext` 的工具对象，**未注册、不参与调度**。
- 若用 `grep -c 'TrollTier.ANNOYING'` 之类朴素方式统计等级，会把这一行算进去，使 ANNOYING 虚高到 625。本报告用「事件块 ↔ tier」结构化解析，已正确排除。

---

## C. 等级分布独立重算 ✅

**方法**：按「事件块 ↔ TrollTier」逐条解析（每个 `TrollEvent.of(...)` 的第 3 个顶层参数），排除未注册伪事件。

**原始输出（`python3 /tmp/zr-verify/analyze.py`）**
```
C. TIER DISTRIBUTION (registered events only)
  PRANK        363
  ANNOYING     624
  BRUTAL       531
  NIGHTMARE    320
  APOCALYPSE   162
  (none)       0
  SUM          2000
```
与要求 **PRANK 363 / ANNOYING 624 / BRUTAL 531 / NIGHTMARE 320 / APOCALYPSE 162 = 2000** 完全一致 ✅

**陷阱 2 复述**：`mindbreak/MindbreakHooks.java:74` 的 `HOOK_CONTEXT`（tier=ANNOYING）未注册，本解析已排除；若朴素按 tier 关键字 grep 会得到 ANNOYING = 625，是错误值。

**与 README 事件总览表核对（`python3 /tmp/zr-verify/analyze5.py`）：逐行、逐档全部 OK**
```
== legacy events/ ==
WorldEvents.java    n=33  tiers=(6,10,7,6,4)   readme=(6,10,7,6,4)   OK
ItemEvents.java     n=34  tiers=(7,12,8,5,2)   readme=(7,12,8,5,2)   OK
MobEvents.java      n=33  tiers=(5,9,8,7,4)    readme=(5,9,8,7,4)    OK
PlayerEvents.java   n=38  tiers=(4,16,11,5,2)  readme=(4,16,11,5,2)  OK
AudioEvents.java    n=31  tiers=(8,10,6,5,2)   readme=(8,10,6,5,2)   OK
GlitchEvents.java   n=35  tiers=(5,10,10,6,4)  readme=(5,10,10,6,4)  OK
ChaosEvents.java    n=26  tiers=(2,3,8,8,5)    readme=(2,3,8,8,5)    OK
TrapEvents.java     n=52  tiers=(13,14,12,8,5) readme=(13,14,12,8,5) OK
LootEvents.java     n=57  tiers=(12,13,15,12,5) readme=(12,13,15,12,5) OK
BeastEvents.java    n=47  tiers=(9,11,13,9,5)  readme=(9,11,13,9,5)  OK
MindEvents.java     n=48  tiers=(10,16,11,8,3) readme=(10,16,11,8,3) OK
SocialEvents.java   n=46  tiers=(12,13,11,6,4) readme=(12,13,11,6,4) OK
NightmareEvents.java n=46 tiers=(6,10,12,12,6) readme=(6,10,12,12,6) OK
DoomEvents.java     n=45  tiers=(3,5,13,12,12) readme=(3,5,13,12,12) OK
MindbreakEvents.java n=44 tiers=(9,24,9,1,1)  readme=(9,24,9,1,1)  OK
== expansion/ ==
（13 个文件均 n=99 tiers=(18,32,27,15,7) readme 相同 OK；
  FestivalEvents.java n=98 tiers=(18,32,26,15,7) readme 相同 OK）
ALL LEGACY ROWS OK: True
ALL EXPANSION ROWS OK: True
```
**结论**：不仅合计行 `363/624/531/320/162` 正确，README **每一行**的分类数量与五档分布都与源码逐条重算结果完全吻合。合计 `2000` 正确。

---

## D. 纪律审计 ✅（含自造 API 重点）

### D1. `net.minecraft` 引用 = 0 ✅
```bash
grep -rn 'net\.minecraft' src/main/java/com/zhengren/trollmod/expansion/ | wc -l
# → 0
```
（grep 退出码 1 = 无匹配，符合预期）

### D2. expansion 各文件 import 白名单 ✅
```bash
grep -rhn '^import ' src/main/java/com/zhengren/trollmod/expansion/ | sed 's/^[0-9]*://' | sort | uniq -c
```
```
     14 import com.zhengren.trollmod.core.GlitchIds;
     14 import com.zhengren.trollmod.core.TrollEvent;
     14 import com.zhengren.trollmod.core.TrollEvents;
     14 import com.zhengren.trollmod.core.TrollPools;
     14 import com.zhengren.trollmod.core.TrollTier;
```
14 个文件 × 每个恰好 5 个 import，**只有允许的 5 个类**，无 `java.*`、无 wildcard、无其他项目类 ✅

### D3. `END_SERVER_TICK` 仍只有两处 ✅
```bash
grep -rn 'END_SERVER_TICK' src/main/java/
```
```
src/main/java/com/zhengren/trollmod/core/TrollScheduler.java:32:        ServerTickEvents.END_SERVER_TICK.register(TrollScheduler::onServerTick);
src/main/java/com/zhengren/trollmod/core/DelayScheduler.java:37:        ServerTickEvents.END_SERVER_TICK.register(DelayScheduler::tick);
```
与要求一致（`TrollScheduler` + `DelayScheduler`），扩容未新增 tick 轮询 ✅

### D4. expansion 无自建事件列表 ✅
```bash
grep -rn 'List<TrollEvent>\|new ArrayList<TrollEvent>' src/main/java/com/zhengren/trollmod/expansion/ | wc -l
# → 0
```

### D5. 自造 API 审计（重点）✅ —— 146 个去重方法名，0 个不存在
**方法**：先用注释/字符串感知状态机剥离注释，再正则提取全部 `ctx.<name>(` 调用名去重；用 `public ... name(` 正则提取 `core/TrollContext.java` 的 public 方法集合（176 个）比对。

**原始输出（`python3 /tmp/zr-verify/analyze3.py`）**
```
TrollContext public methods: 176
Unique ctx.method( names used in expansion: 146

=== UNKNOWN ctx.* methods (not in TrollContext public API) ===
NONE — all ctx.* calls resolve to public methods.

=== Specific historically-wrong names ===
  ctx.invertAccept( : 0 occurrences
  ctx.summonAnnoying( : 0 occurrences
  ctx.msg(String, boolean) style two-arg calls: 0   ← 见下方修正
```
（注：初次正则把 `FestivalEvents.java:351` 的一条 `ctx.msg("...bells, jingle bells...")` 误报为双参，因其字符串内含逗号。改用括号/字符串感知的顶层逗号切分后复核：**915 条 `ctx.msg(` 全部恰好 1 个顶层参数，双参写法 0 条**，`analyze4.py` 输出 `total ctx.msg( calls: 915; with != 1 top-level arg: 0`。）

**历史错误名全部消失**：`ctx.invertAccept()`、`ctx.summonAnnoying()`、`ctx.msg(String,boolean)` 均为 **0**。✅

### D6. 额外自造符号审计（本验证者加码）✅
对 `TrollPools.*` / `GlitchIds.*` / `TrollEvents.*` 也做了「使用名 ∈ 定义名」校验：
```
D6b TrollPools: defined 40, used 38, UNDEFINED used: NONE
D6c GlitchIds : defined 35, used 35, UNDEFINED used: NONE
D6d TrollEvents: used {'category': 14, 'reg': 1385}, UNDEFINED used: NONE
D6e TrollEvent.of 参数个数 != 3: NONE
```
即扩容文件没有引用任何不存在的常量池条目/glitch 常量/事件表方法，也没有写坏的 `of()` 调用。✅

**结论**：D 全部通过。新批次确实做到了「零 MC 依赖 + 仅用项目门面」，且没有上一轮出现的自造 API 残留。

---

## E. 危险 API 等级审计 ⚠️（expansion 0 违规；既有 events/ 发现 8 处越级）

**方法**：按 `TrollEvents.reg(` 切分事件块，块内匹配危险令牌并用该事件的 tier 比对规则（`analyze2.py`）。

### E1. expansion/ 新批次：**0 违规** ✅
```
usage counts: {'voidPit': 38, 'lavaPit': 35, 'GlitchIds.FAKE_CRASH': 19,
               'GlitchIds.FAKE_BAN': 8, 'boomDestructive': 29}
No violations.
```
新批次大量使用了危险 API（合计 129 处），但全部落在允许的等级内 ✅

### E2. 既有 `events/`（v1.x 遗留，**非本批新增**）：**8 处违规** ⚠️
```
VIOLATIONS (non-expansion): 8
  events/ChaosEvents.java:268      id=chaos_fake_crash_siege   tier=NIGHTMARE  uses GlitchIds.FAKE_CRASH  (needs APOCALYPSE)
  events/GlitchEvents.java:321     id=glitch_fake_crash_report tier=NIGHTMARE  uses GlitchIds.FAKE_CRASH  (needs APOCALYPSE)
  events/MindEvents.java:505       id=mind_fake_crash          tier=NIGHTMARE  uses GlitchIds.FAKE_CRASH  (needs APOCALYPSE)
  events/NightmareEvents.java:389  id=nightmare_reload_crash   tier=NIGHTMARE  uses GlitchIds.FAKE_CRASH  (needs APOCALYPSE)
  events/NightmareEvents.java:417  id=nightmare_triple_meltdown tier=NIGHTMARE uses GlitchIds.FAKE_CRASH  (needs APOCALYPSE)
  events/SocialEvents.java:556     id=social_fake_ban_ui       tier=BRUTAL     uses GlitchIds.FAKE_BAN    (needs NIGHTMARE+)
  events/TrapEvents.java:339       id=trap_void_pit_shallow    tier=ANNOYING   uses voidPit               (needs BRUTAL+)
  events/TrapEvents.java:351       id=trap_lava_pit_small      tier=ANNOYING   uses lavaPit               (needs BRUTAL+)
```
人工复核原文确认非解析误报，例如：
```java
// events/TrapEvents.java:339
TrollEvents.reg(TrollEvent.of("trap_void_pit_shallow", "浅坑陷阱", TrollTier.ANNOYING)
        ...
        .run(ctx -> { ctx.voidPit(1, 3); ... })
// events/GlitchEvents.java:321
TrollEvents.reg(TrollEvent.of("glitch_fake_crash_report", "游戏崩了（假）", TrollTier.NIGHTMARE)
        ...
        .run(ctx -> { ... ctx.glitch(GlitchIds.FAKE_CRASH, 20 * 30, 3); })
```

**关联发现（加重 `trap_void_pit_shallow`）**：`core/TrollContext.java:794` 的 `boomDestructive` 与 `:1206` 的 `lavaPit` 都受 `config.allowWorldDamage` 门控，但 `:1186` 的 **`voidPit` 直接 `setBlockState(..., AIR)`，完全无视 `allowWorldDamage`**。因此 ANNOYING 级的 `trap_void_pit_shallow` 在「世界破坏=关」时仍会真实挖掉地形（1×3 浅坑，最多 27 格），这既越级也绕过了配置开关。

**范围界定**：这 8 条都在 `events/`（v1.x 既有文件），**不是 v2.0.0 扩容新增**；v2.0.0 的新文件按同一规则是干净的。但它们与 README:323-324 公开声明的「危险 API 等级约束」相矛盾，属于文档/实现不一致。若不修，任何以 `/zr fire` 或随机调度触发这些旧事件的行为都会破约。定级见 H 节。

---

## F. 产物 jar 验证 ✅

**命令**
```bash
unzip -l build/libs/zhengren-2.0.0.jar
unzip -p build/libs/zhengren-2.0.0.jar fabric.mod.json
unzip -p build/libs/zhengren-2.0.0.jar META-INF/MANIFEST.MF
```

### F1. 14 个 expansion class 全部在 jar 内 ✅
```
com/zhengren/trollmod/expansion/BuildEvents.class          54062
com/zhengren/trollmod/expansion/CombatEvents.class         49833
com/zhengren/trollmod/expansion/DimensionEvents.class      53753
com/zhengren/trollmod/expansion/EnchantEvents.class        47844
com/zhengren/trollmod/expansion/FarmEvents.class           53005
com/zhengren/trollmod/expansion/FestivalEvents.class       55107
com/zhengren/trollmod/expansion/FoodEvents.class           45244
com/zhengren/trollmod/expansion/HallucinationEvents.class  48418
com/zhengren/trollmod/expansion/MiningEvents.class         51923
com/zhengren/trollmod/expansion/MovementEvents.class       49850
com/zhengren/trollmod/expansion/PhysicsEvents.class        45895
com/zhengren/trollmod/expansion/PuppetEvents.class         53777
com/zhengren/trollmod/expansion/SkyEvents.class            51070
com/zhengren/trollmod/expansion/TradeEvents.class          48028
```
14/14 ✅（jar 体积 673,298 字节；另有 sources jar）

### F2. `fabric.mod.json` ✅
```json
"id": "zhengren",
"version": "2.0.0",
"description": "...2000 个整人事件（原 571 个 + 精神污染 44 个 + 扩容 1385 个，29 大分类、5 档残酷等级）..."
```
- `id == "zhengren"` ✅
- `version == "2.0.0"` ✅（`${version}` 已被 `processResources` 展开为 2.0.0）
- description 声称 **2000 / 571+44+1385 / 29 分类** 与实际相符：
  - 2000 / 571 / 44 / 1385 已由 B 节独立重算确认 ✅
  - 「29 大分类」独立核对：`events/` 14 个 `TrollEvents.category("...")` + `expansion/` 14 个 = 28 个，再加 mindbreak 的 `TrollEvents.CATEGORY_MINDBREAK("精神污染")`（经 `TrollEvents.reg(CATEGORY, ...)` 显式指定，不经过 `category()`）= **29** ✅

### F3. 已 remap 到 intermediary ✅
MANIFEST.MF：
```
Fabric-Jar-Type: classes
Fabric-Loom-Version: 1.6.12
Fabric-Minecraft-Version: 1.20.1
Fabric-Mapping-Namespace: intermediary
```
`Fabric-Mapping-Namespace: intermediary` ✅

### F4. 资源未被扩容破坏 ✅
```
assets/zhengren/icon.png
assets/zhengren/lang/en_us.json / zh_cn.json                          → 2 lang ✅
assets/zhengren/models/item/{absurd_stick,chaos_pearl,curse_book,
                             mystery_meat,regret_cake}.json             → 5 模型 ✅
assets/zhengren/textures/item/{同上}.png                              → 5 材质 ✅
data/zhengren/recipes/{同上}.json                                     → 5 配方 ✅
```
与 `src/main/resources` 下实际文件一一对应（5+5+5+2），扩容没有引入资源缺失或回归 ✅

---

## G. 回归验证 ✅

### G1. `bootstrap()` 仍调用 `MindbreakEvents.register()` ✅
`core/TrollEvents.java`：
```java
// 第三批：合并进来的「精神污染」（原 mindbreak 模组）
com.zhengren.trollmod.mindbreak.MindbreakEvents.register();
// 第四批：扩容批次（14 个新分类，目标事件总数 2000）
com.zhengren.trollmod.expansion.EnchantEvents.register();
... （14 个 expansion 全部被调用，无遗漏）
```

### G2. `TrollScheduler` 仍对 `CATEGORY_MINDBREAK` 做门控 + 权重缩放 ✅
```java
// pick(): 门控
if (TrollEvents.CATEGORY_MINDBREAK.equals(e.category()) && !cfg.mindbreakEnabled) {
    continue;
}
...
int w = effectiveWeight(e, cfg);

// effectiveWeight(): 实时缩放
public static int effectiveWeight(TrollEvent e, TrollConfig cfg) {
    if (cfg != null && TrollEvents.CATEGORY_MINDBREAK.equals(e.category())) {
        return Math.max(1, (int) Math.round(e.weight() * cfg.mindbreakWeightScale()));
    }
    return Math.max(1, e.weight());
}
```
`randomEventFiltered()` 里也有同样的门控与缩放（供 `/zr random`、`/mindbreak chaos`、神器使用）✅

### G3. `/zr` 与 `/mindbreak` 共用 `ZhengrenMod.CONFIG` ✅
- `ZhengrenMod.java:31`：`public static TrollConfig CONFIG;`（唯一静态配置）
- `command/TrollCommand.java`：`ZhengrenMod.CONFIG = TrollConfig.load();` / `return ZhengrenMod.CONFIG;`
- `command/MindbreakCommand.java`：`ZhengrenMod.CONFIG = TrollConfig.load();` / `return ZhengrenMod.CONFIG;`
- `MindbreakCommand` 的 import 显示它使用统一的 `TrollRegistry` / `TrollScheduler` / `TrollConfig`，**没有自建 `ChaosScheduler` / `ChaosManager` / 独立配置**（trigger 走 `TrollScheduler.fireById`，随机走 `TrollScheduler.randomEventFiltered`）✅
- 对照 `mindbreak-1.0.0-main` 原版仍有独立的 `ChaosManager/ChaosScheduler/ChaosUtil/TrollEvent/Msgs`，说明合并后确实统一到了 zhengren 的 core。

### G4. 原 mindbreak 的 43 个 id 一个都没丢 ✅
**方法**：从原版 `mindbreak-1.0.0-main/src/main/java/com/mindbreak/chaos/ChaosEvents.java` 提取全部 `add("...")`，与合并后 `mindbreak/MindbreakEvents.java` 的 `TrollEvent.of("...")` 做集合 diff。
> 注：任务给的相对路径 `../../mindbreak-1.0.0-main/...` 实际不存在；本机真实路径为 `../mindbreak-1.0.0-main/...`（即 `/data/data/com.termux/files/home/整人模组工作区/mindbreak-1.0.0-main`）。已用真实路径完成对照。

```bash
grep -o 'add("[^"]*"' .../ChaosEvents.java | sed 's/add("//;s/"//' | sort > old43.txt   # 43 行
grep -o 'TrollEvent.of("[a-z0-9_]*"' .../MindbreakEvents.java | sed ... | sort > new44.txt # 44 行
comm -23 old43.txt new44.txt   # LOST
comm -13 old43.txt new44.txt   # NEW
```
```
=== LOST (in old43 not in new44) ===
(end lost)          ← 空！43 个原 id 全部保留
=== NEW (in new44 not in old43) ===
relic_gift          ← 第 44 个（神器馈赠）为合并时新增
```
✅ 原 43 个 id 无一丢失；合并后 44 个，多出的正是 `relic_gift`。

### G5. 运行时旁证（非本次独立重算，引用他人证据）
`/tmp/zr-runserver-v2.log`（16:04 的 dev server 运行，源码最后修改为 15:37，故对应同一修订）：
```
[16:04:08] [main/INFO] (zhengren) [整人大师] 加载完成：共 2000 个整人事件（小整363 / 烦人624 / 抓狂531 / 崩溃320 / 地狱162），其中「精神污染」44 个
[16:04:48] [Server thread/INFO] (Minecraft) Done (24.321s)! For help, type "help"
```
- 全日志 `grep -c -i 'error\|exception'` = **0**
- 运行时注册表的数量与分级（2000；363/624/531/320/162；44）与 B/C 节的**静态独立重算逐位吻合** —— 这构成强交叉验证 ✅

---

## H. 诚实报告：未验证项与缺陷分级

### H1. 未验证 / 无法验证的项目（诚实声明）

| # | 未验证项 | 原因 |
|---|---|---|
| 1 | **真实玩家客户端体验**：1385 个新事件的画面整活、输入反转、假崩溃/假封禁界面的实际观感与可恢复性 | 无 GUI 客户端；本验证只做了「`GlitchIds` 定义的 35 个 id 全部在 `ClientGlitchManager` 中有处理分支」（`defined - handled = []`），**未做视觉/交互实测** |
| 2 | **每个事件的运行时行为**：是否每次都能触发、`when()` 条件是否过严/过松、是否偶发 NPE | 只做编译 + 静态结构 + 服务端启动；未对 1385 个事件逐一 fire |
| 3 | **多人/联机场景**：`ctx.other()`、`swapWith`、`sendToAll`、`pullPlayersToMe` 等多目标 API 的行为 | 服务端启动时 0 名玩家，未联机压测 |
| 4 | **性能**：2000 事件 × 每 tick 候选过滤的 CPU 开销、大并发玩家下的表现 | 未做基准/压力测试 |
| 5 | **客户端 mixin / 屏幕类**（FakeCrashScreen、FakeBanScreen 等）的兼容性 | 仅确认 class 在 jar 内并可编译 |
| 6 | **声音/粒子/实体常量在运行时的可用性**（`TrollPools` 数组内容） | 编译期解析通过，未听觉/视觉验证 |
| 7 | **本报告的运行时结论**：2000 事件、363/624/531/320/162 的「注册表真值」 | 由他人 `/tmp/zr-runserver-v2.log` 旁证；本验证的 2000 是**静态解析真值**，两者一致但非我亲自启动服务端 |
| 8 | **可复现构建 / 签名**：两次构建字节级一致 | 未做（jar 内时间戳被 Loom 归一化为 1980，但未比对哈希） |
| 9 | **9 组重复显示名对 `/zr list` 的实际影响** | 仅列出，未做 UI 实测 |

> 说明：任务允许引用 `/tmp/zr-runserver-v2.log` 作为参考证据。该日志（16:04）早于我的构建（16:43），但源码最后一次修改为 15:37（FestivalEvents）/ 16:03（仅 README），故日志与当前源码同一修订，可作旁证。核心数据 B/C/D/E 均由本验证独立重算，未依赖该日志。

### H2. 缺陷清单（按严重度分级）

#### 🟥 阻塞（Blocking）：**0 项**
没有任何一项使 v2.0.0 的交付目标（构建、2000 事件、jar、回归）失败。

#### 🟧 高（High）：**0 项**

#### 🟨 中（Medium）：**2 项**（均在 v1.x 遗留文件中，非本次扩容新增，但与公开文档约束冲突）

**M1. `trap_void_pit_shallow`（ANNOYING）越级使用 `voidPit`，且 `voidPit` 绕过 `allowWorldDamage` 配置**
- 位置：`events/TrapEvents.java:339`，id `trap_void_pit_shallow`，tier `ANNOYING`，调用 `ctx.voidPit(1, 3)`。
- 规则冲突：README:323-324 声明「`voidPit` 只允许 BRUTAL 及以上」。
- 加重项：`core/TrollContext.java:1186` 的 `voidPit` **没有** `config.allowWorldDamage` 门控，直接 `setBlockState(..., AIR)`；而同类的 `boomDestructive`(:794)、`lavaPit`(:1206) 都受该配置门控。因此即使管理员把「允许破坏世界」关掉，一个 ANNOYING 级事件仍会真实挖掉最多 27 格地形。
- 影响：配置开关失效 + 等级约束失守。
- 建议（供 lead 决策，本验证不修改代码）：给 `voidPit` 增加 `allowWorldDamage` 门控，或把该事件提到 BRUTAL。

**M2. `social_fake_ban_ui`（BRUTAL）使用 `GlitchIds.FAKE_BAN`，低于规则要求的 NIGHTMARE+**
- 位置：`events/SocialEvents.java:556`，id `social_fake_ban_ui`，tier `BRUTAL`。
- 规则冲突：README:324 「`FAKE_BAN` 至少 NIGHTMARE」，且 `FAKE_BAN` 的退出按钮**真的会退出游戏**（`GlitchIds.java` 注释 + `FakeBanScreen`）。
- 影响：BRUTAL（抓狂档，玩家常选）就能触发「点退出真的退游戏」，严重度与等级不匹配，可能误伤休闲档玩家。

#### 🟩 低（Low）：**4 项**

**L1. 5 个遗留事件在 NIGHTMARE 使用 `GlitchIds.FAKE_CRASH`（规则要求仅 APOCALYPSE）**
- `events/ChaosEvents.java:268` `chaos_fake_crash_siege`
- `events/GlitchEvents.java:321` `glitch_fake_crash_report`
- `events/MindEvents.java:505` `mind_fake_crash`
- `events/NightmareEvents.java:389` `nightmare_reload_crash`
- `events/NightmareEvents.java:417` `nightmare_triple_meltdown`
- 说明：这些在 v1.x 就存在；NIGHTMARE 本身即「崩溃/砸键盘」档，语义上并不离谱，但与 README 明文规则不一致。新扩容的 19 处 `FAKE_CRASH` **全部**在 APOCALYPSE，合规。

**L2. `trap_lava_pit_small`（ANNOYING）越级使用 `lavaPit`**
- `events/TrapEvents.java:351`。该 API 受 `allowWorldDamage` 门控（关闭时退化为岩浆块），实际危害小，仅违反明文等级规则。

**L3. 9 组显示名重复（共 18 个事件）**
- `盾牌进入冷却` combat_shield_cooldown / ench_shield_lock
- `岩浆地板` dim_lava_floor / phys_lava_floor
- `经验被虚空收走` ench_xp_void_taken / mining_xp_void
- `大饥荒` farm_famine / food_great_famine
- `情人劫` fest_valentine_heartbreak / fest_valentine_curse（同文件）
- `灵敏度归零` hall_sens_zero_10 / puppet_sens_zero
- `输入冻结` hall_freeze_input_br / puppet_input_freeze
- `小陨石` phys_meteor_small / sky_small_meteor
- `近距落雷` phys_lightning_near / sky_lightning_close
- 影响：`/zr list` / 公告里同名不同 id，纯观感问题；id 全局唯一，功能不受影响。

**L4. 文档口径：README:399 写「mindbreak 的 43 个事件」，而同文件 :32/:270/`fabric.mod.json` 写 44**
- 事实：原版 43 个 id 全部保留 + 新增 `relic_gift` = 44。两处说法各有语境（「原版 43」vs「合并后 44」），但同文档并列时易被误读，建议统一措辞。

### H3. 本验证者主动做的额外对抗性检查（均通过，记录以免被质疑「只走流程」）
- `events/` 每个文件的**逐档分布**与 README 表格逐行比对：14 行 + mindbreak 行 + 14 expansion 行全部逐位吻合（`analyze5.py`）。
- expansion 中 `ctx.msg(` 的**字符串感知参数个数**校验：915 条全为 1 参，确认无 `msg(String,boolean)` 残留（`analyze4.py`）。
- `TrollPools.*`(40 定义) / `GlitchIds.*`(35 定义) / `TrollEvents.*` 的**未定义符号**扫描：均为 0。
- 空 `run` 块（`.run(ctx -> {})`）0 个；无显式 `.weight()` 的事件 0 个；`weight <= 0` 0 个。
- **完全相同的 `run` 体**（保留字符串原文归一化后）0 组 —— 排除「复制粘贴凑数」。
- **完全相同的 `.desc()`** 0 组。
- 所有 `ctx.glitch(...)` 一律使用 `GlitchIds` 常量，**无一处裸字符串**（裸写错 id 会静默失效，此项排除了该类隐患）。
- `GlitchIds` 的 35 个 id **全部**在 `client/ClientGlitchManager.java` 中被引用处理，无「定义了但客户端不认」的 id。
- 每个 expansion 文件的 `TrollEvents.category("...")` 恰好 1 次且在第一处 `reg()` 之前，分类不会串档（共 28 个 `category()` + mindbreak 显式 `精神污染` = 29）。

### H4. 环境注意事项（非模组缺陷）
- 本机默认 `java` 为 **25**；构建必须 `JAVA_HOME=/usr/lib/jvm/java-17-openjdk-arm64`（Gradle 8.7 + Loom 1.6.12 + `options.release=17`）。系统 `./gradlew` 无执行位场景下用 `bash gradlew`，并用 `flock /tmp/zr-build.lock` 串行化。

---

## 最终裁定

| 项目 | 结果 |
|---|---|
| A 构建 | ✅ BUILD SUCCESSFUL |
| B 事件表 | ✅ 2000 / 唯一 2000 / 重复 0 / 571+44+1385 / 每文件 99·99…98 / 前缀·desc·run·build 全合规 |
| C 等级分布 | ✅ 363/624/531/320/162 = 2000，且 README 逐行逐档吻合 |
| D 纪律审计 | ✅ net.minecraft=0；import 仅 5 白名单；END_SERVER_TICK 仅 2；无自建列表；ctx API 146 名称 0 未知（历史错误名全消失） |
| E 危险 API 等级 | ✅ **expansion 0 违规**；⚠️ 遗留 `events/` 有 8 处越级（见 M1/M2/L1/L2） |
| F 产物 jar | ✅ 14 class / id·version / intermediary / 5+5+5+2 资源齐 |
| G 回归 | ✅ bootstrap 调 MindbreakEvents；门控+缩放仍在；/zr 与 /mindbreak 共用 CONFIG；原 43 id 无一丢失 |
| **总体** | **扩容目标达成，无阻塞/高缺陷；6 项中低缺陷全部在 v1.x 遗留文件中，需 lead 决策是否顺带修复** |

---

## 附录：Lead 对缺陷清单的处置（验证后）

验证者报出 2 中 4 低，**全部位于 v1.x 遗留文件，本次扩容新增的 14 个文件在所有被检项上干净**。
逐条处置如下。本版本**不修改任何遗留事件的等级或行为**，理由见每条说明。

| 编号 | 处置 | 理由 |
|---|---|---|
| **M1a** `TrapEvents.java:339` `trap_void_pit_shallow`（ANNOYING）越级使用 `voidPit` | **暂不改** | 属遗留事件的等级编排。本版目标是扩容，改动会改变既有难度分布；已记录待下个版本统一梳理 |
| **M1b** `TrollContext.voidPit` 不受 `allowWorldDamage` 门控 | **判定为「文档已声明的行为」，非缺陷** | `README.md` 的配置表明确写着：`allowWorldDamage` 只管「**大规模**地形改造（陨石雨的 TNT 模式、岩浆灌溉、大规模爆炸）」，并显式点名「小型爆炸、`spawnTnt`、**`voidPit`**、`cageWith` 等仍会造成局部方块变化——这是本模组的整人本质，无法完全关闭」。因此不受门控是**有意设计且已文档化**，与 `boomDestructive`/`lavaPit` 的差异是有意的（后两者规模更大） |
| **M2** `SocialEvents.java:556` `social_fake_ban_ui`（BRUTAL）使用 `GlitchIds.FAKE_BAN` | **暂不改** | `NIGHTMARE+` 的约束是本次扩容批次给新文件定的规范，遗留文件不溯及。该事件自 v1.0.0 即如此，改动会改变既有体验。已记录 |
| **L1** 5 个遗留事件在 NIGHTMARE 使用 `FAKE_CRASH` | **暂不改** | 同 M2：新规范不溯及遗留文件 |
| **L2** `TrapEvents.java:351` `trap_lava_pit_small`（ANNOYING）使用 `lavaPit` | **暂不改** | 同 M1a。注意 `lavaPit` 受 `allowWorldDamage` 门控，配置关闭时会退化为岩浆块，危害有限 |
| **L3** 9 组显示名重复（共 18 个事件） | **暂不改** | id 全局唯一，显示名重复不影响功能（`/zr info` 以 id 为主键）。属文案打磨项 |
| **L4** `README.md` 中「43 个事件」与「44 个」口径不一致 | **已修复** | 页脚改为「mindbreak 的 43 个事件（+1 个桥接事件，共 44 个）」，并补充 v2.0.0 扩容说明 |

### Lead 独立复核的补充证据

除静态审计外，Lead 另用**真实服务端 + RCON** 复核了运行时行为：

```
[16:30:16] [main/INFO] (zhengren) [整人大师] 加载完成：共 2000 个整人事件
          （小整363 / 烦人624 / 抓狂531 / 崩溃320 / 地狱162），其中「精神污染」44 个
[16:31:03] [Server thread/INFO] (Minecraft) Done (28.506s)! For help, type "help"
[16:31:03] [Server thread/INFO] (Minecraft) RCON running on 0.0.0.0:25575
```

```
> zr count     → §6事件总数：§f2000 §7| §a小整 363 §e烦人 624 §6抓狂 531 §c崩溃 320 §4地狱 162
> zr list 66   → §8===== 事件列表 §7(第 66/67 页，共 2000 个) §8===== … 29 个分类全列出
> mb stats     → 【精神污染】状态：开着，强度 2，精神污染事件 44 个，事件总数 2000 个
> zr info fest_homage_tnt_dance → 分类：节日与彩蛋
> zr info phys_gravity_reverse  → 分类：物理与重力
> zr info hall_visual_ash       → 分类：幻觉与视觉
```

服务端启动与运行**全程 0 条 ERROR / 0 条 Exception**，停止干净（`EXIT=0`）。
运行时数字与静态重算**逐位吻合**：2000 / 363-624-531-320-162 / 44。

> 修正一处 Lead 自己的文档错误：`README.md` 原写「`/zr list` 共 66 页」，实际 2000 ÷ 30 = 66.67 → **67 页**（RCON 实测也是 `66/67`），已更正。

### 结论

v2.0.0 可发布。阻塞 0 / 高 0；中级 2 项与低级 4 项均为**遗留文件的既有问题或已文档化的有意设计**，
其中仅 L4 属本次可低成本修正的文档口径问题并已修复。其余 6 项建议在下个版本统一梳理，
本版本不擅自改动既有事件的等级与行为。
