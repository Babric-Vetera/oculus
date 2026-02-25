# Babric MCP Example Mod (Beta 1.7.3)

A minimal, clean example project showing how to build a **Babric-based
mod** for\
**Minecraft Beta 1.7.3**, using **MCP mappings** and a modern
Gradle workspace.

This template is for:

- learning how Babric works
- experimenting with mixins
- creating lightweight utility mods or clients

## Features

- Fully configured **Babric + Mixin** environment\
- MCP method & field names for Beta 1.7.3\
- Example mixin demonstrating how to interact with vanilla classes\
- Clean, documented project structure\
- Ready for IntelliJ IDEA, Eclipse, or VSCode

## Project Structure

    /src/main/java      -> Your mod and mixins
    /src/main/resources -> mixin configuration + metadata + assets
    gradle/             -> Gradle wrapper files
    build.gradle        -> Dependencies + build configuration
    README.md           -> You're here right now

## Setup

Follow the standard Fabric/Babric setup guide for your IDE:\
https://fabricmc.net/wiki/tutorial:setup

Once imported, you can build the project using:

    ./gradlew build

The output JAR will appear under:

    build/libs/

## Creating Your First Mixin

Inside this template you'll find an example mixin showing how to hook
into\
MCP-named Beta 1.7.3 classes using Babric's modern tooling.

Here's a micro example:

``` java
@Inject(method = "runTick", at = @At("HEAD"))
private void onTick(CallbackInfo ci) {
    System.out.println("Tick!");
}
```

Replace it, expand it, blow it up, whatever suits your mod.

## License

This template is available under the **CC0 license**.\
Feel free to learn from it, copy it, expand it, or use it as the base
for your own project.
