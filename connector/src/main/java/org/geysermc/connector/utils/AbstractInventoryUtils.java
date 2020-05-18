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

package org.geysermc.connector.utils;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.nukkitx.protocol.bedrock.data.ItemData;
import lombok.Getter;
import org.geysermc.connector.GeyserEdition;
import org.geysermc.connector.inventory.Inventory;
import org.geysermc.connector.network.session.GeyserSession;

@Getter
public abstract class AbstractInventoryUtils {
    public static final ItemStack REFRESH_ITEM = new ItemStack(1, 127, new CompoundTag("")); //TODO: stop using this


    private GeyserEdition edition;

    public AbstractInventoryUtils(GeyserEdition edition) {
        this.edition = edition;
    }

    public abstract void openInventory(GeyserSession session, Inventory inventory);

    public abstract void closeInventory(GeyserSession session, int windowId);

    public abstract void updateCursor(GeyserSession session);

    public abstract boolean canStack(ItemStack item1, ItemStack item2);

    public abstract boolean canStack(ItemData item1, ItemData item2);

    public abstract ItemData createUnusableSpaceBlock(String description);


}
