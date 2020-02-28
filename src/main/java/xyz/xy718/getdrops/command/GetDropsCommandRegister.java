package xyz.xy718.getdrops.command;

import java.util.Arrays;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.command.spec.CommandSpec.Builder;
import org.spongepowered.api.text.Text;

import xyz.xy718.getdrops.data.model.Permissions;
import xyz.xy718.getdrops.GetDropsPlugin;
import xyz.xy718.getdrops.command.excutor.ListCommandExcutor;
import xyz.xy718.getdrops.command.excutor.PickmeCommandExcutor;

public class GetDropsCommandRegister {
	
	private enum CommandList{
		GetDropsListme(Permissions.GetDropsListmePMS,new ListCommandExcutor(),Text.of("获取自己的的掉落物列表"),"list","l"),
		GetDropsPickme(Permissions.GetDropsPickmePMS,new PickmeCommandExcutor(),Text.of("获取自己的掉落物"), "pick","p"),

		;
		private String permission;
		private CommandExecutor commandExcutor;
		private List<String> commands;
		private Text commandInfo;
		private CommandList(String permission,CommandExecutor commandExcutor,Text cInfo,String... commands) {
			this.permission=permission;
			this.commandExcutor=commandExcutor;
			this.commandInfo=cInfo;
			this.commands=Arrays.asList(commands);
		}
	}
	
	public static void regCommand(GetDropsPlugin plugin) {
		
		Builder getdrops=CommandSpec.builder()
				.description(Text.of("getdrops plugin info"))
			    .permission("getdrops.info.base");
		
		Sponge.getCommandManager().register(plugin, setChilds(getdrops).build(), "getdrops", "gdrops");
	}
	
	/**
	 * 在该builder下添加子命令
	 * @param getdrops
	 * @return
	 */
	public static Builder setChilds(Builder getdrops) {
		for(CommandList childCommand:CommandList.values()) {
			getdrops.child(CommandSpec.builder()
					.description(childCommand.commandInfo)
					.permission(childCommand.permission)
					.executor(childCommand.commandExcutor)
					.build()
					, childCommand.commands);
		}
		return getdrops;
	}
}
