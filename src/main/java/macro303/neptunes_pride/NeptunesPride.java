package macro303.neptunes_pride;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.Charset;
import java.util.TreeSet;

/**
 * Created by Macro303 on 2018-04-17.
 */
class NeptunesPride {
	private final static String apiAddress = "http://nptriton.cqproject.net/game/";
	private final static long gameNumber = 4620725739847680L;

	private NeptunesPride() {
		TreeSet<Player> allPlayers = getAllPlayers();
		allPlayers.forEach(this::showPlayer);
	}

	public static void main(@NotNull String... args) {
		new NeptunesPride();
	}

	private void showPlayer(@NotNull Player player) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < player.getAlias().length() + 4; i++) {
			builder.append("=");
		}
		System.out.println(builder.toString());
		System.out.println("  " + player.getAlias() + "  ");
		System.out.println(builder.toString());
		System.out.println("Strength: " + player.getTotalStrength());
		System.out.println("Stars: " + player.getTotalStars());
		System.out.println("Fleets: " + player.getTotalFleets());
		System.out.println("Total Stats: " + player.getTotalStats());
		System.out.println("Technology:");
		System.out.println("\tScanning: " + player.getPlayerTechnology().getScanning().getLevel());
		System.out.println("\tPropulsion: " + player.getPlayerTechnology().getPropulsion().getLevel());
		System.out.println("\tTerraforming: " + player.getPlayerTechnology().getTerraforming().getLevel());
		System.out.println("\tResearch: " + player.getPlayerTechnology().getResearch().getLevel());
		System.out.println("\tWeapons: " + player.getPlayerTechnology().getWeapons().getLevel());
		System.out.println("\tBanking: " + player.getPlayerTechnology().getBanking().getLevel());
		System.out.println("\tManufacturing: " + player.getPlayerTechnology().getManufacturing().getLevel());
	}

	@NotNull
	private TreeSet<Player> getAllPlayers() {
		TreeSet<Player> players = new TreeSet<>();
		HttpURLConnection connection = null;
		try {
			URL url = new URL(apiAddress + gameNumber + "/players");
			try {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("bit.datacom.co.nz", 3128));
				connection = (HttpURLConnection) url.openConnection(proxy);
				connection.setRequestMethod("GET");
				connection.connect();
			}catch (UnknownHostException uhe){
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
			}
			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				String response = readAll(connection.getInputStream());
				Gson gson = new GsonBuilder()
						.serializeNulls()
						.setPrettyPrinting()
						.create();
				AllPlayers temp = gson.fromJson(response, AllPlayers.class);
				players = temp.getPlayers();
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