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

    // 일자 초기화
    var ts = new Date();
    $('#div_datepicker').datetimepicker({
        // [Note] 기본값
        // maskInput: true,           // disables the text input mask
        // pickDate: true,            // disables the date picker
        // pickTime: true,            // disables de time picker
        // pick12HourFormat: false,   // enables the 12-hour format time picker
        // pickSeconds: true,         // disables seconds in the time picker
        // startDate: -Infinity,      // set a minimum date
        // endDate: Infinity          // set a maximum date
        maskInput: true,
        pickDate: true,
        pickTime: false,
    });

    $('#input_date').val(ts.format('yyyy-MM-dd'));

    // 시간 초기화
    $('#div_timepicker').datetimepicker({
        maskInput: true,
        pickDate: false,
        pickTime: true,
        pickSeconds: false,
    });

    $('#input_time').val(ts.format('HH:mm'));
});

// navi-bar 탭 클릭 시
function onTabClick(id) {
    var iframe_tag = null;

    if (id == 'record') {
        iframe_tag = $('#iframe_record');
        if (!iframe_tag.attr('src')) {
            iframe_tag.attr('src', recordPageUrl);
        }
    }
    else if (id == 'search') {
        iframe_tag = $('#iframe_search');
        if (!iframe_tag.attr('src')) {
            iframe_tag.attr('src', searchPageUrl);
        }
    }
    else if (id == 'map') {
        iframe_tag = $('#iframe_map');
        if (!iframe_tag.attr('src')) {
            iframe_tag.attr('src', mapPageUrl);
        }
    }
    else if (id == 'stat') {
        iframe_tag = $('#iframe_stat');
        if (!iframe_tag.attr('src')) {
            iframe_tag.attr('src', statPageUrl);
        }
    }
}