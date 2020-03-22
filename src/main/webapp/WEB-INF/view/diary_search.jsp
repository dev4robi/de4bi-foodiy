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
	<link rel="stylesheet" href="/foodiy/css/star-rating.css">
	<script type="text/javascript" src="/foodiy/js/common.js?ver=<%=System.currentTimeMillis()%>"></script>
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
			<div class="text-center pt-3 d-none" id="div_menu_loading">
				<div class="spinner-border text-primary" role="status" style="width:5rem;height:5rem;">
  					<span class="sr-only">Loading...</span>
				</div>
			</div>
			<div class="d-flex flex-wrap d-none" id="div_menu_list">
				<!-- 여기에 검색결과 추가... -->
			</div>
		</div>
		<!-- 결과 모달(기록) -->
		<button type="button" class="btn btn-primary d-none" data-toggle="modal" data-target="#div_modal_result" id="btn_show_modal_result"></button>
		<div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="h5_modal_title" aria-hidden="true" id="div_modal_result">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="h5_modal_title">메뉴제목 </h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<img src="/foodiy/img/foodiy_logo.png" class="card-img-top shadow-sm rounded" alt="사진 불러오기 실패!" onclick="onClickPicture()" id="">
						<div class="custom-file mb-3 d-none">
							<input type="file" class="custom-file-input" onchange="onChangePicture()" id="" accept="image/*">
            				<label class="custom-file-label"></label>
            			</div>
						<div class="form-group">
							<div class="d-flex align-content-end flex-wrap pt-1" id="">
								<!-- 여기에 배지태그 추가... -->
								<div class="pr-1 pb-1" id="div_menu_tag"><span class="badge badge-primary" value="" id="">#태그<i class="fas fa-times fa-sm fa-pull-right" onclick="onClickCloseMenuTag(this)"></i></span></div>
								<div class="pr-1 pb-1" id="div_menu_tag"><span class="badge badge-primary" value="" id="">#태그<i class="fas fa-times fa-sm fa-pull-right" onclick="onClickCloseMenuTag(this)"></i></span></div>
								<div class="pr-1 pb-1" id="div_menu_tag"><span class="badge badge-primary" value="" id="">#태그<i class="fas fa-times fa-sm fa-pull-right" onclick="onClickCloseMenuTag(this)"></i></span></div>
								<div class="pr-1 pb-1" id="div_menu_tag"><span class="badge badge-primary" value="" id="">#태그<i class="fas fa-times fa-sm fa-pull-right" onclick="onClickCloseMenuTag(this)"></i></span></div>
								<div class="pr-1 pb-1" id="div_menu_tag"><span class="badge badge-primary" value="" id="">#태그<i class="fas fa-times fa-sm fa-pull-right" onclick="onClickCloseMenuTag(this)"></i></span></div>
								<div class="pr-1 pb-1" id="div_menu_tag"><span class="badge badge-primary" value="" id="">#태그<i class="fas fa-times fa-sm fa-pull-right" onclick="onClickCloseMenuTag(this)"></i></span></div>
								<div class="pr-1 pb-1" id="div_menu_tag"><span class="badge badge-primary" value="" id="">#태그<i class="fas fa-times fa-sm fa-pull-right" onclick="onClickCloseMenuTag(this)"></i></span></div>
								<div class="pr-1 pb-1" id="div_menu_tag"><span class="badge badge-primary" value="" id="">#태그<i class="fas fa-times fa-sm fa-pull-right" onclick="onClickCloseMenuTag(this)"></i></span></div>
								<div class="pr-1 pb-1" id="div_menu_tag"><span class="badge badge-primary" value="" id="">#태그<i class="fas fa-times fa-sm fa-pull-right" onclick="onClickCloseMenuTag(this)"></i></span></div>
								<div class="pr-1 pb-1" id="div_menu_tag"><span class="badge badge-primary" value="" id="">#태그<i class="fas fa-times fa-sm fa-pull-right" onclick="onClickCloseMenuTag(this)"></i></span></div>
							</div>
							<div class="starrating risingstar d-flex justify-content-center flex-row-reverse">
								<input type="radio" id="input_star5_' + menuCardIdx + '" name="rating_' + menuCardIdx + '" value="5" />       <label for="input_star5_' + menuCardIdx + '"title="5Star">5&nbsp;&nbsp;&nbsp;</label>
								<input type="radio" id="input_star4_' + menuCardIdx + '" name="rating_' + menuCardIdx + '" value="4" />       <label for="input_star4_' + menuCardIdx + '"title="4Star">4&nbsp;&nbsp;&nbsp;</label>
								<input type="radio" id="input_star3_' + menuCardIdx + '" name="rating_' + menuCardIdx + '" value="3" checked/><label for="input_star3_' + menuCardIdx + '"title="3Star">3&nbsp;&nbsp;&nbsp;</label>
								<input type="radio" id="input_star2_' + menuCardIdx + '" name="rating_' + menuCardIdx + '" value="2" />       <label for="input_star2_' + menuCardIdx + '"title="2Star">2&nbsp;&nbsp;&nbsp;</label>
								<input type="radio" id="input_star1_' + menuCardIdx + '" name="rating_' + menuCardIdx + '" value="1" />       <label for="input_star1_' + menuCardIdx + '"title="1Star">1&nbsp;&nbsp;&nbsp;</label>
							</div>
							<div>
								<span>10000￦</span>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
						<button type="button" class="btn btn-primary">Save changes</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script>
$('#btn_show_modal_result').trigger('click');
</script>
</html>
