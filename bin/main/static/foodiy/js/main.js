// 페이지 초기화
$(document).ready(function(){
    // 화면 숨기기
    $('#div_login').hide();
    $('#div_main').hide();
    
    // 이벤트 부착
    $('#a_record-tab').on('click', function(){
        onTabClick('record');
    });

    $('#a_search-tab').on('click', function(){
        onTabClick('search');
    });

    $('#a_map-tab').on('click', function(){
        onTabClick('map');
    });

    $('#a_stat-tab').on('click', function(){
        onTabClick('stat');
    });

    // 로그인 확인
    if (true) {
        // 로그인되어있는 경우
        $('#div_login').hide();
        $('#div_main').show();
        
        // 메인페이지 - 레코드탭 자동 클릭
        $('#a_record-tab').trigger('click');
    }
    else {
        // 로그인되어있지 않는 경우
        $('#div_main').hide();
        $('#div_login').show();

        // 로그인페이지 노출
        // ...
    }
});

// navi-bar 탭 클릭 시
function onTabClick(id) {
    var iframe_tag = null;

    if (id == 'record') {
        iframe_tag = $('#iframe_record');
        if (!iframe_tag.attr('src')) {
            iframe_tag.attr('src', g_recordPageUrl);
        }
    }
    else if (id == 'search') {
        iframe_tag = $('#iframe_search');
        if (!iframe_tag.attr('src')) {
            iframe_tag.attr('src', g_searchPageUrl);
        }
    }
    else if (id == 'map') {
        iframe_tag = $('#iframe_map');
        if (!iframe_tag.attr('src')) {
            iframe_tag.attr('src', g_mapPageUrl);
        }
    }
    else if (id == 'stat') {
        iframe_tag = $('#iframe_stat');
        if (!iframe_tag.attr('src')) {
            iframe_tag.attr('src', g_statPageUrl);
        }
    }
}