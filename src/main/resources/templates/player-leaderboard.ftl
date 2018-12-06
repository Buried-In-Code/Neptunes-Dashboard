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
		<div class="nav-wrapper blue-grey darken-4">
			<ul class="right">
				<li><a class="orange-text text-lighten-4" href="/">Home</a></li>
				<li><a class="orange-text text-lighten-4" href="/game">Game</a></li>
				<li><a class="orange-text text-lighten-4" href="/players">Players</a></li>
				<li class="active"><a class="orange-text text-lighten-2" href="/players/leaderboard">Player Leaderboard</a></li>
				<li><a class="orange-text text-lighten-4" href="/teams">Teams</a></li>
				<li><a class="orange-text text-lighten-4" href="/teams/leaderboard">Team Leaderboard</a></li>
				<li><a class="orange-text text-lighten-4" href="/help">Help</a></li>
			</ul>
		</div>
	</nav>
</div>
<div class="container">
	<table class="blue-grey darken-2 grey-text text-lighten-2">
		<thead>
			<tr>
				<th>Name</th>
				<th>Alias</th>
				<#if leaderboard[0].team != "Unknown">
					<th>Team</th>
				</#if>
				<th>Stars</th>
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
					<td>${player.ships!"0"}</td>
					<td>${player.economy!"0"}</td>
					<td>${player.economyTurn!"0"}</td>
					<td>${player.industry!"0"}</td>
					<td>${player.industryTurn!"0"}</td>
					<td>${player.science!"0"}</td>
				</tr>
			</#list>
		</tbody>
	</table>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</body>
</html>