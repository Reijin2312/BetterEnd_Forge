package org.betterx.betterend.util;

import org.betterx.bclib.util.StructureHelper;
import org.betterx.betterend.BetterEnd;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class EndStructureHelper {
    private EndStructureHelper() {
    }

    public static StructureTemplate readStructure(ResourceLocation resource) {
        if (!BetterEnd.MOD_ID.equals(resource.getNamespace())) {
            return StructureHelper.readStructure(resource);
        }
        String path = structurePath(resource);
        InputStream stream = openResourceStream(path);
        if (stream == null) {
            stream = openFileStream(path);
        }
        if (stream == null) {
            throw new IllegalStateException("Missing BetterEnd structure resource: " + path);
        }
        return readStructureFromStream(path, stream);
    }

    public static StructureTemplate readStructure(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Structure path is empty");
        }
        if (!path.startsWith("/") && path.indexOf(':') > 0) {
            return readStructure(new ResourceLocation(path));
        }
        InputStream stream = openResourceStream(path);
        if (stream != null) {
            return readStructureFromStream(path, stream);
        }
        if (path.startsWith("/data/" + BetterEnd.MOD_ID + "/")) {
            stream = openFileStream(path);
            if (stream != null) {
                return readStructureFromStream(path, stream);
            }
            throw new IllegalStateException("Missing BetterEnd structure resource: " + path);
        }
        return StructureHelper.readStructure(path);
    }

    private static String structurePath(ResourceLocation resource) {
        return "/data/" + resource.getNamespace() + "/structures/" + resource.getPath() + ".nbt";
    }

    private static InputStream openResourceStream(String path) {
        InputStream stream = BetterEnd.class.getResourceAsStream(path);
        if (stream != null) {
            return stream;
        }
        String normalized = path.startsWith("/") ? path.substring(1) : path;
        ClassLoader classLoader = BetterEnd.class.getClassLoader();
        if (classLoader != null) {
            stream = classLoader.getResourceAsStream(normalized);
            if (stream != null) {
                return stream;
            }
        }
        ClassLoader context = Thread.currentThread().getContextClassLoader();
        if (context != null && context != classLoader) {
            stream = context.getResourceAsStream(normalized);
        }
        return stream;
    }

    private static InputStream openFileStream(String path) {
        String normalized = path.startsWith("/") ? path.substring(1) : path;
        Path resourcePath = Paths.get("src", "main", "resources").resolve(normalized);
        if (Files.exists(resourcePath)) {
            try {
                return Files.newInputStream(resourcePath);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to open structure file " + resourcePath, e);
            }
        }
        return null;
    }

    private static StructureTemplate readStructureFromStream(String path, InputStream stream) {
        try (InputStream input = stream) {
            CompoundTag tag = NbtIo.readCompressed(input);
            StructureTemplate template = new StructureTemplate();
            template.load(BuiltInRegistries.BLOCK.asLookup(), tag);
            return template;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read structure " + path, e);
        }
    }
}
