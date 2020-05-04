// 구글맵 전역
var g_map, g_infoWindow, g_marker;

// 기록하기 맵 초기화
function initMapOnRecord() {
	if (!g_map) {
		g_map = new google.maps.Map(document.getElementById('map'), {
			center: {lat: 37.564, lng: 127.001},
			mapTypeControl: false,
			zoom: 16
		});

		g_map.addListener('zoom_changed', function(){
			if (!!g_marker) {
				g_map.setCenter(g_marker.getPosition());
			}
		});

		g_map.addListener('click', function(e) {
			if (!!g_marker) {
				var lat = e.latLng.lat();
				var lng = e.latLng.lng();

				g_marker.setPosition(e.latLng);
				g_infoWindow.setPosition(e.latLng);
				g_infoWindow.open(g_map);
				g_map.panTo(e.latLng);
				$('#input_where_lati').val(lat);
				$('#input_where_longi').val(lng);
				geocodeLatLng(lat, lng);
			}
		});

		addGpsButton();
	}

	if (!g_infoWindow) {
		g_infoWindow = new google.maps.InfoWindow;
	}
	
	updateGeoLocation(null);
}

// 검색하기 맵 초기화
function initMapOnSearch() {
	if (!g_map) {
		g_map = new google.maps.Map(document.getElementById('map'), {
			center: {lat: 37.564, lng: 127.001},
			mapTypeControl: false,
			zoom: 16
		});

		g_map.addListener('zoom_changed', function(){
			if (!!g_marker && $('#input_check_modifying').val() == "true") {
				g_map.setCenter(g_marker.getPosition());
			}
		});

		g_map.addListener('click', function(e){
			if (!!g_marker && $('#input_check_modifying').val()  == "true") {
				var lat = e.latLng.lat();
				var lng = e.latLng.lng();

				g_marker.setPosition(e.latLng);
				g_infoWindow.setPosition(e.latLng);
				g_infoWindow.open(g_map);
				g_map.panTo(e.latLng);
				$('#input_where_lati').val(lat);
				$('#input_where_longi').val(lng);
				geocodeLatLng(lat, lng);
			}
		});

		addGpsButton();

		if (!g_infoWindow) {
			g_infoWindow = new google.maps.InfoWindow;
		}
	}

	var lati = $('#input_where_lati').val();
	var longi = $('#input_where_longi').val();

	if (!!lati && !!longi) {
		var latLng = {lat:lati, lng:longi};
		updateGeoLocation(latLng);
	}
}

// 구글맵 현위치 버튼 컨트롤 추가
function addGpsButton() {
	// Set CSS for the control border.
	var controlDiv = document.createElement('div');
	var controlUI = document.createElement('div');
	controlUI.style.backgroundColor = '#fff';
	controlUI.style.border = '2px solid #fff';
	controlUI.style.borderRadius = '3px';
	controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
	controlUI.style.cursor = 'pointer';
	controlUI.style.margin = '11px';
	controlUI.style.textAlign = 'center';
	controlUI.title = '현위치를 갱신합니다.';
	controlDiv.appendChild(controlUI);

	// Set CSS for the control interior.
	var controlText = document.createElement('div');
	controlText.style.color = 'rgb(25,25,25)';
	controlText.style.fontFamily = 'Roboto,Arial,sans-serif';
	controlText.style.fontSize = '16px';
	controlText.style.lineHeight = '38px';
	controlText.style.paddingLeft = '5px';
	controlText.style.paddingRight = '5px';
	controlText.innerHTML = '<span><i class="fas fa-2x fa-map-marker-alt" data-fa-transform="shrink-4"></i></span>';
	controlUI.appendChild(controlText);

	controlUI.addEventListener('click', function(){
		if (($('#input_check_modifying').val() == "true")) {
			updateGeoLocation(null);
		}
	});

	controlDiv.index = 1;
	g_map.controls[google.maps.ControlPosition.TOP_LEFT].push(controlDiv);
}

// HTML5 Geolocation
function updateGeoLocation(latLng) {
	if (!latLng) {
		// 위도경도 없는 경우
		var navi = navigator;

		if (!navi) {
				alert('GPS가 지원되지 않는 브라우저 혹은 기기입니다.');
				return;
		}

		var geo = navi.geolocation;

		if (!geo) {
				alert('GPS가 지원되지 않는 기기입니다.');
				return;
		}
		
		geo.getCurrentPosition(function(pos){ 
				var coords = pos.coords;

				if (!coords) {
						alert('현위치 획득에 실패했습니다.');
						return;
				}

				var lati = parseFloat(coords.latitude.toFixed(6));
				var longi = parseFloat(coords.longitude.toFixed(6));
				var geoLoc = {lat:lati, lng:longi};

				$('#input_where_lati').val(geoLoc.lat);
				$('#input_where_longi').val(geoLoc.lng);
				g_infoWindow.setPosition(geoLoc);
				g_infoWindow.open(g_map);
				g_map.panTo(geoLoc);
				geocodeLatLng(lati, longi);
		});
	}
	else {
		// 위도경도 있는 경우
		var lati = parseFloat(latLng.lat);
		var longi = parseFloat(latLng.lng);
		var geoLoc = {lat:lati, lng:longi};

		g_infoWindow.setPosition(geoLoc);
		g_infoWindow.open(g_map);
		g_map.panTo(geoLoc);
		geocodeLatLng(lati, longi);
	}
}

// Lati,Longi로 주소 획득
function geocodeLatLng(lati, longi) {
	var geocoder = new google.maps.Geocoder();
	var latLng = {lat:parseFloat(lati), lng:parseFloat(longi)};

	geocoder.geocode({'location':latLng}, function(results, status) {
		if (status === 'OK') {
			if (results[0]) {
				if (!g_marker) {
					g_marker = new google.maps.Marker({
						position: latLng,
						map: g_map
					});
				}
				else {
					g_marker.setPosition(latLng);
				}
				
				$('#input_where_place').val(results[0].formatted_address);
				g_infoWindow.setContent(results[0].formatted_address);
				g_infoWindow.open(g_map, g_marker);
			}
			else {
				window.alert('지명정보 없음!');
			}
		}
		else {
			console.log('Geocoder failed due to: ' + status);
		}
	});
}