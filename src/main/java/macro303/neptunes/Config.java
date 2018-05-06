package macro303.neptunes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import macro303.console.Console;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;

public class Config {
	@NotNull
	private static final Gson gson = new GsonBuilder()
			.serializeNulls()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create();
	@NotNull
	private static final File dataFolder = new File("bin");
	@NotNull
	private static final File configFile = new File(getDataFolder(), "config.json");
	@NotNull
	private final Long gameID;
	@NotNull
	private final HashMap<String, String> playerNames;
	@NotNull
	private final HashMap<String, ArrayList<String>> teams;
	@Nullable
	private String proxyHostname = null;
	@Nullable
	private Integer proxyPort = null;

	public Config(@NotNull Long gameID) {
		this.gameID = gameID;
		this.playerNames = new HashMap<>();
		this.teams = new HashMap<>();
	}

	@NotNull
	private static File getDataFolder() {
		if (!dataFolder.exists())
			dataFolder.mkdirs();
		return dataFolder;
	}

	@NotNull
	public static Config loadConfig() {
		Config config = null;
		try {
			FileReader reader = new FileReader(configFile);
			config = gson.fromJson(reader, Config.class);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} finally {
			if (config == null) {
				Console.displayError("Config couldn't be loaded");
				saveConfig(new Config(1L));
				config = loadConfig();
			}
		}
		return config;
	}

	public static void saveConfig(@NotNull Config config) {
		try {
			FileWriter writer = new FileWriter(configFile);
			gson.toJson(config, writer);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Nullable
	public Proxy getProxy() {
		if (proxyHostname == null || proxyPort == null)
			return null;
		return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHostname, proxyPort));
	}

	@NotNull
	public HashMap<String, String> getPlayerNames() {
		return playerNames;
	}

	@NotNull
	public HashMap<String, ArrayList<String>> getTeams() {
		return teams;
	}
}
