// 페이지 전역
var g_searchPageIdx = 0;
var g_menuMap = new Map();
var g_picMap = new Map();

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
    removeMenuCardAll();

    // 검색버튼 비활성화
    $('#btn_search').attr('disabled', true);
    $('#div_menu_loading').removeClass('d-none');

    // 검색버튼으로 검색 시 항상 0번 페이지부터 검색
    g_searchPageIdx = 0;
    callSearchApi();
}

// 검색
function callSearchApi() {
    var searchCondition = $('input[name="options"]:checked').val();
    var searchKeyword = $('#input_search_keyword').val();
    var apiUrl = g_menuApiUrl;

    if (searchCondition == 'page') {
        apiUrl += ('/page/' + g_searchPageIdx);
    }
    else {
        apiUrl += ('/' + searchCondition + '/' + searchKeyword + '/page/' + g_searchPageIdx);
    }

    var reqHead = {'user_jwt' : g_userJwt};

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
            var tags = menuData.tags;
            var price = menuData.price;
            var menu = {
                "img_url" : imgUrl,
                "name" : name,
                "score" : score,
                "tags" : tags,
                "price" : price
            };

            g_menuMap.set(menuId, menu);
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
        '<div style="width:50%" class="embed-responsive embed-responsive-4by3 shadow-sm rounded p-1" id="div_menu_card_' + menu_id + '">' +
            '<input type="hidden" value="' + menu_id + '" id="input_menu_id">' +
            '<img src="' + g_imgApiUrl + '/' + img + '" class="embed-responsive-item" alt="사진 불러오기 실패!" onclick="onClickMenuCard(' + menu_id + ')">' +
            '<span class="badge badge-' + score_color_ary[score] + ' sticky-top">' + name + ' (★' + score + ')</span>' +
        '</div>');

    div_menu_list.append(div_card_tag);
    div_card_tag.fadeOut(1, function() {div_card_tag.fadeIn(1500);});
}

// 메뉴카드 수정
function updateMenuCard(menu_id, img, name, score) {
    var div_menu_list = $('#div_menu_list');

    if (div_menu_list.length == 0) {
        return;
    }

    var div_card_tag = $(div_menu_list).find('#div_menu_card_' + menu_id);

    if (div_card_tag.length == 0) {
        return;
    }

    var score_color_ary = ['secondary', 'danger', 'warning', 'info', 'success', 'primary'];

    $(div_card_tag).find('img').attr('src', g_imgApiUrl + '/' + img + '?time=' + new Date().getTime());
    $(div_card_tag).find('span').remove();

    var new_span_tag = ('<span class="badge badge-' + score_color_ary[score] + ' sticky-top">' + name + ' (★' + score + ')</span>');
    
    $(div_card_tag).append(new_span_tag);
}

// 메뉴카드 하나 제거
function removeMenuCard(menuId) {
    var div_menu_list = $('#div_menu_list');
    $(div_menu_list).find('#div_menu_card_' + menuId).remove();
}

// 메뉴카드 모두 제거
function removeMenuCardAll() {
    var div_menu_list = $('#div_menu_list');

    if (div_menu_list.length == 0) {
        return;
    }

    div_menu_list.empty();
}

// 메뉴카드 선택
function onClickMenuCard(menuId) {
    // UI 초기화
    onClickModifyMenuCard(false);

    // 캐싱데이터 획득
    var menu = g_menuMap.get(menuId);

    if (!menu) {
        alert('메뉴 정보를 찾지 못했습니다.');
        return;
    }

    // 삭제버튼
    $('#btn_delete_menu').attr('onclick', 'onClickDeleteMenu(' + menuId + ')');
    
    // 이미지
    $('#img_menu').attr('src', g_imgApiUrl + '/' + menu.img_url + '?time=' + new Date().getTime());

    // 제목
    $('#h5_modal_title').html(menu.name);
    
    // 태그
    var tags = menu.tags;
    var tagSplit = tags.split('`');
    var div_tag_list = $('#div_tag_list');
    div_tag_list.empty();

    for (i = 0; i < tagSplit.length; ++i) {
        try {
            var tag = '<div class="pr-1 pb-1" id="div_menu_tag"><span class="badge badge-primary" value="' + tagSplit[i] + '" id="span_menu_tag">#'+ tagSplit[i] + '<i class="fas fa-times fa-sm fa-pull-right tags d-none" onclick="onClickCloseMenuTag(this)"></i></span></div>';
            div_tag_list.append(tag);
        }
        catch (e) {
            console.log(e);
        }
    }
    
    // 점수
    $('#input_star' + menu.score).attr('checked', true);

    // 금액
    $('#span_price').html(numberWithCommas(menu.price) + '￦');

    // 수정내용 저장 버튼 메서드 등록
    $('#btn_update_menu').attr('onclick', 'onClickUpdateMenu(' + menuId + ')');

    // 모달 출력
    $('#btn_show_modal_result').trigger('click');
}

// 메뉴카드 수정 선택
function onClickModifyMenuCard(isModify) {
    // 수정버튼 토글
    if (isModify) {
        $('#btn_modify_menu_true').addClass('d-none');
        $('#btn_modify_menu_false').removeClass('d-none');
    }
    else {
        $('#btn_modify_menu_true').removeClass('d-none');
        $('#btn_modify_menu_false').addClass('d-none');
    }

    // 메뉴제목 인풋 표시
    if (isModify) {
        $('#input_menu_name').val($('#h5_modal_title').html());
        $('#h5_modal_title').addClass('d-none');
        $('#input_menu_name').removeClass('d-none');
    }
    else {
        $('#input_menu_name').val('');
        $('#h5_modal_title').removeClass('d-none');
        $('#input_menu_name').addClass('d-none');
    }

    // 이미지 편집 활성화
    if (isModify) {
        $('#img_menu').attr('onclick', 'onClickPicture()');
    }
    else {
        $('#img_menu').attr('onclick', '');
    }

    // 태그추가 인풋 표시
    if (isModify) {
        $('#input_who_with').removeClass('d-none');
    }
    else {
        $('#input_who_with').addClass('d-none');
    }

    // 점수변경 활성화
    var div_score_labels = $('#div_scores').find('label');

    if (isModify) {
        div_score_labels.each(function(idx, item){
            $(item).attr('for', ('input_star' + (5 - idx)));
        });
    }
    else {
        div_score_labels.each(function(idx, item){
            $(item).attr('for', ('input_starN'));
        });
    }

    // 태그삭제 버튼들 표시
    var div_tag_badges = $('#div_tag_list').find('.tags');

    if (isModify) {
        div_tag_badges.each(function(idx, item){
            $(item).removeClass('d-none');
        });
    }
    else {
        div_tag_badges.each(function(idx, item){
            $(item).addClass('d-none');
        });
    }

    // 태그추가 입력 표시
    if (isModify) {
        $('#div_modify_tags').removeClass('d-none');
    }
    else {
        $('#input_tag').val('');
        $('#div_modify_tags').addClass('d-none');
    }

    // 금액수정 버튼 표시, 금액 숨기기
    if (isModify) {
        var price = $('#span_price').html().replace(',', '').replace('￦', '')
        $('#input_menu_price').val(price);
        $('#input_menu_price').removeClass('d-none');
        $('#span_price').addClass('d-none');
    }
    else {
        $('#input_menu_price').val('');
        $('#input_menu_price').addClass('d-none');
        $('#span_price').removeClass('d-none');
    }

    // 기록보기 버튼 숨기기, 수정내용 저장버튼 활성
    if (isModify) {
        $('#btn_search_record').addClass('d-none');
        $('#btn_update_menu').removeClass('d-none');
    }
    else {
        $('#btn_search_record').removeClass('d-none');
        $('#btn_update_menu').addClass('d-none');
    }
}

// 메뉴 업데이트 버튼 클릭
function onClickUpdateMenu(menuId) {
    if (confirm('정말 메뉴를 수정하나요?')) {
        // 버튼 비활성화
        $('#btn_update_menu').attr('disabled', true);

        // 메뉴 파라미터 획득
        var mAry = new Array();
        var menu_name = $('#input_menu_name');
            
        if (!menu_name) {
            alert('메뉴명 입력칸을 찾을 수 없습니다.');
            return;
        }

        menu_name = menu_name.val();
        
        var menu_score = $('input[name=rating]:checked').val();
        
        // 필수 파라미터 검사
        if (!menu_name) {
            alert('메뉴명은 필수 항목입니다.');
            return;
        }

        if (!menu_score) {
            alert('메뉴 점수가 빈 항목이 있습니다.');
            return;
        }

        var menu_tags = null;
        var div_menu_tag_ary = $('#div_tag_list').children('#div_menu_tag');
    
        if (div_menu_tag_ary.length > 0) {
            menu_tags = '';

            for (j = 0; j < div_menu_tag_ary.length; ++j) {
                var menu_tag = $(div_menu_tag_ary[j]).find('#span_menu_tag').attr('value');
                menu_tags += (menu_tag + '`');
            }

            if (menu_tags.length > 0) {
                menu_tags = menu_tags.substring(0, menu_tags.length - 1);
            }
        }

        var menu_cols = [
            menu_name,
            $('#input_menu_price').val(),
            menu_tags,
            menu_score,
            (!!$('#input_pic').val() ? 1 : 0)
        ];

        console.log(menu_cols);
        mAry.push(menu_cols);

        // 멀티파트 폼 데이터 생성
        var mpForm = new FormData();
        mpForm.append('menus', JSON.stringify(mAry));

        var m_img = $('#input_pic');

        if (m_img.length != 0) {
            m_img = m_img.prop('files');

            if (m_img.length != 0) {
                mpForm.append('menu_pics', m_img[0]);
                console.log(m_img[0]);
            }
        }

        // URL및 유저토큰 획득
        var menuUpdateUrl = (g_menuApiUrl + '/' + menuId);
        var reqHeader = {
            'user_jwt' : g_userJwt
        }

        // 멀티파트 AJAX 전송
        AJAX.mpApiCall('PUT', menuUpdateUrl, reqHeader, mpForm, null, 
            // Success
            function(data, textStatus, jqXHR){
                if (AJAX.checkResultSuccess(data) == false) {
                    alert('메뉴 수정에 실패했습니다!\n(' + data.result_msg + ')');
                    return;
                }

                var menuData = data.result_data.updatedMenus;
                var menuId = menuData.id;
                var imgUrl = menuData.picUrl;
                var name = menuData.name;
                var score = menuData.score;
                var tags = menuData.tags;
                var price = menuData.price;
                var menu = {
                    "img_url" : imgUrl,
                    "name" : name,
                    "score" : score,
                    "tags" : tags,
                    "price" : price
                };

                g_menuMap.set(menuId, menu);
                updateMenuCard(menuId, imgUrl, name, score);
                $('#btn_close_menu').trigger('click');
                onClickMenuCard(menuId);
                alert('메뉴 수정이 완료되었습니다.');
                $('#btn_update_menu').attr('disabled', false);
            },
            // Fail
            function(){
                alert('서버와 통신에 실패했습니다!');
                $('#btn_update_menu').attr('disabled', false);
                return;
            }
        );
    }
    else {
        alert('메뉴 수정이 취소되었습니다.');
        return;
    }
}

// 메뉴 삭제버튼 클릭
function onClickDeleteMenu(menuId) {
    if (confirm('삭제 후 복원할 수 없습니다.\n정말로 삭제할까요?')) {
        var menuDeleteUrl = (g_menuApiUrl + '/' + menuId);
        var header = {
            'user_jwt' : g_userJwt
        }

        AJAX.apiCall('DELETE', menuDeleteUrl, header, null, null,
            // Success
            function(data, textStatus, jqXHR){
                if (AJAX.checkResultSuccess(data) == false) {
                    alert('메뉴 삭제에 실패했습니다!\n(' + data.result_msg + ')');
                    return;
                }

                removeMenuCard(menuId);
                $('#btn_close_menu').trigger('click');
                g_menuMap.delete(menuId);
                alert('메뉴 삭제가 완료되었습니다.');
            },
            // Fail
            function(){
                alert('서버와 통신에 실패했습니다!');
                return;
            }
        );
    }
    else {
        alert('삭제가 취소되었습니다.');
        return;
    }
}

// 메뉴 사진 클릭 시
function onClickPicture() {    
    // 선택이벤트를 발생시켜서 숨겨진 input type="file" 태그를 실행시킴
    $('#input_pic').trigger('click');
}

// 사진 촬영 혹은 저장소에서 선택 시
function onChangePicture(idx) {
    try {
        if (!window.File || !window.FileReader) {
            return alert('사진 업로드가 지원되지 않는 브라우저입니다!');
        }

        var files = event.target.files, file;
        var img = null;

        if (files && files.length > 0) {
            img = files[0];
        }

        var imgReader = new FileReader();
        imgReader.onload = function(e) {
            $('#img_menu').attr('src', e.target.result);
        }

        if (img instanceof Blob) {
            imgReader.readAsDataURL(img);
        }
    }
    catch (e) {
        alert(e);
    }
}

// 메뉴 배지태그 추가
function onClickAddMenuTag() {
    var div_list = $('#div_tag_list');
    var color = 'primary';
    var value_tag = $('#input_tag');
    var value = null;

    if (div_list.length == 0) {
        return;
    }

    if (value_tag.length == 0) {
        return;
    }
    else {
        value = value_tag.val();
        value_tag.val('');
    }

    if (!value || value.length == 0) {
        return;
    }

    var addedTag = '<div class="pr-1 pb-1" id="div_menu_tag"><span class="badge badge-' + color + '"value="' + value + '" id="span_menu_tag">#' + 
        value + '<i class="fas fa-times fa-sm fa-pull-right tags" onclick="onClickCloseMenuTag(this)"></i></span></div>';
    
    div_list.append(addedTag);
}

// 메뉴 배지 제거버튼 클릭
function onClickCloseMenuTag(btn) {
    var div_menu_tag = $(btn).closest('#div_menu_tag');

    if (div_menu_tag.length == 0) {
        return;
    }

    div_menu_tag.remove();
}

// 메뉴 수정까지 완료했음 앞으로 해야할것...
// 1) 2번페이지 조회버튼 추가
// 2) 기록수정 추가
// 3) 기록추가 시 컴포넌트들 초기화... (새로고침?)
// @@ 여기부터 시작