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
	<div class="card blue darken-4">
		<div class="card-content white-text">
			<span class="card-title">${message.title}</span>
			<p>${message.content}</p>
		</div>
	</div>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</body>
</html>