package com.ldv.money_tracker.utils;


public final class ConstansManager {

    public static final String REGISTRATION_SUCCESS = "success";

    public static final String LOGIN_SUCCESS = "success";

    public static final String SUCCESS = "success";

    public static final String SHARE_PREF_FILE_NAME = "money_tracker_shared_pref";

    public static final String LOGIN_BUSY = "Login busy already";

    public static final String WRONG_PASSWORD = "Wrong password";

    public static final String WRONG_LOGIN = "Wrong login";

    public static final String ERROR = "Error";

    public static final String TOKEN_KEY = "token_key";

    public static final String GOOGLE_TOKEN_KEY = "google_token_key";

    private final static String G_PLUS_SCOPE =
            "oauth2:https://www.googleapis.com/auth/plus.me";//Позволяет определить аутентифицированного пользователя. Для этого при вызове API необходимо указать me вместо идентификатора пользователя Google+.

    public final static String USERINFO_SCOPE =
            "https://www.googleapis.com/auth/userinfo.profile";//получения личных данных пользователя (Имя, Фамилия, адрес G+ страницы, аватар)

    public final static String EMAIL_SCOPE =
            "https://www.googleapis.com/auth/userinfo.email";//для получения E-mail.


    public final static String SCOPES = G_PLUS_SCOPE + " " + USERINFO_SCOPE + " " + EMAIL_SCOPE;

    public static final String EMAIL_KEY = "email";

    public static final String NAME_KEY = "name";

    public static final String PICTURE_KEY = "picture";

}
