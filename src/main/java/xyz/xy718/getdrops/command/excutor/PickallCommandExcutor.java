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
import xyz.xy718.getdrops.I18N;
import xyz.xy718.getdrops.data.model.TrackData;
import xyz.xy718.getdrops.util.ItemUtil;
import xyz.xy718.getdrops.util.MessageUtil;

public class PickallCommandExcutor implements CommandExecutor {

	private static Logger LOGGER = GetDropsPlugin.LOGGER;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) 
			throws CommandException {
		if (src instanceof Player) {
		    for(World w:Sponge.getServer().getWorlds()) {
	    		List<Entity> is= ItemUtil.getAllDropItems(w);
	    		if(is.size()<=0) {
	    			src.sendMessage(I18N.getText("list.empty.world", w.getName()));
	    			continue;
	    		}
	    		//合并该世界的 被玩家掉落的物品和自然掉落的物品（鸡下的蛋，被水冲走的铁轨）
	    		Collection<Entity> items=w.getEntities();
	    		
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
