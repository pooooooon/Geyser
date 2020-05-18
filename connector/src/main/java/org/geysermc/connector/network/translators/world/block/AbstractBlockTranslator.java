/*
 * Copyright (c) 2019-2020 GeyserMC. http://geysermc.org
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 *
 *  @author GeyserMC
 *  @link https://github.com/GeyserMC/Geyser
 *
 */

package org.geysermc.connector.network.translators.world.block;

import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.nbt.tag.ListTag;
import it.unimi.dsi.fastutil.ints.Int2BooleanMap;
import it.unimi.dsi.fastutil.ints.Int2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.Getter;
import org.geysermc.connector.GeyserEdition;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class AbstractBlockTranslator {
    public final BlockState air = new BlockState(0);
    // Bedrock carpet ID, used in LlamaEntity.java for decoration
    public final int carpet = 171;
    public final Int2DoubleMap javaRuntimeIdToHardness = new Int2DoubleOpenHashMap();
    public final Int2BooleanMap javaRuntimeIdToCanHarvestWithHand = new Int2BooleanOpenHashMap();
    public final Int2ObjectMap<String> javaRuntimeIdToToolType = new Int2ObjectOpenHashMap<>();
    // For block breaking animation math
    public final IntSet javaRuntimeWoolIds = new IntOpenHashSet();
    protected final Int2IntMap javaToBedrockBlockMap = new Int2IntOpenHashMap();
    protected final Int2ObjectMap<BlockState> bedrockToJavaBlockMap = new Int2ObjectOpenHashMap<>();
    protected final BiMap<String, BlockState> javaIdBlockMap = HashBiMap.create();
    protected final IntSet waterlogged = new IntOpenHashSet();
    protected final Object2IntMap<CompoundTag> itemFrames = new Object2IntOpenHashMap<>();
    protected final Map<BlockState, String> javaIdToBlockEntityMap = new HashMap<>();
    private final int blockStateVersion = 17760256;
    private final GeyserEdition edition;
    public ListTag<CompoundTag> blocks;
    public int bedrockWaterId;
    public int javaRuntimeCobwebId;

    public AbstractBlockTranslator(GeyserEdition edition) {
        this.edition = edition;
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

}
