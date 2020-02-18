package xyz.xy718.getdrops.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

public enum MessageText {
	SUCCESS_FIND_ITEM("您已成功寻回物品"),
	SUCCESS_PICK_ITEM_COUNT("成功捡起{0}*{1}"),
	FAIL_PICK_COUNT("失败[{}]件"),
	ACTION_CANCEL("放入背包的动作被取消了"),
	UNKOWN("我也不知道发生了啥~"),
	DONT_NEED_ANYPICKUP("&c您没有待拾取的物品"),
	SURPLUS_DROPITEMS_COUNT("剩余[{}]个物品"),
	LIST_TRACKING_COUNT("当前您的列表中有[{0}{1}&f]个待拾取物品"),
	;
	private String messageStr;
	private List<Object> placeHolders;
	
	MessageText(String string) {
		this.placeHolders=new ArrayList<Object>();
		this.messageStr=string;
	}
	public MessageText setDropsItemCount(int count) {
		this.placeHolders=new ArrayList<Object>();
		this.placeHolders.add(count);
		return this;
	}
	public void send(Player player) {
		player.sendMessage(
				Text.of(TextSerializers.FORMATTING_CODE.deserialize(
						MessageFormat.format(this.messageStr, placeHolders.toArray()))));
	}
	public void send(UUID playerUUID) {
		this.send(Sponge.getServer().getPlayer(playerUUID).get());
	}
	public MessageText setItemAndCount(String name, int quantity) {
		this.placeHolders=new ArrayList<Object>();
		this.placeHolders.add(name);
		this.placeHolders.add(quantity);
		return this;
	}
	
	public MessageText setColorAndCount(String color, int count) {
		this.placeHolders=new ArrayList<Object>();
		this.placeHolders.add(color);
		this.placeHolders.add(count);
		return this;
	}
}
