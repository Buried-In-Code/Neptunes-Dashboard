<!DOCTYPE html>
<html lang="en">
<head>
	<title>BIT 269's Neptune's Pride</title>
	<meta charset="utf-8">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/1.11.8/semantic.min.css"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/1.11.8/semantic.min.js"></script>
</head>
<body>
	<div style="padding: 10px">
		<div class="ui pointing menu">
			<a class="item" href="/">Home</a>
			<a class="item" href="/game">Game</a>
			<a class="item" href="/players">Players</a>
			<a class="item" href="/teams">Teams</a>
			<a class="item" href="/help">Help</a>
		</div>
		<h1>Something went wrong</h1>
		<div class="ui negative message">
			<div class="header">${error.code}: ${error.request}</div>
			<p>${error.message}</p>
		</div>
    </div>
</body>
</html>