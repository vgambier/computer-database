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
			<a class="navbar-brand" href="/computer-database"> <fmt:message
					key="label.siteHeader" />
			</a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<div class="label label-default pull-right">
						<c:out value="id: ${id}" />
					</div>
					<h1><fmt:message key="label.editComputer"/></h1>

					<!-- Error or confirmation message that shows up after clicking submit -->
					<c:out value="${message}" />

					<form name="editComputer" id="editComputer" action="editComputer"
						method="POST">
						<input type="hidden" value="${id}" id="id" name="id" />
						<fieldset>
							<div class="form-group">
								<label for="computerName"><fmt:message key="label.computerName"/></label> <input
									value="${computer.name}" type="text" class="form-control"
									id="computerName" name="computerName"
									placeholder="Computer name">
							</div>
							<div class="form-group">
								<label for="introduced"><fmt:message key="label.introducedDate"/></label> <input
									value="${computer.introduced}" type="date" class="form-control"
									id="introduced" name="introduced" placeholder="Introduced date">
							</div>
							<div class="form-group">
								<label for="discontinued"><fmt:message key="label.discontinuedDate"/></label> <input
									value="${computer.discontinued}" type="date"
									class="form-control" id="discontinued" name="discontinued"
									placeholder="Discontinued date">
							</div>
							<div class="form-group">
								<label for="companyID"><fmt:message key="label.company"/></label> <select
									class="form-control" id="companyID" name="companyID">
									<option value="0">--</option>
									<c:forEach items="${companies}" var="company">

										<c:choose>
											<c:when test="${company.id == computer.companyID}">
												<option selected="selected" value="${company.getId()}">${company.getName()}</option>
											</c:when>
											<c:otherwise>
												<option value="${company.getId()}">${company.getName()}</option>
											</c:otherwise>
										</c:choose>

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