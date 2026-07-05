package org.betterx.betterend.mixin.common;

import org.betterx.betterend.config.Configs;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(Structure.class)
public abstract class StructureMixin {
    @Inject(method = "generate", at = @At("HEAD"), cancellable = true)
    private void be_skipDisabledStructure(
            RegistryAccess registryAccess,
            ChunkGenerator chunkGenerator,
            BiomeSource biomeSource,
            RandomState randomState,
            StructureTemplateManager structureTemplateManager,
            long seed,
            ChunkPos chunkPos,
            int references,
            LevelHeightAccessor heightAccessor,
            Predicate<Holder<Biome>> validBiome,
            CallbackInfoReturnable<StructureStart> cir
    ) {
        Registry<Structure> registry = registryAccess.registryOrThrow(Registries.STRUCTURE);
        Optional<ResourceKey<Structure>> key = registry.getResourceKey((Structure)(Object)this);
        if (key.isPresent() && !Configs.isStructureEnabled(key.get().location())) {
            cir.setReturnValue(StructureStart.INVALID_START);
        }
    }
}
