package com.aurine.cloudx.push.constant;

/**
 *
 * @ClassName: Constants
 * @author: 邹宇
 * @date: 2021-8-26 09:29:15
 * @Copyright:
 */
public class Constants {
    public static String APP_ID = "";
    public static String APP_SECRET = "";
    public static String ACCESS_TOKEN = "";
    public static String TOKEN = "a97f3f7fedc641c5a05fcca1741b1a2a";
    public static String ENCODING_AES_KEY = "ZYcrkJbB1PyPvVSRHOA4MP6sdDirPDykrehfzmz7CSM";
    public static String APP_ID_APPLET_PROPERTY = "wxaff76c96bb8b6887";
    public static String APP_ID_APPLET_PROPRIETOR = "wxbb287801e93a2878";
    public static String APP_ID_APPLET_VISITOR = "wx024afaba767d5ca4";


    private static String BASE_URL = "https://api.weixin.qq.com";


    public static String GET_TOKEN_PATH = BASE_URL + "/cgi-bin/token";
    public static String NET_CHECK_PATH = BASE_URL + "/cgi-bin/callback/check";

    //素材管理

    public static String MATERIAL_ADD_TEMPORARY = BASE_URL + "/cgi-bin/media/upload?access_token=%s&type=%s";
    public static String MATERIAL_GET_TEMPORARY = BASE_URL + "/cgi-bin/media/get?access_token=%s&media_id=%s";
    public static String MATERIAL_ADD_NEWS = BASE_URL + "/cgi-bin/material/add_news?access_token=%s";
    public static String MATERIAL_UPDATE_NEWS = BASE_URL + "/cgi-bin/material/update_news?access_token=%s";
    public static String MATERIAL_UPLOAD_IMAGE = BASE_URL + "/cgi-bin/media/uploadimg?access_token=%s";
    public static String MATERIAL_UPLOAD = BASE_URL + "/cgi-bin/material/add_material?access_token=%s&type=%s";
    public static String MATERIAL_GET_PERMANENT = BASE_URL + "/cgi-bin/material/get_material?access_token=%s";
    public static String MATERIAL_DELETE_PERMANENT = BASE_URL + "/cgi-bin/material/del_material?access_token=%s";
    public static String MATERIAL_GET_COUNT = BASE_URL + "/cgi-bin/material/get_materialcount?access_token=%s";
    public static String MATERIAL_LIST_GET = BASE_URL + "/cgi-bin/material/batchget_material?access_token=%s";

    //评论管理

    public static String COMMENT_OPEN = BASE_URL + "/cgi-bin/comment/open?access_token=%s";
    public static String COMMENT_CLOSE = BASE_URL + "/cgi-bin/comment/close?access_token=%s";
    public static String COMMENT_LIST = BASE_URL + "/cgi-bin/comment/list?access_token=%s";
    public static String COMMENT_MARKELECT = BASE_URL + "/cgi-bin/comment/markelect?access_token=%s";
    public static String COMMENT_UN_MARKELECT = BASE_URL + "/cgi-bin/comment/unmarkelect?access_token=%s";
    public static String COMMENT_DELETE = BASE_URL + "/cgi-bin/comment/delete?access_token=%s";
    public static String COMMENT_REPLY_ADD = BASE_URL + "/cgi-bin/comment/reply/add?access_token=%s";
    public static String COMMENT_REPLY_DELETE = BASE_URL + "/cgi-bin/comment/reply/delete?access_token=%s";


    //消息模板

    public static String TEMPLATE_SET_INDUSTRY = BASE_URL + "/cgi-bin/template/api_set_industry?access_token=%s";
    public static String TEMPLATE_GET_INDUSTRY = BASE_URL + "/cgi-bin/template/get_industry?access_token=%s";
    public static String TEMPLATE_API_ADD = BASE_URL + "/cgi-bin/template/api_add_template?access_token=%s";
    public static String TEMPLATE_SEND_MESSAGE = BASE_URL + "/cgi-bin/message/template/send?access_token=%s";
    public static String TEMPLATE_GET_ALL_PRIVATE = BASE_URL + "/cgi-bin/template/get_all_private_template?access_token=%s";
    public static String TEMPLATE_DEL_PRIVATE = BASE_URL + "/cgi-bin/template/del_private_template?access_token=%s";

    //用户管理

    public static String USER_GET_INFO = BASE_URL + "/cgi-bin/user/info";
    public static String USER_GET_LIST = BASE_URL + "/cgi-bin/user/get?access_token=%s";
    public static String USER_BATCH_GET_LIST = BASE_URL + "/cgi-bin/user/info/batchget?access_token=%s";
    public static String USER_GET_BLACK_LIST = BASE_URL + "/cgi-bin/tags/members/getblacklist?access_token=%s";
    public static String USER_BATCH_BLACK_LIST = BASE_URL + "/cgi-bin/tags/members/batchunblacklist?access_token=%s";
    public static String USER_BATCH_UN_BLACK_LIST = BASE_URL + "/cgi-bin/tags/members/batchunblacklist?access_token=%s";


    //自定义菜单

    public static String MENU_CREATE = BASE_URL + "/cgi-bin/menu/create?access_token=%s";
    public static String MENU_GET = BASE_URL + "/cgi-bin/get_current_selfmenu_info?access_token=%s";
    public static String MENU_DELETE = BASE_URL + "/cgi-bin/menu/delete?access_token=%s";
    public static String MENU_CREATE_CONDITIONAL = BASE_URL + "/cgi-bin/menu/addconditional?access_token=%s";
    public static String MENU_TRY_MATCH_CONDITIONAL = BASE_URL + "/cgi-bin/menu/trymatch?access_token=%s";
    public static String MENU_DELETE_CONDITIONAL = BASE_URL + "/cgi-bin/menu/delconditional?access_token=%s";
    public static String MENU_GET_CONFIG = BASE_URL + "/cgi-bin/menu/get?access_token=%s";


    //网页授权

    public static String WEB_AUTHORIZATION = BASE_URL + "/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    public static String WEB_GET_USER_INFO = BASE_URL + "/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
}
