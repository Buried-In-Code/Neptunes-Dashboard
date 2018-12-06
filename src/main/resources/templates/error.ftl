<!DOCTYPE html>
<html lang="en">
<head>
	<title>BIT 269's Neptune's Pride</title>
	<meta charset="utf-8">
	<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
	<link href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css" rel="stylesheet">
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	<link href="/styles.css" rel="stylesheet"/>
</head>
<body>
<header>
	<ul class="sidenav sidenav-fixed blue-grey darken-4">
		<li>
			<div class="user-view">
				<img class="circle left" src="/favicon.ico">
				<span class="grey-text name">BIT 269</span>
				<span class="grey-text email">Neptune's Pride</span>
			</div>
		</li>
		<li>
			<a class="orange-text text-lighten-4" href="/">
				<i class="material-icons orange-text text-lighten-4">home</i>Home
			</a>
		</li>
		<li>
			<a class="orange-text text-lighten-4" href="/game">
				<i class="material-icons orange-text text-lighten-4">games</i>Game
			</a>
		</li>
		<li>
			<a class="orange-text text-lighten-4" href="/players">
				<i class="material-icons orange-text text-lighten-4">person</i>Players
			</a>
		</li>
		<li>
			<a class="orange-text text-lighten-4" href="/players/leaderboard">
				<i class="material-icons orange-text text-lighten-4">list</i>Player Leaderboard
			</a>
		</li>
		<li>
			<a class="orange-text text-lighten-4" href="/teams">
				<i class="material-icons orange-text text-lighten-4">group</i>Teams
			</a>
		</li>
		<li>
			<a class="orange-text text-lighten-4" href="/teams/leaderboard">
				<i class="material-icons orange-text text-lighten-4">list</i>Team Leaderboard
			</a>
		</li>
		<li>
			<a class="orange-text text-lighten-4" href="/config">
				<i class="material-icons orange-text text-lighten-4">settings</i>Settings
			</a>
		</li>
		<li>
			<a class="orange-text text-lighten-4" href="/help">
				<i class="material-icons orange-text text-lighten-4">help</i>Help
			</a>
		</li>
	</ul>
</header>
<div class="card opacity red darken-4">
	<div class="card-content red-text text-lighten-4">
		<span class="card-title">${error.code}: ${error.request}</span>
		<p>${error.message}</p>
	</div>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</body>
</html>