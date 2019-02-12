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
			<h1 class="card-title text-center">${alias}</h1>
		</div>
	</div>
	<div class="card-columns mb-4">
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">Name</h3>
				<p class="card-text">${name!"Unknown"}</p>
			</div>
		</div>
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">Team</h3>
				<p class="card-text">${team}</p>
			</div>
		</div>
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">Stars</h3>
				<p class="card-text">${stars}</p>
			</div>
		</div>
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">Ships</h3>
				<p class="card-text">${ships}</p>
			</div>
		</div>
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">Fleet</h3>
				<p class="card-text">${fleet}</p>
			</div>
		</div>
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">Economy</h3>
				<p class="card-text"">${economy}</p>
			</div>
		</div>
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">$ Per Turn</h3>
				<p class="card-text""><i>Economy Per Turn</i></p>
			</div>
		</div>
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">Industry</h3>
				<p class="card-text"">${industry}</p>
			</div>
		</div>
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">Ships Per Turn</h3>
				<p class="card-text""><i>Industry Per Turn</i></p>
			</div>
		</div>
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">Science</h3>
				<p class="card-text"">${science}</p>
			</div>
		</div>
		<div class="card text-white bg-dark">
			<div class="card-body">
				<h3 class="card-title">Science Per Turn</h3>
				<p class="card-text""><i>Science Per Turn</i></p>
			</div>
		</div>
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