<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta content="Macro303" name="author">
	<meta content="width=device-width, initial-scale=1" name="viewport">
	<title>Neptune's Dashboard - Exception</title>
	<link href="https://cdn.jsdelivr.net/npm/bulma@0.8.0/css/bulma.min.css" rel="stylesheet">
	<link href="/style.css" rel="stylesheet"/>
	<script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>
</head>
<body class="has-navbar-fixed-top">
<nav class="navbar is-dark is-fixed-top" id="navbar"></nav>
<section class="section is-medium">
	<div class="container has-text-centered">
		<article class="message is-danger is-large">
			<div class="message-header">${code}: ${request}</div>
			<div class="message-body">${message}</div>
		</article>
	</div>
</section>
<footer class="footer">
	<div class="content has-text-centered">
		<a href="https://bulma.io">
			<img alt="Made with Bulma" height="24" src="https://bulma.io/images/made-with-bulma--dark.png" width="128">
		</a>
	</div>
</footer>
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="/script.js"></script>
<script type="text/javascript">
	$(document).ready(function () {
		$("#navbar").load("/navbar.html");
	});
</script>
</body>
</html>