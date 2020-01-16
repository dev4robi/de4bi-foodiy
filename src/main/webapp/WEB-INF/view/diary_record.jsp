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
	<!-- bootstrap4-datetimepicker -->
	<script type="text/javascript" src="/common/lib/moment-2.24.0/js/moment.min.js"></script>
	<script type="text/javascript" src="/common/lib/moment-2.24.0/locale/ko.js"></script>
	<link rel="stylesheet" href="/common/lib/bootstrap4-datetimepicker/css/bootstrap-datetimepicker.min.css">
	<script type="text/javascript" src="/common/lib/bootstrap4-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
	<!-- common.js -->
	<script type="text/javascript" src="/common/js/common.js?ver=<%=System.currentTimeMillis()%>"></script>
	<!-- diary_record.css/js -->
	<script type="text/javascript" src="/foodiy/js/common.js?ver=<%=System.currentTimeMillis()%>"></script>
	<script type="text/javascript" src="/foodiy/js/diary_record.js?ver=<%=System.currentTimeMillis()%>"></script>
</head>
<body class="container-fluid">
<div class="container">
	<!-- 제목 -->
	<div class="row pt-4">
		<div class="col">
			<div class="form-group">
				<span><i class="fas fa-book fa-sm fa-pull-left"></i>제목</span>
				<div class="input-group date">
					<input type="text" class="form-control" id="input_title"/>
				</div>
			</div>
		</div>
	</div>
	<!-- 사진 -->
	<div class="row">
		<div class="col">
		</div>
	</div>
	<!-- 일자 -->
	<div class="row">
		<div class="col">
			<div class="form-group">
				<span><i class="fas fa-calendar-check fa-sm fa-pull-left"></i>언제?</span>
				<div class="input-group date" id="div_datepicker">
					<input type="text" class="form-control" id="input_datepicker"/>
					<span class="input-group-addon">
						<i class="fas fa-calendar-check fa-2x fa-pull-right"></i>
					</span>
				</div>
			</div>
		</div>
	</div>
	<!-- 시간 -->
	<div class="row">
		<div class="col">
			<div class="form-group">
				<span><i class="fas fa-clock fa-sm fa-pull-left"></i>몇시?</span>
				<div class="input-group time" id="div_timepicker">
					<input type="text" class="form-control" id="input_timepicker"/>
					<span class="input-group-addon">
						<i class="fas fa-clock fa-2x fa-pull-right"></i>
					</span>
				</div>
			</div>
		</div>
	</div>
	<!-- 장소 -->
	<div class="row">
		<div class="col">
			<div class="form-group">
				<span><i class="fas fa-map-marked-alt fa-sm fa-pull-left"></i>어디서?</span>
				<div class="input-group time" id="div_placepicker">
					<input type="text" class="form-control"/>
					<span class="input-group-addon">
						<i class="fas fa-map-marked-alt fa-2x fa-pull-right"></i>
					</span>
				</div>
			</div>
		</div>
	</div>
	<!-- 사람 -->
	<div class="row">
		<div class="col">
			<div class="form-group">
				<span><i class="fas fa-user-alt fa-sm fa-pull-left"></i>누구랑?</span>
				<div class="input-group time" id="div_who">
					<input type="text" class="form-control"/>
				</div>
			</div>
		</div>
	</div>
	<!-- 메뉴와 가격 -->

</div>
</body>
</html>
