<!DOCTYPE html>
<html lang="en">
<head>
	<title>BIT 269's Neptune's Pride</title>
	<meta charset="utf-8">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/semantic.min.css"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/semantic.min.js"></script>
    <link rel="stylesheet" href="../static/css/styles.css"/>
</head>
<body>
	<div class="ui inverted menu">
		<a class="item" href="/">Home</a>
		<a class="item" href="/game">Game</a>
		<a class="item" href="/players">Players</a>
		<a class="active item" href="/teams">Teams</a>
		<a class="item" href="/help">Help</a>
	</div>
	<div class="ui inverted menu">
		<a class="item" href="/teams">List</a>
		<a class="item" href="/teams/leaderboard">Leaderboard</a>
	</div>
	<a class="ui fluid card" href="/teams/${team.name!"Unknown"}">
		<div class="content">
			<div class="header">${team.name!"Unknown"}</div>
			<div class="meta">
				<span>${team.percentage?string.percent!"0%"}</span>
			</div>
			<div class="description">
				<ul>
					<li><b>Stars:</b> ${team.stars!"0"}</li>
					<li><b>Fleet:</b> ${team.fleet!"0"}</li>
					<li><b>Economy:</b> ${team.economy!"0"}</li>
					<li><b>Industry:</b> ${team.industry!"0"}</li>
					<li><b>Science:</b> ${team.science!"0"}</li>
					<li><b>Ships:</b> ${team.ships!"0"}</li>
					<li><b>Technology:</b>
						<ul>
							<li><b>Scanning:</b> ${team.technology.scanning!"0"}</li>
							<li><b>Hyperspace:</b> ${team.technology.hyperspace!"0"}</li>
							<li><b>Terraforming:</b> ${team.technology.terraforming!"0"}</li>
							<li><b>Experimentation:</b> ${team.technology.experimentation!"0"}</li>
							<li><b>Weapons:</b> ${team.technology.weapons!"0"}</li>
							<li><b>Banking:</b> ${team.technology.banking!"0"}</li>
							<li><b>Manufacturing:</b> ${team.technology.manufacturing!"0"}</li>
						</ul>
					</li>
					<li><b>Members:</b>
						<ul>
							<#list team.members as member>
								<li>${member}</li>
							</#list>
						</ul>
					</li>
				</ul>
			</div>
		</div>
	</a>
</body>
</html>