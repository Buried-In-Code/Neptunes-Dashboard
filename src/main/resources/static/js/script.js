function getGame(){
	var gameStars = 700;
	$.ajax({
	    url: '/api/game',
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
 	    url: "/api/teams",
 	    type: 'GET',
 	    contentType: 'application/json',
 	    dataType: 'json',
 	    success: function (data) {
	        console.log(data);
			var teamLabels = [];
			var teamData = [];
			teamLabels.push("Stars Left");
			teamData.push(1);
			var starCount = 0;
			for(count = 0; count < data.length; count++){
				var team = data[count];
				teamLabels.push(team.name);
				var teamStars = 0;
				for(memberCount = 0; memberCount < team.members.length; memberCount++){
					var member = team.members[memberCount];
					teamStars += member.stars;
				}
				teamData.push(teamStars);
				starCount += teamStars;
			}
			teamData[0] = totalStars - starCount;
			createPieGraph(teamLabels, teamData);
 	    },
 	    error: function(xhr, status, error){
 	        alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
 	    }
 	});
}

function getTeamStars(name){
 	$.ajax({
 	    url: "/api/teams/" + name,
 	    type: 'GET',
 	    contentType: 'application/json',
 	    dataType: 'json',
 	    success: function (data) {
	        console.log(data);
			var memberLabels = [];
			var memberData = [];
			var members = data.members;
			for(count = 0; count < members.length; count++){
				var member = members[count];
				memberLabels.push(member.alias);
				memberData.push(member.stars);
			}
			createPieGraph(memberLabels, memberData);
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