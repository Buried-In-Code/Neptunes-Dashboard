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
				<li>
					<a href="/players">Players</a>
				</li>
				<li class="active">
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
	<table class="grey darken-2 white-text">
		<thead>
			<tr>
				<th>Name</th>
				<th>Alias</th>
				<#if leaderboard[0].team != "Unknown">
					<th>Team</th>
				</#if>
				<th>Stars</th>
				<th>%</th>
				<th>Ships</th>
				<th>Economy</th>
				<th>$/Turn</th>
				<th>Industry</th>
				<th>Ships/Turn</th>
				<th>Science</th>
			</tr>
		</thead>
		<tbody>
			<#list leaderboard as player>
				<tr>
					<td>${player.name!"Unknown"}</td>
					<td>${player.alias!"Unknown"}</td>
					<#if player.team != "Unknown">
						<td>${player.team!"None"}</td>
					</#if>
					<td>${player.stars!"0"}</td>
					<td>${player.percentage?string.percent!"0%"}</td>
					<td>${player.ships!"0"}</td>
					<td>${player.economy!"0"}</td>
					<td>${player.economy_turn!"0"}</td>
					<td>${player.industry!"0"}</td>
					<td>${player.industry_turn!"0"}</td>
					<td>${player.science!"0"}</td>
				</tr>
			</#list>
		</tbody>
	</table>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</body>
</html>