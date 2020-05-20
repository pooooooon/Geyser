package org.geysermc.connector.utils;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.packet.BlockEntityDataPacket;
import lombok.Getter;
import org.geysermc.connector.GeyserEdition;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.world.block.entity.BlockEntityTranslator;

@Getter
public class BlockEntityUtils {

    private BlockEntityTranslator emptyTranslator;
    private GeyserEdition edition;

    public BlockEntityUtils(GeyserEdition edition) {
        this.edition = edition;
    }

    public void setup() {
        emptyTranslator = GeyserEdition.TRANSLATORS.getBlockEntityTranslators().get("Empty");
    }

    public String getBedrockBlockEntityId(String id) {
        // These are the only exceptions when it comes to block entity ids
        if (id.contains("piston_head"))
            return "PistonArm";

        if (id.contains("trapped_chest"))
            return "Chest";

        if (id.contains("EnderChest"))
            return "EnderChest";

        if (id.contains("enchanting_table")) {
            return "EnchantTable";
        }

        id = id.toLowerCase()
            .replace("minecraft:", "")
            .replace("_", " ");
        String[] words = id.split(" ");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
        }

        id = String.join(" ", words);
        return id.replace(" ", "");
    }

    public BlockEntityTranslator getBlockEntityTranslator(String name) {
        BlockEntityTranslator blockEntityTranslator = GeyserEdition.TRANSLATORS.getBlockEntityTranslators().get(name);
        if (blockEntityTranslator == null) {
            return emptyTranslator;
        }

        return blockEntityTranslator;
    }

    public void updateBlockEntity(GeyserSession session, com.nukkitx.nbt.tag.CompoundTag blockEntity, Position position) {
        updateBlockEntity(session, blockEntity, Vector3i.from(position.getX(), position.getY(), position.getZ()));
    }

    public void updateBlockEntity(GeyserSession session, com.nukkitx.nbt.tag.CompoundTag blockEntity, Vector3i position) {
        BlockEntityDataPacket blockEntityPacket = new BlockEntityDataPacket();
        blockEntityPacket.setBlockPosition(position);
        blockEntityPacket.setData(blockEntity);
        session.sendUpstreamPacket(blockEntityPacket);
    }
}
