package macro303.neptunes_pride;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import macro303.console.Console;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.Charset;

public class Connection extends Task<Game> {
	public static final SimpleObjectProperty<Config> configProperty = new SimpleObjectProperty<>(null);
	public static final SimpleBooleanProperty connectionUnavailableProperty = new SimpleBooleanProperty(true);
	private static final String apiAddress = "http://nptriton.cqproject.net/game/";

	public static void refreshConfig() {
		configProperty.set(Config.loadConfig());
	}

	@Override
	public Game call() throws Exception {
		refreshConfig();
		HttpURLConnection connection = null;
		try {
			connection = getConnection("full");
			if (connection != null) {
				int responseCode = connection.getResponseCode();
				if (responseCode == 200) {
					String response = readAll(connection.getInputStream());
					Gson gson = new GsonBuilder()
							.serializeNulls()
							.setPrettyPrinting()
							.disableHtmlEscaping()
							.create();
					Game game = gson.fromJson(response, Game.class);
					game.format();
					return game;
				} else
					cancel();
			} else
				cancel();
		} finally {
			if (connection != null)
				connection.disconnect();
		}
		cancel();
		return null;
	}

	@Override
	public void succeeded() {
		super.succeeded();
		connectionUnavailableProperty.set(false);
		Console.displayMessage("Successful");
	}

	@Override
	public void cancelled() {
		super.cancelled();
		connectionUnavailableProperty.set(true);
		Console.displayError("Unable to Connect to API");
	}

	@Override
	public void failed() {
		super.failed();
		connectionUnavailableProperty.set(true);
		Console.displayError("Unable to Connect to API: " + getException().getLocalizedMessage());
	}

	private String getApiAddress() {
		return apiAddress + configProperty.getValue().getGameID() + "/";
	}

	private HttpURLConnection getConnection(@NotNull String address) throws IOException {
		HttpURLConnection connection;
		Console.displayMessage("API Address: " + getApiAddress() + address);
		URL url = new URL(getApiAddress() + address);
		if (configProperty.getValue().getProxyHostname() != null && configProperty.getValue().getProxyPort() != -1) {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(configProperty.getValue().getProxyHostname(), configProperty.getValue().getProxyPort()));
			connection = (HttpURLConnection) url.openConnection(proxy);
		} else {
			connection = (HttpURLConnection) url.openConnection();
		}
		connection.setRequestMethod("GET");
		try {
			connection.connect();
			Console.displayMessage("Connected Successfully");
		} catch (ConnectException ce) {
			connection = null;
			Console.displayMessage("Connected Unsuccessfully: " + ce.getLocalizedMessage());
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