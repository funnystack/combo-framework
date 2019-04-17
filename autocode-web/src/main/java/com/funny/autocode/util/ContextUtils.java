package com.funny.autocode.util;

import com.funny.autocode.common.SystemConstants;

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
        if (dbType.equals(SystemConstants.DB_ORACLE) || dbType.contains(SystemConstants.DB_MYSQL)) {
            return true;
        }
        return false;
    }

    public static String getDatabaseType(String url) {
        if (url.contains(SystemConstants.DB_ORACLE)) {
            return SystemConstants.DB_ORACLE;
        } else if (url.contains(SystemConstants.DB_MYSQL)) {
            return SystemConstants.DB_MYSQL;
        }
        return null;
    }

    public static boolean checkOracleUrl(String url) {
        return true;
    }

    public static boolean checkMysqlUrl(String url) {
        return true;
    }

    public static String getOracleSql(String user) {
        String sql =
                "SELECT T.TABLE_NAME, nvl(T.COMMENTS,' ') COMMENTS, C.CONSTRAINT_NAME, nvl(CC.COLUMN_NAME,' ')COLUMN_NAME"
                        + " FROM USER_TAB_COMMENTS T LEFT JOIN USER_CONSTRAINTS C "
                        + " ON T.TABLE_NAME = C.TABLE_NAME AND C.CONSTRAINT_TYPE = 'P' AND C.OWNER = '"
                        + user.toUpperCase() + "'"
                        + " LEFT JOIN USER_CONS_COLUMNS CC ON CC.OWNER = 'SYW' AND CC.CONSTRAINT_NAME = C.CONSTRAINT_NAME"
                        + " WHERE T.TABLE_TYPE = 'TABLE'";
        return sql;
    }

    public static String getMysqlSql(String url) {
        String[] strs = url.split(":");
        String[] arrs = strs[3].split("\\/");

        String sql = "SELECT t.TABLE_NAME,ta.TABLE_COMMENT,c.COLUMN_NAME FROM "
                + " information_schema.TABLES ta  LEFT JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS t ON ta.TABLE_NAME = t.TABLE_NAME "
                + " AND ta.TABLE_SCHEMA=t.TABLE_SCHEMA  AND t.CONSTRAINT_NAME='PRIMARY' AND t.TABLE_SCHEMA='" + arrs[1]
                + "' LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE  c ON  t.TABLE_NAME = c.TABLE_NAME AND t.TABLE_SCHEMA=c.TABLE_SCHEMA "
                + " AND c.CONSTRAINT_NAME='PRIMARY' AND t.TABLE_SCHEMA='" + arrs[1] + "' WHERE t.TABLE_SCHEMA ='"
                + arrs[1] + "' AND t.CONSTRAINT_TYPE='PRIMARY KEY'";
        return sql;
    }



}
