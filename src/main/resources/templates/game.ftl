<!DOCTYPE html>
<html lang="en">
<head>
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
				<li class="active"><a class="orange-text text-lighten-2" href="/game">Game</a></li>
				<li><a class="orange-text text-lighten-4" href="/players">Players</a></li>
				<li><a class="orange-text text-lighten-4" href="/players/leaderboard">Player Leaderboard</a></li>
				<li><a class="orange-text text-lighten-4" href="/teams">Teams</a></li>
				<li><a class="orange-text text-lighten-4" href="/teams/leaderboard">Team Leaderboard</a></li>
				<li><a class="orange-text text-lighten-4" href="/help">Help</a></li>
			</ul>
		</div>
	</nav>
</div>
<div class="container">
	<div class="card blue-grey darken-2">
		<div class="card-content grey-text text-lighten-2">
			<span class="card-title blue-text"><strong>${game.name!"Unknown"}</strong></span>
			<ul class="browser-default">
				<li><b>Started:</b> ${game.started?c!"false"}</li>
				<li><b>Paused:</b> ${game.paused?c!"true"}</li>
				<li><b>Stars Required to win:</b> ${game.totalStars/2!"0"}/${game.totalStars!"0"}</li>
			</ul>
		</div>
	</div>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</body>
</html>