package com.funny.autocode.util;

import static com.funny.autocode.common.SystemConstants.*;
import static com.funny.autocode.service.InitService.propMap;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import com.funny.autocode.common.SystemConstants;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.JavaTypeResolverConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;

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

    /**
     * @param name
     * @param password
     * @param project
     * @param packagename
     * @param targetpath
     * @return
     */
    public static Context initContext(String url, String name, String password, String table, String project,
                                      String packagename, String targetpath) {
        Context context = new Context(null);
        context.setTargetRuntime("AutoCodeIntrospectedTableMyBatis3Impl");
        context.setId("MySQLTables");
        if (table.equals(",")) {
            String[] tables = table.split(",");
            for (String table_name : tables) {
                TableConfiguration tc = getTableConfiguration(context, table_name);
                context.addTableConfiguration(tc);
            }
        } else {
            TableConfiguration tc = getTableConfiguration(context, table);
            context.addTableConfiguration(tc);
        }

        CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
        commentGeneratorConfiguration.addProperty("suppressDate", "true");
        commentGeneratorConfiguration.addProperty("suppressAllComments", "true");
        context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);

        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        String dbtype = ContextUtils.getDatabaseType(url);
        if ("oracle".equals(dbtype)) {
            jdbcConnectionConfiguration.setDriverClass("oracle.jdbc.driver.OracleDriver");// oracle.jdbc.driver.OracleDriver
        } else if ("mysql".equals(dbtype)) {
            jdbcConnectionConfiguration.setDriverClass("com.mysql.jdbc.Driver");// oracle.jdbc.driver.OracleDriver
        }
        jdbcConnectionConfiguration.setConnectionURL(url); // jdbc:oracle:thin:@127.0.0.1:1521:ETD
        if (stringHasValue(name)) {
            jdbcConnectionConfiguration.setUserId(name);
        }
        if (stringHasValue(password)) {
            jdbcConnectionConfiguration.setPassword(password);
        }
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

        JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration();
        javaTypeResolverConfiguration.addProperty("forceBigDecimals", "false");
        context.setJavaTypeResolverConfiguration(javaTypeResolverConfiguration);
        javaTypeResolverConfiguration
                .setConfigurationType("JavaTypeResolverImpl");

        JavaModelGeneratorConfiguration config = new JavaModelGeneratorConfiguration();
        config.setTargetPackage(
                project + "." + propMap.get(NAME_DOMAIN) + "." + packagename.toLowerCase()); // com.un.general.domain.account
        config.setTargetProject(targetpath);// 存放地址
        config.addProperty("enableSubPackages", "true");
        config.addProperty("trimStrings", "true");
        context.setJavaModelGeneratorConfiguration(config);

        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        javaClientGeneratorConfiguration.setTargetPackage(
                project + "." + propMap.get(NAME_DAO) + "." + packagename.toLowerCase());
        javaClientGeneratorConfiguration.setTargetProject(targetpath);
        javaClientGeneratorConfiguration.addProperty("enableSubPackages", "true");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetPackage(
                project + "." + propMap.get(NAME_MAPPER) + "." + packagename.toLowerCase());
        sqlMapGeneratorConfiguration.setTargetProject(targetpath);
        sqlMapGeneratorConfiguration.addProperty("enableSubPackages", "true");
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);
        return context;
    }

    public static TableConfiguration getTableConfiguration(Context context, String tableName) {
        String domainObjectName = getDomainObjectName(tableName);
        TableConfiguration tabconfig = new TableConfiguration(context);
        tabconfig.setTableName(tableName);
        tabconfig.setDomainObjectName(domainObjectName);
        context.addTableConfiguration(tabconfig);
        GeneratedKey gk = new GeneratedKey("id", "MySQL", true, "mysql");
        tabconfig.setGeneratedKey(gk);
        return tabconfig;
    }

    public static String getDomainObjectName(String table) {
        String suffixes = propMap.get(PREFIX);
        String[] tabs = suffixes.split(",");
        for (int i = 0; i < tabs.length; i++) {
            if (table.toUpperCase().contains(tabs[i])) {
                table = table.substring(tabs[i].length() + 1, table.length());
            }
        }

        String[] names = table.split("_");
        StringBuffer sb = new StringBuffer();
        for (String name : names) {
            sb.append(captureName(name.toLowerCase()));
        }
        return sb.toString();
    }

    public static String captureName(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;

    }

}
