<!DOCTYPE html>
<html lang="en">
<head>
	<title>BIT 269's Neptune's Pride</title>
	<meta charset="utf-8">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/1.11.8/semantic.min.css"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/1.11.8/semantic.min.js"></script>
</head>
<body>
	<div style="padding: 10px">
		<div class="ui pointing menu">
			<a class="item" href="/">Home</a>
			<a class="item" href="/game">Game</a>
			<a class="active item" href="/players">Players</a>
			<a class="item" href="/teams">Teams</a>
			<a class="item" href="/help">Help</a>
		</div>
		<a class="ui card" href="/players/${player.alias!"Unknown"}">
			<div class="content">
				<div class="header">${player.name!"Unknown"}</div>
				<div class="meta">
					<span>${player.alias!"Unknown"}</span>
					<span>${player.percentage?string.percent!"0%"}</span>
				</div>
				<div class="description">
					<ul>
						<li><b>Team:</b> ${player.team!"None"}</li>
						<li><b>Stars:</b> ${player.stars!"0"}</li>
						<li><b>Economy:</b> ${player.economy!"0"}</li>
						<li><b>Industry:</b> ${player.industry!"0"}</li>
						<li><b>Science:</b> ${player.science!"0"}</li>
					</ul>
				</div>
			</div>
		</a>
	</div>
</body>
</html>