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
		<li class="active">
			<a class="orange-text text-lighten-2" href="/players/leaderboard">
				<i class="material-icons orange-text text-lighten-2">list</i>Player Leaderboard
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
<div style="margin-right: 20px;">
	<table class="white">
		<thead>
			<tr>
				<!--<th><a class="btn-flat blue-text" href="?sort=Name">Name</a></th>
				<th><a class="btn-flat blue-text" href="?sort=Alias">Alias</a></th>
				<#if leaderboard[0].team != "Unknown">
					<th><a class="btn-flat blue-text" href="?sort=Team">Team</a></th>
				</#if>
				<th><a class="btn-flat blue-text" href="?sort=Stars">Stars</a></th>
				<th><a class="btn-flat blue-text" href="?sort=Ships">Ships</a></th>
				<th><a class="btn-flat blue-text" href="?sort=Economy">Economy</a></th>
				<th><a class="btn-flat blue-text disabled">$/Turn</a></th>
				<th><a class="btn-flat blue-text" href="?sort=Industry">Industry</a></th>
				<th><a class="btn-flat blue-text disabled">Ships/Turn</a></th>
				<th><a class="btn-flat blue-text" href="?sort=Science">Science</a></th>-->
				<th>Name</th>
				<th>Alias</th>
				<#if leaderboard[0].team != "Unknown">
					<th>Team</th>
				</#if>
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
			<#list leaderboard as player>
				<tr>
					<td>${player.name!"Unknown"}</td>
					<td>${player.alias!"Unknown"}</td>
					<#if player.team != "Unknown">
						<td>${player.team!"None"}</td>
					</#if>
					<td>${player.stars!"0"}</td>
					<td>${player.ships!"0"}</td>
					<td>${player.economy!"0"}</td>
					<td>${player.economyTurn!"0"}</td>
					<td>${player.industry!"0"}</td>
					<td>${player.industryTurn!"0"}</td>
					<td>${player.science!"0"}</td>
				</tr>
			</#list>
		</tbody>
	</table>
</div>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.31.1/js/jquery.tablesorter.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.31.1/js/jquery.tablesorter.widgets.js"></script>
<script src="/script.js"></script>
</body>
</html>