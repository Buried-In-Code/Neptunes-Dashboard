package macro.dashboard.neptunes

import kong.unirest.json.JSONObject
import macro.dashboard.neptunes.tick.Tick
import macro.dashboard.neptunes.tick.TickTable
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.Player
import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.team.Team
import macro.dashboard.neptunes.team.TeamTable
import org.apache.logging.log4j.LogManager
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Created by Macro303 on 2019-Nov-25
 */
object Triton {
	private val LOGGER = LogManager.getLogger(this::class.java)
	private val URL = "https://np.ironhelmet.com/api"

	@Suppress("UNCHECKED_CAST")
	fun getGame(gameId: Long, code: String) {
		val request = Util.postRequest(url = URL, gameId = gameId, code = code) ?: return
		val response = request.`object`.getJSONObject("scanning_data")
		val game = parseGame(gameId, response) ?: return
		parsePlayers(gameId, game.tick, response.getJSONObject("players"))
	}

	private fun parseGame(gameId: Long, json: JSONObject): Game? {
		val isPaused = json.getBoolean("paused")
		val productions = json.getInt("productions")
		val tickFragment = json.getInt("tick_fragment")
		val isGameOver = json.getInt("game_over") != 0
		val isStarted = json.getBoolean("started")
		val startTime = LocalDateTime.ofInstant(
			Instant.ofEpochMilli(json.getLong("start_time")),
			ZoneId.of("Pacific/Auckland")
		)
		val productionCounter = json.getInt("production_counter")
		val tick = json.getInt("tick")
		val war = json.getInt("war")
		val turnTimeout = LocalDateTime.ofInstant(
			Instant.ofEpochMilli(json.getLong("turn_based_time_out")),
			ZoneId.of("Pacific/Auckland")
		)
		val game = GameTable.select(gameId = gameId) ?: Game(
			ID = gameId,
			fleetSpeed = json.getDouble("fleet_speed"),
			isPaused = isPaused,
			productions = productions,
			tickFragment = tickFragment,
			tickRate = json.getInt("tick_rate"),
			productionRate = json.getInt("production_rate"),
			victoryStars = json.getInt("stars_for_victory"),
			isGameOver = isGameOver,
			isStarted = isStarted,
			startTime = startTime,
			totalStars = json.getInt("total_stars"),
			productionCounter = productionCounter,
			isTradeScanned = json.getInt("trade_scanned") == 0,
			tick = tick,
			tradeCost = json.getInt("trade_cost"),
			name = json.getString("name"),
			isTurnBased = json.getInt("turn_based") != 0,
			war = war,
			turnTimeout = turnTimeout,
			gameType = "Triton"
		).insert()

		return game.apply {
			this.isPaused = isPaused
			this.productions = productions
			this.tickFragment = tickFragment
			this.isGameOver = isGameOver
			this.isStarted = isStarted
			this.startTime = startTime
			this.productionCounter = productionCounter
			this.tick = tick
			this.war = war
			this.turnTimeout = turnTimeout
		}.update()
	}

	private fun parsePlayers(gameId: Long, tick: Int, json: JSONObject) {
		json.keySet().forEach {
			val playerObj = json.getJSONObject(it)
			val alias = playerObj.getString("alias")
			if (alias.isNullOrEmpty())
				return@forEach
			val team = TeamTable.select(gameId = gameId, name = "Free For All") ?: Team(
				gameId = gameId,
				name = "Free For All"
			).insert()
			val player = PlayerTable.select(gameId = gameId, alias = alias) ?: Player(
				gameId = gameId,
				alias = alias,
				teamId = team.uuid
			).insert()

			val industry = playerObj.getInt("total_industry")
			val science = playerObj.getInt("total_science")
			val stars = playerObj.getInt("total_stars")
			val fleets = playerObj.getInt("total_fleets")
			val ships = playerObj.getInt("total_strength")
			val isActive = playerObj.getInt("conceded") == 0
			val economy = playerObj.getInt("total_economy")
			val techObj = playerObj.getJSONObject("tech")
			val scanning = techObj.getJSONObject("scanning").getInt("level")
			val propulsion = techObj.getJSONObject("propulsion").getInt("level")
			val terraforming = techObj.getJSONObject("terraforming").getInt("level")
			val research = techObj.getJSONObject("research").getInt("level")
			val weapons = techObj.getJSONObject("weapons").getInt("level")
			val banking = techObj.getJSONObject("banking").getInt("level")
			val manufacturing = techObj.getJSONObject("manufacturing").getInt("level")
			val cycle = TickTable.select(gameId = gameId, playerId = player.alias, tick = tick) ?: Tick(
				gameId = gameId,
				playerId = player.alias,
				tick = tick,
				industry = industry,
				science = science,
				stars = stars,
				fleets = fleets,
				ships = ships,
				isActive = isActive,
				economy = economy,
				scanning = scanning,
				propulsion = propulsion,
				terraforming = terraforming,
				research = research,
				weapons = weapons,
				banking = banking,
				manufacturing = manufacturing
			).insert()

			cycle.apply {
				this.industry = industry
				this.science = science
				this.stars = stars
				this.fleets = fleets
				this.ships = ships
				this.isActive = isActive
				this.economy = economy
				this.scanning = scanning
				this.propulsion = propulsion
				this.terraforming = terraforming
				this.research = research
				this.weapons = weapons
				this.banking = banking
				this.manufacturing = manufacturing
			}.update()
		}
	}
}