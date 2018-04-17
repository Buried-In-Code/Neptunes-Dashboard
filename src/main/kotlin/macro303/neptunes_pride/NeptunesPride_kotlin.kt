package macro303.neptunes_pride

import com.google.gson.GsonBuilder
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL
import java.nio.charset.Charset
import java.util.*

/**
 * Created by Macro303 on 2018-04-17.
 */
internal class NeptunesPride_kotlin private constructor() {

	private val address: String
		get() = "$apiAddress$gameNumber/"

	private val allPlayers: TreeSet<Player_kotlin>
		get() {
			var players = TreeSet<Player_kotlin>()
			var connection: HttpURLConnection? = null
			try {
				val url = URL(address + "players")
				val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("bit.datacom.co.nz", 3128))
				connection = url.openConnection(proxy) as HttpURLConnection
				connection.requestMethod = "GET"
				connection.connect()
				val responseCode = connection.responseCode
				if (responseCode == 200) {
					val response = readAll(connection.inputStream)
					println(response)
					GsonBuilder()
						.serializeNulls()
						.setPrettyPrinting()
						.create().run {
							val result = fromJson(response, AllPlayers_kotlin::class.java)
							players = result.players
						}
				}
			} catch (ioe: IOException) {
				ioe.printStackTrace()
			} finally {
				if (connection != null)
					connection.disconnect()
			}
			return players
		}

	init {
		val allPlayers = allPlayers
		allPlayers.forEach {
			showPlayer(it)
		}
	}

	private fun showPlayer(player: Player_kotlin) {
		println("=".repeat(player.alias?.length ?: 0+4))
		println("  ${player.alias}  ")
		println("=".repeat(player.alias?.length ?: 0+4))
		println("Strength: ${player.totalStrength}")
		println("Stars: ${player.totalStars}")
		println("Fleets: ${player.totalFleets}")
		println("Total Stats: ${player.totalEconomy + player.totalIndustry + player.totalScience}")
		println("Technology:")
		println("\tScanning: ${player.playerTechnology?.scanning?.level ?: 0}")
		println("\tPropulsion: ${player.playerTechnology?.scanning?.level ?: 0}")
		println("\tTerraforming: ${player.playerTechnology?.terraforming?.level ?: 0}")
		println("\tResearch: ${player.playerTechnology?.research?.level ?: 0}")
		println("\tWeapons: ${player.playerTechnology?.weapons?.level ?: 0}")
		println("\tBanking: ${player.playerTechnology?.banking?.level ?: 0}")
		println("\tManufacturing: ${player.playerTechnology?.manufacturing?.level ?: 0}")
	}

	@Throws(IOException::class)
	private fun readAll(stream: InputStream): String {
		val reader = BufferedReader(InputStreamReader(stream, Charset.forName("UTF-8")))
		val builder = StringBuilder()
		var byte: Int
		do {
			byte = reader.read()
			builder.append(byte.toChar())
		} while (byte != -1)
		return builder.toString()
	}

	companion object {
		private const val apiAddress = "http://nptriton.cqproject.net/game/"
		private const val gameNumber = 4620725739847680

		@JvmStatic
		fun main(args: Array<String>) {
			NeptunesPride_kotlin()
		}
	}
}