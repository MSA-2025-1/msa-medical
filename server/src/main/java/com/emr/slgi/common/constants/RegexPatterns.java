package com.emr.slgi.common.constants;

public class RegexPatterns {
    public static final String UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$";
    public static final String RRN = ".+";
    public static final String USERID = ".+";
    public static final String PASSWORD = ".+";
    public static final String JWT_TOKEN = "^[A-Za-z0-9\\-_=]+\\.[A-Za-z0-9\\-_=]+\\.[A-Za-z0-9\\-_=]+$";
    public static final String PHONE = ".+";
    public static final String BIRTH_DATE = "^(\\d{2})(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$";
}
