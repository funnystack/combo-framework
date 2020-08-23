package com.funny.combo.codegen.util;


import com.funny.combo.codegen.core.config.GlobalConfig;

public class ContextUtils {
    /**
     * @param url
     * @return funny 2016年3月4日 上午11:11:12
     */
    public static boolean checkDataBase(String url) {
        String dbType = getDatabaseType(url);
        if (dbType == null) {
            return false;
        }
        if (dbType.equals(GlobalConfig.DB_ORACLE) || dbType.contains(GlobalConfig.DB_MYSQL)) {
            return true;
        }
        return false;
    }

    public static String getDatabaseType(String url) {
        if (url.contains(GlobalConfig.DB_ORACLE)) {
            return GlobalConfig.DB_ORACLE;
        } else if (url.contains(GlobalConfig.DB_MYSQL)) {
            return GlobalConfig.DB_MYSQL;
        }
        return null;
    }
}
