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

package org.geysermc.connector.network.translators.item;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.nukkitx.protocol.bedrock.data.ItemData;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.GeyserEdition;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.ItemStackTranslator;
import org.geysermc.connector.network.translators.NbtItemStackTranslator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemTranslator {

    private Int2ObjectMap<ItemStackTranslator> itemTranslators = new Int2ObjectOpenHashMap();
    private final GeyserEdition edition;
    private Map<String, ItemEntry> javaIdentifierMap = new HashMap<>();

    // Shield ID, used in Entity.java
    public static final int SHIELD = 829;
    private List<NbtItemStackTranslator> nbtItemTranslators = new ArrayList<>();

    public ItemTranslator(GeyserEdition edition) {
        this.edition = edition;
    }

    public void registerNbtItemStackTranslator(NbtItemStackTranslator translator) {
        nbtItemTranslators.add(translator);
    }

    public void registerItemStackTranslator(ItemStackTranslator translator) {
        List<ItemEntry> appliedItems = translator.getAppliedItems();
        for (ItemEntry item : appliedItems) {
            ItemStackTranslator registered = itemTranslators.get(item.getJavaId());
            if (registered != null) {
                GeyserConnector.getInstance().getLogger().error("Could not instantiate item translator " + translator.getClass().getCanonicalName() + "." +
                        " Item translator " + registered.getClass().getCanonicalName() + " is already registered for the item " + item.getJavaIdentifier());
                continue;
            }
            itemTranslators.put(item.getJavaId(), translator);
        }
    }

    public ItemStack translateToJava(GeyserSession session, ItemData data) {
        if (data == null) {
            return new ItemStack(0);
        }
        ItemEntry javaItem = getItem(data);

        ItemStack itemStack;
        ItemStackTranslator itemStackTranslator = itemTranslators.get(javaItem.getJavaId());
        if (itemStackTranslator != null) {
            itemStack = itemStackTranslator.translateToJava(data, javaItem);
        } else {
            itemStack = DEFAULT_TRANSLATOR.translateToJava(data, javaItem);
        }

        if (itemStack != null && itemStack.getNbt() != null) {
            for (NbtItemStackTranslator translator : nbtItemTranslators) {
                if (translator.acceptItem(javaItem)) {
                    translator.translateToJava(itemStack.getNbt(), javaItem);
                }
            }
        }
        return itemStack;
    }

    public ItemData translateToBedrock(GeyserSession session, ItemStack stack) {
        if (stack == null) {
            return ItemData.AIR;
        }

        ItemEntry bedrockItem = getItem(stack);

        ItemStack itemStack = new ItemStack(stack.getId(), stack.getAmount(), stack.getNbt() != null ? stack.getNbt().clone() : null);

        if (itemStack.getNbt() != null) {
            for (NbtItemStackTranslator translator : nbtItemTranslators) {
                if (translator.acceptItem(bedrockItem)) {
                    translator.translateToBedrock(itemStack.getNbt(), bedrockItem);
                }
            }
        }

        ItemStackTranslator itemStackTranslator = itemTranslators.get(bedrockItem.getJavaId());
        if (itemStackTranslator != null) {
            return itemStackTranslator.translateToBedrock(itemStack, bedrockItem);
        } else {
            return DEFAULT_TRANSLATOR.translateToBedrock(itemStack, bedrockItem);
        }
    }

    public ItemEntry getItem(ItemStack stack) {
        return GeyserEdition.TOOLBOX.getItemEntries().get(stack.getId());
    }

    public ItemEntry getItem(ItemData data) {
        for (ItemEntry itemEntry : GeyserEdition.TOOLBOX.getItemEntries().values()) {
            if (itemEntry.getBedrockId() == data.getId() && (itemEntry.getBedrockData() == data.getDamage() || itemEntry.getJavaIdentifier().endsWith("potion"))) {
                return itemEntry;
            }
        }
        // If item find was unsuccessful first time, we try again while ignoring damage
        // Fixes piston, sticky pistons, dispensers and droppers turning into air from creative inventory
        for (ItemEntry itemEntry : GeyserEdition.TOOLBOX.getItemEntries().values()) {
            if (itemEntry.getBedrockId() == data.getId()) {
                return itemEntry;
            }
        }

        GeyserConnector.getInstance().getLogger().debug("Missing mapping for bedrock item " + data.getId() + ":" + data.getDamage());
        return ItemEntry.AIR;
    }

    public ItemEntry getItemEntry(String javaIdentifier) {
        return javaIdentifierMap.computeIfAbsent(javaIdentifier, key -> GeyserEdition.TOOLBOX.getItemEntries().values()
                .stream().filter(itemEntry -> itemEntry.getJavaIdentifier().equals(key)).findFirst().orElse(null));
    }

    private static final ItemStackTranslator DEFAULT_TRANSLATOR = new ItemStackTranslator() {
        @Override
        public List<ItemEntry> getAppliedItems() {
            return null;
        }
    };
}
