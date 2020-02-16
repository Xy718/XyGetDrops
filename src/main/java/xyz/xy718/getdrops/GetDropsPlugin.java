package xyz.xy718.getdrops;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.google.inject.Inject;

import lombok.Getter;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import xyz.xy718.getdrops.command.GetDropsCommandRegister;
import xyz.xy718.getdrops.config.GetDropsConfig;
import xyz.xy718.getdrops.event.AllDropEvent;
import xyz.xy718.getdrops.event.AllPickupEvent;
import xyz.xy718.getdrops.event.ChunkEvent;
@Plugin(id = GetDropsPlugin.PLUGIN_ID
, name = GetDropsPlugin.NAME
, version = GetDropsPlugin.VERSION
, description = "一个简单的掉落物管理插件")
public class GetDropsPlugin {
	@Getter public static final String PLUGIN_ID = "xygetdrops";
	@Getter public static final String NAME = "XyGetDrops Plugin";
	@Getter public static final String VERSION = "@version@";
    
	private static GetDropsPlugin instance;
	
	public static final Logger LOGGER = LoggerFactory.getLogger("XyGetDropsPlugin");
	
	@Inject
	private PluginContainer container;
	@Inject
    @ConfigDir(sharedRoot = false)
    private Path getdropsConfigPath;
	
	@Inject
	@DefaultConfig(sharedRoot = false)
	private ConfigurationLoader<CommentedConfigurationNode> configLoader;

	private static GetDropsConfig mainConfig;
	
    public GetDropsPlugin() {
    	if (instance != null)
			throw new IllegalStateException();
		instance = this;
	}
	
    @Listener
    public void onGamePreStarting(GamePreInitializationEvent event) {
    	LOGGER.info("服务器启动中,先加载配置~");
        // init config
        if (!Files.exists(getdropsConfigPath)) {
            try {
                Files.createDirectories(getdropsConfigPath);
            } catch (IOException e) {
                LOGGER.error("Failed to create main config directory: {}", e);
            }
        }
    	mainConfig=new GetDropsConfig(instance,getdropsConfigPath);
    	LOGGER.info("XyGetDrops当前版本:"+mainConfig.getNode("VERSION").getString("nope"));
    	
    	 // init i18n service
        I18N.setLogger(LOGGER);
        I18N.setPlugin(this);
        String localeStr = mainConfig.getNode("lang").getString();
        if (localeStr == null || localeStr.isEmpty()) {
            localeStr = "zh_CN";
        }
        Locale locale = LocaleUtils.toLocale(localeStr);
        I18N.setLocale(locale);
    }

    @Listener
    public void onGameStarting(GameInitializationEvent event) {
    	LOGGER.info("配置加载完成,GetDrops开始注册事件与指令~");
    	GetDropsCommandRegister.regCommand(this);
    	Sponge.getEventManager().registerListeners(this,new AllDropEvent());
    	Sponge.getEventManager().registerListeners(this,new AllPickupEvent());
    	Sponge.getEventManager().registerListeners(this,new ChunkEvent());
	}
    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    	LOGGER.info("服务器启动成功，XyGetDrops也开始工作了~");
    }
    
    @Listener
    public void onPluginsReload(GameReloadEvent event) {
    	LOGGER.warn("XyGetDrops重新加载~");
    }
	public static GetDropsPlugin get() {
		if (instance == null)
			throw new IllegalStateException("Instance not available");
		return instance;
	}
    public PluginContainer getContainer() {
        return container;
    }
    public static GetDropsConfig getConfig() {
    	return mainConfig;
    }

	public Path getConfigDir() {
		return getdropsConfigPath;
	}
}