<!DOCTYPE html>
<html lang="en">
<head>
	<title>BIT 269's Neptune's Pride</title>
	<meta charset="utf-8">
	<meta content="width=device-width, initial-scale=1" name="viewport">
	<link rel="stylesheet" type="text/css" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="/styles.css">
</head>
<body>
<div id="navbar"></div>
<div class="container">
	<div class="card text-white bg-dark mb-4">
		<div class="card-body">
			<h1 class="card-title text-center">Previous Games</h1>
		</div>
	</div>
	<div class="card-columns mb-4">
		<#list games as game>
			<div class="card text-white bg-dark">
				<div class="card-body">
					<h3 class="card-title">${(game.game.name)!"Unknown"}</h3>
					<p class="card-text"><b>${game.teamName}:</b> ${game.winnerNames?join(", ")}</p>
				</div>
			</div>
		</#list>
	</div>
</div>
<script type="text/javascript" charset="utf8" src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script type="text/javascript" charset="utf8" src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js"></script>
<script type="text/javascript" charset="utf8" src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf8" src="/script.js"></script>
<script type="text/javascript" charset="utf8">
$(document).ready(function(){
	$("#navbar").load("/navbar.html");
});
</script>
</body>
</html>