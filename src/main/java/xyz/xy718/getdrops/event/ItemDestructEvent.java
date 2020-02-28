package xyz.xy718.getdrops.event;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import xyz.xy718.getdrops.GetDropsPlugin;
import xyz.xy718.getdrops.util.ItemUtil;

public class ItemDestructEvent {

    private Logger LOGGER = GetDropsPlugin.LOGGER;
    
    /**
     * 这个部分是用来监听所有物品被捡起的事件的,或被销毁
     * @param event
     */
    @Listener(order = Order.EARLY,beforeModifications = true)
    public void onItemDestruct(
    		DestructEntityEvent event
    		) { 
    	Entity targetItem=event.getTargetEntity();
    	//如果是个掉落物
    	if(targetItem.getType().equals(EntityTypes.ITEM)) {
    		//启用了掉落物守护模式
        	if(GetDropsPlugin.getConfig().isItemProtection()) {
        		//是被追踪的物品
            	if(ItemUtil.getOnTrackingItem().containsKey(targetItem.getUniqueId())) {
            	    //该物品是否在保护名单中
                	if(GetDropsPlugin.getConfig().isProtectItem(targetItem.getType().getId())) {
                		//是插件发起的
                		if(event.getCause().containsType(PluginContainer.class)) {
                			//该清理行为由非XyGetDrops插件发起
                			if(!event.getCause().allOf(PluginContainer.class).get(0).getId().equals(GetDropsPlugin.PLUGIN_ID)) {
                				//保护未过期
                				if(!ItemUtil.getOnTrackingItem().get(targetItem.getUniqueId()).isExpired(GetDropsPlugin.getConfig().getProtectTime())) {
            	        	    	Entity saveItem=getACopyItemEntity(targetItem);
            	        			Task.builder()
            	        				.delayTicks(1)
            	        				.execute(()->{
            	        					saveItem.getWorld().spawnEntity(saveItem);
            	        				})
            	        				.name("XyGetDrops-ProtectTask")
            	        				.submit(GetDropsPlugin.get());
            	        			return;
                	        	}
                			}
                		}
                	}
            	}
        	}
    	}
        //如果都跳过了
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
