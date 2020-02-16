package xyz.xy718.getdrops.command.excutor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import xyz.xy718.getdrops.util.ItemUtil;

public class ListAllCommandExcutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (src instanceof Player) {
		    List<World> ws=new ArrayList<>(Sponge.getServer().getWorlds());
		    for(World w:ws) {
		    	List<Entity> is= w.getEntities()
	    				.stream()
		    			.filter(e -> e.getType().equals(EntityTypes.ITEM)).collect(Collectors.toList());
	    		if(is.size()<=0) {
	    			Text ret=Text.of("世界："+w.getName()+"没有任何掉落物");
	    			src.sendMessage(ret);
	    			continue;
	    		}

	    		Text entityCountText=
			    		Text.builder("世界:"+w.getName()+"有[").append(
			    		Text.builder(is.size()+"").color(TextColors.GREEN).build()).append(
			    		Text.of("]个待拾取物品")).build();
				src.sendMessage(entityCountText);
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
