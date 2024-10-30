package ru.clevertec.news.constant;

public class Constant {

    public static final String OFFSET_DEFAULT = "1";
    public static final String LIMIT_DEFAULT = "15";

    // roles
    public static final String USER_ROLE = "ROLE_USER";
    public static final String ADMIN_ROLE = "ROLE_ADMIN";
    public static final String JOURNALIST_ROLE = "ROLE_JOURNALIST";
    public static final String SUBSCRIBER_ROLE = "ROLE_SUBSCRIBER";

    // token
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // messages
    public static final String INVALID_TOKEN_ERROR = "Invalid token";
    public static final String PARSE_EXCEPTION = "Token parsing exception";
}