package xyz.xy718.getdrops.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import xyz.xy718.getdrops.GetDropsPlugin;
import xyz.xy718.getdrops.command.excutor.ListCommandExcutor;
import xyz.xy718.getdrops.command.excutor.ListAllCommandExcutor;
import xyz.xy718.getdrops.command.excutor.PickallCommandExcutor;
import xyz.xy718.getdrops.command.excutor.PickmeCommandExcutor;

public class GetDropsCommandRegister {
	
	public static void regCommand(GetDropsPlugin plugin) {
		
		CommandSpec getdropsList = CommandSpec.builder()
			    .description(Text.of("获取自己的的掉落物列表"))
			    .permission("getdrops.list.me")
			    .executor(new ListCommandExcutor())
			    .build();
		
		CommandSpec getdropsPickme= CommandSpec.builder()
			    .description(Text.of("获取自己的掉落物"))
			    .permission("getdrops.pickme.base")
			    .executor(new PickmeCommandExcutor())
			    .build();
		
		CommandSpec getdropsPickall= CommandSpec.builder()
			    .description(Text.of("获取所有世界的掉落物"))
			    .permission("getdrops.pickall.base")
			    .executor(new PickallCommandExcutor())
			    .build();
		CommandSpec getdropsListall= CommandSpec.builder()
			    .description(Text.of("获取所有世界的掉落物列表"))
			    .permission("getdrops.list.all")
			    .executor(new ListAllCommandExcutor())
			    .build();
		
		CommandSpec getdrops=CommandSpec.builder()
				.description(Text.of("getdrops plugin info"))
			    .permission("getdrops.info.base")
			    .child(getdropsList, "list","l")
			    .child(getdropsPickme, "pick","p")
			    //.child(getdropsPickall, "pickall","pa")
			    //.child(getdropsListall, "listall","la")
			    .build();
		Sponge.getCommandManager().register(plugin, getdrops, "getdrops", "gdrops");
	}
}
