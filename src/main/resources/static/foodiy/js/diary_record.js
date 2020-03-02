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
    var timeStr = ts.format('a/p hh:mm').replace('AM', '오전').replace('PM', '오후');
    var timepicker = $('#div_timepicker');
    timepicker.datetimepicker({
        format : 'A hh:mm',
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
            var r_where_place = $('input_where_place').val();
            var r_who_with = $('div_who_with').val();
            
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
            // 일시
            if (!r_when_time) {
                alert('시간을 선택해 주세요!');
                return;
            }

            console.log(
                "r_title"       + " : " + r_title       + "\n" +
                "r_when_date"   + " : " + r_when_date   + "\n" +
                "r_when_time"   + " : " + r_when_time   + "\n" +
                "r_where_lati"  + " : " + r_where_lati  + "\n" +
                "r_where_longi" + " : " + r_where_longi + "\n" +
                "r_where_place" + " : " + r_where_place + "\n" +
                "r_who_with"    + " : " + r_who_with    + "\n"
            );

            // 여기부터 시작. @@
            // 사진을 어떻게 가져올 건지, 이후 조립하는 부분, 클라단 검사(날자등) 빡시게 추가! @@

            return;

            // 메뉴 필수 파라미터 검사
            // 제목
            // 점수


            // 멀티파트 폼 데이터 생성
            var mpForm = new FormData();
            mpForm.append('title', r_title);
            mpForm.append('when_date', r_when_date);
            mpForm.append('when_time', r_when_time);
            mpForm.append('where_lati', r_where_lati);
            mpForm.append('where_longi', r_where_longi);
            mpForm.append('where_place', r_where_place);
            mpForm.append('who_with', r_who_with);
            
            var img_files = event.target.files;
            var r_img_cnts = max(img_files.length, g_max_records_img);
            
            for (var imgCnt = 0; imgCnt < r_img_cnts; ++imgCnt) {
                mpForm.append('pics', imgFiles[imgCnt]);
            }

        }
        else {
            alert('기록이 취소되었습니다.');
        }
    }
    catch (e) {
        alert(e);
    }
}