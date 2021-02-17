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
		url: '/api/v2/games/latest',
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
		url: '/api/v2/games/latest',
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			document.getElementById('name').innerHTML = `<u class="text-light"><a class="text-light" href="https://np.ironhelmet.com/game/${data.id}">${data.title}</a></u>`;
			document.getElementById('type').innerHTML = data.type;
			document.getElementById('start').innerHTML = data.isStarted ? data.startTime : false;
			document.getElementById('playerCount').innerHTML = data.players.length.toLocaleString();
			document.getElementById('starCount').innerHTML = `${data.victoryStars.toLocaleString()}/${data.totalStars.toLocaleString()}`;
			document.getElementById('turn').innerHTML = data.turn.toLocaleString();
			document.getElementById('next').innerHTML = data.isPaused ? 'Paused' : data.isGameOver ? 'Game Ended' : data.nextTurn;

			getAllPlayerStars(data.totalStars)
		},
		error: function (xhr, status, error) {
			alert("#ERROR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function getAllPlayerStars(totalStars) {
	$.ajax({
		url: `/api/v2/games/latest/players`,
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
				playerLabels.push(player.username);
				playerData.push(player.turns[player.turns.length - 1].stars);
				claimedStars += player.turns[player.turns.length - 1].stars;
			});
			playerLabels.push("Unclaimed");
			playerData.push(totalStars - claimedStars);
			createPieGraph(playerLabels, playerData);

			let turnLabels = [];
			let graphData = [];
			data.forEach(function (player) {
				let stars = [];
				player.turns.forEach(function (turn) {
					if (graphData.length === 0)
						turnLabels.push("Turn " + turn.turn);
					stars.push(turn.stars)
				});
				let entry = {
					label: player.username,
					fill: false,
					backgroundColor: getBackgroundColour(graphData.length),
					borderColor: getBorderColour(graphData.length),
					data: stars,
					steppedLine: false
				};
				graphData.push(entry);
			});
			createGraph("gameProgression", turnLabels, graphData);
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
		url: '/api/v2/games/latest/players',
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
	let latest = player.turns.length - 1;
	let row = document.createElement('tr');
	row.onclick = () => window.location = `/players/${player.username}`;
	row.innerHTML = `<td>${player.username}</td>` +
		`<td>${!player.team ? '' : player.team}</td>` +
		`<td>${player.turns[latest].stars.toLocaleString()}</td>` +
		`<td>${player.turns[latest].ships.toLocaleString()}</td>` +
		`<td>${player.turns[latest].infrastructure.economy.toLocaleString()}</td>` +
		`<td>${player.turns[latest].infrastructure.industry.toLocaleString()}</td>` +
		`<td>${player.turns[latest].infrastructure.science.toLocaleString()}</td>`;
	return row
}

function parseTechTableRow(player) {
	let latest = player.turns.length - 1;
	let row = document.createElement('tr');
	row.onclick = () => window.location = `/players/${player.username}`;
	row.innerHTML = `<td>${player.username}</td>` +
		`<td>${!player.team ? '' : player.team}</td>` +
		`<td>${player.turns[latest].technology.scanning.toLocaleString()}</td>` +
		`<td>${player.turns[latest].technology.hyperspaceRange.toLocaleString()}</td>` +
		`<td>${player.turns[latest].technology.terraforming.toLocaleString()}</td>` +
		`<td>${player.turns[latest].technology.experimentation.toLocaleString()}</td>` +
		`<td>${player.turns[latest].technology.weapons.toLocaleString()}</td>` +
		`<td>${player.turns[latest].technology.banking.toLocaleString()}</td>` +
		`<td>${player.turns[latest].technology.manufacturing.toLocaleString()}</td>`;
	return row
}

function loadPlayer() {
	let urlParams = window.location.pathname.split('/');
	$.ajax({
		async: false,
		url: `/api/v2/games/latest/players/${urlParams[urlParams.length - 1]}`,
		type: 'GET',
		headers: {
			accept: 'application/json',
			contentType: 'application/json'
		},
		dataType: 'json',
		success: function (data) {
			document.getElementById('player').innerHTML = data.username + (data.team ? ` [${data.team}]` : '');

			let latest = data.turns[data.turns.length - 1];
			document.getElementById('stars').innerHTML = latest.stars.toLocaleString();
			document.getElementById('ships').innerHTML = latest.ships.toLocaleString();
			document.getElementById('carriers').innerHTML = latest.carriers.toLocaleString();

			document.getElementById('economy').innerHTML = latest.infrastructure.economy.toLocaleString();
			document.getElementById('economy-per-hour').innerHTML = ` (${latest.infrastructure.economyPerHr.toLocaleString()} per Hour)`;

			document.getElementById('industry').innerHTML = latest.infrastructure.industry.toLocaleString();
			document.getElementById('industry-per-hour').innerHTML = ` (${latest.infrastructure.industryPerHr.toLocaleString()} per Hour)`;

			document.getElementById('science').innerHTML = latest.infrastructure.science.toLocaleString();
			document.getElementById('science-per-hour').innerHTML = ` (${latest.infrastructure.sciencePerHr.toLocaleString()} per Hour)`;

			document.getElementById('scanning').innerHTML = latest.technology.scanning.toLocaleString();
			document.getElementById('hyperspace-range').innerHTML = latest.technology.hyperspaceRange.toLocaleString();
			document.getElementById('terraforming').innerHTML = latest.technology.terraforming.toLocaleString();
			document.getElementById('experimentation').innerHTML = latest.technology.experimentation.toLocaleString();
			document.getElementById('weapons').innerHTML = latest.technology.weapons.toLocaleString();
			document.getElementById('banking').innerHTML = latest.technology.banking.toLocaleString();
			document.getElementById('manufacturing').innerHTML = latest.technology.manufacturing.toLocaleString();

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
	let carriersData = [];
	let economyData = [];
	let industryData = [];
	let scienceData = [];
	data.turns.forEach(function (turn) {
		tickLabels.push("Turn " + turn.turn);
		starData.push(turn.stars);
		economyData.push(turn.infrastructure.economy);
		industryData.push(turn.infrastructure.industry);
		scienceData.push(turn.infrastructure.science);
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
		label: 'Carriers',
		fill: false,
		backgroundColor: getBackgroundColour(2),
		borderColor: getBorderColour(2),
		data: carriersData,
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
	let turnLabels = [];
	let scanningData = [];
	let hyperspaceRangeData = [];
	let terraformingData = [];
	let experimentationData = [];
	let weaponsData = [];
	let bankingData = [];
	let manufacturingData = [];
	data.turns.forEach(function (turn) {
		turnLabels.push("Turn " + turn.turn);
		scanningData.push(turn.technology.scanning);
		hyperspaceRangeData.push(turn.technology.hyperspaceRange);
		terraformingData.push(turn.technology.terraforming);
		experimentationData.push(turn.technology.experimentation);
		weaponsData.push(turn.technology.weapons);
		bankingData.push(turn.technology.banking);
		manufacturingData.push(turn.technology.manufacturing);
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
	createGraph("research-graph", turnLabels, dataset);
}