<!DOCTYPE html>
<html lang="en">
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
	<link rel="stylesheet" href="../static/css/styles.css"/>
</head>
<body>
	<div class="navbar-fixed">
		<nav>
			<div class="nav-wrapper grey darken-4">
				<ul id="nav-mobile" class="right hide-on-med-and-down">
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
		<div class="card red darken-4">
			<div class="card-content white-text">
				<span class="card-title">${error.code}: ${error.request}</span>
				<p>${error.message}</p>
			</div>
		</div>
	</div>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</body>
</html>