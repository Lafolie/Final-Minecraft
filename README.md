# Final Minecraft Core
### By Lafolie

![FMC Logo](asset/fmcLogo.png "FMC Logo")

### *Add elemental aspects to mobs and equipment. Required by other FMC mods.*

### Final Minecraft is a Fabric mod for Minecraft 1.18.2

### Current version: 0.1.0 (Biggs)

## Features

This mod contains basic functionality required by other Final Minecraft (FMC) mods.

The FMC Core provides a limited set of features by itself:

* Elemental Aspects (configurable)
* Status effects

With Elemental Aspects, mobs and players can have weaknesess/resistances, and equipment can have elemental attributes. Elemental aspects can be toggled off in the config.

Status effects exist in the game but require other FMC mods to use.

### Why do I need FMC Core?

Other mods in the Final Minecraft mod set require FMC Core to work. If you wish to use any Final Minecraft mods, you must install this mod.

If you wish to play with other FMC mods but don't like any of the features provided by FMC Core, you can disable them!

## Installation

*TODO: add links & versions*

### Supported Versions

FMC Core is a a **Fabric Mod**. A forge version will never be created.

FMC Core currently supports **Minecraft 1.18.2**.

Development will always be targeted at the latest stable release of Minecraft. Older versions will not be supported.

### Client / Server

FMC Core should be installed on both clients and the server.

### Dependencies

FMC Core requires the following mods:
* Fabric API
* Cloth-Config

### Optional Dependencies

FMC Core can utilise, but works without the following mods:
* ModMenu

## Final Minecraft

The Final Minecraft project aims to add Final Fantasy inspired content to Minecraft.

The FMC project exists as a set of mods, allowing players to pick and choose features in a modular fashion. All FMC mods are compatible together, and most integrate with each other. However, with the exception of FMC Core, all mods are optional and may be used in any combination.

Mods in the official FMC mod set include:

* FMC Core *- add elemental aspects to mobs and equipment. Required by other FMC mods*
* FMC Crystals *- add crystal items and blocks to your world*

You can join the official discord here: [https://discord.gg/Kv6umW2vzy](https://discord.gg/Kv6umW2vzy)

## For Developers

FMC Core exposes an API for mod developers to create their own FMC add-ons.

### External Mod Support

#### Elemental Aspects

FMC Core's Elemental Aspects feature is fully compatible with items, blocks, and mobs supplied by other mods.
Compatibility is achieved via configuration extensions. Players may add their own configurations to their personal installs, but the FMC project aims to provide built-in support for as many mods as possible.

If you are a developer and wish to supply official configuration extensions for external mods please make a pull-request that adds your configuration data to <TODO: create config file>. See here <TODO: docs> for documentation and guidelines.
*TODO: setup maven on repo*

*TODO: wiki and such*

## License

FMC Core is licensed under the GNU General Public License v3.
