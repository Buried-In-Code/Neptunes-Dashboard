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
			<a class="item" href="/players/leaderboard">
				<i class="list ol icon"></i>Player Leaderboard
			</a>
			<a class="item" href="/teams">
				<i class="users icon"></i>Teams
			</a>
			<a class="item" href="/teams/leaderboard">
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
		<div class="ui negative message opacity">
			<div class="header">${error.code}: ${error.request}</div>
			<p>${error.message}</p>
		</div>
	</div>
</div>
<script src="https://code.jquery.com/jquery-3.3.1.min.js" type="text/javascript"></script>
<script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.3/Chart.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@0.4.0"></script>
<script src="/table-sort.js"></script>
<script src="/script.js"></script>
<script>
</script>
</body>
</html>