package xyz.xy718.getdrops.command.excutor;

import java.util.UUID;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.ChunkTicketManager;
import org.spongepowered.api.world.ChunkTicketManager.LoadingTicket;

import xyz.xy718.getdrops.GetDropsPlugin;
import xyz.xy718.getdrops.data.model.TrackData;
import xyz.xy718.getdrops.util.ItemUtil;
import xyz.xy718.getdrops.util.MessageText;

public class PickmeCommandExcutor implements CommandExecutor {

    private Logger LOGGER = GetDropsPlugin.LOGGER;
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) 
			throws CommandException {
		if (src instanceof Player) {
		    Player player = (Player) src;
		    if(ItemUtil.getPlayerDropsMap(player).isEmpty()) {
		    	//没有物品
		    	MessageText.DONT_NEED_ANYPICKUP.send(player);
		    	return CommandResult.success();
		    }
			//开始pick物品,如果是已加载区块，就直接操作，
		    //如果是未加载区块就请求加载后拾取
			ItemUtil.setTracking(player.getUniqueId());
			while (!ItemUtil.getPlayerDropsMap(player).isEmpty()) {
				TrackData data =ItemUtil.getFirstOrNullOnPlayerDrops(player.getUniqueId());
				//加载需要加载的区块（如果没有加载）
				if(!data.getDropItem()
						.getWorld()														//该世界
						.getChunk(data.getDropItem().getLocation().getChunkPosition())	//的该区块
						.isPresent()) {													//!不存在
					//未加载区块
					/*
					ChunkTicketManager ctm= GetDropsPlugin.getTicketManager();
					Optional<LoadingTicket> ticket=ctm.createTicket(GetDropsPlugin.get(), data.getDropItem().getWorld());
					ticket.get().forceChunk(data.getDropItem().getLocation().getChunkPosition());
					*/
					data.getDropItem().getWorld().loadChunk(data.getDropItem().getLocation().getChunkPosition(), false);
				}else {
					//已加载区块
					ItemUtil.singlePickupAction(data.getDropItem(), data.getPlayerUUID());
					
				}
			}
		}else if(src instanceof ConsoleSource) {
		    src.sendMessage(Text.of("控制台就别用这个指令了吧"));
		}else if(src instanceof CommandBlockSource) {
		    src.sendMessage(Text.of("命令方块用不了哦"));
		}
		return CommandResult.success();
	}

}
