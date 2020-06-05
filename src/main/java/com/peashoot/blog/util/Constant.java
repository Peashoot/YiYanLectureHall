package com.peashoot.blog.util;

public class Constant {
    /**
     * 每秒等于1000毫秒
     */
    public static final int MILLISECONDS_PEY_SECOND = 1000;
    /**
     * 每分钟60秒
     */
    public static final int SECONDS_PEY_MINUTES = 60;
    /**
     * 每小时60分钟
     */
    public static final int MINUTES_PEY_HOUR = 60;
    /**
     * 每天24小时
     */
    public static final int HOURS_PEY_DAY = 24;
    /**
     * 每年按365天计算
     */
    public static final int DAYS_PEY_YEAR = 365;
    /**
     * 用户名检查Pattern
     */
    public static final String PATTERN_CHECK_USERNAME = "^[A-Za-z0-9!#$%&'+/=?^_`{|}~-]{3,30}$";
    /**
     * 邮箱检查Pattern
     */
    public static final String PATTERN_CHECK_EMAIL = "^[A-Za-z0-9!#$%&'+/=?^_`{|}~-]+(.[A-Za-z0-9!#$%&'+/=?^_`{|}~-]+)*" +
            "@([A-Za-z0-9]+(?:-[A-Za-z0-9]+)?.)+[A-Za-z0-9]+(-[A-Za-z0-9]+)?$";
    /**
     * 用户名或邮箱检查Pattern
     */
    public static final String PATTERN_CHECK_USERNAME_OR_EMAIL = "^([A-Za-z0-9!#$%&'+/=?^_`{|}~-]+(.[A-Za-z0-9!#$%&'+/=?^_`{|}~-]+)*@([A-Za-z0-9]+(?:-[A-Za-z0-9]+)?.)+[A-Za-z0-9]+(-[A-Za-z0-9]+)?)|([A-Za-z0-9!#$%&'+/=?^_`{|}~-]{3,30})$";
    /**
     * 密码检查Pattern
     */
    public static final String PATTERN_CHECK_PASSWORD = "^[0-9a-fA-F]{32}$";
}
