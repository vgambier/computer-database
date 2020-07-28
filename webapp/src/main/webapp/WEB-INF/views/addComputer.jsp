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

<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="/webapp"> <fmt:message
					key="label.siteHeader" />
			</a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1><fmt:message
					key="label.addComputer"/></h1>

					<c:out value="${message}" />
					<!-- Error or confirmation message that shows up after clicking submit -->

					<form name="addComputer" id="addComputer" action="addComputer"
						method="POST">
						<fieldset>
							<div class="form-group">
								<label for="computerName"><fmt:message key="label.computerName"/> <fmt:message key="label.mandatory"/></label> <input
									type="text" class="form-control" name="computerName"
									id="computerName" placeholder="Computer name">
							</div>
							<div class="form-group">
								<label for="introduced"><fmt:message key="label.introducedDate"/></label> <input
									type="date" class="form-control" name="introduced"
									id="introduced" placeholder="Introduced date">
							</div>
							<div class="form-group">
								<label for="discontinued"><fmt:message key="label.discontinuedDate"/></label> <input
									type="date" class="form-control" name="discontinued"
									id="discontinued" placeholder="Discontinued date">
							</div>
							<div class="form-group">
								<label for="companyId"><fmt:message key="label.company"/></label> <select
									class="form-control" name="companyID" id="companyID">

									<option value="0">--</option>

									<c:forEach items="${companies}" var="company">
										<option value="${company.getId()}">${company.getName()}</option>
									</c:forEach>

								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" value=<fmt:message key="label.add"/> class="btn btn-primary">
							<fmt:message key="label.or"/> <a href="/computer-database" class="btn btn-default"><fmt:message key="label.cancel"/></a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>