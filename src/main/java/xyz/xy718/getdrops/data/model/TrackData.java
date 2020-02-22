package xyz.xy718.getdrops.data.model;

import java.util.Date;
import java.util.UUID;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;

import com.flowpowered.math.vector.Vector3i;

import lombok.Data;
import xyz.xy718.getdrops.util.TimeUtil;

@Data
public class TrackData {
	private UUID livingId;
	private UUID playerUUID;
	private UUID worldUUID;
	private Vector3i chunkPosition;
	private Date dropTime;

	/**
	 * 这里用playerUUID的原因是因为某些环境下获取不到Player，只有UUID
	 * @param dropItem
	 * @param livingId
	 * @param playerUUID
	 */
	public TrackData(Entity dropItem, UUID livingId, UUID playerUUID) {
		if (dropItem == null)
			throw new IllegalArgumentException("dropItem");
		if (livingId == null)
			throw new IllegalArgumentException("livingId");
		this.worldUUID=dropItem.getWorld().getUniqueId();
		this.chunkPosition=dropItem.getLocation().getChunkPosition();
		this.livingId = livingId;
		this.playerUUID = playerUUID;
		this.dropTime=new Date();
	}
	public TrackData(Entity dropItem, UUID livingId, Player player) {
		this(dropItem, livingId, player.getUniqueId());
	}
	
	public boolean isExpired(int t) {
		return TimeUtil.isExpired(dropTime, t);
	}
}
