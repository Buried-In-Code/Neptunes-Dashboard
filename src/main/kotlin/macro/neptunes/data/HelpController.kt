package macro.neptunes.data

import io.javalin.Context
import macro.neptunes.core.Util

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object HelpController {

	fun get(context: Context) {
		var data = """
			<div style="padding: 10px">
				<h1>BIT 269's Neptune's Pride</h1>
				<p><i>Put a description here</i></p>
				<div class="card-columns">
					<div class="card bg-dark text-white">
						<div class="card-header">
							<h2><code>GET <a href="/">/</a></code></h2>
						</div>
						<div class="card-body">
							<p class="card-text"><i>Put a description here</i></p>
						</div>
						<div class="card-footer">
							<p><b>Response Code:</b> 200</p>
						</div>
					</div>
					<div id="GETGame" class="card bg-dark text-white">
						<div class="card-header">
							<h2><code>GET <a href="/game">/game</a></code></h2>
						</div>
						<div class="card-body">
							<p class="card-text"><i>Put a description here</i></p>
						</div>
						<div class="card-footer">
							<p><b>Response Code:</b> 200</p>
						</div>
					</div>
					<div id="GETPlayers" class="card bg-dark text-white">
						<div class="card-header">
							<h2><code>GET <a href="/players">/players</a></code></h2>
						</div>
						<div class="card-body">
							<p class="card-text"><i>Put a description here</i></p>
							<table class="table table-dark table-striped table-sm">
								<tr>
									<th>Field</th>
									<th>Sort</th>
									<th>Filter</th>
								</tr>
								<tr>
									<td>Name</td>
									<td>&#x2714;</td>
									<td>&#x2714;</td>
								</tr>
								<tr>
									<td>Alias</td>
									<td>&#x2714;</td>
									<td>&#x2714;</td>
								</tr>
								<tr>
									<td>Team</td>
									<td>&#x2714;</td>
									<td>&#x2714;</td>
								</tr>
								<tr>
									<td>Stars</td>
									<td>&#x2714;</td>
									<td>&#x2718;</td>
								</tr>
								<tr>
									<td>Ships</td>
									<td>&#x2714;</td>
									<td>&#x2718;</td>
								</tr>
							</table>
						</div>
						<div class="card-footer">
							<p><b>Response Code:</b> 200</p>
						</div>
					</div>
					<div id="POSTPlayer" class="card bg-dark text-white">
						<div class="card-header">
							<h2><code>POST <a href="/players">/players</a></code></h2>
						</div>
						<div class="card-body">
							<p class="card-text"><i>Put a description here</i></p>
							<p><b>Example Body:</b> <code>[{ "Name": "Alias" }, { "Name": "Alias" }]</code></p>
						</div>
						<div class="card-footer">
							<p><b>Response Code:</b> 204</p>
						</div>
					</div>
					<div id="GETPlayerLeaderboard" class="card bg-dark text-white">
						<div class="card-header">
							<h2><code>GET <a href="/players/leaderboard">/players/leaderboard</a></code></h2>
						</div>
						<div class="card-body">
							<p class="card-text"><i>Put a description here</i></p>
							<table class="table table-dark table-striped table-sm">
								<tr>
									<th>Field</th>
									<th>Sort</th>
									<th>Filter</th>
								</tr>
								<tr>
									<td>Name</td>
									<td>&#x2714;</td>
									<td>&#x2714;</td>
								</tr>
								<tr>
									<td>Alias</td>
									<td>&#x2714;</td>
									<td>&#x2714;</td>
								</tr>
								<tr>
									<td>Team</td>
									<td>&#x2714;</td>
									<td>&#x2714;</td>
								</tr>
								<tr>
									<td>Stars</td>
									<td>&#x2714;</td>
									<td>&#x2718;</td>
								</tr>
								<tr>
									<td>Ships</td>
									<td>&#x2714;</td>
									<td>&#x2718;</td>
								</tr>
							</table>
						</div>
						<div class="card-footer">
							<p><b>Response Code:</b> 200</p>
						</div>
					</div>
					<div id="GETPlayer" class="card bg-dark text-white">
						<div class="card-header">
							<h2><code>GET <a href="/players/Macro303">/players/:alias</a></code></h2>
						</div>
						<div class="card-body">
							<p class="card-text"><i>Put a description here</i></p>
						</div>
						<div class="card-footer">
							<p><b>Response Code:</b> 200</p>
						</div>
					</div>
					<div id="GETTeams" class="card bg-dark text-white">
						<div class="card-header">
							<h2><code>GET <a href="/teams">/teams</a></code></h2>
						</div>
						<div class="card-body">
							<p class="card-text"><i>Put a description here</i></p>
							<table class="table table-dark table-striped table-sm">
								<tr>
									<th>Field</th>
									<th>Sort</th>
									<th>Filter</th>
								</tr>
								<tr>
									<td>Name</td>
									<td>&#x2714;</td>
									<td>&#x2714;</td>
								</tr>
								<tr>
									<td>Player-Name</td>
									<td>&#x2718;</td>
									<td>&#x2714;</td>
								</tr>
								<tr>
									<td>Player-Alias</td>
									<td>&#x2718;</td>
									<td>&#x2714;</td>
								</tr>
								<tr>
									<td>Stars</td>
									<td>&#x2714;</td>
									<td>&#x2718;</td>
								</tr>
								<tr>
									<td>Ships</td>
									<td>&#x2714;</td>
									<td>&#x2718;</td>
								</tr>
							</table>
						</div>
						<div class="card-footer">
							<p><b>Response Code:</b> 200</p>
						</div>
					</div>
					<div id="POSTTeam" class="card bg-dark text-white">
						<div class="card-header">
							<h2><code>POST <a href="/teams">/teams</a></code></h2>
						</div>
						<div class="card-body">
							<p class="card-text"><i>Put a description here</i></p>
							<p><b>Example Body:</b> <code>[ "Name", "Name" ]</code></p>
						</div>
						<div class="card-footer">
							<p><b>Response Code:</b> 204</p>
						</div>
					</div>
					<div id="GETTeamLeaderboard" class="card bg-dark text-white">
						<div class="card-header">
							<h2><code>GET <a href="/teams/leaderboard">/teams/leaderboard</a></code></h2>
						</div>
						<div class="card-body">
							<p class="card-text"><i>Put a description here</i></p>
							<table class="table table-dark table-striped table-sm">
								<tr>
									<th>Field</th>
									<th>Sort</th>
									<th>Filter</th>
								</tr>
								<tr>
									<td>Name</td>
									<td>&#x2714;</td>
									<td>&#x2714;</td>
								</tr>
								<tr>
									<td>Player-Name</td>
									<td>&#x2718;</td>
									<td>&#x2714;</td>
								</tr>
								<tr>
									<td>Player-Alias</td>
									<td>&#x2718;</td>
									<td>&#x2714;</td>
								</tr>
								<tr>
									<td>Stars</td>
									<td>&#x2714;</td>
									<td>&#x2718;</td>
								</tr>
								<tr>
									<td>Ships</td>
									<td>&#x2714;</td>
									<td>&#x2718;</td>
								</tr>
							</table>
						</div>
						<div class="card-footer">
							<p><b>Response Code:</b> 200</p>
						</div>
					</div>
					<div id="GETTeam" class="card bg-dark text-white">
						<div class="card-header">
							<h2><code>GET <a href="/teams/Team%20A">/teams/:name</a></code></h2>
						</div>
						<div class="card-body">
							<p class="card-text"><i>Put a description here</i></p>
						</div>
						<div class="card-footer">
							<p><b>Response Code:</b> 200</p>
						</div>
					</div>
					<div id="GETRefresh" class="card bg-dark text-white">
						<div class="card-header">
							<h2><code>GET <a href="/refresh">/refresh</a></code></h2>
						</div>
						<div class="card-body">
							<p class="card-text"><i>Put a description here</i></p>
						</div>
						<div class="card-footer">
							<p><b>Response Code:</b> 204</p>
						</div>
					</div>
					<div id="GETConfig" class="card bg-dark text-white">
						<div class="card-header">
							<h2><code>GET <a href="/config">/config</a></code></h2>
						</div>
						<div class="card-body">
							<p class="card-text"><i>Put a description here</i></p>
						</div>
						<div class="card-footer">
							<p><b>Response Code:</b> 418</p>
						</div>
					</div>
					<div id="PATCHConfig" class="card bg-dark text-white">
						<div class="card-header">
							<h2><code>PATCH <a href="/config">/config</a></code></h2>
						</div>
						<div class="card-body">
							<p class="card-text"><i>Put a description here</i></p>
						</div>
						<div class="card-footer">
							<p><b>Response Code:</b> 418</p>
						</div>
					</div>
					<div id="GETHelp" class="card bg-dark text-white">
						<div class="card-header">
							<h2><code>GET <a href="/help">/help</a></code></h2>
						</div>
						<div class="card-body">
							<p class="card-text"><i>Put a description here</i></p>
						</div>
						<div class="card-footer">
							<p><b>Response Code:</b> 200</p>
						</div>
					</div>
				</div>
			</div>
		""".trimIndent()
		context.html(Util.addHTML(bodyHTML = data, title = "BIT 269's Neptune's Pride"))
	}
}