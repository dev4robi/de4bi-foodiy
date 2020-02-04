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
	<!-- lib common.js -->
	<script type="text/javascript" src="/common/js/common.js?ver=<%=System.currentTimeMillis()%>"></script>
</head>
<body class="container-fluid">
	<div class="row">
		<div class="col">
			<script>
				function upload() {
					var files = event.target.files;
					for (i = 0; i < files.length; ++i) {
						console.log(files[i]);
					}

					var subFormDataA = new FormData();
					subFormDataA.append('sub_name', 's1');
					subFormDataA.append('sub_pics', files[0]);

					var subFormDataB = new FormData();
					subFormDataB.append('sub_name', 's2');
					subFormDataB.append('sub_pics', files[0]);

					var formData = new FormData();
					formData.append('name', 'Hello Files!');
					formData.append('pic', files[0]);
					formData.append('subs', new Array(subFormDataA, subFormDataB));

					// 여기부터 시작.... subformdata 를 배열에 넣는 방법은?
					// 지금 방식대로 넣으면 object FormData -> Dto 로 치환이 안된다고 스프링부트 에러... @@
					
					$.ajax({
						url : 'http://localhost:40003/upload',
						method : 'POST',
						processData : false,
						contentType : false,
						data : formData,
						success : function(e) {
							alert(e);
						}
					});
				}
			</script>
			<input type="file" id="input_file" onchange='upload();'>
		</div>
	</div>
</body>
</html>
