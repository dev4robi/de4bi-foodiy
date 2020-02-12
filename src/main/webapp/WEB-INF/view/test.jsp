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

					var formData = new FormData();

					// records
					formData.append('title', 'this is title!');
					formData.append('when_date', '20200212');
					formData.append('when_time', '222502');
					formData.append('where_lati', '100.0');
					formData.append('where_longi', '-100.0');
					formData.append('who_with', 'me alone!');
					formData.append('pics', files[0]);
					formData.append('pics', files[0]);
					// menus
					formData.append('menu_names', 'm1;m2;m3');
					formData.append('menu_pirces', '100;1000;10000');
					formData.append('menu_tags', '#good;#bad;#soso');
					formData.append('menu_scores', '5;1;3');
					formData.append('menu_pics', files[0]);
					formData.append('menu_pics', files[0]);
					formData.append('menu_pics', files[0]);
					
					$.ajax({
						url : 'http://localhost:40003/api/records',
						method : 'POST',
						beforeSend: function(request) {
    						request.setRequestHeader("user_jwt", "rSSY42xi6Kyz9cwr6cyi5Z8dsJrMF7gTTX3hSvFCLMJDk-wnUTl_iWH6vVq0yrtDRhBO_WgQOKWNuqQfMPZDIzog_Zx20cJ9UrU5v4RdEgAC0VKcKeSOFZtX10JqQh4PYvHU1ADB-_NzEgVvui6bxBZ1uoFCmw_QsePDgl9wJEcQ0gjG13tKKmztBnP9sM8uea3aWkD1e6xWRewkcoi3tn8MgOeX9rJVVjbgN0Z4e_rZPGWGcQ53Cth-2BP1C7HHThpnEFzcanPb0Ru4StRaBJ5tbtS2x3i9tAVWNvOgk9Zh9ekDpbIfU0c-kAO__VScsjYQ3II-xxXYTXfCFXode8pa_RixJy9E3ARh4WPcb0vpmhv0x8wRpRHhyrRQ2Ncn");
  						},
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
