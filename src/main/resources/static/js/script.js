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
        url: '/api/games/latest',
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

function createPlayerStatsLine(data) {
    let tickLabels = [];
    let starData = [];
    let economyData = [];
    let industryData = [];
    let scienceData = [];
    let ticks = data.ticks;
    ticks.forEach(function (element) {
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
    createGraph("statsLine", tickLabels, dataset);
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
                claimedStars = player.ticks[player.ticks.length - 1].stars;
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
        `<td>${player.ticks[latest].economyPerTick}</td>` +
        `<td>${player.ticks[latest].industry}</td>` +
        `<td>${player.ticks[latest].industryPerTick}</td>` +
        `<td>${player.ticks[latest].science}</td>` +
        `<td>${player.ticks[latest].sciencePerTick}</td>`;
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
            let aliasColumn = playerInfoToBox('Alias', data.alias, 3);
            document.getElementById('player-info-grid').appendChild(aliasColumn);
            let nameColumn = playerInfoToBox('Name', !data.name ? '' : data.name, 3);
            document.getElementById('player-info-grid').appendChild(nameColumn);
            let teamColumn = playerInfoToBox('Team', !data.team ? '' : data.team, 3);
            document.getElementById('player-info-grid').appendChild(teamColumn);

            let latest = data.ticks.length - 1;
            let starsColumn = playerInfoToBox('Stars', data.ticks[latest].stars, 4);
            document.getElementById('player-stats-grid').appendChild(starsColumn);
            let shipsColumn = playerInfoToBox('Ships', data.ticks[latest].ships, 4);
            document.getElementById('player-stats-grid').appendChild(shipsColumn);
            let economyColumn = playerInfoToBox('Economy', data.ticks[latest].economy, 4);
            document.getElementById('player-stats-grid').appendChild(economyColumn);
            let economyTickColumn = playerInfoToBox('$ Per Tick', data.ticks[latest].economyPerTick, 4);
            document.getElementById('player-stats-grid').appendChild(economyTickColumn);
            let industryColumn = playerInfoToBox('Industry', data.ticks[latest].industry, 4);
            document.getElementById('player-stats-grid').appendChild(industryColumn);
            let industryTickColumn = playerInfoToBox('Ships Per Tick', data.ticks[latest].industryPerTick, 4);
            document.getElementById('player-stats-grid').appendChild(industryTickColumn);
            let scienceColumn = playerInfoToBox('Science', data.ticks[latest].science, 4);
            document.getElementById('player-stats-grid').appendChild(scienceColumn);
            let scienceTickColumn = playerInfoToBox('Science Per Tick', data.ticks[latest].sciencePerTick, 4);
            document.getElementById('player-stats-grid').appendChild(scienceTickColumn);

            let scanningColumn = playerInfoToBox('Scanning', data.ticks[latest].research.scanning, 4);
            document.getElementById('player-tech-grid').appendChild(scanningColumn);
            let rangeColumn = playerInfoToBox('Hyperspace Range', data.ticks[latest].research.hyperspaceRange, 4);
            document.getElementById('player-tech-grid').appendChild(rangeColumn);
            let terraformingColumn = playerInfoToBox('Terraforming', data.ticks[latest].research.terraforming, 4);
            document.getElementById('player-tech-grid').appendChild(terraformingColumn);
            let experimentationColumn = playerInfoToBox('Experimentation', data.ticks[latest].research.experimentation, 4);
            document.getElementById('player-tech-grid').appendChild(experimentationColumn);
            let weaponsColumn = playerInfoToBox('Weapons', data.ticks[latest].research.weapons, 4);
            document.getElementById('player-tech-grid').appendChild(weaponsColumn);
            let bankingColumn = playerInfoToBox('Banking', data.ticks[latest].research.banking, 4);
            document.getElementById('player-tech-grid').appendChild(bankingColumn);
            let manufacturingColumn = playerInfoToBox('Manufacturing', data.ticks[latest].research.manufacturing, 4);
            document.getElementById('player-tech-grid').appendChild(manufacturingColumn);

            addGraph('Stats', 'statsLine', 'player-stats-grid');

            createPlayerStatsLine(data);
        },
        error: function (xhr, status, error) {
            alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
        }
    });
}

function addGraph(title, id, element, height = 200, width = 400) {
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