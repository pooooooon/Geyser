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

package org.geysermc.connector.network.translators;

import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.github.steveice10.packetlib.packet.Packet;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.nbt.stream.NBTOutputStream;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.GeyserEdition;
import org.geysermc.connector.network.translators.block.entity.RequiresBlockState;
import org.geysermc.connector.network.translators.inventory.InventoryTranslator;
import org.geysermc.connector.network.translators.item.ItemTranslator;
import org.geysermc.connector.network.translators.sound.SoundHandlerRegistry;
import org.geysermc.connector.network.translators.sound.SoundInteractionHandler;
import org.geysermc.connector.network.translators.world.block.AbstractBlockTranslator;
import org.geysermc.connector.network.translators.world.block.entity.BlockEntityTranslator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Translators {

    private static final CompoundTag EMPTY_TAG = CompoundTagBuilder.builder().buildRootTag();
    private final Registry<Packet> javaTranslators = new Registry<>();
    private final Registry<BedrockPacket> bedrockTranslators = new Registry<>();
    private final byte[] emptyLevelChunkData;
    private final ItemTranslator itemTranslator;

    private final Map<WindowType, InventoryTranslator> inventoryTranslators = new HashMap<>();

    private final SoundHandlerRegistry soundHandlerRegistry;
    private final GeyserEdition edition;
    private final ObjectArrayList<RequiresBlockState> requiresBlockStateMap = new ObjectArrayList<>();
    private final Map<String, BlockEntityTranslator> blockEntityTranslators = new HashMap<>();
    protected AbstractBlockTranslator blockTranslator;

    public Translators(GeyserEdition edition) {
        this.edition = edition;
        this.itemTranslator = new ItemTranslator(edition);
        this.soundHandlerRegistry = new SoundHandlerRegistry(edition);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(new byte[258]); // Biomes + Border Size + Extra Data Size

            try (NBTOutputStream stream = NbtUtils.createNetworkWriter(outputStream)) {
                stream.write(EMPTY_TAG);
            }

            emptyLevelChunkData = outputStream.toByteArray();
        } catch (IOException e) {
            throw new AssertionError("Unable to generate empty level chunk data");
        }
    }

    public Translators registerJavaPacketTranslator(Class<? extends Packet> packet, PacketTranslator<? extends Packet> translator) {
        javaTranslators.register(packet, translator);
        return this;
    }

    public Translators registerBedrockPacketTranslator(Class<? extends BedrockPacket> packet, PacketTranslator<? extends BedrockPacket> translator) {
        bedrockTranslators.register(packet, translator);
        return this;
    }

    public Translators registerInventoryTranslator(WindowType windowType, InventoryTranslator translator) {
        inventoryTranslators.put(windowType, translator);

        if (RequiresBlockState.class.isAssignableFrom(translator.getClass())) {
            GeyserConnector.getInstance().getLogger().debug("Found block entity that requires block state: " + translator.getClass().getCanonicalName());

            requiresBlockStateMap.add((RequiresBlockState) translator);
        }
        return this;
    }

    public Translators registerNbtItemStackTranslator(NbtItemStackTranslator translator) {
        itemTranslator.registerNbtItemStackTranslator(translator);
        return this;
    }

    public Translators registerItemStackTranslator(ItemStackTranslator translator) {
        itemTranslator.registerItemStackTranslator(translator);
        return this;
    }

    public Translators registerSoundInteractionHandler(SoundInteractionHandler<?> handler) {
        soundHandlerRegistry.registerSoundInteractionHandler(handler);
        return this;
    }

    public Translators registerBlockTranslator(AbstractBlockTranslator blockTranslator) {
        this.blockTranslator = blockTranslator;
        return this;
    }

    public Translators registerBlockEntityTranslator(String name, BlockEntityTranslator translator) {
        blockEntityTranslators.put(name, translator);
        return this;
    }


}
