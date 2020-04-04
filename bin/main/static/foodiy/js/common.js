// 전역변수
var g_svcName       = 'foodiy';
var g_svcDomain     = 'http://localhost:40003';
var g_recordPageUrl = g_svcDomain + '/page/record';
var g_searchPageUrl = g_svcDomain + '/page/search';
var g_mapPageUrl    = g_svcDomain + '/page/map';
var g_statPageUrl   = g_svcDomain + '/page/stat';
var g_authServerUrl = 'http://dev4robi.net:40000';
var g_recordApiUrl  = g_svcDomain + '/api/records';
var g_menuApiUrl    = g_svcDomain + '/api/menus';
var g_imgApiUrl     = g_svcDomain + '/etc/img';
var g_userJwt       = $.cookie('user_jwt');