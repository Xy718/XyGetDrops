package xyz.xy718.getdrops.command.excutor;


import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.source.ProxySource;
import org.spongepowered.api.command.source.RemoteSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import xyz.xy718.getdrops.I18N;
import xyz.xy718.getdrops.util.ItemUtil;

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
		    player.sendMessage(I18N.getText("list.sample", warnColor,i));
		}
		else if(src instanceof ConsoleSource) {
		    src.sendMessage(Text.of("控制台就别用这个指令了吧"));
		}
		else if(src instanceof CommandBlockSource) {
		    src.sendMessage(Text.of("命令方块用不了哦"));
		}else if(src instanceof ProxySource) {
		    src.sendMessage(Text.of("~"));
		}
		else if(src instanceof RemoteSource) {
		    src.sendMessage(Text.of("~"));
		}
		return CommandResult.success();
	}

}
