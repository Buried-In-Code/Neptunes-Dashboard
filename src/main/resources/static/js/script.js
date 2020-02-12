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
        },
        error: function (xhr, status, error) {
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
                document.getElementById('contributor-row').appendChild(column);
            }
        },
        error: function (xhr, status, error) {
            alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
        }
    });
}

function contributorToHTML(item) {
    let column = document.createElement('div');
    column.className = 'col align-self-center';
    column.innerHTML = '<div class="card mb-3 text-light bg-dark">' +
        '<div class="row no-gutters">' +
        '<div class="col-md-4">' +
        `<img alt="${item.Title} Avatar" class="card-img-top" onerror="this.onerror=null; this.src='https://ui-avatars.com/api/?name=${item.Title}&size=512&bold=true&background=4682B4&color=FFF'" src="/avatar-${item.Image}">` +
        '</div>' +
        '<div class="col-md-8">' +
        '<div class="card-body">' +
        `<h5 class="card-title">${item.Title}</h5>` +
        `<p class="card-text"><small class="text-muted">${item.Role}</small></p>` +
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
        url: '/api/games/latest',
        type: 'GET',
        headers: {
            accept: 'application/json',
            contentType: 'application/json'
        },
        dataType: 'json',
        success: function (data) {
            let nameColumn = infoToHTML('Name', `<a href="https://np.ironhelmet.com/game/${data.id}">${data.name}</a>`);
            document.getElementById('info-grid').appendChild(nameColumn);
            let typeColumn = infoToHTML('Type', data.gameType);
            document.getElementById('info-grid').appendChild(typeColumn);
            let startedColumn = infoToHTML('Started', data.isStarted ? data.startTime : 'False');
            document.getElementById('info-grid').appendChild(startedColumn);
            let playerColumn = infoToHTML('# of Players', data.players.length);
            document.getElementById('info-grid').appendChild(playerColumn);
            let starsColumn = infoToHTML('Stars to Win', `${data.victoryStars}/${data.totalStars}`);
            document.getElementById('info-grid').appendChild(starsColumn);
            let ticksColumn = infoToHTML('Ticks', data.tick);
            document.getElementById('info-grid').appendChild(ticksColumn);
            let stateColumn = infoToHTML('Next Tick', data.isPaused ? "Paused" : data.isGameOver ? "Game Ended" : data.tickTimeout);
            document.getElementById('info-grid').appendChild(stateColumn);

            addGraph('Stars', 'winPie', 'info-grid', 400, 400);

            getAllPlayerStars(data.totalStars)
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
        url: '/api/games/latest/players',
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
    let latest = item.ticks.length - 1;
    let row = document.createElement('tr');
    row.onclick = () => window.location = `/players/${item.alias}`;
    row.innerHTML = `<td>${item.alias}</td>` +
        `<td>${!item.name ? '' : item.name}</td>` +
        `<td>${!item.team ? '' : item.team}</td>` +
        `<td>${item.ticks[latest].stars}</td>` +
        `<td>${item.ticks[latest].ships}</td>` +
        `<td>${item.ticks[latest].economy}</td>` +
        `<td>${item.ticks[latest].economyPerTick}</td>` +
        `<td>${item.ticks[latest].industry}</td>` +
        `<td>${item.ticks[latest].industryPerTick}</td>` +
        `<td>${item.ticks[latest].science}</td>` +
        `<td>${item.ticks[latest].sciencePerTick}</td>`;
    return row
}

function playerTechToRow(item) {
    let latest = item.ticks.length - 1;
    let row = document.createElement('tr');
    row.onclick = () => window.location = `/players/${item.alias}`;
    row.innerHTML = `<td>${item.alias}</td>` +
        `<td>${!item.name ? '' : item.name}</td>` +
        `<td>${!item.team ? '' : item.team}</td>` +
        `<td>${item.ticks[latest].research.scanning}</td>` +
        `<td>${item.ticks[latest].research.hyperspaceRange}</td>` +
        `<td>${item.ticks[latest].research.terraforming}</td>` +
        `<td>${item.ticks[latest].research.experimentation}</td>` +
        `<td>${item.ticks[latest].research.weapons}</td>` +
        `<td>${item.ticks[latest].research.banking}</td>` +
        `<td>${item.ticks[latest].research.manufacturing}</td>`;
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