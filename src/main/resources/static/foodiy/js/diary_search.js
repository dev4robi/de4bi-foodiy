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

    $('#btn_search').attr('disabled', true);
    $('#div_menu_loading').removeClass('d-none');

    searchApi();

    $('#div_menu_loading').addClass('d-none');
    $('#btn_search').attr('disabled', false);
}

// 검색
function searchApi() {
    var searchCondition = $('input[name="options"]:checked').val();
    var searchKeyword = $('#input_search_keyword').val();
    var apiUrl = searchApiUrl;    

    if (searchCondition == 'page') {
        apiUrl += ('/page/' + g_search_page_idx);
    }
    else {
        apiUrl += (searchKeyword + '/' + searchCondition + '/page/' + g_search_page_idx);
    }

    // API 콜 하는 부분부터 계속... @@
}

// 메뉴카드 추가
function addMenuCard(menu_id, img, title, score) {
    var div_menu_list = $('#div_menu_list');

    if (div_menu_list.length == 0) {
        return;
    }

    var score_color_ary = ['secondary', 'danger', 'warning', 'info', 'success', 'primary'];

    var div_card_tag = $(
        '<div style="width:50%" class="embed-responsive embed-responsive-4by3 shadow-sm rounded p-1">' +
            '<input type="hidden" value="' + menu_id + '" id="input_menu_id">' +
            '<img src="' + img + '" class="embed-responsive-item" alt="사진 불러오기 실패!" onclick="onClickMenu(this)">' +
            '<span class="badge badge-' + score_color_ary[score] + ' sticky-top">' + title + '</span>' +
        '</div>');

    div_menu_list.append(div_card_tag);
    div_card_tag.fadeOut(1, function() {div_card_tag.fadeIn(2000);});
}

// 메뉴카드 모두 제거
function removeMenuCards() {
    var div_menu_list = $('#div_menu_list');

    if (div_menu_list.length == 0) {
        return;
    }

    div_menu_list.empty();
}