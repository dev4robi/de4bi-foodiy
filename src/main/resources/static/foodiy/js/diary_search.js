// 페이지 전역
var g_searchCnt = 0;
var g_searchPageIdx = 0;
var g_lastSearchCondition = '';
var g_lastSearchKeyword = '';
var g_recordMap = new Map();
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
    $('#div_search_condition').change(function(){
        onChangeSearchCondition();
    });
    $('#btn_search').on('click', function(){
        onClickSearchBtn(false);
    });
    $('#btn_search_continue').on('click', function(){
        onClickSearchBtn(true);
    });
    $('#btn_add_tag').on('click', function(){
        onClickAddMenuTag('div_tag_list', $('#input_tag').val());
    });
    $('#btn_add_person').on('click', function(){
        onClickAddMenuTag('div_who_with_list', $('#input_who_with_tag').val());
    });

    // UI 초기화
    $('input[value="page"]').trigger('click');
});

// 검색조건 라디오버튼 변동 시
function onChangeSearchCondition(radioBtn) {
    var checked = $('input[name="options"]:checked').val();

    if (checked == 'page') {
        $('#input_search_keyword').attr('readonly', true);
    }
    else {
        $('#input_search_keyword').attr('readonly', false);
    }
}

// 검색버튼 클릭 시
function onClickSearchBtn(isContinue) {
    var searchCondition = $('input[name="options"]:checked').val();
    var searchKeyword = $('#input_search_keyword').val();

    if (searchCondition != 'page' && !searchKeyword) {
        alert('검색어를 입력해 주세요.');
        return;
    }

    // 검색버튼 비활성화
    $('#btn_search').attr('disabled', true);
    $('#btn_search_continue').attr('disabled', true);
    $('#btn_search_continue').addClass('d-none');
    $('#div_menu_loading').removeClass('d-none');

    callSearchApi(isContinue);
}

// 검색
function callSearchApi(isContinue) {
    var searchCondition = isContinue ? g_lastSearchCondition : $('input[name="options"]:checked').val();
    var searchKeyword = isContinue ? g_lastSearchKeyword : $('#input_search_keyword').val()
    var apiUrl = g_menuApiUrl;

    g_lastSearchCondition = searchCondition;
    g_lastSearchKeyword = searchKeyword;

    if (isContinue == false) {
        // 검색버튼으로 검색 시 항상 0번 페이지부터 검색
        g_searchPageIdx = 0;
        g_searchCnt = 0;

        // 기존 검색결과 지움
        removeMenuCardAll();
    }
    else {
        // 페이지 증가
        ++g_searchPageIdx;
    }

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
            activeSearchBtn();
            alert('비정상 응답!(' + data.result_msg + ')');
        }

        var menuDataAry = resultDatas.selectedMenusList;

        if (!menuDataAry || menuDataAry.length == 0) {
            activeSearchBtn();

            var tgt = $('#input_search_keyword').val();

            if ($('input[name="options"]:checked').val() != 'page' && !!tgt) {
                tgt = ('\'' + tgt + '\' ');
            }
            else {
                tgt = '';
            }
            
            alert(tgt + '조회 결과가 없습니다.');
        }
        else {
            for (i = 0; i < menuDataAry.length; ++i) {
                var menuData = menuDataAry[i];
                var menuId = menuData.id;
                var recordId = menuData.recordId;
                var imgUrl = menuData.picUrl;
                var name = menuData.name;
                var score = menuData.score;
                var tags = menuData.tags;
                var price = menuData.price;
                var menu = {
                    "record_id" : recordId,
                    "img_url" : imgUrl,
                    "name" : name,
                    "score" : score,
                    "tags" : tags,
                    "price" : price
                };

                g_menuMap.set(menuId, menu);
                addMenuCard(menuId, imgUrl, name, score, 'div_menu_list');
            }

            if (menuDataAry.length >= 8) {
                // 한 번에 8개 이상 표현할 수 있어야 계속 검색할게 있음
                $('#btn_search_continue').removeClass('d-none');
            }
        }

        $('#b_title_menu_result').html('결과(' + (g_searchCnt += menuDataAry.length) + ')');
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
    $('#btn_search_continue').attr('disabled', false);
}

// 메뉴카드 추가
function addMenuCard(menu_id, img, name, score, targetDivId) {
    var div_menu_list = $('#' + targetDivId);

    if (div_menu_list.length == 0) {
        return;
    }

    var score_color_ary = ['secondary', 'danger', 'warning', 'info', 'success', 'primary'];
    var picUrl = getImageUrl(img);
    var div_card_tag = $(
        '<div style="width:50%" class="embed-responsive embed-responsive-4by3 shadow-sm rounded p-1" id="div_menu_card_' + menu_id + '">' +
            '<input type="hidden" value="' + menu_id + '" id="input_menu_id">' +
            '<img src="' + picUrl + '" class="embed-responsive-item" alt="사진 불러오기 실패!" onclick="onClickMenuCard(' + menu_id + ')">' +
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
    $('#img_menu').attr('src', getImageUrl(menu.img_url) + '?time=' + new Date().getTime());

    // 제목
    $('#h5_modal_title').html(menu.name);
    
    // 태그
    var div_tag_list = $('#div_tag_list');
    
    div_tag_list.empty();

    var tags = menu.tags;
    
    if (tags != null && tags != "null") {
        var tagSplit = tags.split('`');

        for (i = 0; i < tagSplit.length; ++i) {
            try {
                var tag = '<div class="pr-1 pb-1" id="div_menu_tag"><span class="badge badge-primary" value="' + tagSplit[i] + '" id="span_menu_tag">#'+ tagSplit[i] + '<i class="fas fa-times fa-sm fa-pull-right tags d-none" onclick="onClickCloseMenuTag(this)"></i></span></div>';
                div_tag_list.append(tag);
            }
            catch (e) {
                console.log(e);
            }
        }
    }
    
    // 점수
    $('#input_star' + menu.score).attr('checked', true);

    // 금액
    $('#span_price').html(numberWithCommas(menu.price) + '￦');

    // 수정내용 저장 버튼 메서드 등록
    $('#btn_update_menu').attr('onclick', 'onClickUpdateMenu(' + menuId + ')');

    // 기록보기 버튼 이벤트 변경
    $('#btn_search_record').attr('onclick', 'onClickShowRecords(' + menu.record_id + ', ' + menuId + ')');

    // 모달 출력
    if ($('#div_modal').hasClass('show') == false) {
        $('#btn_show_modal_result').trigger('click');
    }

    // 모달 전환
    $('#div_modal_record').addClass('d-none');
    $('#div_modal_menu').removeClass('d-none');
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
        $('#img_menu').attr('onclick', 'onClickPicture(null)');
    }
    else {
        $('#img_menu').attr('onclick', '');
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
function onClickPicture(idx) {    
    // 선택이벤트를 발생시켜서 숨겨진 input type="file" 태그를 실행시킴
    if (idx == null) {
        $('#input_pic').trigger('click');
    }
    else {
        $('#input_record_pic' + idx).trigger('click');
    }
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

        if (idx == null) {
            console.log('1');
            imgReader.onload = function(e) {
                $('#img_menu').attr('src', e.target.result);
            }
        }
        else {
            console.log('2');
            imgReader.onload = function(e) {
                $('#img_records_img' + idx).attr('src', e.target.result);
            }
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
function onClickAddMenuTag(divId, value) {
    var div_list = $('#' + divId);
    var color = 'success';

    if ($(div_list).length == 0) {
        return;
    }

    if (!value || value == "null") {
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

// 기록조회버튼 클릭
function onClickShowRecords(recordId, menuId) {
    // 버튼 비활성화
    $('#btn_search_record').attr('disabled', true);

    // UI 초기화
    onClickModifyRecordCard(false);

    // 삭제버튼 갱신
    $('#btn_delete_record').attr('onclick', 'onClickDeleteRecord(' + recordId + ')');

    // UI 초기화
    $('#h5_modal_record_title').html('');
    $('#div_record_pics').empty();
    $('#span_record_when').html('?');
    $('#span_record_where').html('?');
    $('#div_who_with_list').empty();
    $('#div_record_menulist').empty();

    // 기록수정 버튼 이벤트 수정
    $('#btn_update_record').attr('onclick', 'onClickUpdateRecord(' + recordId + ')');

    // API 호출
    var apiUrl = g_recordApiUrl + '/' + recordId;
    var reqHead = {'user_jwt' : g_userJwt};

    AJAX.apiCall('GET', apiUrl, reqHead, null, null,
        // 통신 성공
        function(data, textStatus, jqXHR) {
            if (AJAX.checkResultSuccess(data)) {
                // 조회결과 성공
                var recordData = data.result_data.selectedRecord;

                // 기록결과 캐싱
                var record = {
                    "id" : recordData.id,
                    "title" : recordData.title,
                    "pic_urls" : recordData.picUrls,
                    "when_date" : recordData.whenDate,
                    "when_time" : recordData.whenTime,
                    "where_lati" : recordData.whereLati,
                    "where_longi" : recordData.whereLongi,
                    "where_place" : recordData.wherePlace,
                    "who_with" : recordData.whoWith
                }

                g_recordMap.set(recordData.id, record);

                // 제목
                $('#h5_modal_record_title').html(recordData.title);

                // 사진
                var picUrls = recordData.picUrls;
                
                if (!!picUrls) {
                    var picUrlAry = picUrls.split('`');
                    var picCnt = picUrlAry.length;

                    for (i = 0; i < picCnt; ++i) {
                        var tagIdx = (i + 1);
                        var picUrl = getImageUrl(picUrlAry[i]);
                        var picTag = (
                            '<div style="width:100%" class="embed-responsive embed-responsive-4by3 shadow-sm rounded">' +
                                '<input type="file" class="custom-file-input" onchange="onChangePicture(' + tagIdx + ')" id="input_record_pic' + tagIdx + '" accept="image/*">' +
                                '<img src="' + picUrl + '" class="embed-responsive-item" alt="사진 불러오기 실패!" id="img_records_img' + tagIdx + '">' +
                            '</div>'
                        );

                        $('#div_record_pics').append(picTag);
                    }
                }
                else {
                    // 사진없음
                }

                // 일자 및 시간
                var whenDate = recordData.whenDate;
                var whenTime = recordData.whenTime;
                var dateWithTime = '?';

                if (!!whenDate && !!whenTime) {
                    dateWithTime = (whenDate + ' ' + whenTime.substring(0, 5));
                }

                $('#span_record_when').html(dateWithTime);

                // 장소 및 지도
                $('#span_record_where').html(recordData.wherePlace);
                $('#input_where_lati').val(recordData.whereLati);
                $('#input_where_longi').val(recordData.whereLongi);
                initMapOnSearch();

                // 누구랑
                var whoWith = recordData.whoWith;

                if (whoWith != null && whoWith != "null") {
                    var whoWithAry = whoWith.split('`');
                    var whoWithCnt = whoWithAry.length;

                    for (i = 0; i < whoWithCnt; ++i) {
                        var whoName = whoWithAry[i];
                        var whoWithTag = (
                            '<div class="pr-1 pb-1" id="div_menu_tag"><span class="badge badge-primary" value="' + whoName +
                            '" id="span_menu_tag">#'+ whoName +
                            '<i class="fas fa-times fa-sm fa-pull-right tags d-none" onclick="onClickCloseMenuTag(this)"></i></span></div>'
                        );

                        $('#div_who_with_list').append(whoWithTag);
                    }
                }

                // 메뉴데이터 획득을 위해 추가 API호출
                var menuApiUrl = (g_menuApiUrl + '/record-id/' + recordId);
                AJAX.apiCall('GET', menuApiUrl, reqHead, null, 
                    // 항상
                    function() {
                        $('#div_records_menulist_loading').removeClass("d-none");
                    },
                    // 통신 성공
                    function(data, textStatus, jqXHR) {
                        if (AJAX.checkResultSuccess(data)) {
                            var menuDataAry = data.result_data.selectedMenusList;

                            for (i = 0; i < menuDataAry.length; ++i) {
                                var menuData = menuDataAry[i];
                                var menuId = menuData.id;
                                var recordId = menuData.recordId;
                                var imgUrl = menuData.picUrl;
                                var name = menuData.name;
                                var score = menuData.score;
                                var tags = menuData.tags;
                                var price = menuData.price;
                                var menu = {
                                    "record_id" : recordId,
                                    "img_url" : imgUrl,
                                    "name" : name,
                                    "score" : score,
                                    "tags" : tags,
                                    "price" : price
                                };

                                g_menuMap.set(menuId, menu);
                                addMenuCard(menuId, imgUrl, name, score, 'div_record_menulist');
                            }
                        }

                        $('#div_records_menulist_loading').addClass("d-none");
                    },
                    // 통신 실패
                    function() {
                        $('#div_records_menulist_loading').addClass("d-none");
                    }
                );

                // 메뉴로 돌아가기 버튼 업데이트
                $('#btn_backto_menu').attr('onclick', ('onClickMenuCard(' + menuId + ')'));
             
                // 모달 전환
                $('#div_modal_menu').addClass('d-none');
                $('#div_modal_record').removeClass('d-none');
            }
            else {
                // 조회결과 실패
                alert('기록 조회에 실패했습니다.\n(' + data.result_msg + ')');
            }

            $('#btn_search_record').attr('disabled', false);
        },
        // 통신 실패
        function() {
            alert('서버와 통신에 실패했습니다!');
            $('#btn_search_record').attr('disabled', false);
        }
    );
}

// 기록수정 클릭
function onClickModifyRecordCard(isModify) {
    // 수정취소 버튼 스왑
    if (isModify) {
        $('#btn_modify_record_true').addClass('d-none');
        $('#btn_modify_record_false').removeClass('d-none');
    }
    else {
        $('#btn_modify_record_false').addClass('d-none');
        $('#btn_modify_record_true').removeClass('d-none');
    }

    // 제목
    if (isModify) {
        var h5_title = $('#h5_modal_record_title');
        h5_title.addClass('d-none');
        
        var input_title = $('#input_modify_record_name');
        input_title.removeClass('d-none');

        input_title.val(h5_title.html());
    }
    else {
        $('#input_modify_record_name').addClass('d-none');
        $('#h5_modal_record_title').removeClass('d-none');
    }

    // 이미지 편집 활성화
    if (isModify) {
        for (i = 0; i < 3; ++i) {
            $('#img_records_img' + (i + 1)).attr('onclick', 'onClickPicture(' + (i + 1) + ')');
        }
    }
    else {
        for (i = 0; i < 3; ++i) {
            $('#img_records_img' + (i + 1)).attr('onclick', '');
        }
    }

    // 일자 및 시간
    if (isModify) {
        var div_datepicker = $('#div_modify_datepicker');
        div_datepicker.addClass('d-none');

        var div_timepicker = $('#div_modify_timepicker');
        div_timepicker.addClass('d-none');

        var span_record_when = $('#span_record_when');
        span_record_when.addClass('d-none');

        // Datepicker
        var dateStr = span_record_when.html().substring(0, 10); // yyyy-MM-dd
        div_datepicker.datetimepicker({
            format : 'YYYY-MM-DD',
            locale : 'ko',
            showTodayButton : true,
            showClose : false,
            icons: {
                time: 'far fa-clock',
                date: 'far fa-calendar',
                up: 'fas fa-chevron-up',
                down: 'fas fa-chevron-down',
                previous: 'fas fa-chevron-left',
                next: 'fas fa-chevron-right',
                today: 'fas fa-history',
                clear: 'far fa-trash',
                close: 'fas fa-times'
            }
        });
        
        $('#input_datepicker').val(dateStr);

        // Timepicker
        var timeStr = span_record_when.html().substring(11, 16); // HH:mm
        div_timepicker.datetimepicker({
            format : 'HH:mm',
            locale : 'ko',
            showTodayButton : true,
            showClose : false,
            icons: {
                time: 'far fa-clock',
                date: 'far fa-calendar',
                up: 'fas fa-chevron-up',
                down: 'fas fa-chevron-down',
                previous: 'fas fa-chevron-left',
                next: 'fas fa-chevron-right',
                today: 'fas fa-history',
                clear: 'far fa-trash',
                close: 'fas fa-times'
            }
        });
        $('#input_timepicker').val(timeStr);

        div_datepicker.removeClass('d-none');
        div_timepicker.removeClass('d-none');
        $('#hr_modify_datetime_divider').removeClass('d-none');
    }
    else {
        $('#div_modify_datepicker').addClass('d-none');
        $('#div_modify_timepicker').addClass('d-none');
        $('#hr_modify_datetime_divider').addClass('d-none');
        $('#span_record_when').removeClass('d-none');
    }

    // 장소및 지도
    if (isModify) {
        var span_record_where = $('#span_record_where');
        span_record_where.addClass('d-none');

        var div_modify_where = $('#div_modify_where');
        div_modify_where.removeClass('d-none');

        $('#input_where_place').val(span_record_where.html());
        $('#div_map_btn_gps').removeClass('d-none');
    }
    else {
        $('#div_map_btn_gps').addClass('d-none');
        $('#div_modify_where').addClass('d-none');
        $('#span_record_where').removeClass('d-none');
    }

    // 누구랑 태그삭제 버튼들 표시
    var div_tag_badges = $('#div_who_with_list').find('.tags');

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

    // 누구랑
    if (isModify) {
        $('#div_modify_whowith').removeClass('d-none');
    }
    else {
        $('#input_who_with_tag').val('');
        $('#div_modify_whowith').addClass('d-none');
    }

    // 메뉴
    if (isModify) {
        $('#div_record_menus').addClass('d-none');
    }
    else {
        $('#div_record_menus').removeClass('d-none');
    }

    // 하단 버튼
    if (isModify) {
        $('#btn_backto_menu').addClass('d-none');
        $('#btn_update_record').removeClass('d-none');
    }
    else {
        $('#btn_update_record').addClass('d-none');
        $('#btn_backto_menu').removeClass('d-none');
    }

    // 변수 스위칭
    if (isModify) {
        $('#input_check_modifying').val('true');
    }
    else {
        $('#input_check_modifying').val('false');
    }
}

// 기록 수정
function onClickUpdateRecord(recordId) {
    if (confirm('정말 기록을 수정하나요?')) {
        // 버튼 비활성화
        $('#btn_update_record').attr('disabled', true);

        // 기록 파라미터 획득
        var r_title  = $('#input_modify_record_name').val();
        var r_when_date = $('#input_datepicker').val();
        var r_when_time = $('#input_timepicker').val();
        var r_where_lati = $('#input_where_lati').val();
        var r_where_longi = $('#input_where_longi').val();
        var r_where_place = $('#input_where_place').val();
        var r_who_with = null;

        var div_who_with_ary = $('#div_who_with_list').children('#div_menu_tag');
        
        if (div_who_with_ary.length > 0) {
            r_who_with = '';

            for (i = 0; i < div_who_with_ary.length; ++i) {
                var who_with = $(div_who_with_ary[i]).find('#span_menu_tag').attr('value');
                r_who_with += (who_with + '`');
            }

            if (r_who_with.length > 0) {
                r_who_with = r_who_with.substring(0, r_who_with.length - 1);
            }
        }
        
        // 기록 필수 파라미터 검사
        // 제목
        if (!r_title) {
            alert('제목을 입력해 주세요!');
            return;
        }
        // 일자
        if (!r_when_date) {
            alert('일자를 선택해 주세요!');
            return;
        }
        else {
            r_when_date = r_when_date.replace(/-/gi, '');
        }
        // 일시
        if (!r_when_time) {
            alert('시간을 선택해 주세요!');
            return;
        }
        else {
            r_when_time = r_when_time.replace(/:/gi, '');
            r_when_time += '00';
        }

        // 멀티파트 폼 데이터 생성
        var mpForm = new FormData();

        // 레코드
        mpForm.append('title', r_title);
        mpForm.append('when_date', r_when_date);
        mpForm.append('when_time', r_when_time);
        mpForm.append('where_lati', r_where_lati);
        mpForm.append('where_longi', r_where_longi);
        mpForm.append('where_place', r_where_place);
        mpForm.append('who_with', r_who_with);

        for (i = 1; i < 4; ++i) {
            var r_img = $('#input_record_pic' + i);

            if (r_img.length == 0) {
                continue;
            }

            r_img = r_img.prop('files');

            if (r_img.length == 0) {
                continue;
            }

            mpForm.append('pics', r_img[0]);
        }

        // URL및 유저토큰 획득
        var recordUpdateUrl = (g_recordApiUrl + '/' + recordId);
        var reqHeader = {
            'user_jwt' : g_userJwt
        }

        // 멀티파트 AJAX 전송
        AJAX.mpApiCall('PUT', recordUpdateUrl, reqHeader, mpForm, null, 
            // Success
            function(data, textStatus, jqXHR){
                if (AJAX.checkResultSuccess(data) == false) {
                    alert('기록 수정에 실패했습니다!\n(' + data.result_msg + ')');
                    $('#btn_update_record').attr('disabled', false);
                    return;
                }

                var recordData = data.result_data.updatedRecords;

                var record = {
                    "id" : recordData.id,
                    "title" : recordData.title,
                    "pic_urls" : recordData.picUrls,
                    "when_date" : recordData.whenDate,
                    "when_time" : recordData.whenTime,
                    "where_lati" : recordData.whereLati,
                    "where_longi" : recordData.whereLongi,
                    "where_place" : recordData.wherePlace,
                    "who_with" : recordData.whoWith
                };

                g_recordMap.set(recordId, record);
                alert('기록 수정이 완료되었습니다.');
                $('#btn_update_record').attr('disabled', false);
            },
            // Fail
            function(){
                alert('서버와 통신에 실패했습니다!');
                $('#btn_update_record').attr('disabled', false);
                return;
            }
        );
    }
    else {
        alert('메뉴 수정이 취소되었습니다.');
        return;
    }
}

// 기록삭제 클릭
function onClickDeleteRecord(recordId) {
    if (confirm('기록을 삭제하면 등록된 메뉴들 또한 삭제됩니다.\n정말 삭제하나요? 기록과 메뉴 모두 복원할 수 없습니다!')) {
        var apiUrl = (g_recordApiUrl + '/' + recordId);
        var reqHead = {'user_jwt' : g_userJwt};

        AJAX.apiCall('DELETE', apiUrl, reqHead, null, null,
            // Success
            function(data, textStatus, jqXHR){
                if (AJAX.checkResultSuccess(data) == false) {
                    alert('기록 삭제에 실패했습니다!\n(' + data.result_msg + ')');
                    return;
                }

                $('#btn_close_record').trigger('click');
                alert('기록 삭제가 완료되었습니다.');

                g_recordMap.delete(recordId);
                callSearchApi(false);
            },
            // Fail
            function(){
                alert('서버와 통신에 실패했습니다!');
                return;
            }
        );
    }
    else {
        alert('기록 삭제가 취소되었습니다.');
        return;
    }
}

// 이미지 경로 획득
function getImageUrl(name) {
    if (!name) {
        return '/foodiy/img/foodiy_logo.png';
    }

    return g_imgApiUrl + '/' + picUrlAry[i];
}