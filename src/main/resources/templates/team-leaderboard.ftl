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
		<a class="active item" href="/teams/leaderboard">Leaderboard</a>
	</div>
	<table class="ui inverted grey table">
		<thead>
			<tr>
				<th>Name</th>
				<th>Stars</th>
				<th>%</th>
				<th>Ships</th>
				<th>Economy</th>
				<th>$/Turn</th>
				<th>Industry</th>
				<th>Ships/Turn</th>
				<th>Science</th>
			</tr>
		</thead>
		<tbody>
			<#list leaderboard as team>
				<tr>
					<td>${team.name!"Unknown"}</td>
					<td>${team.stars!"0"}</td>
					<td>${team.percentage?string.percent!"0%"}</td>
					<td>${team.ships!"0"}</td>
					<td>${team.economy!"0"}</td>
					<td>${team.economy_turn!"0"}</td>
					<td>${team.industry!"0"}</td>
					<td>${team.industry_turn!"0"}</td>
					<td>${team.science!"0"}</td>
				</tr>
			</#list>
		</tbody>
	</div>
</body>
</html>