package xyz.xy718.getdrops.event;

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
import xyz.xy718.getdrops.util.ItemUtil;

/**
 * 有关掉落物的所有监听
 * <br>
 * @author Xy718
 * @datetime 20200126
 * <br>
 * 
 */
public class AllDropEvent {

    
    private Logger LOGGER = GetDropsPlugin.LOGGER;
    private static GetDropsConfig config=GetDropsPlugin.getConfig();
    
    @Listener
    public void onItemDroping(
    		DropItemEvent.Destruct event
    		,@First Player player
    		) {
    	LOGGER.debug("onItemDroping");
    	//首先判断是否在该世界启用
    	if(!config.getWorldIsModuled(event.getEntities().get(0).getWorld())) {
    		//没有启用
    		return;
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