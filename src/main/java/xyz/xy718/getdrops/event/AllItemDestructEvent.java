package xyz.xy718.getdrops.event;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.sound.midi.Track;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.CauseStackManager.StackFrame;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.AffectEntityEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.SpawnEntityEvent.Spawner;
import org.spongepowered.api.event.entity.item.TargetItemEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import com.google.inject.spi.Message;

import xyz.xy718.getdrops.GetDropsPlugin;
import xyz.xy718.getdrops.data.model.TrackData;
import xyz.xy718.getdrops.util.ItemUtil;
import xyz.xy718.getdrops.util.MessageUtil;
import xyz.xy718.getdrops.util.TimeUtil;

public class AllItemDestructEvent {

    private Logger LOGGER = GetDropsPlugin.LOGGER;
    
    /**
     * 这个部分是用来监听所有物品被捡起的事件的,或被销毁
     * @param event
     */
    @Listener(order = Order.EARLY,beforeModifications = true)
    public void onItemPickup(
    		DestructEntityEvent event
    		) { 
    	Entity targetItem=event.getTargetEntity();
    	if(!targetItem.getType().equals(EntityTypes.ITEM)) {
    		//如果不是个掉落物
    		return;	
    	}
    	//LOGGER.debug("onItemPickup:"+event);
    	//TODO 是否启用了掉落物守护模式
    	
    	//TODO 是否在保护名单中
    	if(ItemUtil.getOnTrackingItem().containsKey(targetItem.getUniqueId())) {
    	//是被追踪的物品
    		if(event.getCause().containsType(PluginContainer.class)) {
    		//该清理行为由非XyGetDrops插件发起
    			if(!event.getCause().allOf(PluginContainer.class).get(0).getId().equals("xygetdrops")) {
    				if(!ItemUtil.getOnTrackingItem().get(targetItem.getUniqueId()).isExpired(60)) {
    	        		//保护未过期
    	        	    	Entity saveItem=getACopyItemEntity(targetItem);
    	        			Task.builder()
    	        				.delayTicks(1)
    	        				.execute(()->{
    	        					saveItem.getWorld().spawnEntity(saveItem);
    	        				})
    	        				.submit(GetDropsPlugin.get());
    	        			return;
    	        		}
    			}
    		}
    	}
    	
    	ItemUtil.untracking(targetItem);
    }
    /**
     * 获取一个不会被remove的物品掉落物
     * @param targetItem
     * @return
     */
	private static Entity getACopyItemEntity(Entity targetItem) {
		EntitySnapshot eSnapShot=targetItem.createSnapshot();
    	Entity saveItem=eSnapShot.restore().get();
    	Field field;
		try {
			field = saveItem.getClass().getField("field_70128_L");
	        field.setBoolean(saveItem, false);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch blockt
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return saveItem;
	}
    
}
