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

package org.geysermc.connector.edition.mcee.v1_12_60;

import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerBossBarPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDeclareCommandsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDeclareRecipesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDifficultyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPluginMessagePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerStopSoundPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTitlePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityAnimationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityCollectItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEquipmentPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRemoveEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityStatusPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerActionAckPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerChangeHeldItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerSetExperiencePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnGlobalEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerDisplayScoreboardPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerScoreboardObjectivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerTeamPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerUpdateScorePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerCloseWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerConfirmTransactionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowPropertyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockBreakAnimPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockValuePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerExplosionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMapDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayBuiltinSoundPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnParticlePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUnloadChunkPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateViewDistancePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateViewPositionPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginPluginRequestPacket;
import com.nukkitx.protocol.bedrock.data.ContainerType;
import com.nukkitx.protocol.bedrock.packet.AnimatePacket;
import com.nukkitx.protocol.bedrock.packet.BlockEntityDataPacket;
import com.nukkitx.protocol.bedrock.packet.BlockPickRequestPacket;
import com.nukkitx.protocol.bedrock.packet.CommandRequestPacket;
import com.nukkitx.protocol.bedrock.packet.ContainerClosePacket;
import com.nukkitx.protocol.bedrock.packet.EntityEventPacket;
import com.nukkitx.protocol.bedrock.packet.InteractPacket;
import com.nukkitx.protocol.bedrock.packet.InventoryTransactionPacket;
import com.nukkitx.protocol.bedrock.packet.ItemFrameDropItemPacket;
import com.nukkitx.protocol.bedrock.packet.LevelSoundEventPacket;
import com.nukkitx.protocol.bedrock.packet.MobEquipmentPacket;
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket;
import com.nukkitx.protocol.bedrock.packet.PlayerActionPacket;
import com.nukkitx.protocol.bedrock.packet.RespawnPacket;
import com.nukkitx.protocol.bedrock.packet.SetLocalPlayerAsInitializedPacket;
import com.nukkitx.protocol.bedrock.packet.ShowCreditsPacket;
import com.nukkitx.protocol.bedrock.packet.TextPacket;
import com.nukkitx.protocol.bedrock.v363.Bedrock_v363;
import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.GeyserEdition;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockActionTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockAnimateTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockBlockEntityDataTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockBlockPickRequestPacketTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockCommandRequestTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockContainerCloseTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockEntityEventTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockInteractTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockInventoryTransactionTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockItemFrameDropItemTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockLevelSoundEventTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockMobEquipmentTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockMovePlayerTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockRespawnTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockSetLocalPlayerAsInitializedTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockShowCreditsTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.bedrock.BedrockTextTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.item.translators.BannerTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.item.translators.PotionTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.item.translators.nbt.BasicItemTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.item.translators.nbt.BookPagesTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.item.translators.nbt.EnchantedBookTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.item.translators.nbt.EnchantmentTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.item.translators.nbt.LeatherArmorTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.item.translators.nbt.MapItemTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.JavaBossBarTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.JavaChatTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.JavaDeclareRecipesTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.JavaDifficultyTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.JavaJoinGameTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.JavaLoginPluginMessageTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.JavaPluginMessageTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.JavaRespawnTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.JavaServerDeclareCommandsTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.JavaTitleTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.JavaEntityAnimationTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.JavaEntityDestroyTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.JavaEntityEffectTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.JavaEntityEquipmentTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.JavaEntityHeadLookTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.JavaEntityMetadataTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.JavaEntityPositionRotationTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.JavaEntityPositionTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.JavaEntityPropertiesTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.JavaEntityRemoveEffectTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.JavaEntityRotationTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.JavaEntityStatusTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.JavaEntityTeleportTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.JavaEntityVelocityTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.player.JavaPlayerAbilitiesTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.player.JavaPlayerActionAckTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.player.JavaPlayerChangeHeldItemTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.player.JavaPlayerHealthTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.player.JavaPlayerListEntryTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.player.JavaPlayerPositionRotationTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.player.JavaPlayerSetExperienceTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.player.JavaPlayerStopSoundTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.spawn.JavaSpawnExpOrbTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.spawn.JavaSpawnGlobalEntityTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.spawn.JavaSpawnMobTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.spawn.JavaSpawnObjectTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.spawn.JavaSpawnPaintingTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.entity.spawn.JavaSpawnPlayerTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.scoreboard.JavaDisplayScoreboardTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.scoreboard.JavaScoreboardObjectiveTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.scoreboard.JavaTeamTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.scoreboard.JavaUpdateScoreTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.window.JavaCloseWindowTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.window.JavaConfirmTransactionTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.window.JavaOpenWindowTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.window.JavaSetSlotTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.window.JavaWindowItemsTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.window.JavaWindowPropertyTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaBlockBreakAnimTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaBlockChangeTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaBlockValueTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaChunkDataTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaCollectItemTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaExplosionTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaMapDataTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaMultiBlockChangeTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaNotifyClientTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaPlayBuiltinSoundTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaPlayEffectTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaPlayerPlaySoundTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaSpawnParticleTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaSpawnPositionTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaUnloadChunkTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaUpdateTileEntityTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaUpdateTimeTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaUpdateViewDistanceTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.java.world.JavaUpdateViewPositionTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.sound.block.BucketSoundInteractionHandler;
import org.geysermc.connector.edition.mcpe.common.network.translators.sound.block.ComparatorSoundInteractHandler;
import org.geysermc.connector.edition.mcpe.common.network.translators.sound.block.DoorSoundInteractionHandler;
import org.geysermc.connector.edition.mcpe.common.network.translators.sound.block.FlintAndSteelInteractionHandler;
import org.geysermc.connector.edition.mcpe.common.network.translators.sound.block.GrassPathInteractionHandler;
import org.geysermc.connector.edition.mcpe.common.network.translators.sound.block.HoeInteractionHandler;
import org.geysermc.connector.edition.mcpe.common.network.translators.sound.block.LeverSoundInteractionHandler;
import org.geysermc.connector.edition.mcpe.common.network.translators.sound.entity.MilkCowSoundInteractionHandler;
import org.geysermc.connector.edition.mcpe.common.network.translators.world.block.BlockTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.world.block.entity.BannerBlockEntityTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.world.block.entity.BedBlockEntityTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.world.block.entity.CampfireBlockEntityTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.world.block.entity.DoubleChestBlockEntityTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.world.block.entity.EmptyBlockEntityTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.world.block.entity.EndGatewayBlockEntityTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.world.block.entity.ShulkerBoxBlockEntityTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.world.block.entity.SignBlockEntityTranslator;
import org.geysermc.connector.edition.mcpe.common.network.translators.world.block.entity.SkullBlockEntityTranslator;
import org.geysermc.connector.edition.mcpe.common.utils.EntityUtils;
import org.geysermc.connector.network.translators.Translators;
import org.geysermc.connector.network.translators.inventory.AnvilInventoryTranslator;
import org.geysermc.connector.network.translators.inventory.BlockInventoryTranslator;
import org.geysermc.connector.network.translators.inventory.BrewingInventoryTranslator;
import org.geysermc.connector.network.translators.inventory.CraftingInventoryTranslator;
import org.geysermc.connector.network.translators.inventory.DoubleChestInventoryTranslator;
import org.geysermc.connector.network.translators.inventory.FurnaceInventoryTranslator;
import org.geysermc.connector.network.translators.inventory.GrindstoneInventoryTranslator;
import org.geysermc.connector.network.translators.inventory.InventoryTranslator;
import org.geysermc.connector.network.translators.inventory.PlayerInventoryTranslator;
import org.geysermc.connector.network.translators.inventory.SingleChestInventoryTranslator;
import org.geysermc.connector.network.translators.inventory.updater.ContainerInventoryUpdater;
import org.geysermc.connector.network.translators.inventory.updater.InventoryUpdater;
import org.geysermc.connector.utils.BlockEntityUtils;
import org.geysermc.connector.utils.BlockUtils;
import org.geysermc.connector.utils.ChunkUtils;
import org.geysermc.connector.utils.DimensionUtils;
import org.geysermc.connector.utils.EffectUtils;
import org.geysermc.connector.utils.InventoryUtils;
import org.geysermc.connector.utils.ItemUtils;
import org.geysermc.connector.utils.LoginEncryptionUtils;
import org.geysermc.connector.utils.MessageUtils;
import org.geysermc.connector.utils.SkinProvider;
import org.geysermc.connector.utils.SkinUtils;
import org.geysermc.connector.utils.SoundUtils;
import org.geysermc.connector.utils.Toolbox;

@Getter
public class Edition extends GeyserEdition {

    private final String signedToken;

    public Edition(GeyserConnector connector) {
        super(connector, "education", "1.12.60");

        TOOLBOX = new Toolbox(this);
        TRANSLATORS = new Translators(this);
        EFFECT_UTILS = new EffectUtils(this);
        BLOCK_ENTITY_UTILS = new BlockEntityUtils(this);
        SOUND_UTILS = new SoundUtils(this);
        BLOCK_UTILS = new BlockUtils(this);
        CHUNK_UTILS = new ChunkUtils(this);
        INVENTORY_UTILS = new InventoryUtils(this);
        DIMENSION_UTILS = new DimensionUtils(this);
        ENTITY_UTILS = new EntityUtils(this);
        ITEM_UTILS = new ItemUtils(this);
        LOGIN_ENCRYPTION_UTILS = new LoginEncryptionUtils(this);
        MESSAGE_UTILS = new MessageUtils(this);
        SKIN_PROVIDER = new SkinProvider(this);
        SKIN_UTILS = new SkinUtils(this);

        codec = Bedrock_v363.V363_CODEC;
        pongEdition = "MCEE";
        signedToken = connector.getConfig().getBedrock().getEducation().getToken();

        TRANSLATORS = new Translators(this);

        // Register Bedrock Packet Translators
        TRANSLATORS
                .registerBedrockPacketTranslator(PlayerActionPacket.class, new BedrockActionTranslator())
                .registerBedrockPacketTranslator(AnimatePacket.class, new BedrockAnimateTranslator())
                .registerBedrockPacketTranslator(BlockEntityDataPacket.class, new BedrockBlockEntityDataTranslator())
                .registerBedrockPacketTranslator(BlockPickRequestPacket.class, new BedrockBlockPickRequestPacketTranslator())
                .registerBedrockPacketTranslator(CommandRequestPacket.class, new BedrockCommandRequestTranslator())
                .registerBedrockPacketTranslator(ContainerClosePacket.class, new BedrockContainerCloseTranslator())
                .registerBedrockPacketTranslator(EntityEventPacket.class, new BedrockEntityEventTranslator())
                .registerBedrockPacketTranslator(InteractPacket.class, new BedrockInteractTranslator())
                .registerBedrockPacketTranslator(InventoryTransactionPacket.class, new BedrockInventoryTransactionTranslator())
                .registerBedrockPacketTranslator(ItemFrameDropItemPacket.class, new BedrockItemFrameDropItemTranslator())
                .registerBedrockPacketTranslator(LevelSoundEventPacket.class, new BedrockLevelSoundEventTranslator())
                .registerBedrockPacketTranslator(MobEquipmentPacket.class, new BedrockMobEquipmentTranslator())
                .registerBedrockPacketTranslator(MovePlayerPacket.class, new BedrockMovePlayerTranslator())
                .registerBedrockPacketTranslator(RespawnPacket.class, new BedrockRespawnTranslator())
                .registerBedrockPacketTranslator(SetLocalPlayerAsInitializedPacket.class, new BedrockSetLocalPlayerAsInitializedTranslator())
                .registerBedrockPacketTranslator(ShowCreditsPacket.class, new BedrockShowCreditsTranslator())
                .registerBedrockPacketTranslator(TextPacket.class, new BedrockTextTranslator());

        // Register Java Packet Translators
        TRANSLATORS
                .registerJavaPacketTranslator(ServerBossBarPacket.class, new JavaBossBarTranslator())
                .registerJavaPacketTranslator(ServerChatPacket.class, new JavaChatTranslator())
                .registerJavaPacketTranslator(ServerDeclareRecipesPacket.class, new JavaDeclareRecipesTranslator())
                .registerJavaPacketTranslator(ServerDifficultyPacket.class, new JavaDifficultyTranslator())
                .registerJavaPacketTranslator(ServerJoinGamePacket.class, new JavaJoinGameTranslator())
                .registerJavaPacketTranslator(LoginPluginRequestPacket.class, new JavaLoginPluginMessageTranslator())
                .registerJavaPacketTranslator(ServerPluginMessagePacket.class, new JavaPluginMessageTranslator())
                .registerJavaPacketTranslator(ServerRespawnPacket.class, new JavaRespawnTranslator())
                .registerJavaPacketTranslator(ServerDeclareCommandsPacket.class, new JavaServerDeclareCommandsTranslator())
                .registerJavaPacketTranslator(ServerTitlePacket.class, new JavaTitleTranslator());

        // Register Java Entity Packet Translators
        TRANSLATORS
                .registerJavaPacketTranslator(ServerEntityAnimationPacket.class, new JavaEntityAnimationTranslator())
                .registerJavaPacketTranslator(ServerEntityDestroyPacket.class, new JavaEntityDestroyTranslator())
                .registerJavaPacketTranslator(ServerEntityEffectPacket.class, new JavaEntityEffectTranslator())
                .registerJavaPacketTranslator(ServerEntityEquipmentPacket.class, new JavaEntityEquipmentTranslator())
                .registerJavaPacketTranslator(ServerEntityHeadLookPacket.class, new JavaEntityHeadLookTranslator())
                .registerJavaPacketTranslator(ServerEntityMetadataPacket.class, new JavaEntityMetadataTranslator())
                .registerJavaPacketTranslator(ServerEntityPositionRotationPacket.class, new JavaEntityPositionRotationTranslator())
                .registerJavaPacketTranslator(ServerEntityPositionPacket.class, new JavaEntityPositionTranslator())
                .registerJavaPacketTranslator(ServerEntityPropertiesPacket.class, new JavaEntityPropertiesTranslator())
                .registerJavaPacketTranslator(ServerEntityRemoveEffectPacket.class, new JavaEntityRemoveEffectTranslator())
                .registerJavaPacketTranslator(ServerEntityRotationPacket.class, new JavaEntityRotationTranslator())
                .registerJavaPacketTranslator(ServerEntityStatusPacket.class, new JavaEntityStatusTranslator())
                .registerJavaPacketTranslator(ServerEntityTeleportPacket.class, new JavaEntityTeleportTranslator())
                .registerJavaPacketTranslator(ServerEntityVelocityPacket.class, new JavaEntityVelocityTranslator());

        // Register Java Entity Player Packet Translators
        TRANSLATORS
                .registerJavaPacketTranslator(ServerPlayerAbilitiesPacket.class, new JavaPlayerAbilitiesTranslator())
                .registerJavaPacketTranslator(ServerPlayerActionAckPacket.class, new JavaPlayerActionAckTranslator())
                .registerJavaPacketTranslator(ServerPlayerChangeHeldItemPacket.class, new JavaPlayerChangeHeldItemTranslator())
                .registerJavaPacketTranslator(ServerPlayerHealthPacket.class, new JavaPlayerHealthTranslator())
                .registerJavaPacketTranslator(ServerPlayerListEntryPacket.class, new JavaPlayerListEntryTranslator())
                .registerJavaPacketTranslator(ServerPlayerPositionRotationPacket.class, new JavaPlayerPositionRotationTranslator())
                .registerJavaPacketTranslator(ServerPlayerSetExperiencePacket.class, new JavaPlayerSetExperienceTranslator())
                .registerJavaPacketTranslator(ServerStopSoundPacket.class, new JavaPlayerStopSoundTranslator());

        // Register Java Entity Spawn Packet Translators
        TRANSLATORS
                .registerJavaPacketTranslator(ServerSpawnExpOrbPacket.class, new JavaSpawnExpOrbTranslator())
                .registerJavaPacketTranslator(ServerSpawnGlobalEntityPacket.class, new JavaSpawnGlobalEntityTranslator())
                .registerJavaPacketTranslator(ServerSpawnMobPacket.class, new JavaSpawnMobTranslator())
                .registerJavaPacketTranslator(ServerSpawnObjectPacket.class, new JavaSpawnObjectTranslator())
                .registerJavaPacketTranslator(ServerSpawnPaintingPacket.class, new JavaSpawnPaintingTranslator())
                .registerJavaPacketTranslator(ServerSpawnPlayerPacket.class, new JavaSpawnPlayerTranslator());

        // Register Java Scoreboard Packet Translators
        TRANSLATORS
                .registerJavaPacketTranslator(ServerDisplayScoreboardPacket.class, new JavaDisplayScoreboardTranslator())
                .registerJavaPacketTranslator(ServerScoreboardObjectivePacket.class, new JavaScoreboardObjectiveTranslator())
                .registerJavaPacketTranslator(ServerTeamPacket.class, new JavaTeamTranslator())
                .registerJavaPacketTranslator(ServerUpdateScorePacket.class, new JavaUpdateScoreTranslator());

        // Register Java Window Packet Translators
        TRANSLATORS
                .registerJavaPacketTranslator(ServerCloseWindowPacket.class, new JavaCloseWindowTranslator())
                .registerJavaPacketTranslator(ServerConfirmTransactionPacket.class, new JavaConfirmTransactionTranslator())
                .registerJavaPacketTranslator(ServerOpenWindowPacket.class, new JavaOpenWindowTranslator())
                .registerJavaPacketTranslator(ServerSetSlotPacket.class, new JavaSetSlotTranslator())
                .registerJavaPacketTranslator(ServerWindowItemsPacket.class, new JavaWindowItemsTranslator())
                .registerJavaPacketTranslator(ServerWindowPropertyPacket.class, new JavaWindowPropertyTranslator());

        // Register Java World Packet Translators
        TRANSLATORS
                .registerJavaPacketTranslator(ServerBlockBreakAnimPacket.class, new JavaBlockBreakAnimTranslator())
                .registerJavaPacketTranslator(ServerBlockChangePacket.class, new JavaBlockChangeTranslator())
                .registerJavaPacketTranslator(ServerBlockValuePacket.class, new JavaBlockValueTranslator())
                .registerJavaPacketTranslator(ServerChunkDataPacket.class, new JavaChunkDataTranslator())
                .registerJavaPacketTranslator(ServerEntityCollectItemPacket.class, new JavaCollectItemTranslator())
                .registerJavaPacketTranslator(ServerExplosionPacket.class, new JavaExplosionTranslator())
                .registerJavaPacketTranslator(ServerMapDataPacket.class, new JavaMapDataTranslator())
                .registerJavaPacketTranslator(ServerMultiBlockChangePacket.class, new JavaMultiBlockChangeTranslator())
                .registerJavaPacketTranslator(ServerNotifyClientPacket.class, new JavaNotifyClientTranslator())
                .registerJavaPacketTranslator(ServerPlayBuiltinSoundPacket.class, new JavaPlayBuiltinSoundTranslator())
                .registerJavaPacketTranslator(ServerPlayEffectPacket.class, new JavaPlayEffectTranslator())
                .registerJavaPacketTranslator(ServerPlaySoundPacket.class, new JavaPlayerPlaySoundTranslator())
                .registerJavaPacketTranslator(ServerSpawnParticlePacket.class, new JavaSpawnParticleTranslator())
                .registerJavaPacketTranslator(ServerSpawnPositionPacket.class, new JavaSpawnPositionTranslator())
                .registerJavaPacketTranslator(ServerUnloadChunkPacket.class, new JavaUnloadChunkTranslator())
                .registerJavaPacketTranslator(ServerUpdateTileEntityPacket.class, new JavaUpdateTileEntityTranslator())
                .registerJavaPacketTranslator(ServerUpdateTimePacket.class, new JavaUpdateTimeTranslator())
                .registerJavaPacketTranslator(ServerUpdateViewDistancePacket.class, new JavaUpdateViewDistanceTranslator())
                .registerJavaPacketTranslator(ServerUpdateViewPositionPacket.class, new JavaUpdateViewPositionTranslator());

        // Register Inventory Translators
        TRANSLATORS
                .registerInventoryTranslator(null, new PlayerInventoryTranslator())
                .registerInventoryTranslator(WindowType.GENERIC_9X1, new SingleChestInventoryTranslator(9))
                .registerInventoryTranslator(WindowType.GENERIC_9X2, new SingleChestInventoryTranslator(18))
                .registerInventoryTranslator(WindowType.GENERIC_9X3, new SingleChestInventoryTranslator(27))
                .registerInventoryTranslator(WindowType.GENERIC_9X4, new DoubleChestInventoryTranslator(36))
                .registerInventoryTranslator(WindowType.GENERIC_9X5, new DoubleChestInventoryTranslator(45))
                .registerInventoryTranslator(WindowType.GENERIC_9X6, new DoubleChestInventoryTranslator(54))
                .registerInventoryTranslator(WindowType.BREWING_STAND, new BrewingInventoryTranslator())
                .registerInventoryTranslator(WindowType.ANVIL, new AnvilInventoryTranslator())
                .registerInventoryTranslator(WindowType.CRAFTING, new CraftingInventoryTranslator())
                .registerInventoryTranslator(WindowType.GRINDSTONE, new GrindstoneInventoryTranslator());
//                .registerInventoryTranslator(WindowType.ENCHANTMENT, new EnchantmentInventoryTranslator()); //@TODO

        // Register Inventory Furnace Translators
        InventoryTranslator furnace = new FurnaceInventoryTranslator();

        TRANSLATORS
                .registerInventoryTranslator(WindowType.FURNACE, furnace)
                .registerInventoryTranslator(WindowType.BLAST_FURNACE, furnace)
                .registerInventoryTranslator(WindowType.SMOKER, furnace);

        // Register Inventory Container Translators
        InventoryUpdater containerUpdater = new ContainerInventoryUpdater();

        TRANSLATORS
                .registerInventoryTranslator(WindowType.GENERIC_3X3, new BlockInventoryTranslator(9, "minecraft:dispenser[facing=north,triggered=false]", ContainerType.DISPENSER, containerUpdater))
                .registerInventoryTranslator(WindowType.HOPPER, new BlockInventoryTranslator(5, "minecraft:hopper[enabled=false,facing=down]", ContainerType.HOPPER, containerUpdater))
                .registerInventoryTranslator(WindowType.SHULKER_BOX, new BlockInventoryTranslator(27, "minecraft:shulker_box[facing=north]", ContainerType.CONTAINER, containerUpdater));
//                .registerInventoryTranslator(WindowType.BEACON, new BlockInventoryTranslator(1, "minecraft:beacon", ContainerType.BEACON)) //@TODO

        // Register Item Translators
        TRANSLATORS
                .registerItemStackTranslator(new BannerTranslator())
                .registerItemStackTranslator(new PotionTranslator());

        // Register Item NBT Translators
        TRANSLATORS
                .registerNbtItemStackTranslator(new BasicItemTranslator())
                .registerNbtItemStackTranslator(new BookPagesTranslator())
                .registerNbtItemStackTranslator(new EnchantedBookTranslator())
                .registerNbtItemStackTranslator(new EnchantmentTranslator())
                .registerNbtItemStackTranslator(new LeatherArmorTranslator())
                .registerNbtItemStackTranslator(new MapItemTranslator());

        // Register Block Sound Handlers
        TRANSLATORS
                .registerSoundInteractionHandler(new BucketSoundInteractionHandler())
                .registerSoundInteractionHandler(new ComparatorSoundInteractHandler())
                .registerSoundInteractionHandler(new DoorSoundInteractionHandler())
                .registerSoundInteractionHandler(new FlintAndSteelInteractionHandler())
                .registerSoundInteractionHandler(new GrassPathInteractionHandler())
                .registerSoundInteractionHandler(new HoeInteractionHandler())
                .registerSoundInteractionHandler(new LeverSoundInteractionHandler());

        // Register Entity Sound Handlers
        TRANSLATORS
                .registerSoundInteractionHandler(new MilkCowSoundInteractionHandler());

        // Register Block Entity Translators
        TRANSLATORS
                .registerBlockEntityTranslator("Banner", new BannerBlockEntityTranslator())
                .registerBlockEntityTranslator("Bed", new BedBlockEntityTranslator())
                .registerBlockEntityTranslator("Campfire", new CampfireBlockEntityTranslator())
                .registerBlockEntityTranslator("Chest", new DoubleChestBlockEntityTranslator())
                .registerBlockEntityTranslator("Empty", new EmptyBlockEntityTranslator())
                .registerBlockEntityTranslator("EndGateway", new EndGatewayBlockEntityTranslator())
                .registerBlockEntityTranslator("ShulkerBox", new ShulkerBoxBlockEntityTranslator())
                .registerBlockEntityTranslator("Sign", new SignBlockEntityTranslator())
                .registerBlockEntityTranslator("Skull", new SkullBlockEntityTranslator());

        // Register Bedrock Block Translator
        TRANSLATORS.registerBlockTranslator(new BlockTranslator(this));

    }
}
