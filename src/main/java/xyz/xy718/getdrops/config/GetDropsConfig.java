package xyz.xy718.getdrops.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.spongepowered.api.util.TypeTokens;
import org.spongepowered.api.world.World;


import lombok.Getter;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import xyz.xy718.getdrops.GetDropsPlugin;

public class GetDropsConfig {
	final private String mainConfName = "config.conf";

	private ConfigurationLoader<CommentedConfigurationNode> configLoader;
	private static Logger LOGGER=GetDropsPlugin.LOGGER;
	// formmater:off
	/** 主节点 */
	private CommentedConfigurationNode mainNode = null;
	/** 追踪击杀是否启用 */
	@Getter private boolean trackKill;
	/** 追踪破坏是否启用 */
	@Getter private boolean trackBreak;
	/** 追踪世界列表是否启用 */
	@Getter private boolean trackWorld;
	/** 掉落物保护是否启用 */
	@Getter private boolean itemProtection;
	
	/** 默认击杀追踪模式 */
	@Getter private boolean defaultKillMode;
	private Map<Object, ? extends CommentedConfigurationNode> killTrackList=null;
	/** 默认破坏追踪模式 */
	@Getter private boolean defaultBreakMode;
	private Map<Object, ? extends CommentedConfigurationNode> breakTrackList=null;
	
	/** 追踪世界列表 */
	private List<String> trackingWorldList=null;
	
	@Getter private int protectTime;
	@Getter private boolean defaultProtectMode;
	
	private Map<Object, ? extends CommentedConfigurationNode> itemProtectMap=null;

	// formmater:on
	public GetDropsConfig(GetDropsPlugin gdp, Path configDir) {
        Path path = configDir.resolve(mainConfName);
        this.configLoader = HoconConfigurationLoader.builder().setPath(path).build();
        //看看配置文件是否存在，不存在就新建，存在就更新
        try {
            if (Files.exists(path)) {
                upgradeConf();
            } else {
                gdp.getContainer().getAsset(mainConfName).get().copyToFile(path);
            }
            //重载配置文件
            reload();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }

	/**
	 * 重载配置文件
	 */
	public void reload() {
		//最后写
        try {
        	//主节点(总配置)
            this.mainNode = this.configLoader.load();
            this.trackKill =this.mainNode.getNode("modules").getNode("track-killing").getBoolean(false);
            this.trackBreak =this.mainNode.getNode("modules").getNode("track-breaking").getBoolean(false);
            this.trackWorld =this.mainNode.getNode("modules").getNode("track-world").getBoolean(false);
            this.itemProtection =this.mainNode.getNode("modules").getNode("item-protection").getBoolean(false);
            
            
            this.defaultKillMode=this.mainNode.getNode("track-killing").getNode("track_default-mode").getBoolean(false);
            this.defaultBreakMode=this.mainNode.getNode("track-breaking").getNode("track-default-mode").getBoolean(false);
            
            
            this.breakTrackList=this.mainNode.getNode("track-breaking").getNode("list").getChildrenMap();
            this.killTrackList=this.mainNode.getNode("track-killing").getNode("list").getChildrenMap();
            
            this.protectTime=this.mainNode.getNode("item-protection").getNode("protectTime").getInt(60);
            this.defaultProtectMode=this.mainNode.getNode("item-protection").getNode("protect-item-default-mode").getBoolean(false);
            this.itemProtectMap=this.mainNode.getNode("item-protection").getNode("list").getChildrenMap();
            try {
            	this.trackingWorldList=this.mainNode.getNode("track-world").getList(TypeTokens.STRING_TOKEN);
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
        //TODO 从新插件更新新版配置文件
    }

    public CommentedConfigurationNode getNode(@Nonnull final Object... keys) {
        return this.mainNode.getNode(keys);
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
    	if(!this.trackWorld) {
    		return false;
    	}
    	if(this.trackingWorldList.contains(world.getName())){
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
    	if(!this.trackBreak) {
    		return false;
    	}
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
    	if(!this.trackKill) {
    		return false;
    	}
    	CommentedConfigurationNode trackState=this.killTrackList.get(entityID);
    	if(trackState==null) {
    		return defaultKillMode;
    	}else {
    		return trackState.getBoolean(false);
    	}
    }

    /**
     * 判断该掉落物是否受到保护
     * @param itemId
     * @return
     */
    public boolean isProtectItem(String itemId) {
    	CommentedConfigurationNode node=this.itemProtectMap.get(itemId);
    	if(node==null) {
    		return defaultProtectMode;
    	}else {
    		return node.getBoolean(false);
		}
	}
}
