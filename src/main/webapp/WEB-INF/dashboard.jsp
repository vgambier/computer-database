<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="../static/css/bootstrap.min.css" rel="stylesheet"
	media="screen">
<link href="../static/css/font-awesome.css" rel="stylesheet"
	media="screen">
<link href="../static/css/main.css" rel="stylesheet" media="screen">

<script src="../static/js/jquery.min.js"></script>
<script src="../static/js/bootstrap.min.js"></script>
<script src="../static/js/dashboard.js"></script>

</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard"> Application - Computer
				Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<h1 id="homeTitle">
				<c:out value="${computerCount} computers found" />
			</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="#" method="GET" class="form-inline">

						<input type="search" id="searchbox" name="search"
							class="form-control" placeholder="Search name" /> <input
							type="submit" id="searchsubmit" value="Filter by name"
							class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addComputer" href="addComputer">Add
						Computer</a> <a class="btn btn-default" id="editComputer" href="#"
						onclick="$.fn.toggleEditMode();">Edit</a>
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
							href="?search=${search}&currentPage=${currentPage}&orderBy=computer_name">Computer
								name</a></th>
						<th><a
							href="?search=${search}&currentPage=${currentPage}&orderBy=introduced">Introduced
								date</a></th>
						<th><a
							href="?search=${search}&currentPage=${currentPage}&orderBy=discontinued">Discontinued
								date</a></th>
						<th><a
							href="?search=${search}&currentPage=${currentPage}&orderBy=company_name">Company</a></th>

					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">

					<c:forEach items="${computerPage.computers}" var="computer">

						<tr>
							<td class="editMode"><input type="checkbox" name="cb"
								class="cb" value="${computer.id}"></td>
							<td><a href="editComputer?id=${computer.id}" onclick="">${computer.getName()}</a></td>
							<td><c:out value="${computer.getIntroduced()}" /></td>
							<td><c:out value="${computer.getDiscontinued()}" /></td>
							<td><c:out value="${computer.getCompany()}" /></td>
						</tr>

					</c:forEach>

				</tbody>
			</table>
		</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">

			<form action="dashboard?search=${search}" method="POST">
				<div class="btn-group btn-group-sm pull-right" role="group">
					<input type="submit" class="btn btn-default" name="action"
						value="10" /> <input type="submit" class="btn btn-default"
						name="action" value="50" /> <input type="submit"
						class="btn btn-default" name="action" value="100" />
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