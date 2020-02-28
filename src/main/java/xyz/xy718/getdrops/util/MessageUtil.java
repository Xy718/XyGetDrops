package xyz.xy718.getdrops.util;

import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;

import xyz.xy718.getdrops.I18N;

public class MessageUtil {

	
	public static void send(UUID pUUID,Text text) {
		Sponge.getServer().getPlayer(pUUID).get().sendMessage(text);
	}
	
	public static void sendLimitToRemoveItem(Player p,String item,int count) {
		p.sendMessage(I18N.getText("list.full.remove", item,count));
	}
}
