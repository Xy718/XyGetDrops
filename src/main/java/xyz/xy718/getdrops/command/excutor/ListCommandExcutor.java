package xyz.xy718.getdrops.command.excutor;

import java.util.Map.Entry;
import java.util.UUID;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import xyz.xy718.getdrops.I18N;
import xyz.xy718.getdrops.data.model.TrackData;
import xyz.xy718.getdrops.event.AllDropEvent;
import xyz.xy718.getdrops.util.ItemUtil;
import xyz.xy718.getdrops.util.MessageText;
import xyz.xy718.getdrops.util.MessageUtil;

public class ListCommandExcutor implements CommandExecutor{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException {
		if (src instanceof Player) {
		    Player player = (Player) src;
		    int i=ItemUtil.getPlayerDropsMap(player).size();
		    String warnColor="&a";
		    if(i>=32) {
		    	warnColor="&6";
		    }
		    player.sendMessage(I18N.getText("list.sample", new String[] {warnColor,i+""}));
		    //MessageText.LIST_TRACKING_COUNT.setColorAndCount(warnColor, i).send(player);;
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
