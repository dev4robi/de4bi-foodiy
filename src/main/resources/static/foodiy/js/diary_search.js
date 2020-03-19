// 페이지 전역
var g_search_page_idx = 0;

// 페이지 초기화
$(document).ready(function(){
    // 로그인 확인
    if (true) {
        // 로그인되어있는 경우
        // ...
    }
    else {
        // 로그인되어있지 않는 경우
        $('#div_main').hide();
        $('#div_login').show();

        // 로그인페이지 노출
        // ...
    }

    // 버튼 이벤트 부착
    $('#div_search_condtion').change(function(){
        onChangeSearchCondition();
    });
    $('#btn_search').on('click', function(){
        onClickSearchBtn();
    });

    // UI 초기화
    $('input[value="page"]').trigger('click');
});

// 검색조건 라디오버튼 변동 시
function onChangeSearchCondition(radioBtn) {
    var checked = $('input[name="options"]:checked').val();

    if (checked == 'page') {
        $('#input_search_keyword').attr('disabled', true);
    }
    else {
        $('#input_search_keyword').attr('disabled', false);
    }
}

// 검색버튼 클릭 시
function onClickSearchBtn() {
    var searchCondition = $('input[name="options"]:checked').val();
    var searchKeyword = $('#input_search_keyword').val();

    if (searchCondition != 'page' && !searchKeyword) {
        alert('검색어를 입력해 주세요.');
        return;
    }

    // 기존 검색결과 지움
    removeMenuCards();

    // 검색버튼 비활성화
    $('#btn_search').attr('disabled', true);
    $('#div_menu_loading').removeClass('d-none');

    // 검색버튼으로 검색 시 항상 0번 페이지부터 검색
    g_search_page_idx = 0;
    callSearchApi();
}

// 검색
function callSearchApi() {
    var searchCondition = $('input[name="options"]:checked').val();
    var searchKeyword = $('#input_search_keyword').val();
    var apiUrl = g_searchApiUrl;

    if (searchCondition == 'page') {
        apiUrl += ('/page/' + g_search_page_idx);
    }
    else {
        apiUrl += ('/' + searchCondition + '/' + searchKeyword + '/page/' + g_search_page_idx);
    }

    var reqHead = {'user_jwt' : g_user_jwt};

    AJAX.apiCall('GET', apiUrl, reqHead, null, null, successSearchApi, failSearchApi);
}

// 검색 성공
function successSearchApi(data, textStatus, jqXHR) {
    if (AJAX.checkResultSuccess(data)) {
        var resultDatas = data.result_data;

        if (!resultDatas) {
            alert('비정상 응답!(' + data.result_msg + ')');
            activeSearchBtn();
        }

        var menuDataAry = resultDatas.selectedMenusList;

        if (!menuDataAry || menuDataAry.length == 0) {
            alert('조회 결과가 없습니다.');
            activeSearchBtn();
        }

        for (i = 0; i < menuDataAry.length; ++i) {
            var menuData = menuDataAry[i];
            var menuId = menuData.id;
            var imgUrl = menuData.picUrl;
            var name = menuData.name;
            var score = menuData.score;
            addMenuCard(menuId, imgUrl, name, score);
        }

        $('#b_title_menu_result').html('결과(' + menuDataAry.length + ')');
        $('#div_menu_list').removeClass('d-none');
        activeSearchBtn();
    }
    else {
        alert('검색 실패!\n(' + data.result_msg + ')');
        activeSearchBtn();
        return;
    }
}

// 검색 실패
function failSearchApi(data) {
    alert('서버와 통신에 실패했습니다!');
    activeSearchBtn();
}

// 검색 버튼 활성화
function activeSearchBtn() {
    $('#div_menu_loading').addClass('d-none');
    $('#btn_search').attr('disabled', false);
}

// 메뉴카드 추가
function addMenuCard(menu_id, img, name, score) {
    var div_menu_list = $('#div_menu_list');

    if (div_menu_list.length == 0) {
        return;
    }

    var score_color_ary = ['secondary', 'danger', 'warning', 'info', 'success', 'primary'];

    var div_card_tag = $(
        '<div style="width:50%" class="embed-responsive embed-responsive-4by3 shadow-sm rounded p-1">' +
            '<input type="hidden" value="' + menu_id + '" id="input_menu_id">' +
            '<img src="' + g_imgApiUrl + '/' + img + '" class="embed-responsive-item" alt="사진 불러오기 실패!" onclick="onClickMenu(this)">' +
            '<span class="badge badge-' + score_color_ary[score] + ' sticky-top">' + name + '</span>' +
        '</div>');

    div_menu_list.append(div_card_tag);
    div_card_tag.fadeOut(1, function() {div_card_tag.fadeIn(1500);});
}

// 메뉴카드 모두 제거
function removeMenuCards() {
    var div_menu_list = $('#div_menu_list');

    if (div_menu_list.length == 0) {
        return;
    }

    div_menu_list.empty();
}