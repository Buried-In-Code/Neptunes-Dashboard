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
	document.getElementById('updateButton').disabled = true;
	$.ajax({
		async: true,
		url: '/api/games/latest',
		type: 'PUT',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function () {
			document.getElementById('updateButton').disabled = false;
			location.reload();
		},
		error: function (xhr, status, error) {
			document.getElementById('updateButton').disabled = false;
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function loadInfo() {
	$.ajax({
		async: false,
		url: '/api/games/latest',
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			document.getElementById('name').innerHTML = `<u class="text-light"><a class="text-light" href="https://np.ironhelmet.com/game/${data.id}">${data.name}</a></u>`;
			document.getElementById('gameType').innerHTML = data.gameType;
			document.getElementById('start').innerHTML = data.isStarted ? new Date(`${data.startTime}Z`).toLocaleString() : false;
			document.getElementById('playerCount').innerHTML = data.players.length;
			document.getElementById('starCount').innerHTML = `${data.victoryStars}/${data.totalStars}`;
			document.getElementById('ticks').innerHTML = data.tick;
			document.getElementById('next').innerHTML = data.isPaused ? 'Paused' : data.isGameOver ? 'Game Ended' : data.tickTimeout;

			getAllPlayerStars(data.totalStars)
		},
		error: function (xhr, status, error) {
			alert("#ERROR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function getAllPlayerStars(totalStars) {
	$.ajax({
		url: `/api/games/latest/players`,
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			let playerLabels = [];
			let playerData = [];
			let claimedStars = 0;
			data.forEach(function (player) {
				playerLabels.push(player.alias + (!player.name ? '' : ` (${player.name})`));
				playerData.push(player.ticks[player.ticks.length - 1].stars);
				claimedStars += player.ticks[player.ticks.length - 1].stars;
			});
			playerLabels.push("Unclaimed");
			playerData.push(totalStars - claimedStars);
			createPieGraph(playerLabels, playerData);

			let tickLabels = [];
			let graphData = [];
			data.forEach(function (player) {
				let stars = [];
				player.ticks.forEach(function (tick) {
					if (graphData.length === 0)
						tickLabels.push("Tick " + tick.tick);
					stars.push(tick.stars)
				});
				let entry = {
					label: player.alias + (!player.name ? '' : ` (${player.name})`),
					fill: false,
					backgroundColor: getBackgroundColour(graphData.length),
					borderColor: getBorderColour(graphData.length),
					data: stars,
					steppedLine: false
				};
				graphData.push(entry);
			});
			createGraph("gameProgression", tickLabels, graphData);
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
		borderWidth: 3
	}];
	createGraph('starDistribution', labels, dataset, 'pie');
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
					fontColor: '#D0D0D0',
					fontSize: 14
				}
			},
			responsive: true,
			maintainAspectRatio: false
		}
	});
}

function loadPlayerTables() {
	$.ajax({
		async: false,
		url: '/api/games/latest/players',
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			for (let player of data) {
				let statsRow = parseStatsTableRow(player);
				document.getElementById('stats-table-data').appendChild(statsRow);
				let techRow = parseTechTableRow(player);
				document.getElementById('tech-table-data').appendChild(techRow);
			}
		},
		error: function (xhr, status, error) {
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function parseStatsTableRow(player) {
	let latest = player.ticks.length - 1;
	let row = document.createElement('tr');
	row.onclick = () => window.location = `/players/${player.alias}`;
	row.innerHTML = `<td>${player.alias}</td>` +
		`<td>${!player.name ? '' : player.name}</td>` +
		`<td>${!player.team ? '' : player.team}</td>` +
		`<td>${player.ticks[latest].stars}</td>` +
		`<td>${player.ticks[latest].ships}</td>` +
		`<td>${player.ticks[latest].economy}</td>` +
		`<td>${player.ticks[latest].industry}</td>` +
		`<td>${player.ticks[latest].science}</td>`;
	return row
}

function parseTechTableRow(player) {
	let latest = player.ticks.length - 1;
	let row = document.createElement('tr');
	row.onclick = () => window.location = `/players/${player.alias}`;
	row.innerHTML = `<td>${player.alias}</td>` +
		`<td>${!player.name ? '' : player.name}</td>` +
		`<td>${!player.team ? '' : player.team}</td>` +
		`<td>${player.ticks[latest].research.scanning}</td>` +
		`<td>${player.ticks[latest].research.hyperspaceRange}</td>` +
		`<td>${player.ticks[latest].research.terraforming}</td>` +
		`<td>${player.ticks[latest].research.experimentation}</td>` +
		`<td>${player.ticks[latest].research.weapons}</td>` +
		`<td>${player.ticks[latest].research.banking}</td>` +
		`<td>${player.ticks[latest].research.manufacturing}</td>`;
	return row
}

function loadPlayer() {
	let urlParams = window.location.pathname.split('/');
	$.ajax({
		async: false,
		url: `/api/games/latest/players/${urlParams[urlParams.length - 1]}`,
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			document.getElementById('player').innerHTML = data.alias + (data.name ? ` (${data.name})` : '') + (data.team ? ` [${data.team}]` : '');

			let latest = data.ticks[data.ticks.length - 1];
			document.getElementById('stars').innerHTML = latest.stars;
			document.getElementById('ships').innerHTML = latest.ships;
			document.getElementById('fleets').innerHTML = latest.fleets;

			document.getElementById('economy').innerHTML = latest.economy;
			document.getElementById('economy-per-cycle').innerHTML = ` (${latest.economyPerTick} per Cycle)`;

			document.getElementById('industry').innerHTML = latest.industry;
			document.getElementById('industry-per-cycle').innerHTML = ` (${latest.industryPerTick} per Cycle)`;

			document.getElementById('science').innerHTML = latest.science;
			document.getElementById('science-per-cycle').innerHTML = ` (${latest.sciencePerTick} per Cycle)`;

			let research = latest.research;
			document.getElementById('scanning').innerHTML = research.scanning;
			document.getElementById('hyperspace-range').innerHTML = research.hyperspaceRange;
			document.getElementById('terraforming').innerHTML = research.terraforming;
			document.getElementById('experimentation').innerHTML = research.experimentation;
			document.getElementById('weapons').innerHTML = research.weapons;
			document.getElementById('banking').innerHTML = research.banking;
			document.getElementById('manufacturing').innerHTML = research.manufacturing;

			createStatsGraph(data);
			createResearchGraph(data);
		},
		error: function (xhr, status, error) {
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function createStatsGraph(data) {
	let tickLabels = [];
	let starData = [];
	let shipsData = [];
	let fleetsData = [];
	let economyData = [];
	let industryData = [];
	let scienceData = [];
	data.ticks.forEach(function (element) {
		tickLabels.push("Tick " + element.tick);
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
		label: 'Ships',
		fill: false,
		backgroundColor: getBackgroundColour(1),
		borderColor: getBorderColour(1),
		data: shipsData,
		steppedLine: false
	}, {
		label: 'Fleets',
		fill: false,
		backgroundColor: getBackgroundColour(2),
		borderColor: getBorderColour(2),
		data: fleetsData,
		steppedLine: false
	}, {
		label: 'Economy',
		fill: false,
		backgroundColor: getBackgroundColour(3),
		borderColor: getBorderColour(3),
		data: economyData,
		steppedLine: false
	}, {
		label: 'Industry',
		fill: false,
		backgroundColor: getBackgroundColour(4),
		borderColor: getBorderColour(4),
		data: industryData,
		steppedLine: false
	}, {
		label: 'Science',
		fill: false,
		backgroundColor: getBackgroundColour(5),
		borderColor: getBorderColour(5),
		data: scienceData,
		steppedLine: false
	}];
	createGraph("stats-graph", tickLabels, dataset);
}

function createResearchGraph(data) {
	let tickLabels = [];
	let scanningData = [];
	let hyperspaceRangeData = [];
	let terraformingData = [];
	let experimentationData = [];
	let weaponsData = [];
	let bankingData = [];
	let manufacturingData = [];
	data.ticks.forEach(function (cycle) {
		tickLabels.push("Tick " + cycle.tick);
		scanningData.push(cycle.research.scanning);
		hyperspaceRangeData.push(cycle.research.hyperspaceRange);
		terraformingData.push(cycle.research.terraforming);
		experimentationData.push(cycle.research.experimentation);
		weaponsData.push(cycle.research.weapons);
		bankingData.push(cycle.research.banking);
		manufacturingData.push(cycle.research.manufacturing);
	});
	let dataset = [{
		label: 'Scanning',
		fill: false,
		backgroundColor: getBackgroundColour(0),
		borderColor: getBorderColour(0),
		data: scanningData,
		steppedLine: false
	}, {
		label: 'Hyperspace Range',
		fill: false,
		backgroundColor: getBackgroundColour(1),
		borderColor: getBorderColour(1),
		data: hyperspaceRangeData,
		steppedLine: false
	}, {
		label: 'Terraforming',
		fill: false,
		backgroundColor: getBackgroundColour(2),
		borderColor: getBorderColour(2),
		data: terraformingData,
		steppedLine: false
	}, {
		label: 'Experimentation',
		fill: false,
		backgroundColor: getBackgroundColour(3),
		borderColor: getBorderColour(3),
		data: experimentationData,
		steppedLine: false
	}, {
		label: 'Weapons',
		fill: false,
		backgroundColor: getBackgroundColour(4),
		borderColor: getBorderColour(4),
		data: weaponsData,
		steppedLine: false
	}, {
		label: 'Banking',
		fill: false,
		backgroundColor: getBackgroundColour(5),
		borderColor: getBorderColour(5),
		data: bankingData,
		steppedLine: false
	}, {
		label: 'Manufacturing',
		fill: false,
		backgroundColor: getBackgroundColour(6),
		borderColor: getBorderColour(6),
		data: manufacturingData,
		steppedLine: false
	}];
	createGraph("research-graph", tickLabels, dataset);
}