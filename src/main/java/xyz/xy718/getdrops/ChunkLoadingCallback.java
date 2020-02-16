package xyz.xy718.getdrops;

import org.spongepowered.api.world.ChunkTicketManager.Callback;
import org.spongepowered.api.world.ChunkTicketManager.LoadingTicket;
import org.slf4j.Logger;
import org.spongepowered.api.world.ChunkTicketManager;
import org.spongepowered.api.world.World;

import com.google.common.collect.ImmutableList;

public class ChunkLoadingCallback implements ChunkTicketManager.Callback {

    private Logger LOGGER = GetDropsPlugin.LOGGER;
    private final GetDropsPlugin plugin;

    public ChunkLoadingCallback(GetDropsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLoaded(ImmutableList<ChunkTicketManager.LoadingTicket> tickets, World world) {
    	LOGGER.info("tickets加载完成:"+world.getName()+"-"+tickets+"");
    }

}
