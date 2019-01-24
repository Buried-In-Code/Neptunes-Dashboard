<!DOCTYPE html>
<html lang="en">
<head>
	<title>BIT 269's Neptune's Pride</title>
	<meta charset="utf-8">
	<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
	<link href="/styles.css" rel="stylesheet"/>
</head>
<body>
<div class="ui relaxed stackable grid">
	<div class="three wide column">
		<div class="ui large left fixed inverted vertical menu">
			<h1 class="ui center aligned icon orange inverted header">
				<img class="circular icon" src="/favicon.ico"/>Neptune's Pride
			</h1>
			<a class="item" href="/">
				<i class="home icon"></i>Home
			</a>
			<a class="item" href="/game">
				<i class="gamepad icon"></i>Game
			</a>
			<a class="item" href="/players">
				<i class="user icon"></i>Players
			</a>
			<a class="item" href="/players/leaderboard">
				<i class="list ol icon"></i>Player Leaderboard
			</a>
			<a class="item" href="/teams">
				<i class="users icon"></i>Teams
			</a>
			<a class="item active" href="/teams/leaderboard">
				<i class="list ol icon"></i>Team Leaderboard
			</a>
			<a class="item" href="/settings">
				<i class="cogs icon"></i>Settings
			</a>
			<a class="item" href="/help">
				<i class="question circle icon"></i>Help
			</a>
		</div>
	</div>
	<div class="twelve wide stretched column" style="margin-top: 25px; margin-bottom: 25px;">
		<table class="ui sortable very simple inverted table opacity">
			<thead>
				<tr>
					<th>Name</th>
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
				<#list Leaderboard as team>
					<tr>
						<td>${team.name}</td>
						<td data-sort-value=${team.totalStars?c}>${team.totalStars}</td>
						<td data-sort-value=${team.totalShips?c}>${team.totalShips}</td>
						<td data-sort-value=${team.totalEconomy?c}>${team.totalEconomy}</td>
						<td data-sort-value=${team.economyTurn?c}>${team.economyTurn}</td>
						<td data-sort-value=${team.totalIndustry?c}>${team.totalIndustry}</td>
						<td data-sort-value=${team.industryTurn?c}>${team.industryTurn}</td>
						<td data-sort-value=${team.totalScience?c}>${team.totalScience}</td>
					</tr>
				</#list>
			</tbody>
		</table>
	</div>
</div>
<script src="https://code.jquery.com/jquery-3.3.1.min.js" type="text/javascript"></script>
<script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.3/Chart.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@0.4.0"></script>
<script src="/table-sort.js"></script>
<script src="/script.js"></script>
<script>
$(document).ready(function(){
	$('table').tablesort();
});
</script>
</body>
</html>