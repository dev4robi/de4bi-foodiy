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
	<!-- diary_search.js -->
	<link rel="stylesheet" href="/foodiy/css/star-rating.css">
	<script type="text/javascript" src="/foodiy/js/common.js?ver=<%=System.currentTimeMillis()%>"></script>
	<script type="text/javascript" src="/foodiy/js/diary_search.js?ver=<%=System.currentTimeMillis()%>"></script>
</head>
<body class="container-fluid">
	<div class="row">
		<!-- 검색 -->
		<div class="col-12 pt-4" id="div_search">
			<span><i class="fas fa-check-square fa-sm fa-pull-left"></i><b>검색</b></span>
			<div class="btn-group btn-group-toggle w-100" data-toggle="buttons" id="div_search_condition">
				<label class="btn btn-info active">
					<input type="radio" name="options" value="page">최근
				</label>	
				<label class="btn btn-info">
					<input type="radio" name="options" value="name">메뉴
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
			<div class="d-flex flex-wrap d-none" id="div_menu_list">
				<!-- 여기에 검색결과 추가... -->
			</div>
			<div class="text-center pt-5 d-none" id="div_menu_loading">
				<div class="spinner-border text-primary" role="status" style="width:5rem;height:5rem;">
  					<span class="sr-only">Loading...</span>
				</div>
			</div>
		</div>
		<!-- 검색(계속) -->
		<div class="col-12 pt-4" id="div_search_continue">
			<button class="btn btn-outline-secondary w-100 d-none" type="button" id="btn_search_continue"">계속 검색</button>
		</div>
		<!-- 결과 모달 -->
		<button type="button" class="btn btn-primary d-none" data-toggle="modal" data-target="#div_modal" id="btn_show_modal_result"></button>
		<div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="h5_modal_title" aria-hidden="true" id="div_modal">
			<!-- 메뉴모달 -->
			<div class="modal-dialog" role="document" id ="div_modal_menu">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="h5_modal_title">메뉴 제목</h5>
						<input type="text" class="form-control modal-title w-50" id="input_menu_name"/>
						<div class="text-right">
							<!-- 삭제 버튼 -->
							<button type="button" class="btn" onclick="" id="btn_delete_menu"><i class="far fa-trash-alt"></i></button>
							<!-- 수정 버튼 -->
							<button type="button" class="btn" onclick="onClickModifyMenuCard(true)" id="btn_modify_menu_true"><i class="far fa-edit"></i></button>
							<!-- 수정취소 버튼 -->
							<button type="button" class="btn d-none" onclick="onClickModifyMenuCard(false)" id="btn_modify_menu_false"><i class="fas fa-edit"></i></button>
							<!-- 닫기 버튼 -->
							<button type="button" class="close" data-dismiss="modal" aria-label="" id="btn_close_menu"><span aria-hidden="true">&times;</span></button>
						</div>
					</div>
					<div class="modal-body">
						<div class="row"><div class="col-12">
							<img src="/foodiy/img/foodiy_logo.png" class="card-img-top shadow-sm rounded" alt="사진 불러오기 실패!" onclick="onClickPicture()" id="img_menu">
							<div class="custom-file mb-3 d-none">
								<input type="file" class="custom-file-input" onchange="onChangePicture(null)" id="input_pic" accept="image/*">
								<label class="custom-file-label"></label>
							</div>
						</div></div>
						<div class="row pt-3"><div class="col-12">
							<div class="form-group">
								<div class="d-flex align-content-end flex-wrap pt-1" id="div_tag_list">
									<!-- 여기에 배지태그 추가... -->
								</div>
								<div class="input-group pt-1 d-none" id="div_modify_tags">
									<input type="text" class="form-control" id="input_tag"/>
									<button class="btn btn-outline-secondary" type="button" id="btn_add_tag">추가</button>
								</div>
							</div>
						</div></div>
						<div class="row justify-content-around pt-1">
							<div class="col-5 align-self-center text-center shadow-sm rounded">
								<label style="font-size: 1rem; font-weight: bold">점수</label><br>
								<div class="starrating risingstar d-flex justify-content-center flex-row-reverse" id="div_scores">
									<input type="radio" id="input_star5" name="rating" value="5"/><label for="input_starN" title="5Star"></label>
									<input type="radio" id="input_star4" name="rating" value="4"/><label for="input_starN" title="4Star"></label>
									<input type="radio" id="input_star3" name="rating" value="3"/><label for="input_starN" title="3Star"></label>
									<input type="radio" id="input_star2" name="rating" value="2"/><label for="input_starN" title="2Star"></label>
									<input type="radio" id="input_star1" name="rating" value="1"/><label for="input_starN" title="1Star"></label>
								</div>
							</div>
							<div class="col-5 align-self-center text-center shadow-sm rounded">
								<label style="font-size: 1rem; font-weight: bold">금액</label><br>
								<input type="number" class="form-control d-none" id="input_menu_price"/>
								<span style="font-size: 1.5rem" id="span_price">0￦</span>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-warning w-100" onclick="" id="btn_search_record">기록 보기</button>
						<button type="button" class="btn btn-success w-100 d-none" onclick="" id="btn_update_menu">수정내용 저장</button>
					</div>
				</div>
			</div>
			<!-- 기록모달 -->
			<div class="modal-dialog d-none" role="document" id="div_modal_record">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="h5_modal_record_title">기록 제목</h5>
						<input type="text" class="form-control modal-title w-50 d-none" id="input_modify_record_name"/>
						<div class="text-right">
							<!-- 삭제 버튼 -->
							<button type="button" class="btn" onclick="" id="btn_delete_record"><i class="far fa-trash-alt"></i></button>
							<!-- 수정 버튼 -->
							<button type="button" class="btn" onclick="onClickModifyRecordCard(true)" id="btn_modify_record_true"><i class="far fa-edit"></i></button>
							<!-- 수정취소 버튼 -->
							<button type="button" class="btn d-none" onclick="onClickModifyRecordCard(false)" id="btn_modify_record_false"><i class="fas fa-edit"></i></button>
							<!-- 닫기 버튼 -->
							<button type="button" class="close" data-dismiss="modal" aria-label="" id="btn_close_record"><span aria-hidden="true">&times;</span></button>
						</div>
					</div>
					<div class="modal-body">
						<!-- 가게 사진 -->
						<div class="row pt-3">
							<div class="col">
								<span><i class="fas fa-camera fa-sm fa-pull-left"></i><b>사진</b></span>
								<div class="d-flex flex-wrap justify-content-around" id="div_record_pics">
									<div style="width:32%" class="embed-responsive embed-responsive-1by1 shadow-sm rounded">
										<input type="file" class="custom-file-input" onchange="onChangePicture(1)" id="input_record_pic1" accept="image/*">	
										<img src="/foodiy/img/foodiy_logo.png" class="embed-responsive-item" alt="사진 불러오기 실패!" id="img_records_img1">
									</div>
									<div style="width:32%" class="embed-responsive embed-responsive-1by1 shadow-sm rounded">
										<input type="file" class="custom-file-input" onchange="onChangePicture(2)" id="input_record_pic2" accept="image/*">
										<img src="/foodiy/img/foodiy_logo.png" class="embed-responsive-item" alt="사진 불러오기 실패!" id="img_records_img2">
									</div>
									<div style="width:32%" class="embed-responsive embed-responsive-1by1 shadow-sm rounded">
										<input type="file" class="custom-file-input" onchange="onChangePicture(3)" id="input_record_pic3" accept="image/*">
										<img src="/foodiy/img/foodiy_logo.png" class="embed-responsive-item" alt="사진 불러오기 실패!" id="img_records_img3">
									</div>
								</div>
							</div>
						</div>
						<!-- 일자 및 시간 -->
						<div class="row justify-content-center pt-3">
							<div class="col-11 align-self-center text-center shadow-sm rounded">
								<div class="form-group">
									<br>
									<span><i class="fas fa-calendar-check fa-sm"></i> <b>일자 및 시간</b></span><br>
									<span id="span_record_when">????-??-?? ??:??</span>
									<div class="input-group date d-none" id="div_modify_datepicker">
										<input type="text" class="form-control" id="input_datepicker"/>
										<span class="input-group-addon">
											<i class="fas fa-calendar-check fa-2x fa-pull-right"></i>
										</span>
									</div>
									<hr class="d-none" id="hr_modify_datetime_divider">
									<div class="input-group time d-none" id="div_modify_timepicker">
										<input type="text" class="form-control" id="input_timepicker"/>
										<span class="input-group-addon">
											<i class="fas fa-clock fa-2x fa-pull-right"></i>
										</span>
									</div>
								</div>
							</div>
						</div>
						<!-- 장소 -->
						<div class="row justify-content-center">
							<div class="col-11 align-self-center text-center shadow-sm rounded">
								<div class="form-group">
									<br>
									<span><i class="fas fa-map-marked-alt fa-sm"></i> <b>장소</b></span><br>
									<span id="span_record_where">?</span>
									<div class="input-group d-none" id="div_modify_where">
										<input type="hidden" class="form-control" id="input_where_lati"/>
										<input type="hidden" class="form-control" id="input_where_longi"/>
										<input type="text" class="form-control" id="input_where_place"/>
										<span class="input-group-addon">
											<i class="fas fa-map-marked-alt fa-2x fa-pull-right" onclick="onClickGps()" id="btn_gps"></i>
										</span>
									</div>
								</div>
							</div>
						</div>
						<!-- 누구랑 -->
						<div class="row pt-1">
							<div class="col">
								<div class="form-group">
									<hr>
									<span><i class="fas fa-user-alt fa-sm"></i> <b>누구랑</b></span><br>
									<div class="d-flex align-content-end flex-wrap pt-1" id="div_whowith_list">
										<!-- 여기에 인물태그 추가... -->
									</div>
									<div class="input-group pt-3 d-none" id="div_modify_whowith">
										<input type="text" class="form-control" id="input_who_with_tag"/>
										<button class="btn btn-outline-secondary" type="button" id="btn_add_person"">추가</button>
									</div>
								</div>
							</div>
						</div>
						<!-- 메뉴 -->
						<div class="row pt-3" id="div_record_menus">
							<div class="col-12">
								<hr>
								<span><i class="fas fa-utensils fa-sm fa-pull-left"></i><b>메뉴</b></span>
								<div class="d-flex flex-wrap d-none" id="div_record_menulist">
									<!-- 여기에 검색결과 추가... -->
								</div>
							</div>
							<div class="col-12" id="div_records_menulist_loading">
							<div class="text-center pt-5">
								<div class="spinner-border text-primary" role="status" style="width:5rem;height:5rem;">
									<span class="sr-only">Loading...</span>
								</div>
							</div>
						</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-warning w-100" id="btn_backto_menu">메뉴로 돌아가기</button>
						<button type="button" class="btn btn-success w-100 d-none" id="btn_update_record">수정내용 저장</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
