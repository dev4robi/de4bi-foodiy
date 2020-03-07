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
<body class="container-fluid" id="body_record">
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
					<input type="hidden" class="form-control" id="input_where_lati"/>
					<input type="hidden" class="form-control" id="input_where_longi"/>
					<input type="text" class="form-control" id="input_where_place"/>
					<span class="input-group-addon">
						<i class="fas fa-map-marked-alt fa-2x fa-pull-right" id="btn_gps"></i>
					</span>
				</div>
			</div>
		</div>
	</div>
	<!-- 사람 -->
	<div class="row">
		<div class="col">
			<div class="form-group">
				<span><i class="fas fa-user-alt fa-sm fa-pull-left"></i>누구랑?</span><br>
				<div class="d-flex align-content-end flex-wrap p-1" id="div_who_with_list">
					<!-- 여기에 배지태그 추가... -->
				</div>
				<div class="input-group">
					<input type="text" class="form-control" id="input_who_with"/>
					<button class="btn btn-outline-secondary" type="button" id="btn_add_person"">추가</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 가게 사진 -->
	<div class="row">
		<div class="col">
			<span><i class="fas fa-camera fa-sm fa-pull-left"></i>사진</span>
			<div class="d-flex justify-content-around">
				<div style="width:32%" class="embed-responsive embed-responsive-1by1 shadow-sm rounded">
					<img src="/foodiy/img/foodiy_logo.png" class="embed-responsive-item" alt="사진 불러오기 실패!" onclick="onClickPicture(-3)" id="img_menu_pic_-3">
        			<div class="custom-file mb-3 d-none">
        				<input type="file" class="custom-file-input" onchange="onChangePicture(-3)" id="input_pic_-3" accept="image/*">
        				<label class="custom-file-label"></label>
					</div>
				</div>
				<div style="width:32%" class="embed-responsive embed-responsive-1by1 shadow-sm rounded">
					<img src="/foodiy/img/foodiy_logo.png" class="embed-responsive-item" alt="사진 불러오기 실패!" onclick="onClickPicture(-2)" id="img_menu_pic_-2">
        			<div class="custom-file mb-3 d-none">
        				<input type="file" class="custom-file-input" onchange="onChangePicture(-2)" id="input_pic_-2" accept="image/*">
        				<label class="custom-file-label"></label>
					</div>
				</div>
				<div style="width:32%" class="embed-responsive embed-responsive-1by1 shadow-sm rounded">
					<img src="/foodiy/img/foodiy_logo.png" class="embed-responsive-item" alt="사진 불러오기 실패!" onclick="onClickPicture(-1)" id="img_menu_pic_-1">
        			<div class="custom-file mb-3 d-none">
        				<input type="file" class="custom-file-input" onchange="onChangePicture(-1)" id="input_pic_-1" accept="image/*">
        				<label class="custom-file-label"></label>
					</div>
				</div>
			</div>
			<div class="d-flex justify-content-end pt-3 pb-3">
				<span style="color: red;" onclick="onClickPictureInit()"><i class="far fa-minus-square"></i>&nbsp;사진 초기화</span>
			</div>
		</div>
	</div>
	<!-- 메뉴와 가격 -->
	<div class="row">
		<div class="col-12">
			<span><i class="fas fa-utensils fa-sm fa-pull-left"></i>메뉴는?</span>
			<div id="div_menucard_list">
				<!-- 메뉴카드 리스트 항목 -->
				<!-- ... -->
			</div>			
		</div>
		<div class="col-12">
			<button class="btn-sm btn-primary w-100" id="btn_add_menu"><i class="far fa-plus-square fa-lg"></i>&nbsp;메뉴 추가</button>
		</div>
	</div>
	<!-- 초기화/맨위로 버튼 -->
	<div class="row pt-4 pb-1 no-gutters">
		<div class="col-5">
			<button class="btn-sm btn-danger w-100" id="btn_init_record"><i class="fas fa-eraser"></i>&nbsp;초기화</button>
		</div>
		<div class="col">
			<button class="btn-sm btn-warning w-100" id="btn_goto_top"><i class="fas fa-arrow-circle-up"></i>&nbsp;맨 위로</button>
		</div>
	</div>
	<!-- 기록 버튼 -->
	<div class="row pt-1 pb-2">
		<div class="col">
			<button class="btn-sm btn-success w-100" id="btn_upload_record"><i class="fas fa-pencil-alt"></i>&nbsp;기록 올리기</button>
		</div>
	</div>
</div>
</body>
</html>
