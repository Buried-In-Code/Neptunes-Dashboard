function getRandomInt(max) {
	var value = Math.floor(Math.random() * Math.floor(max));
	console.log("Random: " + value)
	return value
}

function getGame(){
	$.ajax({
	    url: '/game',
	    type: 'GET',
	    contentType: 'application/json',
	    dataType: 'json',
	    success: function (data) {
	        console.log(data);
	        document.getElementById("gameName").innerHTML = data.name;
	        if(data.isStarted){
	            document.getElementById("gameStarted").innerHTML = data.startTime;
            }else{
                document.getElementById("gameStarted").innerHTML = "false";
            }
	        document.getElementById("gamePaused").innerHTML = data.isPaused;
	        document.getElementById("gamePlayers").innerHTML = data.playerCount;
	        document.getElementById("gameTeams").innerHTML = data.teamCount;
	        document.getElementById("gameStars").innerHTML = data.victoryStars + "/" + data.totalStars;
	    },
	    error: function(xhr, status, error){
	        alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
	    }
	});
}

function getGameStars(pie, stars){
	console.log("Depreciated")
}

function getTeamPlayerStars(pie, teamName, stars){
	console.log("Depreciated")
}

function getTeamStars(totalStars){
 	$.ajax({
 	    url: "/teams",
 	    type: 'GET',
 	    contentType: 'application/json',
 	    dataType: 'json',
 	    success: function (data) {
	        console.log(data);
			var teamLabels = []
			var teamData = []
			teamLabels.push("Stars Left")
			teamData.push(1)
			var starCount = 0
			for(count = 0; count < data.length; count++){
				var team = data[count]
				teamLabels.push(team.alias);
				var teamStars = 0
				for(memberCount = 0; memberCount < team.members.length; memberCount++){
					var member = team.members[memberCount]
					teamStars += member.stars
				}
				teamData.push(teamStars);
				starCount += teamStars;
			}
			teamData[0] = totalStars - starCount;
			createPieGraph(teamLabels, teamData)
 	    },
 	    error: function(xhr, status, error){
 	        alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
 	    }
 	});
 }

function getPlayerStars(totalStars){
	$.ajax({
		url: "/players",
		type: 'GET',
		contentType: 'application/json',
		dataType: 'json',
		success: function (data) {
			console.log(data);
			var playerLabels = []
			var playerData = []
			playerLabels.push("Stars Left")
			playerData.push(1)
			var starCount = 0
			for(count = 0; count < data.length; count++){
				var player = data[count]
				playerLabels.push(player.alias);
				playerData.push(player.stars);
				starCount += player.stars;
			}
			playerData[0] = totalStars - starCount;
			createPieGraph(playerLabels, playerData)
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
					'rgba(255,165,0,0.5)',
					'rgba(255,255,0,0.5)',
					'rgba(255,192,203,0.5)',
					'rgba(0,128,0,0.5)',
					'rgba(0,128,128,0.5)',
					'rgba(0,0,255,0.5)',
					'rgba(0,255,255,0.5)',
					'rgba(128,0,128,0.5)',
					'rgba(165,42,42,0.5)',
					'rgba(128,128,128,0.5)',
					'rgba(135,206,235,0.5)',
					'rgba(245,245,220,0.5)',
					'rgba(255,105,180,0.5)',
					pattern.draw('plus', 'rgba(255,0,0,0.5)'),
					pattern.draw('cross', 'rgba(255,165,0,0.5)'),
					pattern.draw('dash', 'rgba(255,255,0,0.5)'),
					pattern.draw('cross-dash', 'rgba(255,192,203,0.5)'),
					pattern.draw('dot', 'rgba(0,128,0,0.5)'),
					pattern.draw('dot-dash', 'rgba(0,128,128,0.5)'),
					pattern.draw('disc', 'rgba(0,0,255,0.5)'),
					pattern.draw('ring', 'rgba(0,255,255,0.5)'),
					pattern.draw('line', 'rgba(128,0,128,0.5)'),
					pattern.draw('line-vertical', 'rgba(165,42,42,0.5)'),
					pattern.draw('weave', 'rgba(128,128,128,0.5)')
				],
				borderColor: [
                    'rgba(255,0,0,1)',
                    'rgba(255,165,0,1)',
                    'rgba(255,255,0,1)',
                    'rgba(255,192,203,1)',
                    'rgba(0,128,0,1)',
                    'rgba(0,128,128,1)',
                    'rgba(0,0,255,1)',
                    'rgba(0,255,255,1)',
                    'rgba(128,0,128,1)',
                    'rgba(165,42,42,1)',
                    'rgba(128,128,128,1)',
                    'rgba(135,206,235,1)',
                    'rgba(245,245,220,1)',
                    'rgba(255,105,180,1)',
                    'rgba(255,0,0,1)',
                    'rgba(255,165,0,1)',
                    'rgba(255,255,0,1)',
                    'rgba(255,192,203,1)',
                    'rgba(0,128,0,1)',
                    'rgba(0,128,128,1)',
                    'rgba(0,0,255,1)',
                    'rgba(0,255,255,1)',
                    'rgba(128,0,128,1)',
                    'rgba(165,42,42,1)',
                    'rgba(128,128,128,1)'
                ],
				borderWidth: 2
			}]
		},
		options: {
			legend: {
				labels: {
					fontColor: "black",
					fontSize: 14
				}
			},
			responsive: true,
			maintainAspectRatio: false,
			plugins: {
				datalabels: {
					color: 'black',
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
}