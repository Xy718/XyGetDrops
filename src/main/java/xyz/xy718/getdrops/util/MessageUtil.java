package xyz.xy718.getdrops.util;

import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

public class MessageUtil {

	
	public static void send(UUID pUUID,Text text) {
		Sponge.getServer().getPlayer(pUUID).get().sendMessage(text);
	}
}
