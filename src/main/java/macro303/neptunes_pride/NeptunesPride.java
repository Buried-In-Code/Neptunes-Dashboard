package macro303.neptunes_pride;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import macro303.console.Console;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created by Macro303 on 2018-04-17.
 */
class NeptunesPride {
	private static final String apiAddress = "http://nptriton.cqproject.net/game/";
	private Config config;

	private NeptunesPride() {
		loadConfig();
		mainMenu();
	}

	public static void main(@NotNull String... args) {
		new NeptunesPride();
	}

	private void loadConfig() {
		config = Config.loadConfig();
		if (config == null) {
			Config.saveConfig(new Config(-1L));
			config = Config.loadConfig();
		}
	}

	private void mainMenu() {
		int option;
		do {
			option = Console.displayMenu(new String[]{"All Players", "Player By Alias", "Player By Name", "Exit"}, "Main Menu");
			switch (option) {
				case 1:
					TreeSet<Player> allPlayers = getAllPlayers();
					allPlayers.forEach(this::showPlayer);
					break;
				case 2:
					Player aliasPlayer = getPlayerByAlias(Console.displayPrompt("Alias"));
					showPlayer(aliasPlayer);
					break;
				case 3:
					Player namePlayer = getPlayerByName(Console.displayPrompt("Name"));
					showPlayer(namePlayer);
					break;
			}
		} while (option != 0);
	}

	@Nullable
	private Player getPlayerByAlias(@Nullable String alias) {
		return getAllPlayers().stream().filter(player -> player.getAlias().equalsIgnoreCase(alias)).findFirst().orElse(null);
	}

	@Nullable
	private Player getPlayerByName(@Nullable String name) {
		return getPlayerByAlias(config.getPlayers().get(name));
	}

	private void showPlayer(@Nullable Player player) {
		if(player == null){
			Console.displayWarning("No Player Found");
		}else {
			Console.displayHeading(player.getAlias());
			Console.displayItemValue("Strength", player.getTotalStrength());
			Console.displayItemValue("Stars", player.getTotalStars());
			Console.displayItemValue("Fleets", player.getTotalFleets());
			Console.displayItemValue("Total Stats", player.getTotalStats());
			Console.displayMessage("Technology:");
			Console.displayItemValue("\tScanning", player.getPlayerTechnology().getScanning().getLevel());
			Console.displayItemValue("\tPropulsion", player.getPlayerTechnology().getPropulsion().getLevel());
			Console.displayItemValue("\tTerraforming", player.getPlayerTechnology().getTerraforming().getLevel());
			Console.displayItemValue("\tResearch", player.getPlayerTechnology().getResearch().getLevel());
			Console.displayItemValue("\tWeapons", player.getPlayerTechnology().getWeapons().getLevel());
			Console.displayItemValue("\tBanking", player.getPlayerTechnology().getBanking().getLevel());
			Console.displayItemValue("\tManufacturing", player.getPlayerTechnology().getManufacturing().getLevel());
		}
	}

	private String getApiAddress() {
		return apiAddress + config.getGameID() + "/";
	}

	private HttpURLConnection getConnection(@NotNull String address) throws IOException {
		HttpURLConnection connection;
		URL url = new URL(getApiAddress() + address);
		if (config.getProxyHostname() != null && config.getProxyPort() != -1) {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getProxyHostname(), config.getProxyPort()));
			connection = (HttpURLConnection) url.openConnection(proxy);
		} else {
			connection = (HttpURLConnection) url.openConnection();
		}
		connection.setRequestMethod("GET");
		connection.connect();
		return connection;
	}

	@NotNull
	private TreeSet<Player> getAllPlayers() {
		TreeSet<Player> players = new TreeSet<>();
		HttpURLConnection connection = null;
		try {
			connection = getConnection("players");
			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				String response = readAll(connection.getInputStream());
				Gson gson = new GsonBuilder()
						.serializeNulls()
						.setPrettyPrinting()
						.create();
				Type token = new TypeToken<HashMap<String, Player>>() {
				}.getType();
				/*AllPlayers temp = gson.fromJson(response, AllPlayers.class);
				players = temp.getPlayers();*/
				HashMap<String, Player> temp = gson.fromJson(response, token);
				players = new TreeSet<>(temp.values());
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (connection != null)
				connection.disconnect();
			connection = null;
		}
		return players;
	}

	@NotNull
	private String readAll(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
		StringBuilder builder = new StringBuilder();
		int cp;
		while ((cp = reader.read()) != -1)
			builder.append((char) cp);
		return builder.toString();
	}
}