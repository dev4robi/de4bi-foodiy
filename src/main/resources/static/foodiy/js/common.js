// 전역변수
var g_svcName       = 'foodiy';
var g_svcProtocol   = location.protocol + '//';
var g_svcDomain     = g_svcProtocol + (location.host.includes('localhost') ? location.host :'foodiy.de4bi.com');
var g_recordPageUrl = g_svcDomain + '/page/record';
var g_searchPageUrl = g_svcDomain + '/page/search';
var g_mapPageUrl    = g_svcDomain + '/page/map';
var g_statPageUrl   = g_svcDomain + '/page/stat';
var g_authServerUrl = 'https://auths.de4bi.com';
var g_recordApiUrl  = g_svcDomain + '/api/records';
var g_menuApiUrl    = g_svcDomain + '/api/menus';
var g_imgApiUrl     = g_svcDomain + '/etc/img';
var g_userJwt       = $.cookie('user_jwt');
