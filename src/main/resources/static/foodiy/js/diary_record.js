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