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
				<li><a class="orange-text text-lighten-4" href="/players/leaderboard">Player Leaderboard</a></li>
				<li><a class="orange-text text-lighten-4" href="/teams">Teams</a></li>
				<li class="active"><a class="orange-text text-lighten-2" href="/teams/leaderboard">Team Leaderboard</a></li>
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
			<#list leaderboard as team>
				<tr>
					<td>${team.name!"Unknown"}</td>
					<td>${team.stars!"0"}</td>
					<td>${team.percentage?string.percent!"0%"}</td>
					<td>${team.ships!"0"}</td>
					<td>${team.economy!"0"}</td>
					<td>${team.economy_turn!"0"}</td>
					<td>${team.industry!"0"}</td>
					<td>${team.industry_turn!"0"}</td>
					<td>${team.science!"0"}</td>
				</tr>
			</#list>
		</tbody>
	</table>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</body>
</html>