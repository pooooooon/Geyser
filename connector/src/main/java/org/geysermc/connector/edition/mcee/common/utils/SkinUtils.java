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

package org.geysermc.connector.edition.mcee.common.utils;

import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.GeyserEdition;
import org.geysermc.connector.entity.PlayerEntity;
import org.geysermc.connector.network.session.auth.BedrockClientData;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SkinUtils extends org.geysermc.connector.utils.SkinUtils {

    public SkinUtils(GeyserEdition edition) {
        super(edition);
    }

    public void handleBedrockSkin(PlayerEntity playerEntity, BedrockClientData clientData) {
        GameProfileData data = GameProfileData.from(playerEntity.getProfile());

        GeyserConnector.getInstance().getLogger().info("Registering bedrock skin for " + playerEntity.getUsername() + " (" + playerEntity.getUuid() + ")");

        try {
            byte[] skinBytes = Base64.getDecoder().decode(clientData.getSkinData().getBytes(StandardCharsets.UTF_8));
            byte[] capeBytes = clientData.getCapeData();

            byte[] geometryNameBytes = clientData.getLegacyGeometryName().getBytes();
            byte[] geometryBytes = Base64.getDecoder().decode(clientData.getLegacyGeometryData().getBytes(StandardCharsets.UTF_8));

            if (skinBytes.length <= (128 * 128 * 4) && !clientData.isPersonaSkin()) {
                GeyserEdition.SKIN_PROVIDER.storeBedrockSkin(playerEntity.getUuid(), data.getSkinUrl(), skinBytes);
                GeyserEdition.SKIN_PROVIDER.storeBedrockGeometry(playerEntity.getUuid(), geometryNameBytes, geometryBytes);
            } else {
                GeyserConnector.getInstance().getLogger().info("Unable to load bedrock skin for '" + playerEntity.getUsername() + "' as they are likely using a customised skin");
                GeyserConnector.getInstance().getLogger().debug("The size of '" + playerEntity.getUsername() + "' skin is: " + clientData.getSkinImageWidth() + "x" + clientData.getSkinImageHeight());
            }

            if (capeBytes.length != 0) {
                GeyserEdition.SKIN_PROVIDER.storeBedrockCape(playerEntity.getUuid(), capeBytes);
            }
        } catch (Exception e) {
            throw new AssertionError("Failed to cache skin for bedrock user (" + playerEntity.getUsername() + "): ", e);
        }
    }
}
