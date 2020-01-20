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
	<link rel="stylesheet" href="/foodiy/css/star-rating.css">
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
				<div class="input-group" id="div_map">
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
				<div class="input-group" id="div_who">
					<input type="text" class="form-control"/>
				</div>
			</div>
		</div>
	</div>
	<!-- 메뉴와 가격 -->
	<div class="row">
		<div class="col-12">
			<span><i class="fas fa-utensils fa-sm fa-pull-left"></i>메뉴는?</span>
			<div id="div_menucard_list">
				<!-- 메뉴카드 리스트 항목 -->
				<div class="card" id="div_card_000">
					<img src="/foodiy/img/foodiy_logo.png" class="card-img-top" alt="...">
					<div class="card-body">
						<div class="form-group">
							<span>메뉴</span>
							<input type="text" class="form-control"/>
							<span>가격</span>
							<input type="text" class="form-control"/>
							<span>평점</span>
							<div class="starrating risingstar d-flex justify-content-center flex-row-reverse">
								<input type="radio" id="input_star5" name="rating" value="5" /><label for="input_star5" title="5Star">5&nbsp;&nbsp;&nbsp;</label>
								<input type="radio" id="input_star4" name="rating" value="4" /><label for="input_star4" title="4Star">4&nbsp;&nbsp;&nbsp;</label>
								<input type="radio" id="input_star3" name="rating" value="3" checked/><label for="input_star3" title="3Star">3&nbsp;&nbsp;&nbsp;</label>
								<input type="radio" id="input_star2" name="rating" value="2" /><label for="input_star2" title="2Star">2&nbsp;&nbsp;&nbsp;</label>
								<input type="radio" id="input_star1" name="rating" value="1" /><label for="input_star1" title="1Star">1&nbsp;&nbsp;&nbsp;</label>
							</div>
							<span>태그</span>
							<input type="text" class="form-control"/>
						</div>
						<span style="color: red;" id="span_remove_menu"><i class="far fa-minus-square"></i>삭제</span>
					</div>
				</div>
				<br>
			</div>			
		</div>
		<div class="col-12">
			<button class="btn-sm btn-primary w-100"><i class="far fa-plus-square fa-lg"></i>&nbsp;추가</button>
		</div>
	</div>
</div>
</body>
</html>
