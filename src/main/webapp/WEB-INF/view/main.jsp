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
	<!-- main.js -->
	<script type="text/javascript" src="/view/js/main.js?ver=<%=System.currentTimeMillis()%>"></script>
</head>
<body class="container-fluid">
	<!-- 로고 -->
	<div class="row align-items-center justify-content-center">
		<div class="col-xm-10 align-self-center text-center pt-4 pb-4">
			<img src="/foodiy/img/foodiy_logo.png" class="card-img-top" alt="Foodiy!">
		</div>
	</div>
	<!-- 서비스 네이게이션 바 -->
	<div class="row align-items-center justify-content-center">
		<div class="col">
			<ul class="nav nav-tabs" id="ul_navitab_tab" role="tablist">
				<!-- 탭:기록하기 -->
				<li class="nav-item">
					<a class="nav-link active" id="a_record-tab" data-toggle="tab" href="#record" role="tab" aria-controls="record" aria-selected="true"><b>기록하기</b></a>
				</li>
				<!-- 탭:검색 -->
				<li class="nav-item">
					<a class="nav-link" id="a_search-tab" data-toggle="tab" href="#search" role="tab" aria-controls="search" aria-selected="false"><b>찾기</b></a>
				</li>
				<!-- 탭:맛집지도 -->
				<li class="nav-item">
					<a class="nav-link" id="a_map-tab" data-toggle="tab" href="#map" role="tab" aria-controls="map" aria-selected="false"><b>맛집지도</b></a>
				</li>
				<!-- 탭:통계 -->
				<li class="nav-item">
					<a class="nav-link" id="a_stat-tab" data-toggle="tab" href="#stat" role="tab" aria-controls="stat" aria-selected="false"><b>통계</b></a>
				</li>
			</ul>
			<div class="tab-content" id="div_navitab_content">
				<!-- 콘텐츠:기록하기 -->
				<div class="tab-pane fade embed-responsive embed-responsive-16by9 show active" id="record" role="tabpanel" aria-labelledby="home-tab">
					<iframe class="embed-responsive-item allowfullscreen" src="/page/record" id="iframe_record"></iframe>
				</div>
				<!-- 콘텐츠:검색 -->
				<div class="tab-pane fade embed-responsive embed-responsive-16by9" id="search" role="tabpanel" aria-labelledby="profile-tab">
					<iframe class="embed-responsive-item allowfullscreen" src="/page/search" id="iframe_search"></iframe>
				</div>
				<!-- 콘텐츠:맛집지도 -->
				<div class="tab-pane fade embed-responsive embed-responsive-16by9" id="map" role="tabpanel" aria-labelledby="contact-tab">
					<iframe class="embed-responsive-item allowfullscreen" src="/page/map" id="iframe_map"></iframe>
				</div>
				<!-- 콘텐츠:통계 -->
				<div class="tab-pane fade embed-responsive embed-responsive-16by9" id="stat" role="tabpanel" aria-labelledby="contact-tab">
					<iframe class="embed-responsive-item allowfullscreen" src="/page/stat" id="iframe_stat"></iframe>
				</div>
			</div>
		</div>
	</div>
</body>
</html>