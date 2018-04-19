package macro303.neptunes_pride;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;

/**
 * Created by Macro303 on 2018-04-18.
 */
class Config {
	private transient static final Gson gson = new GsonBuilder()
			.serializeNulls()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create();
	private transient static final File configFile = new File("bin", "config.json");

	private long gameID;
	@NotNull
	private HashMap<String, String> players = new HashMap<>();
	@Nullable
	private String proxyHostname = null;
	private int proxyPort = -1;

	Config(long gameID) {
		this.gameID = gameID;
	}

	static Config loadConfig() {
		if (!configFile.getParentFile().exists())
			configFile.getParentFile().mkdirs();
		Config config = null;
		try (FileReader fr = new FileReader(configFile)) {
			config = gson.fromJson(fr, Config.class);
		} catch (FileNotFoundException ignored) {
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (config == null) {
				saveConfig(new Config(1));
				config = loadConfig();
			}
		}
		return config;
	}

	static void saveConfig(@NotNull Config config) {
		try (FileWriter fw = new FileWriter(configFile)) {
			gson.toJson(config, fw);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	long getGameID() {
		return gameID;
	}

	void setGameID(long gameID) {
		this.gameID = gameID;
	}

	@NotNull
	HashMap<String, String> getPlayers() {
		return players;
	}

	void setPlayers(@NotNull HashMap<String, String> players) {
		this.players = players;
	}

	@Nullable
	String getProxyHostname() {
		return proxyHostname;
	}

	void setProxyHostname(@Nullable String proxyHostname) {
		this.proxyHostname = proxyHostname;
	}

	int getProxyPort() {
		return proxyPort;
	}

	void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Config)) return false;

		Config config = (Config) o;

		if (gameID != config.gameID) return false;
		if (proxyPort != config.proxyPort) return false;
		if (!players.equals(config.players)) return false;
		return proxyHostname != null ? proxyHostname.equals(config.proxyHostname) : config.proxyHostname == null;
	}

	@Override
	public int hashCode() {
		int result = (int) (gameID ^ (gameID >>> 32));
		result = 31 * result + players.hashCode();
		result = 31 * result + (proxyHostname != null ? proxyHostname.hashCode() : 0);
		result = 31 * result + proxyPort;
		return result;
	}

	@Override
	public String toString() {
		return "Config{" +
				"gameID=" + gameID +
				", players=" + players +
				", proxyHostname='" + proxyHostname + '\'' +
				", proxyPort=" + proxyPort +
				'}';
	}
}