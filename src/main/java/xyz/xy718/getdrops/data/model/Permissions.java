package xyz.xy718.getdrops.data.model;

import java.util.Arrays;
import java.util.List;

import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import xyz.xy718.getdrops.command.excutor.ListCommandExcutor;
import xyz.xy718.getdrops.command.excutor.PickmeCommandExcutor;

public class Permissions {
	/** 查看掉落物列表的权限*/
	public static String GetDropsListmePMS="getdrops.user.list.me";
	/** 捡起自己掉落物的权限*/
	public static String GetDropsPickmePMS="getdrops.user.pickme.base";
	/** 能够拥有追踪物品的权限*/
	public static String GetDropsTrackPMS="getdrops.user.track";
}
