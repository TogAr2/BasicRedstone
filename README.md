# BasicRedstone

[![license](https://img.shields.io/github/license/Bloepiloepi/BasicRedstone.svg?style=flat-square)](LICENSE)

BasicRedstone is a **very** basic redstone implementation for Minestom.
It does not intend to be a fully featured extension, however,
it might be just right for some server owners.

It is mainly developed for minigame servers that want basic redstone support (to open doors by a lever, for example).

## Table of Contents

- [Features](#features)
- [Usage](#usage)
- [Customization](#customization)
- [Contributing](#contributing)

## Features

This extension only has very basic features.

- Manual door/trapdoor opening and closing (by hand)
- Redstone powering directly or through blocks
- Redstone components (doors and trapdoors are builtin)
- Redstone sources (buttons and levers are builtin)

Note: currently, every block is a conductor (including air and glass-like blocks).
This may change in the future.

## Usage

To use this extension, you can either build a jar file
and put it in your extensions folder, or shade it as a library in your server.

The extension will not work by itself, you have to enable separate features.

### Enabling events

You can get an `EventNode` with all events listening by using `BasicRedstoneExtension.events()`.
You can add this node as a child to any other node, and the redstone will work in the scope.
You can also get a node with separated events of this extension.

`buttonEvents()` creates an EventNode with the events for button pressing and breaking.

`leverEvents()` creates an EventNode with the events for lever pulling and breaking.

`doorEvents()` creates an EventNode with the events for manual (no redstone) door interaction.

`trapdoorEvents()` creates an EventNode with the events for manual (no redstone) trapdoor interaction.

### Using redstone components

For every new instance created, you have to specify which redstone components to use in this instance.

For example:
```java
Instance instance;
PowerNet powerNet = Redstone.getPowerNet(instance);
powerNet.useBuiltinComponents();
```

`useBuiltinComponents()` will add doors and trapdoors to the instance.

## Customization

It is possible to create your own redstone components, just implement `RedstoneComponent`
and use `PowerNet.useComponent(...)` to add it to an instance.

It is also possible (but slightly more difficult) to create your own redstone sources (like buttons and levers).
To do so, take a look at how the builtin sources do it.

## Contributing

I don't want to add a lot of features, but if you found a bug, an issue or a pull request is welcome!