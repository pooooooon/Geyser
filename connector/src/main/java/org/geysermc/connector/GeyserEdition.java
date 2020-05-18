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

package org.geysermc.connector;

import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import lombok.Getter;
import org.geysermc.connector.network.translators.Translators;
import org.geysermc.connector.utils.BlockEntityUtils;
import org.geysermc.connector.utils.BlockUtils;
import org.geysermc.connector.utils.ChunkUtils;
import org.geysermc.connector.utils.DimensionUtils;
import org.geysermc.connector.utils.EffectUtils;
import org.geysermc.connector.utils.IEntityUtils;
import org.geysermc.connector.utils.InventoryUtils;
import org.geysermc.connector.utils.ItemUtils;
import org.geysermc.connector.utils.LoginEncryptionUtils;
import org.geysermc.connector.utils.MessageUtils;
import org.geysermc.connector.utils.SkinProvider;
import org.geysermc.connector.utils.SkinUtils;
import org.geysermc.connector.utils.SoundUtils;
import org.geysermc.connector.utils.Toolbox;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class GeyserEdition {

    private static final Map<String, Map<String, Class<? extends GeyserEdition>>> editions = new HashMap<>();
    public static Translators TRANSLATORS;
    public static Toolbox TOOLBOX;
    public static EffectUtils EFFECT_UTILS;
    // -- Convenience -- //
    public static BlockEntityUtils BLOCK_ENTITY_UTILS;
    public static SoundUtils SOUND_UTILS;
    public static BlockUtils BLOCK_UTILS;
    public static ChunkUtils CHUNK_UTILS;
    public static InventoryUtils INVENTORY_UTILS;
    public static DimensionUtils DIMENSION_UTILS;
    public static IEntityUtils ENTITY_UTILS;
    public static ItemUtils ITEM_UTILS;
    public static LoginEncryptionUtils LOGIN_ENCRYPTION_UTILS;
    public static MessageUtils MESSAGE_UTILS;
    public static SkinProvider SKIN_PROVIDER;
    public static SkinUtils SKIN_UTILS;
    private static GeyserEdition INSTANCE;
    private final GeyserConnector connector;
    private final String edition;
    private final String version;
    protected BedrockPacketCodec codec;
    protected String pongEdition;

    protected GeyserEdition(GeyserConnector connector, String edition, String version) {
        INSTANCE = this;
        this.connector = connector;
        this.edition = edition;
        this.version = version;
    }

    public GeyserEdition(GeyserConnector connector) {
        throw new NotImplementedException();
    }

    public static void registerEdition(String edition, String version, Class<? extends GeyserEdition> cls) {
        if (!editions.containsKey(edition)) {
            editions.put(edition, new HashMap<>());
        }
        editions.get(edition).put(version, cls);
    }

    /**
     * Create a new GeyserEdition instance
     */
    public static GeyserEdition create(GeyserConnector connector, String edition, String version) throws InvalidEditionException {
        if (!editions.containsKey(edition)) {
            throw new InvalidEditionException("Invalid Edition: " + edition);
        }

        if (!editions.get(edition).containsKey(version)) {
            throw new InvalidEditionException("Invalid Version: " + version);
        }

        try {
            return editions.get(edition).get(version).getConstructor(GeyserConnector.class).newInstance(connector);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new InvalidEditionException("Unable to create Edition: " + edition + " version " + version, e);
        }
    }

    public static class InvalidEditionException extends Exception {
        public InvalidEditionException(String message) {
            super(message);
        }

        public InvalidEditionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
