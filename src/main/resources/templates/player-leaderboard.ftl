<!DOCTYPE html>
<html lang="en">
<head>
	<title>BIT 269's Neptune's Pride</title>
	<meta charset="utf-8">
	<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
	<style>
	body {
    	background: url(/background.jpg) no-repeat center center fixed;
    	-webkit-background-size: 100%;
    	-moz-background-size: 100%;
    	-o-background-size: 100%;
    	background-size: 100%;
    }
	</style>
</head>
<body>
<div class="ui fixed inverted vertical menu" style="height: 100%;">
	<h2 class="ui center aligned icon orange inverted header">
		<img class="circular icon" src="/favicon.ico">
		Neptune's Pride
	</h2>
	<a class="item" href="/">
		<i class="home icon"></i>Home
	</a>
	<a class="item" href="/game">
		<i class="gamepad icon"></i>Game
	</a>
	<a class="item" href="/players">
		<i class="user icon"></i>Players
	</a>
	<a class="item active" href="/players/leaderboard">
		<i class="list ol icon"></i>Player Leaderboard
	</a>
	<a class="item" href="/teams">
		<i class="users icon"></i>Teams
	</a>
	<a class="item" href="/teams/leaderboard">
		<i class="list ol icon"></i>Team Leaderboard
	</a>
	<a class="item" href="/config">
		<i class="cogs icon"></i>Settings
	</a>
	<a class="item" href="/help">
		<i class="question circle icon"></i>Help
	</a>
</div>
<div class="ui container">
	<table class="ui sortable very simple inverted table" style="opacity: 0.9;">
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
<script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
<script src="/table-sort.js"></script>
<script>
$(document).ready(function(){
	$('table').tablesort();
});
</script>
</body>
</html>