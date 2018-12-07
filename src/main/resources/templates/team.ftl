<!DOCTYPE html>
<html lang="en">
<head>
	<title>BIT 269's Neptune's Pride</title>
	<meta charset="utf-8">
	<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
	<link href="/styles.css" rel="stylesheet"/>
</head>
<body>
<div class="ui relaxed stackable grid">
	<div class="three wide column">
		<div class="ui large left fixed inverted vertical menu">
			<h2 class="ui center aligned icon orange inverted header">
				<img class="circular icon" src="/favicon.ico">
				Neptune's Pride
			</h2>
			<a class="item" href="/">
				<i class="home icon"></i>Home
			</a>
			<a class="item" href="/game">
				<i class="gamepad icon"></i>Game
			</a>
			<a class="item" href="/players">
				<i class="user icon"></i>Players
			</a>
			<a class="item" href="/players/leaderboard">
				<i class="list ol icon"></i>Player Leaderboard
			</a>
			<a class="item active" href="/teams">
				<i class="users icon"></i>Teams
			</a>
			<a class="item" href="/teams/leaderboard">
				<i class="list ol icon"></i>Team Leaderboard
			</a>
			<a class="item" href="/settings">
				<i class="cogs icon"></i>Settings
			</a>
			<a class="item" href="/help">
				<i class="question circle icon"></i>Help
			</a>
		</div>
	</div>
	<div class="twelve wide stretched column" style="margin-top: 25px; margin-bottom: 25px;">
		<div class="ui inverted segment opacity">
			<h2 class="ui center aligned header">${team.name}</h2>
			<div class="ui relaxed stackable grid">
				<div class="three wide column">
					<ul>
						<li><strong>Stars:</strong> ${team.totalStars}</li>
						<li><strong>Fleet:</strong> ${team.totalFleet}</li>
						<li><bstrong>Economy:</strong> ${team.totalEconomy}</li>
						<li><strong>Industry:</strong> ${team.totalIndustry}</li>
						<li><strong>Science:</strong> ${team.totalScience}</li>
						<li><strong>Ships:</strong> ${team.totalShips}</li>
						<li><strong>Technology:</strong>
							<ul>
								<li><strong>Scanning:</strong> ${team.technology.scanning}</li>
								<li><strong>Hyperspace:</strong> ${team.technology.hyperspace}</li>
								<li><strong>Terraforming:</strong> ${team.technology.terraforming}</li>
								<li><strong>Experimentation:</strong> ${team.technology.experimentation}</li>
								<li><strong>Weapons:</strong> ${team.technology.weapons}</li>
								<li><strong>Banking:</strong> ${team.technology.banking}</li>
								<li><strong>Manufacturing:</strong> ${team.technology.manufacturing}</li>
							</ul>
						</li>
					</ul>
				</div>
				<div class="twelve wide stretched column">
					<h3 class="ui center aligned header inverted">Stars</h3>
					<div class="chart-container">
						<canvas id="winPie" width="400" height="400">
							<p>Star Percentage</p>
						</canvas>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="https://code.jquery.com/jquery-3.3.1.min.js" type="text/javascript"></script>
<script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.3/Chart.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@0.4.0"></script>
<script src="/table-sort.js"></script>
<script src="/script.js"></script>
<script>
$(document).ready(function(){
	getMembers(winPie, "${team.name}", ${team.totalStars});
});
var ctx = document.getElementById("winPie");
var winPie = new Chart(ctx, {
    type: 'pie',
    data: {
        labels: ["Stars Left", "Stars"],
        datasets: [{
            data: [0, ${team.totalStars}],
            backgroundColor: [
                'rgba(229, 115, 115, 0.5)',
                'rgba(186, 104, 200, 0.5)',
                'rgba(100, 181, 246, 0.5)',
                'rgba(129, 199, 132, 0.5)',
                'rgba(255, 213, 79, 0.5)',
                'rgba(161, 136, 127, 0.5)',
                'rgba(224, 224, 224, 0.5)'
            ],
            borderColor: [
                'rgba(211, 47, 47, 1)',
                'rgba(123, 31, 162, 1)',
                'rgba(25, 118, 210, 1)',
                'rgba(56, 142, 60, 1)',
                'rgba(255, 160, 0, 1)',
                'rgba(93, 64, 55, 1)',
                'rgba(97, 97, 97, 1)'
            ],
            borderWidth: 2
        }]
    },
    options: {
        legend: {
            labels: {
                fontColor: "white",
                fontSize: 14
            }
        },
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            datalabels: {
                color: 'white',
                display: function(context) {
                    var dataset = context.dataset;
                    var count = dataset.data.length;
                    var value = dataset.data[context.dataIndex];
                    return value > count;
                },
                font: {
                    weight: 'bold'
                },
                formatter: Math.round
            }
        }
    }
});
</script>
</body>
</html>