package org.betterx.betterend.blocks;

import org.betterx.bclib.behaviours.interfaces.BehaviourStone;
import org.betterx.bclib.blocks.BaseBlockNotFull;
import org.betterx.bclib.client.models.BasePatterns;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.ModelsHelper.MultiPartBuilder;
import org.betterx.bclib.client.models.PatternsHelper;
import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.PostInitable;
import org.betterx.bclib.interfaces.RenderLayerProvider;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.JsonFactory;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.blocks.basis.PottableLeavesBlock;
import org.betterx.betterend.client.models.Patterns;
import org.betterx.betterend.config.Configs;
import org.betterx.betterend.interfaces.PottablePlant;
import org.betterx.betterend.interfaces.PottableTerrain;
import org.betterx.betterend.registry.EndBlocks;

import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.loader.api.FabricLoader;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.joml.Vector3f;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FlowerPotBlock extends BaseBlockNotFull implements RenderLayerProvider, PostInitable {
    private static final IntegerProperty PLANT_ID = EndBlockProperties.PLANT_ID;
    private static final IntegerProperty SOIL_ID = EndBlockProperties.SOIL_ID;
    private static final IntegerProperty POT_LIGHT = EndBlockProperties.POT_LIGHT;
    private static final VoxelShape SHAPE_EMPTY;
    private static final VoxelShape SHAPE_FULL;
    private static Block[] plants;
    private static Block[] soils;

    public FlowerPotBlock(Block source) {
        super(FabricBlockSettings.copyOf(source).luminance(state -> state.getValue(POT_LIGHT) * 5));
        this.registerDefaultState(
                this.defaultBlockState()
                    .setValue(PLANT_ID, 0)
                    .setValue(SOIL_ID, 0)
                    .setValue(POT_LIGHT, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PLANT_ID, SOIL_ID, POT_LIGHT);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
        List<ItemStack> drop = Lists.newArrayList(new ItemStack(this));
        int id = state.getValue(SOIL_ID) - 1;
        if (id >= 0 && id < soils.length && soils[id] != null) {
            drop.add(new ItemStack(soils[id]));
        }
        id = state.getValue(PLANT_ID) - 1;
        if (id >= 0 && id < plants.length && plants[id] != null) {
            drop.add(new ItemStack(plants[id]));
        }
        return drop;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(
            BlockState state,
            Direction facing,
            BlockState neighborState,
            LevelAccessor world,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        int plantID = state.getValue(PLANT_ID);
        if (plantID < 1 || plantID > plants.length || plants[plantID - 1] == null) {
            return state.getValue(POT_LIGHT) > 0 ? state.setValue(POT_LIGHT, 0) : state;
        }
        int light = plants[plantID - 1].defaultBlockState().getLightEmission() / 5;
        if (state.getValue(POT_LIGHT) != light) {
            state = state.setValue(POT_LIGHT, light);
        }
        return state;
    }

    @Override
    public void postInit() {
        if (FlowerPotBlock.plants != null) {
            return;
        }

        Block[] plants = new Block[128];
        Block[] soils = new Block[16];

        Map<String, Integer> reservedPlantsIDs = Maps.newHashMap();
        Map<String, Integer> reservedSoilIDs = Maps.newHashMap();

        JsonObject obj = JsonFactory.getJsonObject(new File(
                FabricLoader.getInstance().getConfigDir().toFile(),
                BetterEnd.MOD_ID + "/blocks.json"
        ));
        if (obj.get("flower_pots") != null) {
            JsonElement plantsObj = obj.get("flower_pots").getAsJsonObject().get("plants");
            JsonElement soilsObj = obj.get("flower_pots").getAsJsonObject().get("soils");
            if (plantsObj != null) {
                plantsObj.getAsJsonObject().entrySet().forEach(entry -> {
                    String name = parseConfigKey(entry.getKey());
                    reservedPlantsIDs.put(name, entry.getValue().getAsInt());
                });
            }
            if (soilsObj != null) {
                soilsObj.getAsJsonObject().entrySet().forEach(entry -> {
                    String name = parseConfigKey(entry.getKey());
                    reservedSoilIDs.put(name, entry.getValue().getAsInt());
                });
            }
        }

        loadPlantIdsFromBundledFlowerPotState(reservedPlantsIDs);
        loadSoilIdsFromBundledFlowerPotModels(reservedSoilIDs);

        List<Block> sortedBlocks = new ArrayList<>(EndBlocks.getModBlocks());
        sortedBlocks.sort(Comparator.comparing(block -> {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
            return id == null ? "" : id.toString();
        }));

        sortedBlocks.forEach(block -> {
            if (block instanceof PottablePlant && ((PottablePlant) block).canBePotted()) {
                processBlock(plants, block, "flower_pots.plants", reservedPlantsIDs);
            } else if (block instanceof PottableTerrain && ((PottableTerrain) block).canBePotted()) {
                processBlock(soils, block, "flower_pots.soils", reservedSoilIDs);
            }
        });
        Configs.BLOCK_CONFIG.saveChanges();

        FlowerPotBlock.plants = new Block[maxNotNull(plants) + 1];
        System.arraycopy(plants, 0, FlowerPotBlock.plants, 0, FlowerPotBlock.plants.length);

        FlowerPotBlock.soils = new Block[maxNotNull(soils) + 1];
        System.arraycopy(soils, 0, FlowerPotBlock.soils, 0, FlowerPotBlock.soils.length);

        if (PLANT_ID.getValue(Integer.toString(FlowerPotBlock.plants.length)).isEmpty()) {
            throw new RuntimeException("There are too much plant ID values!");
        }
        if (SOIL_ID.getValue(Integer.toString(FlowerPotBlock.soils.length)).isEmpty()) {
            throw new RuntimeException("There are too much soil ID values!");
        }
    }

    private int maxNotNull(Block[] array) {
        int max = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                max = i;
            }
        }
        return max;
    }

    private void processBlock(Block[] target, Block block, String path, Map<String, Integer> idMap) {
        ResourceLocation location = BuiltInRegistries.BLOCK.getKey(block);
        Integer reserved = idMap.get(location.getPath());
        if (reserved != null && reserved >= 0 && reserved < target.length && target[reserved] == null) {
            target[reserved] = block;
        } else {
            for (int i = 0; i < target.length; i++) {
                if (!idMap.containsValue(i)) {
                    target[i] = block;
                    idMap.put(location.getPath(), i);
                    Configs.BLOCK_CONFIG.getInt(path, location.getPath(), i);
                    break;
                }
            }
        }
    }

    private void loadPlantIdsFromBundledFlowerPotState(Map<String, Integer> reservedPlantsIDs) {
        JsonObject stateJson = loadBundledJson("assets/betterend/blockstates/endstone_flower_pot.json");
        if (stateJson == null) {
            return;
        }

        JsonArray multipart = asArray(stateJson.get("multipart"));
        if (multipart == null || multipart.isEmpty()) {
            return;
        }

        Map<String, Integer> modelToId = Maps.newHashMap();
        for (JsonElement partElement : multipart) {
            JsonObject part = asObject(partElement);
            JsonObject when = part == null ? null : asObject(part.get("when"));
            JsonObject apply = part == null ? null : asObject(part.get("apply"));
            if (when == null || apply == null) {
                continue;
            }
            JsonElement plantIdElement = when.get("plant_id");
            JsonElement modelElement = apply.get("model");
            if (plantIdElement == null || modelElement == null || !modelElement.isJsonPrimitive()) {
                continue;
            }
            int id = parseInt(plantIdElement.getAsString(), -1);
            if (id < 1) {
                continue;
            }
            String modelId = normalizeModelId(modelElement.getAsString(), BetterEnd.MOD_ID);
            modelToId.put(modelId, id - 1);
        }

        if (modelToId.isEmpty()) {
            return;
        }

        EndBlocks.getModBlocks().forEach(block -> {
            if (!(block instanceof PottablePlant)) {
                return;
            }
            PottablePlant plant = (PottablePlant) block;
            if (!plant.canBePotted()) {
                return;
            }

            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
            if (blockId == null) {
                return;
            }

            String modelId = resolvePlantModelId(block, plant, blockId);
            Integer mapped = modelToId.get(modelId);
            if (mapped != null) {
                reservedPlantsIDs.put(blockId.getPath(), mapped);
            }
        });
    }

    private String resolvePlantModelId(Block block, PottablePlant plant, ResourceLocation blockId) {
        String pottedModel = blockId.getNamespace() + ":block/" + blockId.getPath() + "_potted";
        String pottedModelPath = "assets/" + blockId.getNamespace() + "/models/block/" + blockId.getPath() + "_potted.json";

        if (hasBundledResource(pottedModelPath)
                || block instanceof SaplingBlock
                || block instanceof PottableLeavesBlock) {
            return pottedModel;
        }

        JsonObject blockState = loadBundledJson("assets/" + blockId.getNamespace() + "/blockstates/" + blockId.getPath() + ".json");
        if (blockState == null) {
            return pottedModel;
        }

        JsonElement variants = blockState.get("variants");
        if (variants == null) {
            return pottedModel;
        }

        String modelPath = null;
        if (variants.isJsonArray() && variants.getAsJsonArray().size() > 0) {
            modelPath = extractModel(variants.getAsJsonArray().get(0));
        } else if (variants.isJsonObject()) {
            JsonElement selected = variants.getAsJsonObject().get(plant.getPottedState());
            if (selected != null) {
                if (selected.isJsonArray() && selected.getAsJsonArray().size() > 0) {
                    modelPath = extractModel(selected.getAsJsonArray().get(0));
                } else {
                    modelPath = extractModel(selected);
                }
            }
        }

        return modelPath == null ? pottedModel : normalizeModelId(modelPath, blockId.getNamespace());
    }

    private void loadSoilIdsFromBundledFlowerPotModels(Map<String, Integer> reservedSoilIDs) {
        Map<String, Integer> textureToId = Maps.newHashMap();

        for (int i = 0; i < 64; i++) {
            JsonObject model = loadBundledJson("assets/betterend/models/block/flower_pot_soil_" + i + ".json");
            if (model == null) {
                continue;
            }

            JsonObject textures = asObject(model.get("textures"));
            if (textures == null) {
                continue;
            }

            JsonElement textureElement = textures.get("texture");
            if (textureElement == null || !textureElement.isJsonPrimitive()) {
                continue;
            }

            textureToId.put(textureElement.getAsString(), i);
        }

        if (textureToId.isEmpty()) {
            return;
        }

        EndBlocks.getModBlocks().forEach(block -> {
            if (!(block instanceof PottableTerrain terrain) || !terrain.canBePotted()) {
                return;
            }

            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
            if (blockId == null) {
                return;
            }

            String texture = blockId.getNamespace() + ":block/" + blockId.getPath() + "_top";
            if (blockId.getPath().contains("rutiscus")) {
                texture += "_1";
            }

            Integer soilId = textureToId.get(texture);
            if (soilId != null) {
                reservedSoilIDs.put(blockId.getPath(), soilId);
            }
        });
    }

    private static String extractModel(JsonElement element) {
        JsonObject obj = asObject(element);
        JsonElement modelElement = obj == null ? null : obj.get("model");
        if (modelElement == null || !modelElement.isJsonPrimitive()) {
            return null;
        }
        return modelElement.getAsString();
    }

    private static String parseConfigKey(String key) {
        int separator = key.indexOf(' ');
        return separator > 0 ? key.substring(0, separator) : key;
    }

    private static JsonObject loadBundledJson(String path) {
        try (InputStream stream = FlowerPotBlock.class.getClassLoader().getResourceAsStream(path)) {
            if (stream == null) {
                return null;
            }
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            JsonElement json = JsonFactory.loadJson(reader);
            if (json != null && json.isJsonObject()) {
                return json.getAsJsonObject();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private static boolean hasBundledResource(String path) {
        return FlowerPotBlock.class.getClassLoader().getResource(path) != null;
    }

    private static String normalizeModelId(String modelId, String defaultNamespace) {
        ResourceLocation model = modelId.contains(":")
                ? ResourceLocation.tryParse(modelId)
                : ResourceLocation.tryBuild(defaultNamespace, modelId);
        if (model == null) {
            return defaultNamespace + ":" + modelId;
        }
        return model.toString();
    }

    private static int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }

    private static JsonObject asObject(JsonElement element) {
        return element != null && element.isJsonObject() ? element.getAsJsonObject() : null;
    }

    private static JsonArray asArray(JsonElement element) {
        return element != null && element.isJsonArray() ? element.getAsJsonArray() : null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        if (level.isClientSide) {
            return InteractionResult.CONSUME;
        }
        ItemStack itemStack = player.getItemInHand(hand);
        int soilID = state.getValue(SOIL_ID);
        if (soilID == 0 || soilID > soils.length || soils[soilID - 1] == null) {
            if (!(itemStack.getItem() instanceof BlockItem)) {
                return InteractionResult.PASS;
            }
            Block block = ((BlockItem) itemStack.getItem()).getBlock();
            for (int i = 0; i < soils.length; i++) {
                if (block == soils[i]) {
                    BlocksHelper.setWithUpdate(level, pos, state.setValue(SOIL_ID, i + 1));
                    if (!player.isCreative()) {
                        itemStack.shrink(1);
                    }
                    level.playSound(
                            player,
                            pos.getX() + 0.5,
                            pos.getY() + 0.5,
                            pos.getZ() + 0.5,
                            SoundEvents.SOUL_SOIL_PLACE,
                            SoundSource.BLOCKS,
                            1,
                            1
                    );
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        }

        int plantID = state.getValue(PLANT_ID);
        if (itemStack.isEmpty()) {
            if (plantID > 0 && plantID <= plants.length && plants[plantID - 1] != null) {
                BlocksHelper.setWithUpdate(level, pos, state.setValue(PLANT_ID, 0).setValue(POT_LIGHT, 0));
                player.addItem(new ItemStack(plants[plantID - 1]));
                return InteractionResult.SUCCESS;
            }
            if (soilID > 0 && soilID <= soils.length && soils[soilID - 1] != null) {
                BlocksHelper.setWithUpdate(level, pos, state.setValue(SOIL_ID, 0));
                player.addItem(new ItemStack(soils[soilID - 1]));
            }
            return InteractionResult.PASS;
        }
        if (!(itemStack.getItem() instanceof BlockItem)) {
            return InteractionResult.PASS;
        }
        BlockItem item = (BlockItem) itemStack.getItem();
        for (int i = 0; i < plants.length; i++) {
            if (item.getBlock() == plants[i]) {
                if (!((PottablePlant) plants[i]).canPlantOn(soils[soilID - 1])) {
                    return InteractionResult.PASS;
                }
                int light = plants[i].defaultBlockState().getLightEmission() / 5;
                BlocksHelper.setWithUpdate(level, pos, state.setValue(PLANT_ID, i + 1).setValue(POT_LIGHT, light));
                level.playSound(
                        player,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        SoundEvents.HOE_TILL,
                        SoundSource.BLOCKS,
                        1,
                        1
                );
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BlockModel getItemModel(ResourceLocation blockId) {
        Optional<String> pattern = PatternsHelper.createJson(Patterns.BLOCK_FLOWER_POT, blockId);
        return ModelsHelper.fromPattern(pattern);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public UnbakedModel getModelVariant(
            ResourceLocation stateId,
            BlockState blockState,
            Map<ResourceLocation, UnbakedModel> modelCache
    ) {
        MultiPartBuilder model = MultiPartBuilder.create(stateDefinition);
        model.part(new ModelResourceLocation(stateId.getNamespace(), stateId.getPath(), "inventory")).add();
        Transformation offset = new Transformation(new Vector3f(0, 7.5F / 16F, 0), null, null, null);

        for (int i = 0; i < plants.length; i++) {
            if (plants[i] == null) {
                continue;
            }

            final int compareID = i + 1;
            ResourceLocation modelPath = BuiltInRegistries.BLOCK.getKey(plants[i]);
            ResourceLocation objSource = new ResourceLocation(
                    modelPath.getNamespace(),
                    "models/block/" + modelPath.getPath() + "_potted.json"
            );

            if (Minecraft.getInstance().getResourceManager().getResource(objSource).isPresent()) {
                objSource = new ResourceLocation(modelPath.getNamespace(), "block/" + modelPath.getPath() + "_potted");
                model.part(objSource)
                     .setTransformation(offset)
                     .setCondition(state -> state.getValue(PLANT_ID) == compareID)
                     .add();
                continue;
            } else if (plants[i] instanceof SaplingBlock) {
                ResourceLocation loc = BuiltInRegistries.BLOCK.getKey(plants[i]);
                modelPath = new ResourceLocation(loc.getNamespace(), "block/" + loc.getPath() + "_potted");
                Map<String, String> textures = Maps.newHashMap();
                textures.put("%modid%", loc.getNamespace());
                textures.put("%texture%", loc.getPath());
                Optional<String> pattern = Patterns.createJson(BasePatterns.BLOCK_CROSS, textures);
                UnbakedModel unbakedModel = ModelsHelper.fromPattern(pattern);
                modelCache.put(modelPath, unbakedModel);
                model.part(modelPath)
                     .setTransformation(offset)
                     .setCondition(state -> state.getValue(PLANT_ID) == compareID)
                     .add();
                continue;
            } else if (plants[i] instanceof PottableLeavesBlock) {
                ResourceLocation loc = BuiltInRegistries.BLOCK.getKey(plants[i]);
                modelPath = new ResourceLocation(loc.getNamespace(), "block/" + loc.getPath() + "_potted");
                Map<String, String> textures = Maps.newHashMap();
                textures.put("%leaves%", loc.getPath().contains("lucernia") ? loc.getPath() + "_1" : loc.getPath());
                textures.put("%stem%", loc.getPath().replace("_leaves", "_log_side"));
                Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_POTTED_LEAVES, textures);
                UnbakedModel unbakedModel = ModelsHelper.fromPattern(pattern);
                modelCache.put(modelPath, unbakedModel);
                model.part(modelPath)
                     .setTransformation(offset)
                     .setCondition(state -> state.getValue(PLANT_ID) == compareID)
                     .add();
                continue;
            }

            objSource = new ResourceLocation(modelPath.getNamespace(), "blockstates/" + modelPath.getPath() + ".json");
            JsonObject obj = JsonFactory.getJsonObject(objSource);
            if (obj != null) {
                JsonElement variants = obj.get("variants");
                JsonElement list = null;
                String path = null;

                if (variants == null) {
                    continue;
                }

                if (variants.isJsonArray()) {
                    list = variants.getAsJsonArray().get(0);
                } else if (variants.isJsonObject()) {
                    list = variants.getAsJsonObject().get(((PottablePlant) plants[i]).getPottedState());
                }

                if (list == null) {
                    BetterEnd.LOGGER.warning("Incorrect json for pot plant " + objSource + ", no matching variants");
                    continue;
                }

                if (list.isJsonArray()) {
                    path = list.getAsJsonArray().get(0).getAsJsonObject().get("model").getAsString();
                } else {
                    path = list.getAsJsonObject().get("model").getAsString();
                }

                if (path == null) {
                    BetterEnd.LOGGER.warning("Incorrect json for pot plant " + objSource + ", no matching variants");
                    continue;
                }

                model.part(new ResourceLocation(path))
                     .setTransformation(offset)
                     .setCondition(state -> state.getValue(PLANT_ID) == compareID)
                     .add();
            } else {
                ResourceLocation loc = BuiltInRegistries.BLOCK.getKey(plants[i]);
                modelPath = new ResourceLocation(loc.getNamespace(), "block/" + loc.getPath() + "_potted");
                Map<String, String> textures = Maps.newHashMap();
                textures.put("%modid%", loc.getNamespace());
                textures.put("%texture%", loc.getPath());
                Optional<String> pattern = Patterns.createJson(BasePatterns.BLOCK_CROSS, textures);
                UnbakedModel unbakedModel = ModelsHelper.fromPattern(pattern);
                modelCache.put(modelPath, unbakedModel);
                model.part(modelPath)
                     .setTransformation(offset)
                     .setCondition(state -> state.getValue(PLANT_ID) == compareID)
                     .add();
            }
        }

        for (int i = 0; i < soils.length; i++) {
            if (soils[i] == null) {
                continue;
            }

            ResourceLocation soilLoc = BetterEnd.makeID("flower_pot_soil_" + i);
            if (!modelCache.containsKey(soilLoc)) {
                String texture = BuiltInRegistries.BLOCK.getKey(soils[i]).getPath() + "_top";
                if (texture.contains("rutiscus")) {
                    texture += "_1";
                }
                Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_FLOWER_POT_SOIL, texture);
                UnbakedModel soil = ModelsHelper.fromPattern(pattern);
                modelCache.put(soilLoc, soil);
            }
            final int compareID = i + 1;
            model.part(soilLoc).setCondition(state -> state.getValue(SOIL_ID) == compareID).add();
        }

        UnbakedModel result = model.build();
        modelCache.put(stateId, result);
        return result;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
        int id = state.getValue(PLANT_ID);
        return id > 0 && id <= plants.length ? SHAPE_FULL : SHAPE_EMPTY;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
        return SHAPE_EMPTY;
    }

    @Override
    public BCLRenderLayer getRenderLayer() {
        return BCLRenderLayer.CUTOUT;
    }

    public static class Stone extends FlowerPotBlock implements BehaviourStone {
        public Stone(Block source) {
            super(source);
        }
    }

    static {
        SHAPE_EMPTY = Shapes.or(Block.box(4, 1, 4, 12, 8, 12), Block.box(5, 0, 5, 11, 1, 11));
        SHAPE_FULL = Shapes.or(SHAPE_EMPTY, Block.box(3, 8, 3, 13, 16, 13));
    }
}