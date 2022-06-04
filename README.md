# Final Minecraft Core
### By Lafolie

![FMC Logo](asset/fmcLogo.png "FMC Logo")

### *Add Elemental Aspects to mobs and equipment. Required by other FMC mods.*

### Final Minecraft is a [Fabric mod](https://www.fabricmc.net/) for [Minecraft](https://www.minecraft.net/en-us) 1.18.2

### Current version: 0.1.0 (Biggs)

## Features

This mod contains basic functionality required by other Final Minecraft (FMC) mods.

The FMC Core provides a limited set of features by itself:

* Elemental Aspects (configurable)
* Status effects

With Elemental Aspects, mobs and players can have weaknesess/resistances, and equipment can have elemental attributes. Elemental aspects can be toggled off in the config.

~~Status effects exist in the mod but require other FMC mods to use.~~

### Why do I need FMC Core?

Other mods in the Final Minecraft mod set require FMC Core to work. If you wish to use any Final Minecraft mods, you must install this mod.

If you wish to play with other FMC mods but don't like any of the features provided by FMC Core, you can disable them!

## Installation

*TODO: add links & versions*

### Supported Versions

FMC Core is a **Fabric Mod**. A forge version will never be created.

FMC Core currently supports **Minecraft 1.18.2**.

Development will always be targeted at the latest stable release of Minecraft. Older versions will not be supported.

### Client / Server

FMC Core should be installed on both clients and the server.

### Dependencies

FMC Core requires the following mods:
* Fabric API v.???
* Cloth-Config v.???
* Cardinal-Components v.???

*TODO: Add links*

### Optional Dependencies

FMC Core can utilise, but works without the following mods:
* ModMenu v.???

## Final Minecraft

The Final Minecraft project aims to add Final Fantasy inspired content to Minecraft.

The FMC project exists as a set of mods, allowing players to pick and choose features in a modular fashion. All FMC mods are compatible together, and most integrate with each other. However, with the exception of FMC Core, all mods in the set are optional and may be used in any combination.

Mods in the official FMC mod set include:

* FMC Core *- add elemental aspects to mobs and equipment. Required by other FMC mods*
* FMC Crystals *- add crystal items and blocks to your world*
* *More to come...!*

You can join the official discord here: [https://discord.gg/Kv6umW2vzy](https://discord.gg/Kv6umW2vzy)

## For Developers

FMC Core exposes an API for mod developers to create their own FMC add-ons.

### External Mod Support

#### Elemental Aspects

FMC Core's Elemental Aspects feature is fully compatible with items, blocks, and mobs supplied by other mods.
Compatibility is achieved via tags.

If you are a developer and wish to make your blocks, items, and entities compatible, all you need to do is add your items to appropriate elemental tag. There is an elemental tag for each Elemental Aspect, and they use the [Fabric common tags scheme](https://www.fabricmc.net/wiki/tutorial:tags#common_tags_vs_mod_tags):

| Element	| Tag					|
|----------:|-----------------------|
|Fire		|c:fire_elemental		|
|Ice		|c:ice_elemental		|
|Lightning	|c:lightning_elemental	|
|Wind		|c:wind_elemental		|
|Water		|c:water_elemental		|
|Earth		|c:earth_elemental		|
|Poison		|c:poison_elemental		|
|Holy		|c:holy_elemental		|
|Dark		|c:dark_elemental		|
|Gravity	|c:gravity_elemental 	|

FMC code uses the following object categories for elemental tags:

* Blocks
* Items
* Fluids
* Entity-Types
* ~~Enchantments~~

This repository also contains a tool written in Lua that helps to generate tag json from a CSV file (such as that exported from a spreasheet), run `lua genTags.lua -h` for a list of options. See the [lua/genTags.lua](lua/genTags.lua) file for more information.

*TODO: setup maven on repo*

*TODO: wiki and such*

## Authors

Lafolie - designer and developer.

### Special Thanks
Linguardium - of the fabric discord, for his tremendous help in learning how to do this stuff!

## License

FMC Core is licensed under the GNU General Public License v3.
