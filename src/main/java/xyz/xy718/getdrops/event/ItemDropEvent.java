package xyz.xy718.getdrops.event;

import java.util.Optional;

import org.slf4j.Logger;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.DropItemEvent;

import xyz.xy718.getdrops.GetDropsPlugin;
import xyz.xy718.getdrops.config.GetDropsConfig;
import xyz.xy718.getdrops.data.model.Permissions;
import xyz.xy718.getdrops.util.ItemUtil;
import xyz.xy718.getdrops.util.PlayerUtil;

/**
 * 有关掉落物的所有监听
 * <br>
 * @author Xy718
 * @datetime 20200126
 * <br>
 * 
 */
public class ItemDropEvent {

    
    private static Logger LOGGER = GetDropsPlugin.LOGGER;
    private static GetDropsConfig config=GetDropsPlugin.getConfig();
    
    @Listener
    public void onItemDroping(
    		DropItemEvent.Destruct event
    		,@First Player player
    		) {
    	LOGGER.debug("onItemDroping");
    	//判断物品是否存在
    	if(event.getEntities().isEmpty()) {
    		return;
    	}
    	//判断是否在该世界启用
    	if(!config.getWorldIsModuled(player.getWorld())) {
    		//没有启用
    		return;
    	}
    	//判断用户是否有权限来追踪物品
    	if(!player.hasPermission(Permissions.GetDropsTrackPMS)) {
    		return;
    	}
    	//判断用户的metadata是不是不大于0(剩下的判断移除之类的放在ItemUtil)
    	//存在就判断
    	Optional<Integer> temp= PlayerUtil.getPlayerTrackLimit(player);
    	if(temp.isPresent()) {
    		if(temp.get()<1) {
    			return;
    		}
    	}
    	if(event.getSource() instanceof BlockSnapshot) {
    		//挖掘破坏行为
    		//是否启用了break追踪&该方块是否追踪
			if(!config.getIsBreakTracking(((BlockSnapshot)event.getSource()).getExtendedState().getType().getId())) {
				return;
			}
			ItemUtil.tracking(event.getEntities(),player.getUniqueId());
		}else if(event.getSource() instanceof EntityDamageSource) {
    		//击杀生物行为
			Entity e=null;
			for(Object entity:event.getCause().all()) {
				if(!(entity instanceof Player)&&!(entity instanceof DamageSource)) {
					//获取到了生物
					e=(Entity) entity;
					break;
				}
			}
			if(e==null) {
				return;
			}
			if(!config.getIsKillTracking(e.getType().getId())) {
				return;
			}
			ItemUtil.tracking(event.getEntities(),player.getUniqueId());
		}
		
    }
}