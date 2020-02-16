package xyz.xy718.getdrops.command.excutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Carrier;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import xyz.xy718.getdrops.GetDropsPlugin;
import xyz.xy718.getdrops.data.model.TrackData;
import xyz.xy718.getdrops.util.ItemUtil;
import xyz.xy718.getdrops.util.MessageUtil;

public class PickallCommandExcutor implements CommandExecutor {

	private static Logger LOGGER = GetDropsPlugin.LOGGER;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) 
			throws CommandException {
		if (src instanceof Player) {
		    List<World> ws=new ArrayList<>(Sponge.getServer().getWorlds());

			//这里是因为要记录被track完的物品
			List<TrackData> trackedList=new ArrayList<>();
		    for(World w:ws) {
	    		List<Entity> is= ItemUtil.getAllDropItems(w);
	    		if(is.size()<=0) {
	    			Text ret=Text.of("世界："+w.getName()+"没有任何掉落物");
	    			src.sendMessage(ret);
	    			continue;
	    		}
	    		//这里是未成功的原因
				String failCause=null;
				int trackedCount=0;
		    	for (Entity entity:is) {
		    		if(!(entity instanceof Item)) {
		    			LOGGER.info("666");
		    		}
		    		ItemStack item = entity.get(Keys.REPRESENTED_ITEM).get().createStack();

					Inventory inv = ((PlayerInventory) ((Player) src).getInventory()).getMain();
    				InventoryTransactionResult iResult= inv.offer(item);
    				if(iResult.getType().equals(InventoryTransactionResult.Type.SUCCESS)) {
						trackedCount++;
    					if(entity.getCreator().orElse(null) != null) {
    						//将待删除记录加上
    						trackedList.add(new TrackData(entity, entity.getUniqueId(), (entity.getCreator().get())));
    					}
    					ItemUtil.destructDropItem(entity);
    				}else {
    					switch (iResult.getType()) {
    					case FAILURE:
    						failCause="你的背包满了？";
    						break;
    					case CANCELLED:
    						failCause="放入背包的动作被取消了~";
    						break;
    					case UNDEFINED:
    						failCause="我也不知道发送了啥~";
    						break;
    					default:
    						failCause="我也不知道发送了啥";
    						break;
    					}
    					//有一次失败就break
    					break;
    				}
				}
				//取消追踪
				for(TrackData t:trackedList) {
					ItemUtil.untracking(t.getDropItem());
				}
				
				Text count=Text.of("]件");
				Text t1=Text.of(w.getName()+"您已成功捡起物品");
				Text t2=Text.builder("成功[").append(Text.builder(trackedCount+"").color(TextColors.GREEN).build()).append(count).build();
				Text t3=Text.builder("失败[").append(Text.builder(ItemUtil.getAllDropItems(w).size()+"").color(TextColors.RED).build()).append(count).build();
				src.sendMessage(t1);
				src.sendMessage(t2);
				src.sendMessage(t3);
				if(failCause!=null) {
					Text failCauseT=Text.builder("失败原因:").append(Text.builder(failCause).color(TextColors.RED).build()).build();
					src.sendMessage(failCauseT);
				}
		    }
		}
		else if(src instanceof ConsoleSource) {
		    src.sendMessage(Text.of("控制台就别用这个指令了吧"));
		}
		else if(src instanceof CommandBlockSource) {
		    src.sendMessage(Text.of("命令方块用不了哦"));
		}
		return CommandResult.success();
	}

}
