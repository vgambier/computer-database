<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>

<html>
<head>
<title><fmt:message key="label.siteName" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="static/css/bootstrap.min.css" rel="stylesheet"
	media="screen">
<link href="static/css/font-awesome.css" rel="stylesheet" media="screen">
<link href="static/css/main.css" rel="stylesheet" media="screen">

<script src="static/js/jquery.min.js"></script>
<script src="static/js/bootstrap.min.js"></script>
<script src="static/js/dashboard.js"></script>

</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="/computer-database"> <fmt:message
					key="label.siteHeader" />
			</a>
		</div>
	</header>

    <div class="col-xs-12">		<a
			href="?lang=en&search=${search}&currentPage=${currentPage}&orderBy=${orderBy}"><fmt:message
				key="label.lang.en" /></a> | <a
			href="?lang=fr&search=${search}&currentPage=${currentPage}&orderBy=${orderBy}"><fmt:message
				key="label.lang.fr" /></a>
	</div>

	<section id="main">
		<div class="container">
			<h1 id="homeTitle">
				<c:out value="${computerCount}" />
				<fmt:message key="label.computersFound" />
			</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="#" method="GET" class="form-inline">

						<input type="search" id="searchbox" name="search"
							class="form-control"
							placeholder=<fmt:message key="label.search"/> /> <input
							type="submit" id="searchsubmit"
							value=<fmt:message key="label.filter"/> class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addComputer" href="addComputer"><fmt:message
							key="label.addComputer" /></a> <a class="btn btn-default"
						id="editComputer" href="#" onclick="$.fn.toggleEditMode();"><fmt:message
							key="label.edit" /></a>
				</div>
			</div>
		</div>

		<form id="deleteForm" action="delete" method="POST">
			<input type="hidden" name="selection" value="">
		</form>

		<div class="container" style="margin-top: 10px;">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<!-- Variable declarations for passing labels as parameters -->
						<!-- Table header for Computer Name -->

						<th class="editMode" style="width: 60px; height: 22px;"><input
							type="checkbox" id="selectall" /> <span
							style="vertical-align: top;"> - <a href="#"
								id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
									class="fa fa-trash-o fa-lg"></i>
							</a>
						</span></th>
						<th><a
							href="?search=${search}&orderBy=computer.name"><fmt:message
									key="label.computerName" /> </a></th>
						<th><a
							href="?search=${search}&orderBy=introduced"><fmt:message
									key="label.introduced" /> </a></th>
						<th><a
							href="?search=${search}&orderBy=discontinued"><fmt:message
									key="label.discontinued" /> </a></th>
						<th><a
							href="?search=${search}&orderBy=computer.company.name"><fmt:message
									key="label.company" /> </a></th>

					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">

					<c:forEach items="${computers}" var="computer">

						<tr>
							<td class="editMode"><input type="checkbox" name="cb"
								class="cb" value="${computer.id}"></td>
							<td><a href="editComputer?id=${computer.id}" onclick="">${computer.getName()}</a></td>
							<td><c:out value="${computer.getIntroduced()}" /></td>
							<td><c:out value="${computer.getDiscontinued()}" /></td>
							<td><c:out value="${computer.getCompanyName()}" /></td>
						</tr>

					</c:forEach>

				</tbody>
			</table>
		</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">

			<form method="POST">
				<div class="btn-group btn-group-sm pull-right" role="group">

					<input type="submit" class="btn btn-default" name="nbEntries"
						value="10" /> <input type="submit" class="btn btn-default"
						name="nbEntries" value="50" /> <input type="submit"
						class="btn btn-default" name="nbEntries" value="100" />

				</div>
			</form>

			<ul class="pagination">

				<c:if test="${currentPage != 1}">
					<li><a
						href="?search=${search}&currentPage=${currentPage-1}&orderBy=${orderBy}"
						aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>

				<c:forEach begin="${Math.max(1, currentPage-3)}"
					end="${Math.min(nbPages, Integer.parseInt(currentPage+3) )}"
					var="i">
					<li class="page-item"><a class="page-link"
						href="?search=${search}&currentPage=${i}&orderBy=${orderBy}">${i}</a></li>
				</c:forEach>

				<c:if test="${currentPage != nbPages}">
					<li><a
						href="?search=${search}&currentPage=${currentPage+1}&orderBy=${orderBy}"
						aria-label="Next"> <span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>
			</ul>
		</div>

	</footer>

</body>
</html>