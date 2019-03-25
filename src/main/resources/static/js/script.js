function getBackgroundColour(index){
	var colours = [
		'rgba(255,0,0,0.5)',
		'rgba(255,215,0,0.5)',
		'rgba(0,128,0,0.5)',
		'rgba(0,191,255,0.5)',
		'rgba(128,0,128,0.5)',
		'rgba(128,128,128,0.5)',
		'rgba(160,82,45,0.5)'
	]
	while(index > colours.length){
		index = index - colours.length;
	}
	return colours[index];
}

function getBorderColour(index){
	var colours = [
		'rgba(255,0,0,1)',
		'rgba(255,215,0,1)',
		'rgba(0,128,0,1)',
		'rgba(0,191,255,1)',
		'rgba(128,0,128,1)',
		'rgba(128,128,128,1)',
		'rgba(160,82,45,1)'
	]
	while(index > colours.length){
		index = index - colours.length;
	}
	return colours[index];
}

function getGame(){
	var gameStars = 700;
	$.ajax({
		async: false,
	    url: '/api/games/latest',
	    type: 'GET',
	    contentType: 'application/json',
	    dataType: 'json',
	    success: function (data) {
	        document.getElementById("gameName").innerHTML = '<a href="https://np.ironhelmet.com/game/' + data.ID + '">' + data.name + '</a>';
	        if(data.isStarted){
	            document.getElementById("gameStarted").innerHTML = data.startTime;
            }else{
                document.getElementById("gameStarted").innerHTML = "false";
            }
	        document.getElementById("gamePaused").innerHTML = data.isPaused;
	        document.getElementById("gamePlayers").innerHTML = data.players;
	        document.getElementById("gameTeams").innerHTML = data.teams;
	        document.getElementById("gameStars").innerHTML = data.victoryStars + "/" + data.totalStars;
	        if(data.tick == 0){
	            document.getElementById("gameTurn").innerHTML = 0;
            }else{
                document.getElementById("gameTurn").innerHTML = data.tick / 12;
            }
            document.getElementById("gameTurnTime").innerHTML = data.turnTimeout;
	        gameStars = data.totalStars;
	    },
	    error: function(xhr, status, error){
	        alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
	    }
	});
	return gameStars;
}

function getAllTeamStars(totalStars){
 	$.ajax({
 	    url: "/api/latest/teams",
 	    type: 'GET',
 	    contentType: 'application/json',
 	    dataType: 'json',
 	    success: function (data) {
	        if(data.length <= 1){
	            getAllPlayerStars(totalStars, data[0].players)
            }else{
				var teamLabels = [];
				var teamData = [];
				for(const team of data){
					teamLabels.push(team.name);
					var teamStars = 0;
					for(const player of team.players){
						teamStars += player.turns[0].stars;
					}
					teamData.push(teamStars);
				}
				createPieGraph(teamLabels, teamData);
			}
 	    },
 	    error: function(xhr, status, error){
 	        alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
 	    }
 	});
}

function getAllPlayerStars(totalStars, data){
	var playerLabels = [];
	var playerData = [];
	for(count = 0; count < data.length; count++){
		var player = data[count];
		playerLabels.push(player.alias);
		playerData.push(player.turns[0].stars);
	}
	createPieGraph(playerLabels, playerData);
}

function createPlayerStatsLine(ID){
 	$.ajax({
 	    url: "/api/players/" + ID,
 	    type: 'GET',
 	    contentType: 'application/json',
 	    dataType: 'json',
 	    success: function (data) {
			var turnLabels = [];
			var starData = [];
			var economyData = [];
			var industryData = [];
			var scienceData = [];
			var turns = data.turns.reverse()
			for(const turn of turns){
				turnLabels.push("Turn " + turn.tick/12);
				starData.push(turn.stars);
				economyData.push(turn.economy);
				industryData.push(turn.industry);
				scienceData.push(turn.science);
			}
			var dataset = [{
				label: 'Stars',
				fill: false,
				backgroundColor: getBackgroundColour(0),
				borderColor: getBorderColour(0),
				data: starData,
				steppedLine: false
			},{
				label: 'Economy',
				fill: false,
				backgroundColor: getBackgroundColour(1),
				borderColor: getBorderColour(1),
				data: economyData,
				steppedLine: false
			},{
				label: 'Industry',
				fill: false,
				backgroundColor: getBackgroundColour(2),
				borderColor: getBorderColour(2),
				data: industryData,
				steppedLine: false
			},{
				label: 'Science',
				fill: false,
				backgroundColor: getBackgroundColour(3),
				borderColor: getBorderColour(3),
				data: scienceData,
				steppedLine: false
			}];
			createLineGraph("statsLine", turnLabels, dataset);
 	    },
 	    error: function(xhr, status, error){
 	        alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
 	    }
 	});
}

function createTeamStatLines(ID){
 	$.ajax({
 	    url: "/api/teams/" + ID,
 	    type: 'GET',
 	    contentType: 'application/json',
 	    dataType: 'json',
 	    success: function (data) {
			var turnLabels = [];
			var starSet = [];
			var shipSet = [];
			var economySet = [];
			var industrySet = [];
			var scienceSet = [];
			for(count = 0; count < data.players.length; count++){
				var player = data.players[count];
				var starData = [];
				var shipData = [];
				var economyData = [];
				var industryData = [];
				var scienceData = [];
				for(const turn of player.turns.reverse()){
					if(count == 0){
						turnLabels.push("Turn " + turn.tick/12);
					}
					starData.push(turn.stars);
					shipData.push(turn.ships);
					economyData.push(turn.economy);
					industryData.push(turn.industry);
					scienceData.push(turn.science);
				}
				starSet.push({
					label: player.alias + " (" + player.name + ")",
					fill: false,
					backgroundColor: getBackgroundColour(count),
					borderColor: getBorderColour(count),
					data: starData,
					steppedLine: false
				});
				shipSet.push({
					label: player.alias + " (" + player.name + ")",
					fill: false,
					backgroundColor: getBackgroundColour(count),
					borderColor: getBorderColour(count),
					data: shipData,
					steppedLine: false
				});
				economySet.push({
					label: player.alias + " (" + player.name + ")",
					fill: false,
					backgroundColor: getBackgroundColour(count),
					borderColor: getBorderColour(count),
					data: economyData,
					steppedLine: false
				});
				industrySet.push({
					label: player.alias + " (" + player.name + ")",
					fill: false,
					backgroundColor: getBackgroundColour(count),
					borderColor: getBorderColour(count),
					data: industryData,
					steppedLine: false
				});
				scienceSet.push({
					label: player.alias + " (" + player.name + ")",
					fill: false,
					backgroundColor: getBackgroundColour(count),
					borderColor: getBorderColour(count),
					data: scienceData,
					steppedLine: false
				});
			}
			createLineGraph("starLine", turnLabels, starSet);
			createLineGraph("shipLine", turnLabels, shipSet);
			createLineGraph("economyLine", turnLabels, economySet);
			createLineGraph("industryLine", turnLabels, industrySet);
			createLineGraph("scienceLine", turnLabels, scienceSet);
 	    },
 	    error: function(xhr, status, error){
 	        alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
 	    }
 	});
}

function createPieGraph(labels, data){
	var ctx = document.getElementById("winPie");
	var pieGraph = new Chart(ctx, {
		type: 'pie',
		data: {
			labels: labels,
			datasets: [{
				data: data,
				backgroundColor: [
					'rgba(255,0,0,0.5)',
					'rgba(255,215,0,0.5)',
					'rgba(0,128,0,0.5)',
					'rgba(0,191,255,0.5)',
					'rgba(128,0,128,0.5)',
					'rgba(128,128,128,0.5)',
					'rgba(160,82,45,0.5)',
					pattern.draw('plus', 'rgba(255,0,0,0.5)'),
					pattern.draw('cross', 'rgba(255,215,0,0.5)'),
					pattern.draw('dash', 'rgba(0,128,0,0.5)'),
					pattern.draw('cross-dash', 'rgba(0,191,255,0.5)'),
					pattern.draw('dot', 'rgba(128,0,128,0.5)'),
					pattern.draw('dot-dash', 'rgba(128,128,128,0.5)'),
					pattern.draw('disc', 'rgba(160,82,45,0.5)'),
					pattern.draw('ring', 'rgba(255,0,0,0.5)'),
					pattern.draw('line', 'rgba(255,215,0,0.5)'),
					pattern.draw('line-vertical', 'rgba(0,128,0,0.5)'),
					pattern.draw('weave', 'rgba(0,191,255,0.5)'),
					pattern.draw('zigzag', 'rgba(128,0,128,0.5)'),
					pattern.draw('zigzag-vertical', 'rgba(128,128,128,0.5)'),
					pattern.draw('diagonal', 'rgba(160,82,45,0.5)'),
					pattern.draw('diagonal-right-left', 'rgba(255,0,0,0.5)'),
					pattern.draw('square', 'rgba(255,215,0,0.5)'),
					pattern.draw('box', 'rgba(0,128,0,0.5)'),
					pattern.draw('triangle', 'rgba(0,191,255,0.5)'),
					pattern.draw('triangle-inverted', 'rgba(128,0,128,0.5)'),
					pattern.draw('diamond', 'rgba(128,128,128,0.5)'),
					pattern.draw('diamond-box', 'rgba(160,82,45,0.5)')
				],
				borderColor: [
					'rgba(255,0,0,1)',
					'rgba(255,215,0,1)',
					'rgba(0,128,0,1)',
					'rgba(0,191,255,1)',
					'rgba(128,0,128,1)',
					'rgba(128,128,128,1)',
					'rgba(160,82,45,1)',
					'rgba(255,0,0,1)',
					'rgba(255,215,0,1)',
					'rgba(0,128,0,1)',
					'rgba(0,191,255,1)',
					'rgba(128,0,128,1)',
					'rgba(128,128,128,1)',
					'rgba(160,82,45,1)',
					'rgba(255,0,0,1)',
					'rgba(255,215,0,1)',
					'rgba(0,128,0,1)',
					'rgba(0,191,255,1)',
					'rgba(128,0,128,1)',
					'rgba(128,128,128,1)',
					'rgba(160,82,45,1)',
					'rgba(255,0,0,1)',
					'rgba(255,215,0,1)',
					'rgba(0,128,0,1)',
					'rgba(0,191,255,1)',
					'rgba(128,0,128,1)',
					'rgba(128,128,128,1)',
					'rgba(160,82,45,1)'
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
			maintainAspectRatio: false
		}
	});
}

function createLineGraph(name, labels, dataset){
	var ctx = document.getElementById(name);
	var pieGraph = new Chart(ctx, {
		type: 'line',
		data: {
            labels: labels,
            datasets: dataset
        },
        options: {
            legend: {
                labels: {
                    fontColor: "white",
                    fontSize: 14
                }
            },
            responsive: true,
            maintainAspectRatio: false
        }
	});
}