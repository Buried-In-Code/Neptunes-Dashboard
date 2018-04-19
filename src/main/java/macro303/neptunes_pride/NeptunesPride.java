package macro303.neptunes_pride;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import macro303.console.Console;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Macro303 on 2018-04-17.
 */
class NeptunesPride {
	private static final String apiAddress = "http://nptriton.cqproject.net/game/";
	private Config config;
	private Game game;

	private NeptunesPride() {
		loadConfig();
		refreshGame();
		mainMenu();
	}

	public static void main(@NotNull String... args) {
		new NeptunesPride();
	}

	private void loadConfig() {
		config = Config.loadConfig();
		if (config == null) {
			Config.saveConfig(new Config(-1L));
			Console.displayError("Config couldn't be loaded new config has been created. Please add your details and reload");
		}
	}

	private void mainMenu() {
		int option;
		do {
			option = Console.displayMenu(new String[]{"Game Settings", "Fleet Menu", "Star Menu", "Player Menu", "Refresh", "Exit"}, "Main Menu");
			switch (option) {
				case 1:
					Console.displayWarning("To Be Implemented");
					break;
				case 2:
					Console.displayWarning("To Be Implemented");
					break;
				case 3:
					Console.displayWarning("To Be Implemented");
					break;
				case 4:
					playerMenu();
					break;
				case 5:
					refreshGame();
					break;
			}
		} while (option != 0);
	}

	private void fleetMenu(){
		int option;
		do {
			option = Console.displayMenu(new String[]{"Back"}, "Fleet Menu");
		} while (option != 0);
	}

	private void starMenu(){
		int option;
		do {
			option = Console.displayMenu(new String[]{"Back"}, "Star Menu");
		} while (option != 0);
	}

	private void playerMenu(){
		int option;
		do {
			option = Console.displayMenu(new String[]{"All Players", "Player By Alias", "Player By Name", "Back"}, "Player Menu");
			switch (option) {
				case 1:
					game.getPlayers().forEach(this::showPlayer);
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

	private void refreshGame() {
		HttpURLConnection connection = null;
		try {
			connection = getConnection("full");
			if (connection != null) {
				int responseCode = connection.getResponseCode();
				if (responseCode == 200) {
					Console.displayMessage("Pulling Information from Server.....");
					String response = readAll(connection.getInputStream());
					Gson gson = new GsonBuilder()
							.serializeNulls()
							.setPrettyPrinting()
							.disableHtmlEscaping()
							.create();
					game = gson.fromJson(response, Game.class);
					game.format();
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	@Nullable
	private Player getPlayerByAlias(@Nullable String alias) {
		return game.getPlayers().stream().filter(player -> player.getAlias().equalsIgnoreCase(alias)).findFirst().orElse(null);
	}

	@Nullable
	private Player getPlayerByName(@Nullable String name) {
		return getPlayerByAlias(config.getPlayers().get(name));
	}

	private void showPlayer(@Nullable Player player) {
		if (player == null) {
			Console.displayWarning("No Player Found");
		} else {
			Console.displayHeading(player.getAlias());
			Optional<Map.Entry<String, String>> name = config.getPlayers().entrySet().stream().filter(item -> item.getValue().equalsIgnoreCase(player.getAlias())).findFirst();
			name.ifPresent(item -> Console.displayItemValue("Name", item.getKey()));
			Console.displayItemValue("Strength", player.getTotalStrength());
			Console.displayItemValue("Stars", player.getTotalStars());
			Console.displayItemValue("Fleets", player.getTotalFleets());
			Console.displayItemValue("Total Stats", player.getTotalStats());
			Console.displayMessage("Technology Levels:");
			Console.displayItemValue("\tScanning", player.getTechnologyMap().get("scanning").getLevel());
			Console.displayItemValue("\tPropulsion", player.getTechnologyMap().get("propulsion").getLevel());
			Console.displayItemValue("\tTerraforming", player.getTechnologyMap().get("terraforming").getLevel());
			Console.displayItemValue("\tResearch", player.getTechnologyMap().get("research").getLevel());
			Console.displayItemValue("\tWeapons", player.getTechnologyMap().get("weapons").getLevel());
			Console.displayItemValue("\tBanking", player.getTechnologyMap().get("banking").getLevel());
			Console.displayItemValue("\tManufacturing", player.getTechnologyMap().get("manufacturing").getLevel());
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
		try {
			Console.displayMessage("Connecting to Server.....");
			connection.connect();
		} catch (ConnectException ce) {
			Console.displayError("Unable to Connect to API");
			connection = null;
		}
		return connection;
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