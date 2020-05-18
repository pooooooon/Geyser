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

package org.geysermc.connector.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.nbt.stream.NBTInputStream;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.GeyserEdition;
import org.geysermc.connector.network.translators.item.ItemEntry;
import org.geysermc.connector.network.translators.item.ToolItemEntry;

import java.io.*;
import java.util.*;

@Getter
public class Toolbox {

    public static final ObjectMapper JSON_MAPPER = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

    private final CompoundTag biomes;
    private final ItemData[] creativeItems;

    private final List<StartGamePacket.ItemEntry> items = new ArrayList<>();

    private final Int2ObjectMap<ItemEntry> itemEntries = new Int2ObjectOpenHashMap<>();
    private final GeyserEdition edition;
    private CompoundTag entityIdentifiers;
    private int barrierIndex = 0;

    public Toolbox(GeyserEdition edition) {
        this.edition = edition;

        /* Load biomes */
        InputStream biomestream = getResource("data/biome_definitions.dat");
        if (biomestream == null) {
            throw new AssertionError("Unable to load biome definitions");
        }

        CompoundTag biomesTag;

        try (NBTInputStream biomenbtInputStream = NbtUtils.createNetworkReader(biomestream)) {
            biomesTag = (CompoundTag) biomenbtInputStream.readTag();
            biomes = biomesTag;
        } catch (Exception ex) {
            GeyserConnector.getInstance().getLogger().warning("Failed to get biomes from biome definitions, is there something wrong with the file?");
            throw new AssertionError(ex);
        }

        /* Load item palette */
        InputStream stream = getResource("data/items.json");

        TypeReference<List<JsonNode>> itemEntriesType = new TypeReference<List<JsonNode>>() {
        };

        List<JsonNode> itemEntries;
        try {
            itemEntries = JSON_MAPPER.readValue(stream, itemEntriesType);
        } catch (Exception e) {
            throw new AssertionError("Unable to load Bedrock runtime item IDs", e);
        }

        for (JsonNode entry : itemEntries) {
            items.add(new StartGamePacket.ItemEntry(entry.get("name").textValue(), (short) entry.get("id").intValue()));
        }

        stream = getResource("mappings/items.json");

        JsonNode items;
        try {
            items = JSON_MAPPER.readTree(stream);
        } catch (Exception e) {
            throw new AssertionError("Unable to load Java runtime item IDs", e);
        }

        int itemIndex = 0;
        Iterator<Map.Entry<String, JsonNode>> iterator = items.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            if (entry.getValue().has("tool_type")) {
                if (entry.getValue().has("tool_tier")) {
                    this.itemEntries.put(itemIndex, new ToolItemEntry(
                            entry.getKey(), itemIndex,
                            entry.getValue().get("bedrock_id").intValue(),
                            entry.getValue().get("bedrock_data").intValue(),
                            entry.getValue().get("tool_type").textValue(),
                            entry.getValue().get("tool_tier").textValue(),
                            entry.getValue().get("is_block").booleanValue()));
                } else {
                    this.itemEntries.put(itemIndex, new ToolItemEntry(
                            entry.getKey(), itemIndex,
                            entry.getValue().get("bedrock_id").intValue(),
                            entry.getValue().get("bedrock_data").intValue(),
                            entry.getValue().get("tool_type").textValue(),
                            "",
                            entry.getValue().get("is_block").booleanValue()));
                }
            } else {
                this.itemEntries.put(itemIndex, new ItemEntry(
                        entry.getKey(), itemIndex,
                        entry.getValue().get("bedrock_id").intValue(),
                        entry.getValue().get("bedrock_data").intValue(),
                        entry.getValue().get("is_block").booleanValue()));
            }
            if (entry.getKey().equals("minecraft:barrier")) {
                barrierIndex = itemIndex;
            }

            itemIndex++;
        }

        // Load the locale data
        LocaleUtils.init();

        /* Load creative items */
        stream = getResource("data/creative_items.json");

        JsonNode creativeItemEntries;
        try {
            creativeItemEntries = JSON_MAPPER.readTree(stream).get("items");
        } catch (Exception e) {
            throw new AssertionError("Unable to load creative items", e);
        }

        List<ItemData> creativeItems = new ArrayList<>();
        for (JsonNode itemNode : creativeItemEntries) {
            short damage = 0;
            if (itemNode.has("damage")) {
                damage = itemNode.get("damage").numberValue().shortValue();
            }
            if (itemNode.has("nbt_b64")) {
                byte[] bytes = Base64.getDecoder().decode(itemNode.get("nbt_b64").asText());
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                try {
                    com.nukkitx.nbt.tag.CompoundTag tag = (com.nukkitx.nbt.tag.CompoundTag) NbtUtils.createReaderLE(bais).readTag();
                    creativeItems.add(ItemData.of(itemNode.get("id").asInt(), damage, 1, tag));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                creativeItems.add(ItemData.of(itemNode.get("id").asInt(), damage, 1));
            }
        }
        this.creativeItems = creativeItems.toArray(new ItemData[0]);


        /* Load entity identifiers */
        stream = getResource("data/entity_identifiers.dat");

        try (NBTInputStream nbtInputStream = NbtUtils.createNetworkReader(stream)) {
            entityIdentifiers = (CompoundTag) nbtInputStream.readTag();
        } catch (Exception e) {
            throw new AssertionError("Unable to get entities from entity identifiers", e);
        }
    }

    /**
     * Get an InputStream for the given resource path, throws AssertionError if resource is not found
     *
     * @param resource Resource to get
     * @return InputStream of the given resource
     */
    public InputStream getResource(String resource) {
        String resourceName = edition.getEdition().replace(".", "_") + "/" + edition.getVersion().replace(".", "_") + "/" + resource;
        InputStream stream = Toolbox.class.getClassLoader().getResourceAsStream(resourceName);
        if (stream == null) {
            throw new AssertionError("Unable to find resource: " + resourceName);
        }
        return stream;
    }
}