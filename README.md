# <img src="src/main/resources/assets/ocelotsignmod/icon.png" width="32" align="absmiddle" /> 豹猫指示牌模组 (Ocelot Sign Mod)

[![官方网站与文档](https://img.shields.io/badge/豹猫指示牌模组-官方文档-00a1d6)](https://creeper-cola123.github.io/OcelotSignMod_Docs/)
[![Modrinth](https://img.shields.io/modrinth/dt/ocelotsignmod?logo=modrinth&label=Modrinth&color=00af5c)](https://modrinth.com/mod/ocelotsignmod)
[![CurseForge](https://img.shields.io/badge/CurseForge-Ocelot_Sign_Mod-f16436?logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/ocelotsignmod)
[![MC百科](https://img.shields.io/badge/MC百科-28882-blue)](https://www.mcmod.cn/class/28882.html)
[![哔哩哔哩宣传片](https://img.shields.io/badge/Bilibili-宣传片-ff69b4?logo=bilibili)](https://www.bilibili.com/video/BV1kFgq6PE4D/)
[![QQ群](https://img.shields.io/badge/QQ群-871229937-12b7f5?logo=tencentqq)](https://qun.qq.com/universal-share/share?ac=1&authKey=4Nn/HUptHFXeX0LvwZ7aVyW/psDyh8tgyhMhHEGnCtr0EuQzFpoIvXeOd%2Ba6cW%2BN&busi_data=eyJncm91cENvZGUiOiI4NzEyMjk5MzciLCJ1aW4iOiIxNTk5Mjk0OTU3In0=&data=-XUNHn2ONLeDg7l3sslDVejgEuAq73ALu_2TClospj7g4S3rv98L3mZCSgnivTvV_yUbZ7-xv-b6Owi73v6CdkAT1lxJ3l9MzYfWaiOT7pg&svctype=5&tempid=h5_group_info)
[![License](https://img.shields.io/badge/License-LGPL_3.0-blue.svg)](LICENSE)

[简体中文](README.md) | [English](README_EN.md)

> [!IMPORTANT]
> **官方主仓库声明**：这里是《豹猫指示牌模组》**唯一的官方原版仓库（非第三方分叉模组）**，提供最完整的原生功能与第一手更新。仓库针对不同 Minecraft 版本维护了多个分支，请按需切换。

**《豹猫指示牌模组》** 是基于 Fabric 平台开发的《[迷上城建](https://github.com/SolidBlock-cn/mishanguc)》附属模组。它继承了迷上城建高自由度的文字与图案编辑系统，专注于道路交通指引与公共导视细节，同时对原模组的告示牌操作体验做了进一步打磨。

---

## 模组特性

1. 此模组根据 GB5768 内置了中国大陆道路指示牌图案，能为你的道路创作增添更多细节；
2. 此模组收录了大量公共符号，适配地铁站、机场等各类公共场所的场景搭建；
3. 此模组新增支持自定义编辑的专属道路指示牌方块，以及配套的指示牌立柱；
4. 此模组在《迷上城建》在道路方块的基础上，补充地面预告导向箭头、橙色箭头及各类禁止类箭头；
5. 此模组支持通过资源包自定义指示牌图案，不仅能还原中国大陆的道路指示牌，还能制作专属资源包还原各个地区的指示牌；
6. 此模组官网开设了创意广场，你可以将自己的路牌作品投稿至官网分享给其他玩家，也可以从创意广场下载其他玩家的资源包，共建城建生态。

## 安装指南

运行环境要求 **Fabric Loader** 与 **Java 17+**。

### 1. 准备前置与本体
请确保以下模组均已下载并放入 `.minecraft/mods` 文件夹：
1. **[Fabric API](https://modrinth.com/mod/fabric-api)**
2. **[迷上城建 (MISHANGUC)](https://modrinth.com/mod/mishang-urban-construction)**
3. **本模组**（从 [Modrinth](https://modrinth.com/mod/ocelotsignmod) 或 [CurseForge](https://www.curseforge.com/minecraft/mc-mods/ocelotsignmod) 下载对应版本的 `.jar` 文件）

安装后的目录结构参考：

```text
.minecraft/
└── mods/
    ├── fabric-api-0.92.0+1.20.1.jar
    ├── mishanguc-1.6.5-alpha.1-1.20.1.jar
    └── ocelotsignmod-1.0.2.jar
```

> **服务端安装**：开服时，服务端与客户端均需安装上述三个模组，且版本必须保持一致。

## 相关链接

- **使用文档与创意广场**：[查阅《图案与字体列表》及在线工具](https://creeper-cola123.github.io/OcelotSignMod_Docs/)
- **下载地址**：[Modrinth](https://modrinth.com/mod/ocelotsignmod) · [CurseForge](https://www.curseforge.com/minecraft/mc-mods/ocelotsignmod) · [MC百科 (MCMOD)](https://www.mcmod.cn/class/28882.html)
- **视频演示**：[哔哩哔哩宣传片](https://www.bilibili.com/video/BV1kFgq6PE4D/)
- **交流与反馈**：遇到 Bug 请直接提交 [Issue](../../issues)，或加入 [官方 QQ 群 (871229937)](https://qun.qq.com/universal-share/share?ac=1&authKey=4Nn/HUptHFXeX0LvwZ7aVyW/psDyh8tgyhMhHEGnCtr0EuQzFpoIvXeOd%2Ba6cW%2BN&busi_data=eyJncm91cENvZGUiOiI4NzEyMjk5MzciLCJ0b2tlbiI6ImxUK1UwN25ybGVaWnpDakwxdy9zZEYwQ1ZHWnhkM25tYkRXUWVrUXRYMmk3c0RUT0JFWEVESFUxNUsyUVZjSnciLCJ1aW4iOiIxNTk5Mjk0OTU3In0=&data=-XUNHn2ONLeDg7l3sslDVejgEuAq73ALu_2TClospj7g4S3rv98L3mZCSgnivTvV_yUbZ7-xv-b6Owi73v6CdkAT1lxJ3l9MzYfWaiOT7pg&svctype=5&tempid=h5_group_info)。

## 从源码构建

需要配置 **JDK 17**。项目自带 Gradle Wrapper，无需额外安装 Gradle。

在终端执行构建（Windows 请使用 `gradlew.bat`）：

```bash
./gradlew build
```

编译产物位于 `build/libs/` 目录下：
- `ocelotsignmod-x.x.x.jar`：可直接放入游戏使用的正式包
- `ocelotsignmod-x.x.x-dev.jar`：未混淆的开发环境调试包
- `ocelotsignmod-x.x.x-sources.jar`：源码包

如需在开发环境下直接启动游戏调试，运行 `./gradlew run` 即可。

*注：前置模组以本地 JAR 形式存放在 `libs/` 目录下。若需更新前置版本，直接替换该目录下的文件并同步修改 `build.gradle` 中的依赖文件名即可。*

---

## 鸣谢

感谢每一位参与模组开发、测试与移植的社区伙伴！

### 核心开发团队

| 成员 | 职责 | 个人主页 |
| :--- | :--- | :--- |
| **豹猫苦力怕可乐** (Ocelot Creeper Cola) | 项目发起 / 代码 / 模型 / 美术 | [哔哩哔哩](https://space.bilibili.com/226720371) · [GitHub](https://github.com/Creeper-Cola123) |
| **花老花** (Hualaohua) | 交通标志规范指导 | [哔哩哔哩](https://space.bilibili.com/646146582) |
| **XFunny** | 游戏测试 | [哔哩哔哩](https://space.bilibili.com/34849566) · [GitHub](https://github.com/X-Funny) |

### 前置模组作者

| 成员 | 职责 | 个人主页 |
| :--- | :--- | :--- |
| **Solid-Block** | 迷上城建 (MISHANGUC) 作者 | [哔哩哔哩](https://space.bilibili.com/87328531) · [GitHub](https://github.com/SolidBlock-cn) |

### 社区贡献者 (PR)

| 成员 | 贡献内容 | 个人主页 |
| :--- | :--- | :--- |
| **北咕** (Beigu) | 1.19.2 版本移植 | [哔哩哔哩](https://space.bilibili.com/3493084226652280) · [GitHub](https://github.com/BGSDT) |
| **鸽秋** (Pigeon0v0) | 26.1.2 版本移植 | [哔哩哔哩](https://space.bilibili.com/354878609) · [GitHub](https://github.com/Pigeon0v0) |

---

## 开源协议与版权声明

详细条款请参阅仓库根目录下的 [LICENSE](LICENSE) 文件。

### 一、模组开源协议 (LGPL-3.0)

本模组基于 **LGPL-3.0** 协议开源。你可以自由使用、修改和分发本模组的代码与美术资产（例如基于本模组制作其他《Minecraft》衍生模组，或移植为《都市：天际线》等城建游戏的模组资产），但必须遵守以下规则：

#### 1. 二次开发与分发要求
- **保持开源**：修改后的核心代码必须同样以 LGPL-3.0 协议开源。
- **保留署名**：发布修改版时，必须在模组内、发布页、文档及 README 的显著位置标注原作者与本仓库链接，并明确声明“基于《豹猫指示牌模组》二次修改”，不得声称为个人或团队完全独立制作。
- **避免混淆**：修改版的名称、图标与宣发不得与官方版本混淆，避免误导玩家。如需深度二次开发，建议提前联系原作者沟通。

#### 2. 红线规则（严格禁止）
- 违反 LGPL-3.0 协议闭源分发代码。
- 在二次分发版本中植入恶意、破坏性或窃取隐私的代码。
- 冒充原作者，或将修改版伪装成官方版本发布。
- **商业倒卖**：严禁将本模组上传至淘宝、闲鱼等电商平台售卖，或搬运至《我的世界》中国版（网易版）组件中心开启付费下载。

#### 3. 关于非官方移植与分叉
原作者团队仅对本仓库发布的**官方版本**负责。社区基于本模组二次开发的非官方分支（如特定版本移植、魔改版），其代码质量与兼容性由对应维护者独立负责。使用非官方分支遇到问题时，请优先联系该分支的维护者反馈。我们非常欢迎社区贡献，但因精力有限，无法为非官方分支提供兜底技术支持，敬请谅解。

### 二、前置模组合规声明

- **前置项目**：MISHANGUC（迷上城建）
- **开源协议**：[GNU LGPL v3.0](https://www.gnu.org/licenses/lgpl-3.0.html)
- **项目地址**：[SolidBlock-cn/mishanguc](https://github.com/SolidBlock-cn/mishanguc)
- **合规说明**：本模组在 LGPL-3.0 许可范围内以动态链接/附属库形式调用前置，完整保留原作者版权信息。

### 三、内置字体授权声明

本模组内嵌了以下开源/免费商用字体：

- **MiSans**：版权归小米公司所有，遵循[《MiSans 字体免费商用及内嵌许可协议》](https://hyperos.mi.com/font/zh/faq/)（[字体官网](https://hyperos.mi.com/font/zh/)）。
- **Roadgeek 2014**：由 Sammi Ta 基于 FHWA 标准设计，采用 **CC BY-SA 4.0** 协议授权（[GitHub 仓库](https://github.com/sammdot/roadgeek-fonts)）。
- **思源黑体 (Source Han Sans)**：采用 **SIL Open Font License 1.1** 授权（[GitHub 仓库](https://github.com/adobe-fonts/source-han-sans)）。
- **思源宋体 (Source Han Serif)**：采用 **SIL Open Font License 1.1** 授权（[GitHub 仓库](https://github.com/adobe-fonts/source-han-serif)）。

### 四、官网与工具版权

本模组的[文档网站及在线工具（含网页端资源包创建器）](https://creeper-cola123.github.io/OcelotSignMod_Docs/)虽公开托管，但**不适用开源协议**，作者保留所有权利。严禁任何人对网页代码、工具及站点资源进行抓取、修改、镜像或二次分发。

*注：官网“创意广场”中由玩家自行上传的资源包（ZIP 文件），其版权归上传者所有，具体授权方式以作者标注为准。*
