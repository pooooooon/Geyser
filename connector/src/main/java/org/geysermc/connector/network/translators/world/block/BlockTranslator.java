/*
 * Copyright (c) 2019-2020 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Geyser
 */

package org.geysermc.connector.network.translators.world.block;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.nbt.stream.NBTInputStream;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.nbt.tag.ListTag;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.GeyserEdition;
import org.geysermc.connector.network.translators.world.block.entity.BlockEntity;
import org.geysermc.connector.network.translators.world.block.entity.BlockEntityTranslator;
import org.geysermc.connector.utils.Toolbox;

import java.io.InputStream;
import java.util.*;

@Getter
public class BlockTranslator {
    public ListTag<CompoundTag> blocks;
    public final BlockState air = new BlockState(0);
    public int bedrockWaterId;

    protected final Int2IntMap javaToBedrockBlockMap = new Int2IntOpenHashMap();
    protected final Int2ObjectMap<BlockState> bedrockToJavaBlockMap = new Int2ObjectOpenHashMap<>();
    protected final BiMap<String, BlockState> javaIdBlockMap = HashBiMap.create();
    protected final IntSet waterlogged = new IntOpenHashSet();
    protected final Object2IntMap<CompoundTag> itemFrames = new Object2IntOpenHashMap<>();

    // Bedrock carpet ID, used in LlamaEntity.java for decoration
    public final int carpet = 171;

    protected final Map<BlockState, String> javaIdToBlockEntityMap = new HashMap<>();

    public final Int2DoubleMap javaRuntimeIdToHardness = new Int2DoubleOpenHashMap();
    public final Int2BooleanMap javaRuntimeIdToCanHarvestWithHand = new Int2BooleanOpenHashMap();
    public final Int2ObjectMap<String> javaRuntimeIdToToolType = new Int2ObjectOpenHashMap<>();

    // For block breaking animation math
    public final IntSet javaRuntimeWoolIds = new IntOpenHashSet();
    public int javaRuntimeCobwebId;

    private final int blockStateVersion = 17760256;

    private final GeyserEdition edition;


    public BlockTranslator(GeyserEdition edition) {
        this.edition = edition;
    }

    public void setup() {
        /* Load block palette */
        InputStream stream = GeyserEdition.TOOLBOX.getResource("data/runtime_block_states.dat");

        ListTag<CompoundTag> blocksTag;
        try (NBTInputStream nbtInputStream = NbtUtils.createNetworkReader(stream)) {
            blocksTag = (ListTag<CompoundTag>) nbtInputStream.readTag();
        } catch (Exception e) {
            throw new AssertionError("Unable to get blocks from runtime block states", e);
        }

        Map<CompoundTag, CompoundTag> blockStateMap = new HashMap<>();

        for (CompoundTag tag : blocksTag.getValue()) {
            CompoundTagBuilder tagBuilder = CompoundTag.builder();

            tagBuilder.tag(tag.getCompound("block"));

            if (tag.getShort("meta", (short) -1) != -1) {
                tagBuilder.shortTag("meta", tag.getShort("meta"));
            }

            if (blockStateMap.putIfAbsent(tagBuilder.build("ref"), tag) != null) {
                throw new AssertionError("Duplicate block states in Bedrock palette");
            }
        }

        stream = GeyserEdition.TOOLBOX.getResource("mappings/blocks.json");
        JsonNode blocks;
        try {
            blocks = Toolbox.JSON_MAPPER.readTree(stream);
        } catch (Exception e) {
            throw new AssertionError("Unable to load Java block mappings", e);
        }
        Object2IntMap<CompoundTag> addedStatesMap = new Object2IntOpenHashMap<>();
        addedStatesMap.defaultReturnValue(-1);
        List<CompoundTag> paletteList = new ArrayList<>();

        int waterRuntimeId = -1;
        int javaRuntimeId = -1;
        int bedrockRuntimeId = 0;
        int cobwebRuntimeId = -1;
        Iterator<Map.Entry<String, JsonNode>> blocksIterator = blocks.fields();
        while (blocksIterator.hasNext()) {
            javaRuntimeId++;
            Map.Entry<String, JsonNode> entry = blocksIterator.next();
            String javaId = entry.getKey();
            BlockState javaBlockState = new BlockState(javaRuntimeId);
            CompoundTag blockTag = buildBedrockState(entry.getValue());

            // TODO fix this, (no block should have a null hardness)
            JsonNode hardnessNode = entry.getValue().get("block_hardness");
            if (hardnessNode != null) {
                javaRuntimeIdToHardness.put(javaRuntimeId, hardnessNode.doubleValue());
            }

            javaRuntimeIdToCanHarvestWithHand.put(javaRuntimeId, entry.getValue().get("can_break_with_hand").booleanValue());

            JsonNode toolTypeNode = entry.getValue().get("tool_type");
            if (toolTypeNode != null) {
                javaRuntimeIdToToolType.put(javaRuntimeId, toolTypeNode.textValue());
            }

            if (javaId.contains("wool")) {
                javaRuntimeWoolIds.add(javaRuntimeId);
            }

            if (javaId.contains("cobweb")) {
                cobwebRuntimeId = javaRuntimeId;
            }

            javaIdBlockMap.put(javaId, javaBlockState);

            // Used for adding all "special" Java block states to block state map
            String identifier;
            String bedrock_identifer = entry.getValue().get("bedrock_identifier").asText();
            for (BlockEntityTranslator translator : GeyserEdition.TRANSLATORS.getBlockEntityTranslators().values()) {
                identifier = translator.getClass().getAnnotation(BlockEntity.class).regex();
                // Endswith, or else the block bedrock gets picked up for bed
                if (bedrock_identifer.endsWith(identifier) && !identifier.equals("")) {
                    javaIdToBlockEntityMap.put(javaBlockState, translator.getClass().getAnnotation(BlockEntity.class).name());
                    break;
                }
            }

            BlockStateValues.storeBlockStateValues(entry, javaBlockState);

            // Get the tag needed for non-empty flower pots
            if (entry.getValue().get("pottable") != null) {
                BlockStateValues.getFlowerPotBlocks().put(entry.getKey().split("\\[")[0], buildBedrockState(entry.getValue()));
            }

            if ("minecraft:water[level=0]" .equals(javaId)) {
                waterRuntimeId = bedrockRuntimeId;
            }
            boolean waterlogged = entry.getKey().contains("waterlogged=true")
                    || javaId.contains("minecraft:bubble_column") || javaId.contains("minecraft:kelp") || javaId.contains("seagrass");

            if (waterlogged) {
                bedrockToJavaBlockMap.putIfAbsent(bedrockRuntimeId | 1 << 31, javaBlockState);
                this.waterlogged.add(javaRuntimeId);
            } else {
                bedrockToJavaBlockMap.putIfAbsent(bedrockRuntimeId, javaBlockState);
            }

            CompoundTag runtimeTag = blockStateMap.remove(blockTag);
            if (runtimeTag != null) {
                addedStatesMap.put(blockTag, bedrockRuntimeId);
                paletteList.add(runtimeTag);
            } else {
                int duplicateRuntimeId = addedStatesMap.getOrDefault(blockTag, -1);
                if (duplicateRuntimeId == -1) {
                    GeyserConnector.getInstance().getLogger().debug("Mapping " + javaId + " was not found for bedrock edition!");
                } else {
                    javaToBedrockBlockMap.put(javaRuntimeId, duplicateRuntimeId);
                }
                continue;
            }
            javaToBedrockBlockMap.put(javaRuntimeId, bedrockRuntimeId);

            bedrockRuntimeId++;
        }

        if (cobwebRuntimeId == -1) {
            throw new AssertionError("Unable to find cobwebs in palette");
        }
        javaRuntimeCobwebId = cobwebRuntimeId;

        if (waterRuntimeId == -1) {
            throw new AssertionError("Unable to find water in palette");
        }
        bedrockWaterId = waterRuntimeId;

        paletteList.addAll(blockStateMap.values()); // Add any missing mappings that could crash the client

        // Loop around again to find all item frame runtime IDs
        int frameRuntimeId = 0;
        for (CompoundTag tag : paletteList) {
            CompoundTag blockTag = tag.getCompound("block");
            if (blockTag.getString("name").equals("minecraft:frame")) {
                itemFrames.put(tag, frameRuntimeId);
            }
            frameRuntimeId++;
        }

        this.blocks = new ListTag<>("", CompoundTag.class, paletteList);
    }

    public int getBedrockBlockId(BlockState state) {
        return javaToBedrockBlockMap.get(state.getId());
    }

    public int getBedrockBlockId(int javaId) {
        return javaToBedrockBlockMap.get(javaId);
    }

    public BlockState getJavaBlockState(int bedrockId) {
        return bedrockToJavaBlockMap.get(bedrockId);
    }

    public int getItemFrame(CompoundTag tag) {
        return itemFrames.getOrDefault(tag, -1);
    }

    public boolean isItemFrame(int bedrockBlockRuntimeId) {
        return itemFrames.values().contains(bedrockBlockRuntimeId);
    }

    public int getBlockStateVersion() {
        return blockStateVersion;
    }

    public BlockState getJavaBlockState(String javaId) {
        return javaIdBlockMap.get(javaId);
    }

    public String getBlockEntityString(BlockState javaId) {
        return javaIdToBlockEntityMap.get(javaId);
    }

    public boolean isWaterlogged(BlockState state) {
        return waterlogged.contains(state.getId());
    }

    public BiMap<String, BlockState> getJavaIdBlockMap() {
        return javaIdBlockMap;
    }

    public BlockState getJavaWaterloggedState(int bedrockId) {
        return bedrockToJavaBlockMap.get(1 << 31 | bedrockId);
    }

    protected CompoundTag buildBedrockState(JsonNode node) {
        CompoundTagBuilder tagBuilder = CompoundTag.builder();
        tagBuilder.stringTag("name", node.get("bedrock_identifier").textValue());

        tagBuilder.intTag("version", getBlockStateVersion());

        CompoundTagBuilder statesBuilder = CompoundTag.builder();

        // check for states
        if (node.has("bedrock_states")) {
            Iterator<Map.Entry<String, JsonNode>> statesIterator = node.get("bedrock_states").fields();

            while (statesIterator.hasNext()) {
                Map.Entry<String, JsonNode> stateEntry = statesIterator.next();
                JsonNode stateValue = stateEntry.getValue();
                switch (stateValue.getNodeType()) {
                    case BOOLEAN:
                        statesBuilder.booleanTag(stateEntry.getKey(), stateValue.booleanValue());
                        continue;
                    case STRING:
                        statesBuilder.stringTag(stateEntry.getKey(), stateValue.textValue());
                        continue;
                    case NUMBER:
                        statesBuilder.intTag(stateEntry.getKey(), stateValue.intValue());
                }
            }
        }

        tagBuilder.tag(statesBuilder.build("states"));

        tagBuilder = CompoundTagBuilder.builder()
                .tag(tagBuilder.build("block"));

        return tagBuilder.build("ref");
    }
}
