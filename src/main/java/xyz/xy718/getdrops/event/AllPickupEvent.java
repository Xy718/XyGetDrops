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

public class AllPickupEvent {

    private Logger LOGGER = GetDropsPlugin.LOGGER;
    
    /**
     * 这个部分是用来监听所有物品被捡起的事件的
     * @param event
     */
    @Listener
    public void onItemPickup(
    		DestructEntityEvent event
    		) { 
    	//LOGGER.debug("onItemPickup:"+event);
    	//如果有Creator且是物品
    	if(event.getTargetEntity().getCreator().orElse(null)!=null
    			&&event.getTargetEntity().getType().equals(EntityTypes.ITEM)) {
    		ItemUtil.untracking(event.getTargetEntity());
    		/*
    		MessageUtil.simpleSendToPlayer(
    	    		Sponge.getServer().getPlayer(event.getTargetEntity().getCreator().get()).get()
    	    		, Text.of("您的物品："+event.getTargetEntity()+"被"+event.getSource()+"捡走"));
    	    		*/
    	}
    }
    
}
