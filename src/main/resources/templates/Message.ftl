<!DOCTYPE html>
<html lang="en">

<head>
	<title>BIT 269's Neptune's Pride</title>
	<meta charset="utf-8">
	<meta content="width=device-width, initial-scale=1" name="viewport">
	<link href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.2/css/bulma.min.css" rel="stylesheet">
	<link href="/styles.css" rel="stylesheet">
</head>

<body class="has-navbar-fixed-top">
<div id="navbar"></div>
<section class="section">
	<div class="container">
		<section class="hero is-info is-bold opacity">
			<div class="hero-body">
				<div class="container">
					<h1 class="title">${title}</h1>
					<p>${content}</p>
				</div>
			</div>
		</section>
	</div>
</section>
<script src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script src="/script.js"></script>
<script>
$(document).ready(function(){
	$("#navbar").load("/navbar.html");
});
</script>
</body>
</html>