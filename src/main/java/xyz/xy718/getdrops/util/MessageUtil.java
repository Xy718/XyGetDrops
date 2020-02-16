package xyz.xy718.getdrops.util;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.network.Message;
import org.spongepowered.api.text.Text;

public class MessageUtil{

	public Message playerMessage;
	public Message consoleMessage;
	public Message commandBlockMessage;
	
	/**
	 * 发送一个Text到指定player<br>
	 * 多此一举？<br>
	 * 意义不明
	 * @param p Player
	 * @param t Text
	 */
	public static void simpleSendToPlayer(
			Player p
			,Text t
			) {
		p.sendMessage(t);
	}

}
