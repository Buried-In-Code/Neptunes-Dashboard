<!DOCTYPE html>
<html lang="en">
<head>
	<title>BIT 269's Neptune's Pride</title>
	<meta charset="utf-8">
	<meta content="width=device-width, initial-scale=1" name="viewport">
	<link rel="stylesheet" type="text/css" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="/styles.css">
</head>
<body>
<div id="navbar"></div>
<div class="container">
	<div class="card text-white bg-dark mb-4">
		<div class="card-body">
			<h1 class="card-title">${name}</h1>
		</div>
	</div>
	<div class="card-columns mb-4">
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">Total Stars</h3>
				<p class="card-text"><b>Total:</b> ${totalStars}</p>
				<p class="card-text"><b>Average:</b> ${totalStars/members?size}</p>
			</div>
		</div>
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">Total Ships</h3>
				<p class="card-text"><b>Total:</b> ${totalShips}</p>
				<p class="card-text"><b>Average:</b> ${totalShips/members?size}</p>
			</div>
		</div>
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">Total Fleet</h3>
				<p class="card-text"><b>Total:</b> ${totalFleet}</p>
				<p class="card-text"><b>Average:</b> ${totalFleet/members?size}</p>
			</div>
		</div>
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">Total Economy</h3>
				<p class="card-text"><b>Total:</b> ${totalEconomy}</p>
				<p class="card-text"><b>Average:</b> ${totalEconomy/members?size}</p>
			</div>
		</div>
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">Total Industry</h3>
				<p class="card-text"><b>Total:</b> ${totalIndustry}</p>
				<p class="card-text"><b>Average:</b> ${totalIndustry/members?size}</p>
			</div>
		</div>
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">Total Science</h3>
				<p class="card-text"><b>Total:</b> ${totalScience}</p>
				<p class="card-text"><b>Average:</b> ${totalScience/members?size}</p>
			</div>
		</div>
		<#list members as player>
			<div class="card text-white bg-dark">
				<div class="card-body">
					<h3 class="card-title">${player.name} (${player.alias})</h3>
					<p class="card-text"><b>Stars:</b> ${player.stars}</p>
					<p class="card-text"><b>Ships:</b> ${player.ships}</p>
				</div>
			</div>
		</#list>
	</div>
	<div class="card text-white bg-dark mb-4">
		<div class="card-body">
			<div class="chart-container">
				<canvas id="winPie" width="600" height="400"></canvas>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript" charset="utf8" src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script type="text/javascript" charset="utf8" src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js"></script>
<script type="text/javascript" charset="utf8" src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf8" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.3/Chart.min.js"></script>
<script type="text/javascript" charset="utf8" src="https://cdn.jsdelivr.net/npm/patternomaly@1.3.2/dist/patternomaly.min.js"></script>
<script type="text/javascript" charset="utf8" src="/script.js"></script>
<script type="text/javascript" charset="utf8">
$(document).ready(function(){
	$("#navbar").load("/navbar.html");
	getTeamStars("${name}");
});
</script>
</body>
</html>