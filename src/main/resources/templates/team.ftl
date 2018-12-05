<!DOCTYPE html>
<html lang="en">
<head>
	<title>BIT 269's Neptune's Pride</title>
	<meta charset="utf-8">
	<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
	<link href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css" rel="stylesheet">
	<link href="/styles.css" rel="stylesheet"/>
</head>
<body>
<div class="navbar-fixed">
	<nav>
		<div class="nav-wrapper blue-grey darken-4">
			<ul class="right">
				<li><a class="orange-text text-lighten-4" href="/">Home</a></li>
				<li><a class="orange-text text-lighten-4" href="/game">Game</a></li>
				<li><a class="orange-text text-lighten-4" href="/players">Players</a></li>
				<li><a class="orange-text text-lighten-4" href="/players/leaderboard">Player Leaderboard</a></li>
				<li class="active"><a class="orange-text text-lighten-2" href="/teams">Teams</a></li>
				<li><a class="orange-text text-lighten-4" href="/teams/leaderboard">Team Leaderboard</a></li>
				<li><a class="orange-text text-lighten-4" href="/help">Help</a></li>
			</ul>
		</div>
	</nav>
</div>
<div class="container">
	<div class="card blue-grey darken-2">
		<div class="card-content grey-text text-lighten-2">
			<span class="card-title blue-text"><strong>${team.name!"Unknown"}</strong></span>
			<div class="row">
				<div class="col s4">
					<ul class="browser-default">
						<li><b>Stars:</b> ${team.stars!"0"}</li>
						<li><b>Percentage:</b> ${team.percentage?string.percent!"0%"}</li>
						<li><b>Fleet:</b> ${team.fleet!"0"}</li>
						<li><b>Economy:</b> ${team.economy!"0"}</li>
						<li><b>Industry:</b> ${team.industry!"0"}</li>
						<li><b>Science:</b> ${team.science!"0"}</li>
						<li><b>Ships:</b> ${team.ships!"0"}</li>
						<li><b>Technology:</b>
							<ul class="browser-default">
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
							<ul class="browser-default">
								<#list team.members as member, stars>
									<li>${member}</li>
								</#list>
							</ul>
						</li>
					</ul>
				</div>
				<div class="col s7 offset-s1">
					<div>
						<h6 class="blue-text center-align"><strong>Star Count</strong> ${team.percentage?string.percent!"0%"}</h6>
					</div>
					<div class="chart-container">
						<canvas id="winPie" width="200" height="200">
							<p>Star Percentage</p>
						</canvas>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.3/Chart.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@0.4.0"></script>
<script>
function getTotalStars(){
	$.ajax({
	    url: '/game/totalStars',
	    type: 'GET',
	    contentType: 'application/json; charset=utf-8',
	    success: function (data) {
	        console.log(data);
	        winPie.data.datasets[0].data[0] = data.totalStars - ${team.stars};
	        winPie.update();
	    },
	    error: function(xhr, status, error){
	        alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error );
	    }
	});
}
$(document).ready(function(){
	getTotalStars()
});
var ctx = document.getElementById("winPie");
var winPie = new Chart(ctx, {
    type: 'pie',
    data: {
        labels: ["Stars Left", "Stars"],
        datasets: [{
            data: [0, ${team.stars}],
            backgroundColor: [
                'rgba(229, 115, 115, 0.25)',
                'rgba(129, 199, 132, 0.25)'
            ],
            borderColor: [
                'rgba(211, 47, 47, 1)',
                'rgba(56, 148, 60, 1)'
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