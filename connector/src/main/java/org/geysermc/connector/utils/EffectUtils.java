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

import com.fasterxml.jackson.databind.JsonNode;
import com.github.steveice10.mc.protocol.data.game.world.particle.ParticleType;
import com.nukkitx.protocol.bedrock.data.LevelEventType;
import com.nukkitx.protocol.bedrock.data.SoundEvent;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import lombok.Getter;
import lombok.NonNull;

import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.GeyserEdition;
import org.geysermc.connector.network.translators.effect.Effect;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Getter
public class EffectUtils {

    public final Map<String, Effect> effects = new HashMap<>();
    public final Int2ObjectMap<SoundEvent> records = new Int2ObjectOpenHashMap<>();

    private Map<ParticleType, LevelEventType> particleTypeMap = new HashMap<>();
    private Map<ParticleType, String> particleStringMap = new HashMap<>();

    private GeyserEdition edition;

    public EffectUtils(GeyserEdition edition) {
        this.edition = edition;
    }

    public void setup() {
        /* Load particles */
        InputStream particleStream = GeyserEdition.TOOLBOX.getResource("mappings/particles.json");
        JsonNode particleEntries;
        try {
            particleEntries = Toolbox.JSON_MAPPER.readTree(particleStream);
        } catch (Exception e) {
            throw new AssertionError("Unable to load particle map", e);
        }

        Iterator<Map.Entry<String, JsonNode>> particlesIterator = particleEntries.fields();
        while (particlesIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = particlesIterator.next();
            try {
                setIdentifier(ParticleType.valueOf(entry.getKey().toUpperCase()), LevelEventType.valueOf(entry.getValue().asText().toUpperCase()));
            } catch (IllegalArgumentException e1) {
                try {
                    setIdentifier(ParticleType.valueOf(entry.getKey().toUpperCase()), entry.getValue().asText());
                    GeyserConnector.getInstance().getLogger().debug("Force to map particle "
                            + entry.getKey()
                            + "=>"
                            + entry.getValue().asText()
                            + ", it will take effect.");
                } catch (IllegalArgumentException e2){
                    GeyserConnector.getInstance().getLogger().warning("Fail to map particle " + entry.getKey() + "=>" + entry.getValue().asText());
                }
            }
        }

        /* Load effects */
        InputStream effectsStream = GeyserEdition.TOOLBOX.getResource("mappings/effects.json");
        JsonNode effects;
        try {
            effects = Toolbox.JSON_MAPPER.readTree(effectsStream);
        } catch (Exception e) {
            throw new AssertionError("Unable to load effects mappings", e);
        }

        Iterator<Map.Entry<String, JsonNode>> effectsIterator = effects.fields();
        while (effectsIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = effectsIterator.next();
            // Separate records database since they're handled differently between the two versions
            if (entry.getValue().has("records")) {
                JsonNode records = entry.getValue().get("records");
                Iterator<Map.Entry<String, JsonNode>> recordsIterator = records.fields();
                while (recordsIterator.hasNext()) {
                    Map.Entry<String, JsonNode> recordEntry = recordsIterator.next();
                    this.records.put(Integer.parseInt(recordEntry.getKey()), SoundEvent.valueOf(recordEntry.getValue().asText()));
                }
            }
            String identifier = (entry.getValue().has("identifier")) ? entry.getValue().get("identifier").asText() : "";
            int data = (entry.getValue().has("data")) ? entry.getValue().get("data").asInt() : -1;
            Effect effect = new Effect(entry.getKey(), entry.getValue().get("name").asText(), entry.getValue().get("type").asText(), data, identifier);
            this.effects.put(entry.getKey(), effect);
        }
    }

    public void setIdentifier(ParticleType type, LevelEventType identifier) {
        particleTypeMap.put(type, identifier);
    }

    public void setIdentifier(ParticleType type, String identifier) {
        particleStringMap.put(type, identifier);
    }

    public LevelEventType getParticleLevelEventType(@NonNull ParticleType type) {
        return particleTypeMap.getOrDefault(type, null);
    }

    public String getParticleString(@NonNull ParticleType type) {
        return particleStringMap.getOrDefault(type, null);
    }

}
