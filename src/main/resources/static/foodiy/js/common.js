// 전역변수
var g_svcName       = 'foodiy';
var g_svcDomain     = 'http://localhost:40003';
var g_recordPageUrl = g_svcDomain + '/page/record';
var g_searchPageUrl = g_svcDomain + '/page/search';
var g_mapPageUrl    = g_svcDomain + '/page/map';
var g_statPageUrl   = g_svcDomain + '/page/stat';
var g_authServerUrl = 'http://dev4robi.net:40000';
var g_recordApiUrl  = g_svcDomain + '/api/records';
var g_menuApiUrl    = g_svcDomain + '/api/menus';
var g_imgApiUrl     = g_svcDomain + '/etc/img';
var g_userJwt       = $.cookie('user_jwt');

// GPS 버튼 클릭 시
function onClickGps() {
    try {
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

            var lati = coords.latitude.toFixed(6);
            var longi = coords.longitude.toFixed(6);

            $('#input_where_lati').val(lati);
            $('#input_where_longi').val(longi);
            // 추후 API(geocoding)로 현재 위치 이름 가져와서 적용
            $('#input_where_place').val('Lat:' + lati + ', Lng: ' + longi);
        });
    }
    catch (e) {
        alert(e);
    }
}