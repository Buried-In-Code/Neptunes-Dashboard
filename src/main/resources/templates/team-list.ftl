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
		<a class="active item" href="/teams">List</a>
		<a class="item" href="/teams/leaderboard">Leaderboard</a>
	</div>
	<div class="ui cards">
		<#list teams as team>
			<a class="card" href="/teams/${team.name!"Unknown"}">
				<div class="content">
					<div class="header">${team.name!"Unknown"}</div>
					<div class="meta">
						<span>${team.percentage?string.percent!"0%"}</span>
					</div>
					<div class="description">
						<ul>
							<li><b>Stars:</b> ${team.stars!"0"}</li>
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
		</#list>
	</div>
</body>
</html>