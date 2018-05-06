package macro303.neptunes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.concurrent.Task;
import macro303.console.Console;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connection extends Task<Game> {
	@NotNull
	private static final Gson gson = new GsonBuilder()
			.serializeNulls()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create();
	@NotNull
	private static final String API = "http://nptriton.cqproject.net/game/";
	@NotNull
	private static Config config = Config.loadConfig();

	@NotNull
	public static Config getConfig() {
		return config;
	}

	@Override
	protected Game call() throws Exception {
		config = Config.loadConfig();
		HttpURLConnection connection = null;
		try {
			connection = getConnection("full");
			if (connection != null && connection.getResponseCode() == 200)
				return gson.fromJson(new InputStreamReader(connection.getInputStream()), Game.class);
			else
				cancel();
		} finally {
			if (connection != null)
				connection.disconnect();
		}
		cancel();
		return null;
	}

	@Override
	protected void succeeded() {
		super.succeeded();
		Console.displayMessage("Successful");
	}

	@Override
	protected void cancelled() {
		super.cancelled();
		Console.displayMessage("Unable to connect to API");
	}

	@Override
	protected void failed() {
		super.failed();
		Console.displayMessage("Exception while connecting to API: " + getException().getLocalizedMessage());
	}

	@Nullable
	private HttpURLConnection getConnection(@NotNull String address) throws IOException {
		Console.displayMessage("API Address: " + API + address);
		URL url = new URL(API + address);
		HttpURLConnection connection;
		if (config.getProxy() == null)
			connection = (HttpURLConnection) url.openConnection();
		else
			connection = (HttpURLConnection) url.openConnection(config.getProxy());
		connection.setRequestMethod("GET");
		try {
			connection.connect();
			Console.displayMessage("Connected Successfully");
		} catch (ConnectException ce) {
			connection = null;
			Console.displayWarning("Connected Unsuccessfully: " + ce.getLocalizedMessage());
		}
		return connection;
	}
}
