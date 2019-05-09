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
		<h1 class="ui horizontal divider header">${name}</h1>
		<div class="ui three stackable cards">
			<div class="ui orange card opacity">
				<div class="content">
					<div class="header">Total Stars</div>
					<div class="description">
						<b>Total:</b> ${totalStars}<br/>
						<b>Average:</b> ${totalStars/players?size}
					</div>
				</div>
			</div>
			<div class="ui orange card opacity">
				<div class="content">
					<div class="header">Total Ships</div>
					<div class="description">
						<b>Total:</b> ${totalShips}<br/>
						<b>Average:</b> ${totalShips/players?size}
					</div>
				</div>
			</div>
			<div class="ui orange card opacity">
				<div class="content">
					<div class="header">Total Economy</div>
					<div class="description">
						<b>Total:</b> ${totalEconomy}<br/>
						<b>Average:</b> ${totalEconomy/players?size}
					</div>
				</div>
			</div>
			<div class="ui orange card opacity">
				<div class="content">
					<div class="header">Total Industry</div>
					<div class="description">
						<b>Total:</b> ${totalIndustry}<br/>
						<b>Average:</b> ${totalIndustry/players?size}
					</div>
				</div>
			</div>
			<div class="ui orange card opacity">
				<div class="content">
					<div class="header">Total Science</div>
					<div class="description">
						<b>Total:</b> ${totalScience}<br/>
						<b>Average:</b> ${totalScience/players?size}
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="ui inverted segment opacity">
		<h4 class="ui horizontal divider header">Stars</h4>
		<div class="chart-container">
			<canvas height="200" id="starLine" width="600"></canvas>
		</div>
		<h4 class="ui horizontal divider header">Ships</h4>
		<div class="chart-container">
			<canvas height="200" id="shipLine" width="600"></canvas>
		</div>
		<h4 class="ui horizontal divider header">Economy</h4>
		<div class="chart-container">
			<canvas height="200" id="economyLine" width="600"></canvas>
		</div>
		<h4 class="ui horizontal divider header">Industry</h4>
		<div class="chart-container">
			<canvas height="200" id="industryLine" width="600"></canvas>
		</div>
		<h4 class="ui horizontal divider header">Science</h4>
		<div class="chart-container">
			<canvas height="200" id="scienceLine" width="600"></canvas>
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
	createTeamStatLines(${ID});
});
</script>
</body>
</html>