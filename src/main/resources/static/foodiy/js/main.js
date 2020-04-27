// 페이지 전역
// ...

// 페이지 초기화
$(document).ready(function(){    
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

    // 유저토큰 획득
    var newUserJwt = getUrlParameter('userJwt');
    if (!!newUserJwt) {
        g_userJwt = newUserJwt;

        if (!!getUrlParameter('keepLoggedIn')) {
            $.cookie('user_jwt', newUserJwt, { expires: 15 }); // 15days
        }
        else {
            $.cookie('user_jwt', newUserJwt, { expires: 1 }); // 1day
        }
    }

    // 로그인 확인
    {
        if (!!g_userJwt == false) {
            alert('로그인이 필요한 서비스입니다.');
            replaceToAuthPage();
            return;
        }

        var authApiUrl = g_svcDomain + '/api/users/validate';
        var header = {
            'user_jwt' : g_userJwt
        }

        AJAX.apiCall('POST', authApiUrl, header, null,
            // Always
            function(data_jqXHR, textStatus, jqXHR_errorThrown){
                // ...
            },
            // Success
            function(data, textStatus, jqXHR){
                if (!AJAX.checkResultSuccess(data)) {
                    alert('인증에 실패했습니다. 다시 로그인 해 주세요.\n(' + data.result_msg + ')');
                    replaceToAuthPage();
                    return;
                }

                // 메인페이지 - 레코드탭 자동 클릭
                $('#a_record-tab').trigger('click');

                return;
            },
            // Fail
            function(jqXHR, textStatus, errorThrown){
                alert('인증서버 통신에 실패했습니다.');
                return;
            }
        );
    }
});

// 인증페이지 이동
function replaceToAuthPage() {
    location.replace(g_authServerUrl + '/main?audience=' + g_svcName + '&afterIssueParam=' + g_svcDomain + '/page/main');
}

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