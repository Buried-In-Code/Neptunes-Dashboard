<!DOCTYPE html>
<html lang="en">
<head>
	<title>BIT 269's Neptune's Pride</title>
	<meta charset="utf-8">
	<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
	<link href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css" rel="stylesheet">
	<link href="/styles.css" rel="stylesheet"/>
</head>
<body>
<div class="navbar-fixed">
	<nav>
		<div class="nav-wrapper grey darken-4">
			<ul class="right hide-on-med-and-down" id="nav-mobile">
				<li>
					<a href="/">Home</a>
				</li>
				<li>
					<a href="/game">Game</a>
				</li>
				<li class="active">
					<a href="/players">Players</a>
				</li>
				<li>
					<a href="/players/leaderboard">Player Leaderboard</a>
				</li>
				<li>
					<a href="/teams">Teams</a>
				</li>
				<li>
					<a href="/teams/leaderboard">Team Leaderboard</a>
				</li>
				<li>
					<a href="/help">Help</a>
				</li>
			</ul>
		</div>
	</nav>
</div>
<div class="container">
	<div class="card grey darken-2">
		<div class="card-content white-text">
			<span class="card-title">${player.name!"Unknown"}</span>
			<ul class="browser-default">
				<li><b>Alias:</b> ${player.alias!"Unknown"}</li>
				<#if player.team != "Unknown">
					<li><b>Team:</b> ${player.team!"None"}</li>
				</#if>
				<li><b>Stars:</b> ${player.stars!"0"}</li>
				<li><b>Percentage:</b> ${player.percentage?string.percent!"0%"}</li>
				<li><b>Fleet:</b> ${player.fleet!"0"}</li>
				<li><b>Economy:</b> ${player.economy!"0"}</li>
				<li><b>Industry:</b> ${player.industry!"0"}</li>
				<li><b>Science:</b> ${player.science!"0"}</li>
				<li><b>Ships:</b> ${player.ships!"0"}</li>
				<li><b>Technology:</b>
					<ul class="browser-default">
						<li><b>Scanning:</b> ${player.technology.scanning!"0"}</li>
						<li><b>Hyperspace:</b> ${player.technology.hyperspace!"0"}</li>
						<li><b>Terraforming:</b> ${player.technology.terraforming!"0"}</li>
						<li><b>Experimentation:</b> ${player.technology.experimentation!"0"}</li>
						<li><b>Weapons:</b> ${player.technology.weapons!"0"}</li>
						<li><b>Banking:</b> ${player.technology.banking!"0"}</li>
						<li><b>Manufacturing:</b> ${player.technology.manufacturing!"0"}</li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</body>
</html>