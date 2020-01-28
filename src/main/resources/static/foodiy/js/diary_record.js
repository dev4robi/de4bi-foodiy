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
    // 메뉴추가 버튼
    $('#btn_add_menu').on('click', onClickAddMenu);

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

// 메뉴추가 클릭 시
function onClickAddMenu() {
    var addMenuBtn = $('#btn_add_menu');
    addMenuBtn.prop('disabled', true);

    var newCardId = '';
    var menuCardIdx = 0;

    for (var i = 0; i <= 10; ++i) {
        menuCardIdx = i;
        newCardId = 'div_card_' + menuCardIdx;

        if (menuCardIdx == 10) {
            // 메뉴 개수는 10개 이하
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
        '<span>메뉴</span>' +
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
        '<span style="color: red;" onclick="onClickDeleteMenu(' + menuCardIdx + ')" id="span_remove_menu"><i class="far fa-minus-square"></i>삭제</span>' +
        '</div>' +
        '</div><br id="br_' + menuCardIdx + '">';

    menuCardList.append(menuCardHtml);
    $('#' + newCardId).fadeIn('slow');
    addMenuBtn.prop('disabled', false);
}

// 사진 클릭 시
function onClickPicture(idx) {    
    // 선택이벤트를 발생시켜서 숨겨진 input type="file" 태그를 실행시킴
    $('#input_pic_' + idx).trigger('click');
}

// 사진 촬영 혹은 저장소에서 선택 시
function onChangePicture(idx) {
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

    imgReader.readAsDataURL(img);
}

// 메뉴삭제 클릭 시
function onClickDeleteMenu(idx) {
    var cardMenuId = '#div_card_' + idx;
    var cardMenu = $(cardMenuId);
    var cardMenuName = cardMenu.find('#input_menu_name_' + idx);
    var menuNameStr = ((!!cardMenuName.val()) ? (' "' + cardMenuName.val() + '"을(를)') : ('를'));

    if (confirm('메뉴' + menuNameStr + ' 정말로 삭제하나요?')) {
        $('#br_' + idx).remove();
        cardMenu.remove();
    }
}