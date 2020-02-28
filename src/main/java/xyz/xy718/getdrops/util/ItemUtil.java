package xyz.xy718.getdrops.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.UUID;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.world.World;

import lombok.Getter;
import xyz.xy718.getdrops.GetDropsPlugin;
import xyz.xy718.getdrops.I18N;
import xyz.xy718.getdrops.data.model.TrackData;

/**
 * 物品管理工具类<br>
 * 基本所有的物品操作都写在这里嗷
 * @author Xy718
 *
 */
public class ItemUtil {

    private static Logger LOGGER = GetDropsPlugin.LOGGER;
	@Getter
    private static HashSet<UUID> chunkTrackingPlayers=new HashSet<UUID>();
	@Getter
	private static Map<UUID, LinkedHashMap<UUID,TrackData>> playerDropsMap=new HashMap<>();
	@Getter
	private static Map<UUID, TrackData> onTrackingItem=new HashMap<>();
    /**
     * 将某个掉落物放入玩家的追踪列表<br>
     * 如果超出了就先删除再增加
     * @param data
     */
    public static void tracking(TrackData data) {
		// 将追踪物品放入追踪列表中
		if(playerDropsMap.get(data.getPlayerUUID())==null) {
			//玩家不存在服务列表中
			LinkedHashMap<UUID, TrackData> temp=new LinkedHashMap<>(0);
			playerDropsMap.put(data.getPlayerUUID(),temp);
		}
		Optional<Player> tempP=Sponge.getServer().getPlayer(data.getPlayerUUID());
    	//判断是否有超出可保存上限的部分
		if(tempP.isPresent()) {
			Optional<Integer> tempMeta=PlayerUtil.getPlayerTrackLimit(tempP.get());
			//如果metadata存在
			if(tempMeta.isPresent()) {
				//判断是否超出
				if(tempMeta.get()<=playerDropsMap.get(data.getPlayerUUID()).size()) {
					//超出了，就要删除第0个
					delOutItem(tempP.get(),tempMeta.get());
				}
			}else {
				//如果metadata没有
				//且配置文件正常
				if(GetDropsPlugin.getConfig().getWorkCount()>0){
		    		//是否超出配置文件的配置上限
		    		if(ItemUtil.getP_TrackedCount(tempP.get().getUniqueId()) >= GetDropsPlugin.getConfig().getWorkCount()) {
		    			delOutItem(tempP.get(), GetDropsPlugin.getConfig().getWorkCount());
		    		}
		    	}
			}
		}
		
		//再增加
		playerDropsMap.get(data.getPlayerUUID()).put(data.getLivingId(), data);
		onTrackingItem.put(data.getLivingId(), data);
	}
    /**
     * 获取玩家的追踪的掉落物数量
     * @param uniqueId
     * @return
     */
    public static int getP_TrackedCount(UUID uniqueId) {
		return playerDropsMap.get(uniqueId).size();
	}
	/**
	 * 从列表头删除直到少一个
	 * @param p
	 * @param limit
	 */
    private static void delOutItem(Player p,int limit) {
    	for(Entry<UUID, TrackData> temp:playerDropsMap.get(p.getUniqueId()).entrySet()) {
			MessageUtil.sendLimitToRemoveItem(p, temp.getValue().getItemID(),temp.getValue().getItemCount());
			playerDropsMap.get(p.getUniqueId()).remove(temp.getKey());
			if(playerDropsMap.get(p.getUniqueId()).size()<limit) {
				break;//删除到直到比定义的小（保险起见）
			}
		}
    }
    /**
     * 将某些掉落物放入玩家的追踪列表
     * @param data
     */
    public static void tracking(List<Entity> entities,UUID player) {
		// 将追踪物品放入追踪列表中
		for(Entity dropItem : entities) {
			ItemUtil.tracking(new TrackData(
							dropItem,dropItem.getUniqueId(), player
							));
		}
	}
    /**
     * 将某个掉落物移出追踪列表
     * @param data Entity(EntityItem)
     */
    public static void untracking(Entity data) {
    		TrackData beUntrackData=onTrackingItem.get(data.getUniqueId());
    		if(beUntrackData!=null) {
    			playerDropsMap.get(beUntrackData.getPlayerUUID())
    				.remove(beUntrackData.getLivingId());
    			onTrackingItem.remove(data.getUniqueId());
    		}
	}
    public static void destructDropItem(Entity entity) {
    	entity.getWorld().getEntity(entity.getUniqueId()).get().remove();
    }
    public static void destructDropItem(UUID livingID) {
    	Sponge.getServer().getWorlds().forEach(w->{
    		Optional<Entity> entity=w.getEntity(livingID);
    		if (entity.isPresent()) {
				entity.get().remove();
			}
    	});
    }
    /**
     * 获取服务器当前的追踪列表
     * @param player
     * @return
     */
	public static Map<UUID, TrackData> getPlayerDropsMap(Player player){
		if (player==null) {
			return null;
		}
		return getPlayerDropsMap(player.getUniqueId());
	}
    /**
     * 获取服务器当前的追踪列表
     * @param playerUUID
     * @return
     */
	public static Map<UUID, TrackData> getPlayerDropsMap(UUID playerUUID){
		if (playerUUID==null) {
			return null;
		}
		if(playerDropsMap.get(playerUUID)==null) {
			//玩家不存在服务列表中
			LinkedHashMap<UUID, TrackData> temp=new LinkedHashMap<>(0);
			playerDropsMap.put(playerUUID,temp);
		}
		return playerDropsMap.get(playerUUID);
	}
	/**
	 * 获取当前的在记追踪的掉落物数量
	 * @return
	 */
	public static int getTrackingCount() {
		int c=0;
		for(Entry<UUID, LinkedHashMap<UUID, TrackData>> player:playerDropsMap.entrySet()) {
			c+=player.getValue().size();
		}
		return c;
	}
	/**
	 * 获取该世界当前已加载区块内的所有掉落物
	 * @param w
	 * @return
	 */
	public static List<Entity> getAllDropItems(World w) {
		return w.getEntities()
		.stream()
		.filter(e -> e.getType().equals(EntityTypes.ITEM)).collect(Collectors.toList());
	}
	
	public static InventoryTransactionResult giveItem(Entity entity,Player player) {
		Inventory inv = ((PlayerInventory) player.getInventory()).getMain();
		ItemStack item = entity.get(Keys.REPRESENTED_ITEM).get().createStack();
		return inv.offer(item);
	}
	
	public static InventoryTransactionResult giveItem(Entity entity,UUID playerUUID) {
		Inventory inv = ((PlayerInventory) Sponge.getServer().getPlayer(playerUUID).get().getInventory()).getMain();
		ItemStack item = entity.get(Keys.REPRESENTED_ITEM).get().createStack();
		return inv.offer(item);
	}
	public static void setUnTracking(UUID playerUUID) {
		chunkTrackingPlayers.remove(playerUUID);
	}
	public static void setTracking(UUID playerUUID) {
		chunkTrackingPlayers.add(playerUUID);
	}
	/**
	 * 返回该实体是否正在被追踪到摸个玩家
	 * @param itemUUID
	 * @return
	 */
	public static boolean isTrackingItem(UUID itemUUID) {
		return onTrackingItem.containsKey(itemUUID);
	}
	/**
	 * 单次拾取操作<br>
	 * 包括：<br>
	 * 放入背包<br>
	 * 删除记录<br>
	 * 删除地面物品<br>
	 * 返回信息<br>
	 * @param entity
	 * @param playerUUID
	 */
	public static void singlePickupAction(Entity entity,UUID playerUUID) {
		//给予物品
		InventoryTransactionResult iResult= ItemUtil.giveItem(entity, playerUUID);

		//捡完成功后删除地上的物品和追踪记录
		if(iResult.getType().equals(InventoryTransactionResult.Type.SUCCESS)) {
			//删除玩家列表的记录
			ItemUtil.getPlayerDropsMap(playerUUID).remove(entity.getUniqueId());
			
			MessageUtil.send(
					playerUUID
					, I18N.getText("pick.success.item",entity.get(Keys.REPRESENTED_ITEM).get().getType().getId(),entity.get(Keys.REPRESENTED_ITEM).get().getQuantity()));
			
			//如果玩家的待拾取列表清空了就UuChunkTracking
			if(ItemUtil.getPlayerDropsMap(playerUUID).isEmpty()){
				ItemUtil.setUnTracking(playerUUID);
			}
			
			//删除物品
			ItemUtil.destructDropItem(entity.getUniqueId());
		}else {
			//如果有一次失败就停止接下来的所有拾取
			ItemUtil.setUnTracking(playerUUID);
			
			switch (iResult.getType()) {
			case FAILURE:
				MessageUtil.send(playerUUID, I18N.getText("pick.fail.cause.full"));
				break;
			case CANCELLED:
				MessageUtil.send(playerUUID, I18N.getText("pick.fail.cause.cancel"));
				break;
			default:
				MessageUtil.send(playerUUID, I18N.getText("pick.fail.cause.unkown"));
				break;
			}
		}
	} 
	/**
	 * 获取第一位物品，以保证实时删除时不会出现空指针
	 * @param playerUUID
	 * @return
	 */
	public static TrackData getFirstOrNullOnPlayerDrops(UUID playerUUID) {
		TrackData obj =null;
        for (Entry<UUID, TrackData> entry : ItemUtil.getPlayerDropsMap(playerUUID).entrySet()) {
            obj=entry.getValue();
            if (obj!=null) {
                break;
            }
        }
        return  obj;
    }
	public static Entity getItemEntity(TrackData data) {
		return Sponge.getServer().getWorld(data.getWorldUUID()).get().getEntity(data.getLivingId()).get();
	}
	
	public static Set<UUID> getTrackingItemsByWorld(UUID worldUUID) {
		Set<UUID> retList=new HashSet<>();
		for(Entry<UUID, LinkedHashMap<UUID, TrackData>> entry:playerDropsMap.entrySet()) {
			for(Entry<UUID, TrackData> entry2:entry.getValue().entrySet()) {
				if(entry2.getValue().getWorldUUID().equals(worldUUID)) {
					retList.add(entry2.getValue().getLivingId());
				}
			}
		}
		return retList;
	}
}
