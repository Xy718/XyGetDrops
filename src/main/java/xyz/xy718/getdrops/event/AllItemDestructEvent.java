package xyz.xy718.getdrops.event;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.item.TargetItemEvent;
import org.spongepowered.api.text.Text;

import com.google.inject.spi.Message;

import xyz.xy718.getdrops.GetDropsPlugin;
import xyz.xy718.getdrops.util.ItemUtil;
import xyz.xy718.getdrops.util.MessageUtil;

public class AllItemDestructEvent {

    private Logger LOGGER = GetDropsPlugin.LOGGER;
    
    /**
     * 这个部分是用来监听所有物品被捡起的事件的,或被销毁
     * @param event
     */
    @Listener
    public void onItemPickup(
    		DestructEntityEvent event
    		) { 
    	//LOGGER.debug("onItemPickup:"+event);
    	//TODO 是否启用了掉落物守护模式
    	
    	//TODO 该掉落物的保护时间是否已过期
    	
    	//TODO 是否在保护名单中
    	
    	
    	if(event.getTargetEntity().getType().equals(EntityTypes.ITEM)) {
    		ItemUtil.untracking(event.getTargetEntity());

    	}
    }
    
}
