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

To use this extension, you can either use the jar file
and put it in your extensions folder, or shade it as a library in your server
(in which case you have to add the events, see below).

### Using the events

To use the events, add `BasicRedstoneExtension.REDSTONE_EVENTS` as a child to any `EventNode` you want.

This only applies when you use it as a library instead of an extension.

### Using redstone components

For every new instance, you can specify which redstone components to use in this instance.

For example:
```java
Instance instance;
RedstoneComponent component;
PowerNet powerNet = Redstone.getPowerNet(instance);
powerNet.useComponent(component);
```

Doors and trapdoors are added by default, but if you don't want them, they can be removed like this:
```java
powerNet.removeComponent(Doors.DOOR_COMPONENT);
powerNet.removeComponent(Trapdoors.TRAPDOOR_COMPONENT);
```

## Customization

It is possible to create your own redstone components, just implement `RedstoneComponent`
and use `PowerNet.useComponent(...)` to add it to an instance.

It is also possible (but slightly more difficult) to create your own redstone sources (like buttons and levers).
To do so, take a look at how the builtin sources do it.

## Contributing

I don't want to add a lot of features, but if you found a bug, an issue or a pull request is welcome!