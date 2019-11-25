function getBackgroundColour(index) {
	let colours = [
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
	let colours = [
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
	document.getElementById("updateButton").className = "button is-info is-rounded is-outlined is-loading";
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
			document.getElementById("updateButton").className = "button is-info is-rounded is-outlined";
			location.reload();
		},
		error: function (xhr, status, error) {
			document.getElementById("updateButton").className = "button is-info is-rounded is-outlined";
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function getAllTeamStars(totalStars) {
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
				let teamLabels = [];
				let teamData = [];
				data.forEach(function (element) {
					teamLabels.push(element.name);
					let teamStars = 0;
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
	let playerLabels = [];
	let playerData = [];
	for (let count = 0; count < data.length; count++) {
		let player = data[count];
		playerLabels.push(player.alias + (!player.name ? '' : ` (${player.name})`));
		playerData.push(player.cycles[0].stars);
	}
	createPieGraph(playerLabels, playerData);
}

function createPlayerStatsLine(alias) {
	$.ajax({
		url: `/api/players/${alias}`,
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			let cycleLabels = ["Cycle 0"];
			let starData = [0];
			let economyData = [0];
			let industryData = [0];
			let scienceData = [0];
			let cycles = data.cycles.reverse();
			cycles.forEach(function (element) {
				cycleLabels.push("Cycle " + element.cycle);
				starData.push(element.stars);
				economyData.push(element.economy);
				industryData.push(element.industry);
				scienceData.push(element.science);
			});
			let dataset = [{
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
			createGraph("statsLine", cycleLabels, dataset);
		},
		error: function (xhr, status, error) {
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function createTeamStatLines(name) {
	$.ajax({
		url: `/api/teams/${name}`,
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			let cycleLabels = ["Cycle 0"];
			let starSet = [];
			let shipSet = [];
			let economySet = [];
			let industrySet = [];
			let scienceSet = [];
			for (let count = 0; count < data.players.length; count++) {
				let player = data.players[count];
				let starData = [0];
				let shipData = [0];
				let economyData = [0];
				let industryData = [0];
				let scienceData = [0];
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
				let playerLabel = player.alias + (!player.name ? '' : ` (${player.name})`);
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
			createGraph("starLine", cycleLabels, starSet);
			createGraph("shipLine", cycleLabels, shipSet);
			createGraph("economyLine", cycleLabels, economySet);
			createGraph("industryLine", cycleLabels, industrySet);
			createGraph("scienceLine", cycleLabels, scienceSet);
		},
		error: function (xhr, status, error) {
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function createPieGraph(labels, data) {
	let dataset = [{
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
	}];
	createGraph('winPie', labels, dataset, 'pie');
}

function createGraph(name, labels, dataset, type = 'line') {
	let ctx = document.getElementById(name);
	new Chart(ctx, {
		type: type,
		data: {
			labels: labels,
			datasets: dataset
		},
		options: {
			legend: {
				labels: {
					fontColor: "black",
					fontSize: 14
				}
			},
			responsive: true,
			maintainAspectRatio: false
		}
	});
}

function loadContributors() {
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

function contributorToHTML(item) {
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

function loadInfoGrid() {
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
			let nameColumn = infoToHTML('Name', `<a href="https://np.ironhelmet.com/game/${data.ID}">${data.name}</a>`);
			document.getElementById('info-grid').appendChild(nameColumn);
			let typeColumn = infoToHTML('Type', data.gameType);
			document.getElementById('info-grid').appendChild(typeColumn);
			let startedColumn = infoToHTML('Started', data.isStarted ? data.startTime : 'False');
			document.getElementById('info-grid').appendChild(startedColumn);
			let playerColumn = infoToHTML('# of Players', data.playerCount);
			document.getElementById('info-grid').appendChild(playerColumn);
			let teamColumn = infoToHTML('# of Teams', data.teamCount);
			document.getElementById('info-grid').appendChild(teamColumn);
			let starsColumn = infoToHTML('Stars to Win', `${data.victoryStars}/${data.totalStars}`);
			document.getElementById('info-grid').appendChild(starsColumn);
			let cyclesColumn = infoToHTML('Cycles', data.cycles);
			document.getElementById('info-grid').appendChild(cyclesColumn);
			let stateColumn = infoToHTML('Next Cycle', data.isPaused ? "Paused" : data.isGameOver ? "Game Ended" : data.cycleTimeout);
			document.getElementById('info-grid').appendChild(stateColumn);

			addGraph('Stars', 'winPie', 'info-grid', 600, 600);

			getAllTeamStars(data.totalStars)
		},
		error: function (xhr, status, error) {
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
	return gameStars;
}

function infoToHTML(title, info) {
	let column = document.createElement('div');
	column.className = 'column is-one-quarter';
	column.innerHTML = '<div class="box">' +
		'<article class="media">' +
		'<div class="media-content">' +
		'<div class="content">' +
		`<p><strong>${title}</strong><br>${info}</p>` +
		'</div>' +
		'</div>' +
		'</article>' +
		'</div>';
	return column
}

function loadPlayers() {
	$.ajax({
		async: false,
		url: '/api/players',
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			for (let player of data) {
				let statsRow = playerStatsToRow(player);
				document.getElementById('player-stats-table').appendChild(statsRow);
				let techRow = playerTechToRow(player);
				document.getElementById('player-tech-table').appendChild(techRow);
			}
		},
		error: function (xhr, status, error) {
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function playerStatsToRow(item) {
	let row = document.createElement('tr');
	row.onclick = () => window.location = `/players/${item.alias}`;
	row.innerHTML = `<td>${item.alias}</td>` +
		`<td>${!item.name ? '' : item.name}</td>` +
		`<td>${item.team}</td>` +
		`<td>${item.cycles.stars}</td>` +
		`<td>${item.cycles.ships}</td>` +
		`<td>${item.cycles.economy}</td>` +
		`<td>${item.cycles.economyPerCycle}</td>` +
		`<td>${item.cycles.industry}</td>` +
		`<td>${item.cycles.industryPerCycle}</td>` +
		`<td>${item.cycles.science}</td>` +
		`<td>${item.cycles.sciencePerCycle}</td>`;
	return row
}

function playerTechToRow(item) {
	let row = document.createElement('tr');
	row.onclick = () => window.location = `/players/${item.alias}`;
	row.innerHTML = `<td>${item.alias}</td>` +
		`<td>${!item.name ? '' : item.name}</td>` +
		`<td>${item.team}</td>` +
		`<td>${item.cycles.tech.scanning}</td>` +
		`<td>${item.cycles.tech.range}</td>` +
		`<td>${item.cycles.tech.terraforming}</td>` +
		`<td>${item.cycles.tech.experimentation}</td>` +
		`<td>${item.cycles.tech.weapons}</td>` +
		`<td>${item.cycles.tech.banking}</td>` +
		`<td>${item.cycles.tech.manufacturing}</td>`;
	return row
}

function loadTeams() {
	$.ajax({
		async: false,
		url: '/api/teams',
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			for (let team of data) {
				let statsRow = teamStatsToRow(team);
				document.getElementById('team-stats-table').appendChild(statsRow);
			}
		},
		error: function (xhr, status, error) {
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function teamStatsToRow(item) {
	let row = document.createElement('tr');
	row.onclick = () => window.location = `/teams/${item.name}`;
	row.innerHTML = `<td>${item.name}</td>` +
		`<td>${item.cycles.stars}</td>` +
		`<td>${item.cycles.ships}</td>` +
		`<td>${item.cycles.economy}</td>` +
		`<td>${item.cycles.economyPerCycle}</td>` +
		`<td>${item.cycles.industry}</td>` +
		`<td>${item.cycles.industryPerCycle}</td>` +
		`<td>${item.cycles.science}</td>` +
		`<td>${item.cycles.sciencePerCycle}</td>`;
	return row
}

function loadPlayer() {
	let urlParams = window.location.pathname.split('/');
	$.ajax({
		async: false,
		url: `/api/players/${urlParams[urlParams.length - 1]}`,
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			let aliasColumn = playerInfoToBox('Alias', data.alias, 3);
			document.getElementById('player-info-grid').appendChild(aliasColumn);
			let nameColumn = playerInfoToBox('Name', !data.name ? '' : data.name, 3);
			document.getElementById('player-info-grid').appendChild(nameColumn);
			let teamColumn = playerInfoToBox('Team', !data.team ? '' : data.team, 3);
			document.getElementById('player-info-grid').appendChild(teamColumn);

			let starsColumn = playerInfoToBox('Stars', data.cycles[0].stars, 4);
			document.getElementById('player-stats-grid').appendChild(starsColumn);
			let shipsColumn = playerInfoToBox('Ships', data.cycles[0].ships, 4);
			document.getElementById('player-stats-grid').appendChild(shipsColumn);
			let economyColumn = playerInfoToBox('Economy', data.cycles[0].economy, 4);
			document.getElementById('player-stats-grid').appendChild(economyColumn);
			let economyCycleColumn = playerInfoToBox('$ Per Cycle', data.cycles[0].economyPerCycle, 4);
			document.getElementById('player-stats-grid').appendChild(economyCycleColumn);
			let industryColumn = playerInfoToBox('Industry', data.cycles[0].industry, 4);
			document.getElementById('player-stats-grid').appendChild(industryColumn);
			let industryCycleColumn = playerInfoToBox('Ships Per Cycle', data.cycles[0].industryPerCycle, 4);
			document.getElementById('player-stats-grid').appendChild(industryCycleColumn);
			let scienceColumn = playerInfoToBox('Science', data.cycles[0].science, 4);
			document.getElementById('player-stats-grid').appendChild(scienceColumn);
			let scienceCycleColumn = playerInfoToBox('Science Per Cycle', data.cycles[0].sciencePerCycle, 4);
			document.getElementById('player-stats-grid').appendChild(scienceCycleColumn);

			let scanningColumn = playerInfoToBox('Scanning', data.cycles[0].tech.scanning, 4);
			document.getElementById('player-tech-grid').appendChild(scanningColumn);
			let rangeColumn = playerInfoToBox('Hyperspace Range', data.cycles[0].tech.range, 4);
			document.getElementById('player-tech-grid').appendChild(rangeColumn);
			let terraformingColumn = playerInfoToBox('Terraforming', data.cycles[0].tech.terraforming, 4);
			document.getElementById('player-tech-grid').appendChild(terraformingColumn);
			let experimentationColumn = playerInfoToBox('Experimentation', data.cycles[0].tech.experimentation, 4);
			document.getElementById('player-tech-grid').appendChild(experimentationColumn);
			let weaponsColumn = playerInfoToBox('Weapons', data.cycles[0].tech.weapons, 4);
			document.getElementById('player-tech-grid').appendChild(weaponsColumn);
			let bankingColumn = playerInfoToBox('Banking', data.cycles[0].tech.banking, 4);
			document.getElementById('player-tech-grid').appendChild(bankingColumn);
			let manufacturingColumn = playerInfoToBox('Manufacturing', data.cycles[0].tech.manufacturing, 4);
			document.getElementById('player-tech-grid').appendChild(manufacturingColumn);

			addGraph('Stats', 'statsLine', 'player-stats-grid');

			createPlayerStatsLine(data.alias);
		},
		error: function (xhr, status, error) {
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function addGraph(title, id, element, height=200, width=400){
	let graphColumn = document.createElement('div');
	graphColumn.className = 'column is-half';
	graphColumn.innerHTML = '<div class="box">' +
		'<article class="media">' +
		'<div class="media-content">' +
		'<div class="content">' +
		`<h3 class="title is-3 has-text-centered">${title}</h3>` +
		'<div class="chart-container">' +
		`<canvas height="${height}" width="${width}" id="${id}"></canvas>` +
		'</div>' +
		'</div>' +
		'</div>' +
		'</article>' +
		'</div>';
	document.getElementById(element).appendChild(graphColumn);
}

function playerInfoToBox(title, info, columns) {
	let column = document.createElement('div');
	column.className = columns === 3 ? 'column is-one-third' : columns === 4 ? 'column is-one-quarter' : 'column is-one-fifth';
	column.innerHTML = '<div class="box">' +
		'<article class="media">' +
		'<div class="media-content">' +
		'<div class="content">' +
		`<p><strong>${title}</strong><br>${info}</p>` +
		'</div>' +
		'</div>' +
		'</article>' +
		'</div>';
	return column
}

function loadTeam() {
	let urlParams = window.location.pathname.split('/');
	$.ajax({
		async: false,
		url: `/api/teams/${urlParams[urlParams.length - 1]}`,
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			document.getElementById('team-label').textContent = data.name;

			let starsColumn = teamInfoToCard('Total Stars', data.cycles.stars, data.cycles.stars / data.players.length, 3);
			document.getElementById('team-stats-grid').appendChild(starsColumn);
			let shipsColumn = teamInfoToCard('Total Ships', data.cycles.ships, data.cycles.ships / data.players.length, 3);
			document.getElementById('team-stats-grid').appendChild(shipsColumn);
			let economyColumn = teamInfoToCard('Total Economy', data.cycles.economy, data.cycles.economy / data.players.length, 3);
			document.getElementById('team-stats-grid').appendChild(economyColumn);
			let industryColumn = teamInfoToCard('Total Industry', data.cycles.industry, data.cycles.industry / data.players.length, 3);
			document.getElementById('team-stats-grid').appendChild(industryColumn);
			let scienceColumn = teamInfoToCard('Total Science', data.cycles.science, data.cycles.science / data.players.length, 3);
			document.getElementById('team-stats-grid').appendChild(scienceColumn);

			addGraph('Stars', 'starLine', 'team-stats-grid');
			addGraph('Ships', 'shipLine', 'team-stats-grid');
			addGraph('Economy', 'economyLine', 'team-stats-grid');
			addGraph('Industry', 'industryLine', 'team-stats-grid');
			addGraph('Science', 'scienceLine', 'team-stats-grid');

			createTeamStatLines(data.name);
		},
		error: function (xhr, status, error) {
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function teamInfoToCard(heading, total, average, columns) {
	console.log(heading);
	let column = document.createElement('div');
	column.className = columns === 3 ? 'column is-one-third' : columns === 4 ? 'column is-one-quarter' : 'column is-one-fifth';
	column.innerHTML = '<div class="card">' +
		'<div class="card-content">' +
		'<div class="media">' +
		'<div class="media-content">' +
		`<p class="title is-4 has-text-centered">${heading}</p>` +
		'</div>' +
		'</div>' +
		`<div class="content has-text-left"><strong>Total:</strong> ${total}<br><strong>Average:</strong> ${average}</div>` +
		'</div>' +
		'</div>';
	return column
}