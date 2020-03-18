<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<!-- Meta -->
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<!-- Javascript and css library -->
	<!-- jquery 3.4.0 -->
	<script type="text/javascript" src="/common/lib/jquery-3.4.0/jquery-3.4.0.min.js"></script>
	<script type="text/javascript" src="/common/lib/jquery-3.4.0/jquery-cookie-1.4.1.js"></script>
	<!-- bootstrap 4.3.1 -->
	<link rel="stylesheet" href="/common/lib/bootstrap-4.3.1/css/bootstrap.min.css">
	<script type="text/javascript" src="/common/lib/bootstrap-4.3.1/js/bootstrap.min.js"></script>
	<!-- popper 1.14.7 -->
	<script type="text/javascript" src="/common/lib/popper-1.14.7/popper-1.14.7.js"></script>
	<script type="text/javascript" src="/common/lib/popper-1.14.7/tooltip-1.3.2.js"></script>
	<!-- fontawesome 5.8.1 -->
	<link rel="stylesheet" href="/common/lib/fontawesome-5.8.1/css/fontawesome-5.8.1.css">
	<script type="text/javascript" src="/common/lib/fontawesome-5.8.1/js/fontawesome-5.8.1.js"></script>
	<!-- common.js -->
	<script type="text/javascript" src="/common/js/common.js?ver=<%=System.currentTimeMillis()%>"></script>
	<!-- diary_search.js -->
	<script type="text/javascript" src="/foodiy/js/diary_search.js?ver=<%=System.currentTimeMillis()%>"></script>
</head>
<body class="container-fluid">
	<div class="row">
		<!-- 검색 -->
		<div class="col-12 pt-4" id="div_search">
			<span><i class="fas fa-check-square fa-sm fa-pull-left"></i><b>검색</b></span>
			<div class="btn-group btn-group-toggle w-100" data-toggle="buttons" id="div_search_condtion">
				<label class="btn btn-info active">
					<input type="radio" name="options" value="page">최근
				</label>	
				<label class="btn btn-info">
					<input type="radio" name="options" value="name">제목
				</label>
				<label class="btn btn-info">
					<input type="radio" name="options" value="tag">태그
				</label>
				<label class="btn btn-info">
					<input type="radio" name="options" value="place">장소
				</label>
				<label class="btn btn-info">
					<input type="radio" name="options" value="who">누구랑
				</label>
			</div>
			<div>
				<!-- 제목, 태그, 장소, 누구랑 -->
				<div class="input-group pt-2">
					<input type="text" class="form-control" id="input_search_keyword"/>
					<button class="btn btn-outline-secondary" type="button" id="btn_search"">검색</button>
				</div>
			</div>
		</div>
		<!-- 결과(메뉴) -->
		<div class="col-12 pt-4" id="div_search_result">
			<span><i class="fas fa-list fa-sm fa-pull-left"></i><b id="b_title_menu_result">결과(0)</b></span>
			<div class="text-center pt-3 d-none" id="div_menu_loading">
				<div class="spinner-border text-primary" role="status" style="width:5rem;height:5rem;">
  				<span class="sr-only">Loading...</span>
			</div>
			<div class="d-flex flex-wrap d-none" id="div_menu_list">
				<!-- 여기에 검색결과 추가... -->
			</div>
		</div>
		<!-- 결과(기록) -->
		<div class="col-12 pt-4 d-none">
			<span><i class="fas fa-list fa-sm fa-pull-left"></i><b>상세</b></span>

		</div>
	</div>
</body>
</html>
