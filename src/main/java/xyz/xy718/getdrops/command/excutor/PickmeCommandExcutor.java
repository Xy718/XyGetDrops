package xyz.xy718.getdrops.command.excutor;

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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import xyz.xy718.getdrops.GetDropsPlugin;
import xyz.xy718.getdrops.I18N;
import xyz.xy718.getdrops.data.model.TrackData;
import xyz.xy718.getdrops.util.ItemUtil;
import xyz.xy718.getdrops.util.MessageText;
import xyz.xy718.getdrops.util.MessageUtil;

public class PickmeCommandExcutor implements CommandExecutor {

    private Logger LOGGER = GetDropsPlugin.LOGGER;
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) 
			throws CommandException {
		if (src instanceof Player) {
		    Player player = (Player) src;
		    if(ItemUtil.getPlayerDropsMap(player).isEmpty()) {
		    	//没有物品
		    	player.sendMessage(I18N.getText("list.empty"));
		    	return CommandResult.success();
		    }
			//开始pick物品,如果是已加载区块，就直接操作，
		    //如果是未加载区块就请求加载后拾取
			ItemUtil.setTracking(player.getUniqueId());
			while (!ItemUtil.getPlayerDropsMap(player).isEmpty()) {
				TrackData data =ItemUtil.getFirstOrNullOnPlayerDrops(player.getUniqueId());
				//加载需要加载的区块（如果没有加载）
				
				if(!Sponge.getServer()
						.getWorld(data.getWorldUUID()).get()
						.getChunk(data.getChunkPosition())
						.isPresent()) {
					//未加载区块
					Sponge.getServer().getWorld(data.getWorldUUID()).get()
						.loadChunk(data.getChunkPosition(), false);
				}else {
					//已加载区块
					Entity entity=ItemUtil.getItemEntity(data);
					ItemUtil.singlePickupAction(entity, data.getPlayerUUID());
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
