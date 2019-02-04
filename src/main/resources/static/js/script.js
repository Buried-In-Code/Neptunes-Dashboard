function getRandomInt(max) {
	var value = Math.floor(Math.random() * Math.floor(max));
	console.log("Random: " + value)
	return value
}

function getGameStars(pie, stars){
	$.ajax({
	    url: '/game',
	    type: 'GET',
	    contentType: 'application/json',
	    dataType: 'json',
	    success: function (data) {
	        console.log(data);
	        if(pie != null){
	            pie.data.datasets[0].data[0] = data.totalStars - stars;
                pie.update();
	        }
	    },
	    error: function(xhr, status, error){
	        alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
	    }
	});
}

function getTeamPlayerStars(pie, teamName, stars){
 	$.ajax({
 	    url: "/teams/" + teamName + "/players",
 	    type: 'GET',
 	    contentType: 'application/json',
 	    dataType: 'json',
 	    success: function (data) {
	        console.log(data);
 	        if(pie != null){
 	            for(count = 0; count < data.length; count++){
 	                var member = data[count]
 	                pie.data.labels[count + 1] = member.alias;
 	                pie.data.datasets[0].data[count + 1] = member.stars;
                }
                getGameStars(pie, stars)
 	        }
 	    },
 	    error: function(xhr, status, error){
 	        alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
 	    }
 	});
}

var dynamicColors = function() {
    var r = Math.floor(Math.random() * 255);
    var g = Math.floor(Math.random() * 255);
    var b = Math.floor(Math.random() * 255);
    return "rgb(" + r + "," + g + "," + b + ",0.5)";
};

function getTeamStars(pie, totalStars){
 	$.ajax({
 	    url: "/teams",
 	    type: 'GET',
 	    contentType: 'application/json',
 	    dataType: 'json',
 	    success: function (data) {
	        console.log(data);
 	        if(pie != null){
 	            var starCount = 0
 	            for(teamCount = 0; teamCount < data.length; teamCount++){
 	                var team = data[teamCount]
 	                pie.data.labels[teamCount + 1] = team.name;
 	                var teamStars = 0
 	                for(memberCount = 0; memberCount < team.members.length; memberCount++){
 	                    var member = team.members[memberCount]
 	                    teamStars += member.stars
 	                }
 	                pie.data.datasets[0].data[teamCount + 1] = teamStars;
 	                pie.data.datasets[0].backgroundColour[teamCount + 1] = dynamicColors();
 	                starCount += teamStars;
                }
	            pie.data.datasets[0].data[0] = totalStars - starCount;
                pie.update();
 	        }
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
			var playerColour = []
			playerLabels.push("Stars Left")
			playerData.push(1)
			playerColour.push('rgba(230, 25, 75, 0.5)')
			var starCount = 0
			for(count = 0; count < data.length; count++){
				var player = data[count]
				playerLabels.push(player.alias);
				playerData.push(player.stars);
				playerColour.push(dynamicColors());
				starCount += player.stars;
			}
			console.log("Player Data: " + playerData)
			console.log("Star Data: " + playerData[0])
			playerData[0] = totalStars - starCount;
			createPieGraph(playerLabels, playerData, playerColour)
		},
		error: function(xhr, status, error){
			alert("#ERR: xhr.status=" + xhr.status + ", xhr.statusText=" + xhr.statusText + "\nstatus=" + status + ", error=" + error);
		}
	});
}

function createPieGraph(labels, data, colour){
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