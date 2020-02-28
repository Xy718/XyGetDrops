package xyz.xy718.getdrops.util;

import java.util.Optional;

import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.Player;

import xyz.xy718.getdrops.GetDropsPlugin;
import xyz.xy718.getdrops.data.model.Permissions;

public class PlayerUtil {

    private static Logger LOGGER = GetDropsPlugin.LOGGER;
	
	public static Optional<Integer> getPlayerTrackLimit(Player player) {
		Optional<String> optS=getPlayerMetaData(player, Permissions.METATrackLimit);
		if(optS.isPresent()) {
			try {
				return Optional.ofNullable(Integer.parseInt(optS.get()));
			} catch (NumberFormatException e) {
				LOGGER.info("Formatting failed:{}, please check metadata",optS);
				return Optional.empty();
			}
		}
		return Optional.empty();
	}
	
	public static Optional<String> getPlayerMetaData(Player player,String meta) {
		return player.getOption(meta);
	}
}
