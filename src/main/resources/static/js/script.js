function getBackgroundColour(index) {
	var colours = [
		'rgba(255,0,0,0.5)',
		'rgba(255,215,0,0.5)',
		'rgba(0,128,0,0.5)',
		'rgba(0,191,255,0.5)',
		'rgba(128,0,128,0.5)',
		'rgba(128,128,128,0.5)',
		'rgba(160,82,45,0.5)'
	];
	while (index >= colours.length) {
		index = index - colours.length;
	}
	return colours[index];
}

function getBorderColour(index) {
	var colours = [
		'rgba(255,0,0,1)',
		'rgba(255,215,0,1)',
		'rgba(0,128,0,1)',
		'rgba(0,191,255,1)',
		'rgba(128,0,128,1)',
		'rgba(128,128,128,1)',
		'rgba(160,82,45,1)'
	];
	while (index >= colours.length) {
		index = index - colours.length;
	}
	return colours[index];
}

function update() {
	document.getElementById("updateButton").className = "ui loading inverted blue button";
	$.ajax({
		async: true,
		url: '/api/game',
		type: 'PUT',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function () {
			document.getElementById("updateButton").className = "ui inverted blue button";
			location.reload(true);
		},
		error: function (xhr, status, error) {
			document.getElementById("updateButton").className = "ui inverted blue button";
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function getGame() {
	var gameStars = 700;
	$.ajax({
		async: false,
		url: '/api/game',
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			document.getElementById("gameName").innerHTML = '<a href="https://np.ironhelmet.com/game/' + data.ID + '">' + data.name + '</a>';
			document.getElementById("gameType").innerHTML = data.gameType;
			if (data.isStarted) {
				document.getElementById("gameStarted").innerHTML = data.startTime;
			} else {
				document.getElementById("gameStarted").innerHTML = "false";
			}
			document.getElementById("playerCount").innerHTML = data.playerCount;
			document.getElementById("teamCount").innerHTML = data.teamCount;
			document.getElementById("gameStars").innerHTML = data.victoryStars + "/" + data.totalStars;
			document.getElementById("gameCycles").innerHTML = data.cycles;
			if (data.isPaused) {
				document.getElementById("gameState").innerHTML = "Paused";
			} else if (data.isGameOver) {
				document.getElementById("gameState").innerHTML = "Game Ended";
			} else {
				document.getElementById("gameState").innerHTML = data.cycleTimeout;
			}
			gameStars = data.totalStars;
		},
		error: function (xhr, status, error) {
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
	return gameStars;
}

function getAllTeamStars(gameID, totalStars) {
	$.ajax({
		url: "/api/teams",
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			if (data.length <= 1) {
				getAllPlayerStars(totalStars, data[0].players)
			} else {
				var teamLabels = [];
				var teamData = [];
				data.forEach(function (element) {
					teamLabels.push(element.name);
					var teamStars = 0;
					element.players.forEach(function (element) {
						teamStars += element.cycles[0].stars;
					});
					teamData.push(teamStars);
				});
				createPieGraph(teamLabels, teamData);
			}
		},
		error: function (xhr, status, error) {
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function getAllPlayerStars(totalStars, data) {
	var playerLabels = [];
	var playerData = [];
	for (var count = 0; count < data.length; count++) {
		var player = data[count];
		if (player.name == null)
			playerLabels.push(player.alias);
		else
			playerLabels.push(player.alias + " (" + player.name + ")");
		playerData.push(player.cycles[0].stars);
	}
	createPieGraph(playerLabels, playerData);
}

function createPlayerStatsLine(ID) {
	$.ajax({
		url: "/api/players/" + ID,
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			var cycleLabels = ["Cycle 0"];
			var starData = [0];
			var economyData = [0];
			var industryData = [0];
			var scienceData = [0];
			var cycles = data.cycles.reverse();
			cycles.forEach(function (element) {
				cycleLabels.push("Cycle " + element.cycle);
				starData.push(element.stars);
				economyData.push(element.economy);
				industryData.push(element.industry);
				scienceData.push(element.science);
			});
			var dataset = [{
				label: 'Stars',
				fill: false,
				backgroundColor: getBackgroundColour(0),
				borderColor: getBorderColour(0),
				data: starData,
				steppedLine: false
			}, {
				label: 'Economy',
				fill: false,
				backgroundColor: getBackgroundColour(1),
				borderColor: getBorderColour(1),
				data: economyData,
				steppedLine: false
			}, {
				label: 'Industry',
				fill: false,
				backgroundColor: getBackgroundColour(2),
				borderColor: getBorderColour(2),
				data: industryData,
				steppedLine: false
			}, {
				label: 'Science',
				fill: false,
				backgroundColor: getBackgroundColour(3),
				borderColor: getBorderColour(3),
				data: scienceData,
				steppedLine: false
			}];
			createLineGraph("statsLine", cycleLabels, dataset);
		},
		error: function (xhr, status, error) {
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function createTeamStatLines(ID) {
	$.ajax({
		url: "/api/teams/" + ID,
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			var cycleLabels = ["Cycle 0"];
			var starSet = [];
			var shipSet = [];
			var economySet = [];
			var industrySet = [];
			var scienceSet = [];
			for (var count = 0; count < data.players.length; count++) {
				var player = data.players[count];
				var starData = [0];
				var shipData = [0];
				var economyData = [0];
				var industryData = [0];
				var scienceData = [0];
				player.cycles.reverse().forEach(function (element) {
					if (count === 0) {
						cycleLabels.push("Cycle " + element.cycle);
					}
					starData.push(element.stars);
					shipData.push(element.ships);
					economyData.push(element.economy);
					industryData.push(element.industry);
					scienceData.push(element.science);
				});
				var playerLabel = player.alias;
				if (player.name != null)
					playerLabel += " (" + player.name + ")";
				starSet.push({
					label: playerLabel,
					fill: false,
					backgroundColor: getBackgroundColour(count),
					borderColor: getBorderColour(count),
					data: starData,
					steppedLine: false
				});
				shipSet.push({
					label: playerLabel,
					fill: false,
					backgroundColor: getBackgroundColour(count),
					borderColor: getBorderColour(count),
					data: shipData,
					steppedLine: false
				});
				economySet.push({
					label: playerLabel,
					fill: false,
					backgroundColor: getBackgroundColour(count),
					borderColor: getBorderColour(count),
					data: economyData,
					steppedLine: false
				});
				industrySet.push({
					label: playerLabel,
					fill: false,
					backgroundColor: getBackgroundColour(count),
					borderColor: getBorderColour(count),
					data: industryData,
					steppedLine: false
				});
				scienceSet.push({
					label: playerLabel,
					fill: false,
					backgroundColor: getBackgroundColour(count),
					borderColor: getBorderColour(count),
					data: scienceData,
					steppedLine: false
				});
			}
			createLineGraph("starLine", cycleLabels, starSet);
			createLineGraph("shipLine", cycleLabels, shipSet);
			createLineGraph("economyLine", cycleLabels, economySet);
			createLineGraph("industryLine", cycleLabels, industrySet);
			createLineGraph("scienceLine", cycleLabels, scienceSet);
		},
		error: function (xhr, status, error) {
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function createPieGraph(labels, data) {
	var ctx = document.getElementById("winPie");
	new Chart(ctx, {
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

function createLineGraph(name, labels, dataset) {
	var ctx = document.getElementById(name);
	new Chart(ctx, {
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

function loadContributors(){
	$.ajax({
		async: false,
		url: '/api/contributors',
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			for (let contributor of data) {
				let column = contributorToHTML(contributor);
				document.getElementById('contributor-grid').appendChild(column);
			}
		},
		error: function (xhr, status, error) {
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function contributorToHTML(item){
	let column = document.createElement('div');
	column.className = 'column is-one-third';
	column.innerHTML = '<div class="card">' +
		'<div class="card-content">' +
		'<div class="media">' +
		'<div class="media-left">' +
		'<figure class="image is-128x128">' +
		`<img alt="${item.Title} Avatar" class="is-rounded" onerror="this.onerror=null; this.src='https://ui-avatars.com/api/?name=${item.Title}&size=512&bold=true&background=4682B4&color=FFF'" src="/avatar-${item.Image}">` +
		'</figure>' +
		'</div>' +
		'<div class="media-content">' +
		`<p class="title is-4">${item.Title}</p>` +
		`<p class="subtitle is-6">${item.Role}</p>` +
		'</div>' +
		'</div>' +
		'</div>' +
		'</div>';
	return column
}