package xyz.xy718.getdrops.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.util.TypeTokens;
import org.spongepowered.api.world.World;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import xyz.xy718.getdrops.GetDropsPlugin;

public class GetDropsConfig {
	final private String mainConfName = "config.conf";

	private ConfigurationLoader<CommentedConfigurationNode> configLoader;
	private GetDropsPlugin plugin;
	private static Logger LOGGER=GetDropsPlugin.LOGGER;
	private CommentedConfigurationNode mainNode = null;
	
	private CommentedConfigurationNode breakNode = null;
	private CommentedConfigurationNode killNode = null;
	private CommentedConfigurationNode worldNode = null;
	
	private boolean defaultKillMode;
	private boolean defaultBreakMode;
	
	private List<String> trackingWorldList=null;
	private @NonNull Map<Object, ? extends CommentedConfigurationNode> breakTrackList=null;
	private @NonNull Map<Object, ? extends CommentedConfigurationNode> killTrackList=null;
	
	public GetDropsConfig(GetDropsPlugin gdp, Path configDir) {
        Path path = configDir.resolve(mainConfName);
        this.plugin = gdp;
        this.configLoader = HoconConfigurationLoader.builder().setPath(path).build();
        try {
            if (Files.exists(path)) {
                upgradeConf();
            } else {
                gdp.getContainer().getAsset(mainConfName).get().copyToFile(path);
            }
            reload();

        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }

	public void reload() {
		//最后写
        try {
            this.mainNode = this.configLoader.load();
            this.breakNode = this.mainNode.getNode("track_breaking");
            this.killNode = this.mainNode.getNode("track_killing");
            this.worldNode=this.mainNode.getNode("track_world");
            
            this.defaultKillMode=this.killNode.getNode("track_default_mode").getBoolean(false);
            this.defaultBreakMode=this.breakNode.getNode("track_default_mode").getBoolean(false);
            
            this.breakTrackList=this.breakNode.getNode("list").getChildrenMap();
            this.killTrackList=this.killNode.getNode("list").getChildrenMap();
            
            try {
            	this.trackingWorldList=this.worldNode.getList(TypeTokens.STRING_TOKEN);
			} catch (ObjectMappingException e) {
				this.trackingWorldList=new ArrayList<String>();
			}
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.warn("reload failed: {}", mainConfName);
        }
    }
    private void upgradeConf() {
    	LOGGER.info("更新配置文件（假的）");
        // merge config.conf from newer jar/assets.
    }

    public CommentedConfigurationNode getNode(@Nonnull final Object... keys) {
        return this.mainNode.getNode(keys);
    }
    private boolean getModuleIsOpen(String moduleName) {
    	return this.mainNode.getNode("modules").getNode(moduleName).getBoolean(false);
	}
    /**
     * 根据传入的世界判断该世界是否已启用<br>
     * 会先判断modules里的值
     * @param world
     * @return
     */
    public boolean getWorldIsModuled(World world) {
    	if(world==null) {
    		return false;
    	}
    	boolean f=getModuleIsOpen("track_world");
    	if(!f) {
    		return true;
    	}else if(this.trackingWorldList.contains(world.getName())){
    		return true;
    	}
    	return false;
    }
    /**
     * 综合判断该方块是否被追踪(不包含世界)
     * @param blockID
     * @return
     */
    public boolean getIsBreakTracking(String blockID) {
    	CommentedConfigurationNode trackState=this.breakTrackList.get(blockID);
    	if(trackState==null) {
    		return defaultBreakMode;
    	}else {
    		return trackState.getBoolean(false);
    	}
    }
    /**
     * 综合判断该生物是否被追踪(不包含世界)
     * @param entityID
     * @return
     */
    public boolean getIsKillTracking(String entityID) {
    	CommentedConfigurationNode trackState=this.killTrackList.get(entityID);
    	if(trackState==null) {
    		return defaultKillMode;
    	}else {
    		return trackState.getBoolean(false);
    	}
    }
}
