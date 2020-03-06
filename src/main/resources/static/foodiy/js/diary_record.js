// 페이지 전역
var g_max_records_img = 3;
var g_max_menus = 6;

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

    // 버튼 이벤트 초기화
    $('#btn_gps').on('click', onClickGps);                      // GPS 버튼
    $('#btn_add_menu').on('click', onClickAddMenu);             // 메뉴추가 버튼
    $('#btn_init_record').on('click', onClickInitRecord);       // 초기화 버튼
    $('#btn_goto_top').on('click', onClickGotoTop);             // 맨 위로 버튼
    $('#btn_upload_record').on('click', onClickUploadRecord);   // 기록 업로드 버튼

    // 일자 초기화
    var ts = new Date();
    var dateStr = ts.format('yyyy-MM-dd');
    var datepicker = $('#div_datepicker');
    datepicker.datetimepicker({
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

    // 시간 초기화
    var timeStr = ts.format('HH:mm');
    var timepicker = $('#div_timepicker');
    timepicker.datetimepicker({
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
});

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

// 메뉴추가 클릭 시
function onClickAddMenu() {
    try {
        var addMenuBtn = $('#btn_add_menu');
        addMenuBtn.prop('disabled', true);

        var newCardId = '';
        var menuCardIdx = 0;

        for (var i = 0; i <= g_max_menus; ++i) {
            menuCardIdx = i;
            newCardId = 'div_card_' + menuCardIdx;

            if (menuCardIdx == g_max_menus) {
                // 메뉴 개수는 g_max_menus(6)개 이하
                alert('메뉴를 너무 많이 추가했어요!');
                addMenuBtn.prop('disabled', false);
                return;
            }
            else if ($('#' + newCardId).length > 0) {
                // 해당 카드id가 존재하면 반복
                continue;
            }
            else {
                // 해당 카드id가 존재하지 않으면 탈출
                break;
            }
        }

        var menuCardList = $('#div_menucard_list');
        var menuCardHtml =
            '<div class="card p-1" style="display: none;" id="' + newCardId + '">' +
            '<img src="/foodiy/img/foodiy_logo.png" class="card-img-top shadow-sm rounded" alt="사진 불러오기 실패!" onclick="onClickPicture(' + menuCardIdx + ')" id="img_menu_pic_' + menuCardIdx + '">' +
            '<div class="custom-file mb-3 d-none">' +
            '<input type="file" class="custom-file-input" onchange=onChangePicture(' + menuCardIdx + ') id="input_pic_' + menuCardIdx + '" accept="image/*">' +
            '<label class="custom-file-label"></label>' +
            '</div>' +
            '<div class="card-body">' +
            '<div class="form-group">' +
            '<span>메뉴명</span>' +
            '<input type="text" class="form-control" id="input_menu_name_' + menuCardIdx + '"/>' +
            '<span>가격</span>' +
            '<input type="text" class="form-control" id="input_menu_price_' + menuCardIdx + '"/>' +
            '<span>태그</span>' +
            '<input type="text" class="form-control" id="input_menu_tag_' + menuCardIdx + '"/>' +
            '<span>평점</span>' +
            '<div class="starrating risingstar d-flex justify-content-center flex-row-reverse">' +
            '<input type="radio" id="input_star5_' + menuCardIdx + '" name="rating_' + menuCardIdx + '" value="5" />       <label for="input_star5_' + menuCardIdx + '"title="5Star">5&nbsp;&nbsp;&nbsp;</label>' + 
            '<input type="radio" id="input_star4_' + menuCardIdx + '" name="rating_' + menuCardIdx + '" value="4" />       <label for="input_star4_' + menuCardIdx + '"title="4Star">4&nbsp;&nbsp;&nbsp;</label>' + 
            '<input type="radio" id="input_star3_' + menuCardIdx + '" name="rating_' + menuCardIdx + '" value="3" checked/><label for="input_star3_' + menuCardIdx + '"title="3Star">3&nbsp;&nbsp;&nbsp;</label>' + 
            '<input type="radio" id="input_star2_' + menuCardIdx + '" name="rating_' + menuCardIdx + '" value="2" />       <label for="input_star2_' + menuCardIdx + '"title="2Star">2&nbsp;&nbsp;&nbsp;</label>' + 
            '<input type="radio" id="input_star1_' + menuCardIdx + '" name="rating_' + menuCardIdx + '" value="1" />       <label for="input_star1_' + menuCardIdx + '"title="1Star">1&nbsp;&nbsp;&nbsp;</label>' + 
            '</div>' +
            '</div>' +
            '<div class="d-flex justify-content-end pt-3"><span style="color: red;" onclick="onClickDeleteMenu(' + menuCardIdx + ')" id="span_remove_menu"><i class="far fa-minus-square"></i>&nbsp;삭제</span></div>' +
            '</div>' +
            '</div><br id="br_' + menuCardIdx + '">';

        menuCardList.append(menuCardHtml);
        $('#' + newCardId).fadeIn('slow');
        addMenuBtn.prop('disabled', false);
    }
    catch (e) {
        alert(e);
    }
}

// 사진 클릭 시
function onClickPicture(idx) {    
    // 선택이벤트를 발생시켜서 숨겨진 input type="file" 태그를 실행시킴
    $('#input_pic_' + idx).trigger('click');
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
            $('#img_menu_pic_' + idx).attr('src', e.target.result);
        }

        if (img instanceof Blob) {
            imgReader.readAsDataURL(img);
        }
    }
    catch (e) {
        alert(e);
    }
}

// 사진 초기화 클릭 시
function onClickPictureInit() {
    try {
        if (confirm('정말로 사진을 초기화 하나요?')) {
            for (i = -3; i < 0; ++i) {
                $('#' + 'input_pic_' + i).val('');
                $('#' + 'img_menu_pic_' + i).attr('src', '/foodiy/img/foodiy_logo.png');
            }

            return alert('사진 초기화를 완료했습니다.');
        }
        else {
            return alert('사진 초기화를 취소했습니다.');
        }
    }
    catch (e) {
        alert(e);
    }
}

// 메뉴삭제 클릭 시
function onClickDeleteMenu(idx) {
    try {
        var cardMenuId = '#div_card_' + idx;
        var cardMenu = $(cardMenuId);
        var cardMenuName = cardMenu.find('#input_menu_name_' + idx);
        var menuNameStr = ((!!cardMenuName.val()) ? (' "' + cardMenuName.val() + '"을(를)') : ('를'));

        if (confirm('메뉴' + menuNameStr + ' 정말로 삭제하나요?')) {
            $('#br_' + idx).remove();
            cardMenu.remove();
            return alert('메뉴' + menuNameStr + ' 삭제했습니다.');
        }
        else {
            return alert('메뉴 삭제를 취소했습니다.');
        }
    }
    catch (e) {
        alert(e);
    }
}

// 초기화 버튼
function onClickInitRecord() {
    if (confirm('정말 초기화 하나요?')) {
        location.reload();
    }
    else {
        alert('초기화를 취소했습니다.');
    }
}

// 맨 위로 버튼
function onClickGotoTop() {
    $('#body_record').scrollTop(0);
}

// 기록 업로드 버튼
function onClickUploadRecord() {
    try {
        if (confirm('위 내용으로 기록할까요?')) {
            // 레코드 파라미터 획득
            var r_title  = $('#input_title').val();
            var r_when_date = $('#input_datepicker').val();
            var r_when_time = $('#input_timepicker').val();
            var r_where_lati = $('#input_where_lati').val();
            var r_where_longi = $('#input_where_longi').val();
            var r_where_place = $('#input_where_place').val();
            var r_who_with = $('#input_who_with').val();
            
            // 레코드 필수 파라미터 검사
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

            // 메뉴 파라미터 획득
            var mAry = new Array();

            for (i = 0; i < g_max_menus; ++i) {
                // 메뉴 필수 파라미터 검사
                var menu_name = $('#input_menu_name_' + i);
                
                if (!menu_name) {
                    // 메뉴 태그검사
                    continue;
                }

                menu_name = menu_name.val();
                
                var menu_score = $('input[name=rating_' + i + ']:checked').val();
                
                if (!menu_name) {
                    continue;
                }

                if (!menu_score) {
                    alert('메뉴 점수가 빈 항목이 있습니다.');
                    return;
                }

                var menu_cols = [
                    menu_name,
                    $('#input_menu_price_' + i).val(),
                    $('#input_menu_tag_' + i).val(),
                    menu_score,
                    (!!$('#input_pic_' + i).val() ? 1 : 0)
                ];

                mAry.push(menu_cols);
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

            for (i = -3; i < 0; ++i) {
                var r_img = $('#input_pic_' + i);

                if (r_img.length == 0) {
                    continue;
                }

                r_img = r_img.prop('files');

                if (r_img.length == 0) {
                    continue;
                }

                mpForm.append('pics', r_img[0]);
            }

            // 메뉴
            mpForm.append('menus', JSON.stringify(mAry));

            for (i = 0; i < g_max_menus; ++i) {
                var m_img = $('#input_pic_' + i);

                if (m_img.length == 0) {
                    continue;
                }

                m_img = m_img.prop('files');

                if (m_img.length == 0) {
                    continue;
                }

                mpForm.append('menu_pics', m_img[0]);
                console.log(m_img[0]);
            }

            // AJAX 업로드 호출
            var reqHeader = {
                'user_jwt' : '-CkcWpJaYYF5ThH0UkXadT8b6AOTQQH3-6BC9kjjGTqQCYj4TRbjpye3AJJKUL9ZLowwVkA8bgs6u8YVQjPeGtNXoOqMcXKWkmQsFRJIG-xp4GD9maPe5iEuF2nWs27AHvXAskMVkMFE8WqVPZqSDFuyTJcEGlEqWDc7-Yhn7mxvRf2roCLLJXvZFYgBPmwGGz4xr_sa9RxjPIR7kdyIpPIz2sVLGzeYHDbChJNX2zWX-utaZblUH979uXmgMfcbKDZ9GJhxQxXwc1oOhOLBqyX-sBF5Yy7nOTf8R3G9Nu1wFUWj3Ur6IdoVon_Uua9FNkUiOqO-ob7jAQCoDuJj5HH2DFC6EfwTXW1jRAUTg4PYIdZ75jwWX3hSaz76Let_'
            };
            
            AJAX.mpApiCall('POST', recordApiUrl, reqHeader, mpForm, null, recordSuccess, recordFail);
        }
        else {
            alert('기록이 취소되었습니다.');
        }
    }
    catch (e) {
        alert(e);
    }
}

// 기록 성공
function recordSuccess(rst) {
    if (AJAX.checkResultSuccess(rst)) {
        alert('기록이 완료되었습니다.');
    }
    else {
        recordFail(rst);
    }
}

// 기록 실패
function recordFail(rst) {
    alert('기록에 실패했습니다.\n(' + AJAX.getResultData(rst, 'result_msg') + ')');
}

// 배지태그 추가
function addBadgeTag(base_list, color, value) {
    var div_list = $('#' + base_list);

    if (div_list.length == 0) {
        return;
    }

    var addedTag = '<div class="pr-1 pb-1" id="div_who_with"><span class="badge badge-' + color + '" id="span_who_with">' + 
        value + '<i class="fas fa-times fa-sm fa-pull-right" onclick="onClickCloseBadge()"></i></span></div>';
    
    div_list.append(addedTag);
}

// 배지제거버튼 클릭
function onClickCloseBadge() {
    var div_who_with = $(this).closest('div'); // 여기부터 시작... x누르면 닫아버리기 구현@
    console.log(div_who_with);

    if (div_who_with.length == 0) {
        return;
    }

    div_who_with.remove();
}