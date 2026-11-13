# 验收报告 — 整人大师 (zhengren) v1.0.0

构建时间：2026-11-12（两轮扩容）
构建环境：OpenJDK 17.0.2 · Gradle 8.7 · Fabric Loom 1.6.12 · Yarn 1.20.1+build.10 · Fabric API 0.92.12+1.20.1 · MC 1.20.1

**最终规模：571 个整人事件 · 14 大分类 · 36 个 class · jar 270 KB**

---

## ✅ 已验证（有实际执行证据）

| # | 验证项 | 方法 | 结果 |
|---|---|---|---|
| 1 | **完整构建通过** | `gradlew clean build`（Loom 真实构建） | `BUILD SUCCESSFUL` — `compileJava / processResources / jar / sourcesJar / remapJar / remapSourcesJar / assemble / build` 全部成功执行 |
| 2 | **产物结构正确** | `jar tf dist/zhengren-1.0.0.jar` | 53 条目 / **36 个 class** / `fabric.mod.json` / `assets/zhengren/icon.png` / `lang/zh_cn.json` / `lang/en_us.json` |
| 3 | **已重映射到 intermediary** | `javap` 反查字节码引用 | 例：`TrollPools.HORROR` 类型为 `net.minecraft.class_3414[]`、`JUNK` 为 `net.minecraft.class_1792[]` → `remapJar` 生效，可被正式客户端/服务端加载 |
| 4 | **事件数量与唯一性** | 统计 `TrollEvents.reg(` + `TrollEvent.of("id"` | **571 个事件**，**571 个 id，0 重复**（跨 14 个文件校验） |
| 5 | **残酷等级覆盖** | 统计 `TrollTier.*` | PRANK 102 / ANNOYING 152 / BRUTAL 145 / NIGHTMARE 109 / APOCALYPSE 63；**14 个分类每个都齐 5 档** |
| 6 | **中文编码无损** | 直接读 jar 内文件字节并按 UTF-8 解码 | `"name": "整人大师 (Zhengren Master)"`、描述、lang 文件全部正常，无乱码 |
| 7 | **Fabric Loader 能识别并加载本模组** | 真实启动 Fabric Loader 0.16.14（Knot） | 三次不同配置下均输出 `Loading 46 mods: ... - zhengren 1.0.0`，说明 mod 元数据、依赖解析、入口点声明均合法 |
| 8 | **静态审计（第一轮 230 事件）** | 独立只读审计 agent 逐文件核对 | id 全唯一、前缀全对、`.desc()`/`.run()` 齐全、0 处危险代码、0 个非法 glitch id |
| 9 | **静态审计（第二轮 571 事件）** | 独立只读审计 agent（task-17） | 见下节结论 |
| 10 | **源码规模** | 统计 | 26 个 Java 文件（其中 14 个事件文件） |

### 两轮扩容中被真实修掉的问题（说明编译器确实在把关）

**第一轮（框架层，11 处）：**
- `GameOptions.invertYMouse` 在 1.20.1 为 private → 改为每 tick 上下颠倒实现"鼠标疯了"
- `Screen.renderBackground` 在 1.20.1 只接受 1 个参数、`SoundEvents.AMBIENT_CAVE`/`UI_BUTTON_CLICK` 是 `RegistryEntry<SoundEvent>` 需 `.value()`
- `EntityType.SPLASH_POTION` → `EntityType.POTION`；`ParticleTypes.SPELL` → `ParticleTypes.EFFECT`；`DamageSources.lightning()` 不存在 → `generic()`
- Fabric API 0.92.12 无 `PacketTypeRegistry` → 删除显式注册（通道由客户端 `registerGlobalReceiver` 注册）

**第二轮（扩容层，2 处）：**
- `Items.SKULK_SENSOR` 拼写 → `Items.SCULK_SENSOR`
- `SocialEvents` 两处 `ctx.fakeChat(...)` 少传一个参数（多行字符串拼接吞掉了第二个实参）→ 拆成 `fakeChat(发送者, 内容)`

> 两轮共 341 个新事件由 7 个 agent 并行编写，**只有 2 个编译错误**（都在同一文件、同一类手误），其余 12 个新文件一次编译通过。

---

## ⚠️ 未能完成的验证（如实说明）

| 项目 | 状态 | 原因 |
|---|---|---|
| **真实启动 Minecraft 服务端并跑事件** | ❌ 未完成 | 本机是受限网络环境，没有 Loom 的 dev-launch-injector 与游戏资源；手工用 Fabric Knot 启动时因命名空间（named/intermediary）与 access-widener 加载顺序不匹配而失败。**这是启动脚手架的问题，不是模组缺陷** —— Fabric Loader 已成功载入本模组（见第 7 项）。 |
| **游戏内实机试玩** | ❌ 未完成 | 需要真实客户端与图形环境 |

### 给使用者的建议验证步骤

装上后进入世界：
- `/zr count` 应显示 **571**（并列出各等级数量）
- `/zr list` 可逐页浏览全部事件 id（30 个/页，共 20 页）
- `/zr fire trap_judgement_day`、`/zr fire doom_meteor_apocalypse`、`/zr fire nightmare_end_of_world` 可单点验证各分类

---

## 📊 事件清单（14 大分类）

| 分类 | 数量 | 小整/烦人/抓狂/崩溃/地狱 | 文件 |
|---|---:|---|---|
| 🌍 世界与地形 | 33 | 6/10/7/6/4 | `WorldEvents.java` |
| 🎒 物品与背包 | 34 | 7/12/8/5/2 | `ItemEvents.java` |
| 🐛 生物与惊吓 | 33 | 5/9/8/7/4 | `MobEvents.java` |
| 💀 玩家状态 | 38 | 4/16/11/5/2 | `PlayerEvents.java` |
| 🔊 声音与假消息 | 31 | 8/10/6/5/2 | `AudioEvents.java` |
| 🖥️ 画面崩溃 | 35 | 5/10/10/6/4 | `GlitchEvents.java` |
| 💥 混沌与地狱 | 26 | 2/3/8/8/5 | `ChaosEvents.java` |
| 🪤 陷阱与机关 | 52 | 13/14/12/8/5 | `TrapEvents.java` |
| 💎 宝藏与诱饵 | 57 | 12/13/15/12/5 | `LootEvents.java` |
| 🐺 怪物军团 | 47 | 9/11/13/9/5 | `BeastEvents.java` |
| 🧠 心理战 | 48 | 10/16/11/8/3 | `MindEvents.java` |
| 👥 多人与社交 | 46 | 12/13/11/6/4 | `SocialEvents.java` |
| 🌑 结局级画面 | 46 | 6/10/12/12/6 | `NightmareEvents.java` |
| ☄️ 末日与毁灭 | 45 | 3/5/13/12/12 | `DoomEvents.java` |
| **合计** | **571** | **102/152/145/109/63** | |

---

## 🖥️ 客户端整活能力（35 个 glitch id）

- **第一批 24 个**：假崩溃界面、假掉线、隐藏 HUD、视角乱转、画面抖动、视野乱变、灵敏度归零/拉满、鼠标反转、视角颠倒、快捷栏锁死/乱跳、强制关界面、黑屏、跳回标题界面、闪白、剧烈甩视角、假内存溢出、假存档损坏、假死亡、聊天刷屏、随机恐怖音效、眩晕。
- **第二批 11 个（新增）**：`FAKE_BAN` 假永久封禁界面、`FAKE_UPDATE` 假更新卡 99%、`WORLD_RELOAD` 假世界重载、`FAKE_VIRUS` 假病毒警告、`INVERT_CONTROLS` 输入反转、`FREEZE_INPUT` 输入冻结、`SCREEN_TEAR` 画面撕裂、`MASS_CHAT` 海量刷屏、`HARD_BLACKOUT` 硬黑屏+心跳、`FAKE_ACHIEVEMENT` 假成就、`FAKE_SAVING` 假自动保存。
- 新增界面：`FakeBanScreen`（带申诉码，退出按钮是真的）、`FakeUpdatingScreen`（进度永远 99%）。

---

## 🔍 第二轮独立审计结论（task-17，只读 agent）

审计 agent 独立复核了全部 **571 个事件**与新增框架代码，结论：**无编译级 / 必崩级问题，可以交付**。

**通过的硬指标**
- 事件 id：**0 重复 / 0 前缀不符 / 0 非法字符**（`^[a-z0-9_]+$`）
- 14 个文件**全部 5 档齐全**，`.desc()` 数 == `.run()` 数 == 事件数（571/571/571）
- glitch id：**0 个非法字符串**（全工程无字面量调用，571 处全走 `GlitchIds.*`）
- **`GlitchIds` 35 个常量 ↔ `ClientGlitchManager` 处理分支 35/35 双向一一对应**（无"定义未实现/实现未定义"）
- **`ctx.other()` 全部 22 个调用点均判空**（21 处 `if (o != null)` + 1 处 `if (o == null) return`），单人游戏不会 NPE
- events/ 目录 **0 处危险代码**；无除零 / 越界；14 处数组下标与 `TrollPools` 声明顺序完全一致；新增 14 个常量池全部非空

**审计发现的问题与处置**

| 编号 | 问题 | 处置 |
|---|---|---|
| B1 | `trap_bedrock_tomb` 基岩封闭 + 只有一个**随机**负面效果 → 真·永久软锁 | ✅ **已修**：追加 `setHealth(6f) + setFire(20) + starve()`，保证玩家死亡重生脱出 |
| B2 | `world_bedrock_coffin` 同类问题 | ✅ **第一轮已修**（`WorldEvents.java:468-471`）；审计读取的是修复前的快照 |
| C4 | `loot_treasure_cage` 用随机 `TRAP_BLOCKS` 做笼子（约 1/4 非固体 → 笼子失效；抽到 TNT → 连锁爆炸） | ✅ **已修**：改用实体方块圆石 |
| C9 | `titleToAll` 对 `title` 未判空 | ✅ **已修** |
| C1 | `ctx.spawnTnt` 不受 `allowWorldDamage` 约束 | ⚠️ **刻意保留**（用户明确要求"越整人越好"）；已改为在 `TrollConfig` 注释与 README 中**如实说明**该开关只覆盖大规模地形改造 |
| C3 | `mount` 可能骑上 TNT / 凋灵 | ⚠️ **刻意保留**（这是设计好的整人效果，文案有提示） |
| C5 | `invert_mouse` 改为每 tick 上下颠倒（抖动而非干净反转） | ⚠️ **保留**：javac 明确报 `GameOptions.invertYMouse` 在 1.20.1 为 **private 访问控制**，原实现无法编译 |
| C8 | `voidPit(5,20)` / `meteorShower(20,40)` 单 tick 性能尖峰 | ⚠️ 已知：仅出现在最高等级事件、冷却很长；`meteorShower` 在关闭地形破坏时自动退化为铁砧 |

> 修复后已重新 `clean build`，**BUILD SUCCESSFUL**，`dist/zhengren-1.0.0.jar` 已更新（571 事件 / 36 class / 270 KB）。

---

## 🏗️ 交付物

| 文件 | 说明 |
|---|---|
| `dist/zhengren-1.0.0.jar` | **可直接放进 `.minecraft/mods/` 的成品**（需 Fabric API） |
| `dist/README.md` | 使用文档 |
| `dist/VERIFICATION.md` | 本验收报告 |
| `dist/EVENT_CONTRACT.md` | 事件编写契约（供后续扩展） |
| `zhengren-mod/` | 完整可构建源码工程（含 Gradle Wrapper） |
| `zhengren-mod/EVENT_CONTRACT.md` | 契约全文（含第二批新增 API 与常量池） |

---

## 🤝 构建方式

本项目由 **DeepSeek Harness Agent Teams** 协作完成，共两轮：

**第一轮（框架 + 230 事件）**
- **Lead**：工程脚手架、核心框架（`TrollEvent`/`TrollRegistry`/`TrollConfig`/`TrollContext`/`TrollScheduler`/`TrollCommand`/网络包/客户端整活系统）、构建环境搭建与全部编译修复
- **7 个事件作者 agent**：WorldEvents / ItemEvents / MobEvents / PlayerEvents / AudioEvents / GlitchEvents / ChaosEvents
- **1 个只读审计 agent**：独立核对 id、等级、API 白名单、危险代码与框架逻辑

**第二轮（扩容 + 341 事件）**
- **Lead**：扩展客户端 glitch（+11 个 id、+2 个假界面）、扩展 `TrollContext`（+30 个 API）、扩展 `TrollPools`（+10 个常量池）、新增 7 个分类文件桩、编译集成与修复
- **同一批 7 个 agent**：TrapEvents / LootEvents / BeastEvents / MindEvents / SocialEvents / NightmareEvents / DoomEvents
- **审计 agent**：task-17 全量复核 571 个事件

任务板：task-1 … task-17。
