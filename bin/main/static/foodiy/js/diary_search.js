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