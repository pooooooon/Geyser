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

package org.geysermc.connector.entity;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import lombok.Getter;
import lombok.Setter;
import org.geysermc.connector.entity.attribute.Attribute;
import org.geysermc.connector.entity.attribute.IAttributeType;
import org.geysermc.connector.entity.type.IEntityType;
import org.geysermc.connector.network.session.GeyserSession;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractEntity {
    protected long entityId;
    protected long geyserId;

    protected int dimension;

    protected Vector3f position;
    protected Vector3f motion;

    /**
     * x = Yaw, y = Pitch, z = HeadYaw
     */
    protected Vector3f rotation;

    protected float scale = 1;

    protected IEntityType entityType;

    protected boolean valid;

    protected LongSet passengers = new LongOpenHashSet();
    protected Map<IAttributeType, Attribute> attributes = new HashMap<>();
    protected EntityDataMap metadata = new EntityDataMap();

    public AbstractEntity(long entityId, long geyserId, IEntityType entityType, Vector3f position, Vector3f motion, Vector3f rotation) {
        this.entityId = entityId;
        this.geyserId = geyserId;
        this.entityType = entityType;
        this.motion = motion;
        this.rotation = rotation;

        this.valid = false;
        this.dimension = 0;

        setPosition(position);
    }

    public abstract void spawnEntity(GeyserSession session);

    /**
     * Despawns the entity
     *
     * @param session The GeyserSession
     * @return can be deleted
     */
    public abstract boolean despawnEntity(GeyserSession session);

    public void moveRelative(GeyserSession session, double relX, double relY, double relZ, float yaw, float pitch, boolean isOnGround) {
        moveRelative(session, relX, relY, relZ, Vector3f.from(yaw, pitch, yaw), isOnGround);
    }

    public abstract void moveRelative(GeyserSession session, double relX, double relY, double relZ, Vector3f rotation, boolean isOnGround);

    public void moveAbsolute(GeyserSession session, Vector3f position, float yaw, float pitch, boolean isOnGround, boolean teleported) {
        moveAbsolute(session, position, Vector3f.from(yaw, pitch, yaw), isOnGround, teleported);
    }

    public abstract void moveAbsolute(GeyserSession session, Vector3f position, Vector3f rotation, boolean isOnGround, boolean teleported);

    public abstract void updateBedrockAttributes(GeyserSession session);

    public abstract void updateBedrockMetadata(EntityMetadata entityMetadata, GeyserSession session);

    public abstract void updateBedrockMetadata(GeyserSession session);

    /**
     * x = Pitch, y = HeadYaw, z = Yaw
     *
     * @return the bedrock rotation
     */
    public Vector3f getBedrockRotation() {
        return Vector3f.from(rotation.getY(), rotation.getZ(), rotation.getX());
    }

    @SuppressWarnings("unchecked")
    public <I extends AbstractEntity> I as(Class<I> entityClass) {
        return entityClass.isInstance(this) ? (I) this : null;
    }

    public <I extends AbstractEntity> boolean is(Class<I> entityClass) {
        return entityClass.isInstance(this);
    }
}
