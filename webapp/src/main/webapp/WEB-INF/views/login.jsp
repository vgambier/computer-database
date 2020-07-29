<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>

<html>
<head>

<title><fmt:message key="label.siteName" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="static/css/bootstrap.min.css" rel="stylesheet"
	media="screen">
<link href="static/css/font-awesome.css" rel="stylesheet" media="screen">
<link href="static/css/main.css" rel="stylesheet" media="screen">
<script type="text/javascript" src="static/js/jquery.min.js"></script>
<script type="text/javascript" src="static/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="static/js/formvalidation.js"></script>

</head>

<!--  This page is non-functional. The website currently uses Spring Security's default login page -->

<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="/webapp"> <fmt:message
					key="label.siteHeader" />
			</a>
		</div>
	</header>

		<form action="doLogin" method="post">

			<div>
				<label> Username : <input type="text" name="username" />
				</label>
			</div>
			<div>
				<label> Password: <input type="password" name="password" />
				</label>
			</div>
			<div>
				<input type="submit" value="Sign in" />
			</div>
		</form>
</body>
</html>