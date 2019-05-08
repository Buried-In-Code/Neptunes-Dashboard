<!DOCTYPE html>
<html lang="en">
<head>
	<title>Neptune's Dashboard</title>
	<meta charset="utf-8">
	<meta content="width=device-width, initial-scale=1" name="viewport">
	<link href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css" rel="stylesheet" type="text/css">
	<link href="https://cdn.datatables.net/1.10.18/css/dataTables.semanticui.css" rel="stylesheet" type="text/css"/>
	<link href="/styles.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="navbar"></div>
<div class="ui container">
	<div class="ui inverted segment opacity">
		<h1 class="ui horizontal divider orange header">Welcome to Neptune's Dashboard</h1>
		<div class="ui four stackable cards">
			<div class="ui orange card opacity">
				<div class="content">
					<div class="header">Name</div>
					<div class="description">
						<a href="https://np.ironhelmet.com/game/${ID?c}">${name}</a>
					</div>
				</div>
			</div>
			<div class="ui orange card opacity">
				<div class="content">
					<div class="header">Type</div>
					<div class="description">${gameType}</div>
				</div>
			</div>
			<div class="ui orange card opacity">
				<div class="content">
					<div class="header">Started</div>
					<#if isStarted>
						<div class="description">${startTime}</div>
					<#else>
						<div class="description">false</div>
					</#if>
				</div>
			</div>
			<div class="ui orange card opacity">
				<div class="content">
					<div class="header"># of Players</div>
					<div class="description">${players}</div>
				</div>
			</div>
			<div class="ui orange card opacity">
				<div class="content">
					<div class="header"># of Teams</div>
					<div class="description">${teams}</div>
				</div>
			</div>
			<div class="ui orange card opacity">
				<div class="content">
					<div class="header">Stars to Win</div>
					<div class="description">${victoryStars}/${totalStars}</div>
				</div>
			</div>
			<div class="ui orange card opacity">
				<div class="content">
					<div class="header">Cycles</div>
					<div class="description">${productions}</div>
				</div>
			</div>
			<div class="ui orange card opacity">
				<div class="content">
					<div class="header">Next Turn</div>
					<#if isPaused>
						<div class="description">Paused</div>
					<#elseif isGameOver>
						<div class="description">Game Ended</div>
					<#else>
						<div class="description">${turnTimeout}</div>
					</#if>
				</div>
			</div>
		</div>
	</div>
	<div class="ui inverted container segment opacity">
		<h2 class="ui horizontal divider yellow header">Stars</h2>
		<div class="chart-container">
			<canvas height="400" id="winPie" width="600"></canvas>
		</div>
	</div>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js" type="text/javascript"></script>
<script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js" type="text/javascript"></script>
<script src="https://cdn.datatables.net/1.10.18/js/jquery.dataTables.js" type="text/javascript"></script>
<script src="https://cdn.datatables.net/1.10.18/js/dataTables.semanticui.js" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.3/Chart.min.js" type="text/javascript"></script>
<script src="https://cdn.jsdelivr.net/npm/patternomaly@1.3.2/dist/patternomaly.min.js" type="text/javascript"></script>
<script src="/script.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#navbar").load("/navbar.html");
	console.log("GameID: " + ${ID?c});
	getAllTeamStars(${ID?c}, ${totalStars});
	$('#gameSelection').dropdown();
});
</script>
</body>
</html>