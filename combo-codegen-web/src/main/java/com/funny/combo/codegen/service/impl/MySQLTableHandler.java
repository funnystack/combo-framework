package com.funny.combo.codegen.service.impl;

import com.funny.combo.codegen.core.config.GlobalConfig;
import com.funny.combo.codegen.po.CodeURL;
import com.funny.combo.codegen.po.Table;
import com.funny.combo.codegen.service.AbstractQueryTable;
import com.funny.combo.core.exception.SysException;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

import static com.alibaba.druid.util.JdbcConstants.MYSQL_DRIVER_6;

@Service
public class MySQLTableHandler extends AbstractQueryTable {

    @Override
    public List<Table> getDatabaseTable(CodeURL codeURL) {
        String url = codeURL.getUrl();
        if (!checkUrl(url)) {
            throw new SysException("请检查mysql数据库连接错误");
        }
        codeURL.setDbName(url.substring(url.lastIndexOf("/")+1,url.length()));
        codeURL.setUrl(url + GlobalConfig.MYSQL_CONCAT_URL);

        ResultSet rs = null;
        List<Table> tables = Lists.newArrayList();

        try {
            Class.forName(MYSQL_DRIVER_6);
            Connection con = DriverManager.getConnection(codeURL.getUrl(), codeURL.getUsr(), codeURL.getPas());
            PreparedStatement st = con.prepareStatement(getQueryTableSQL(codeURL.getDbName()));
            // 查询，得出结果集
            rs = st.executeQuery();
            while (rs.next()) {
                Table table = new Table(rs.getString("TABLE_NAME"), rs.getString("TABLE_COMMENT"),
                        rs.getString("COLUMN_NAME"));
                tables.add(table);
            }
            closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tables;
    }

    @Override
    protected boolean checkUrl(String url) {
        return true;
    }

    @Override
    protected String getQueryTableSQL(String dbName) {
        String sql = "SELECT t.TABLE_NAME,ta.TABLE_COMMENT,c.COLUMN_NAME FROM "
                + " information_schema.TABLES ta  LEFT JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS t ON ta.TABLE_NAME = t.TABLE_NAME "
                + " AND ta.TABLE_SCHEMA=t.TABLE_SCHEMA  AND t.CONSTRAINT_NAME='PRIMARY' AND t.TABLE_SCHEMA='" + dbName
                + "' LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE  c ON  t.TABLE_NAME = c.TABLE_NAME AND t.TABLE_SCHEMA=c.TABLE_SCHEMA "
                + " AND c.CONSTRAINT_NAME='PRIMARY' AND t.TABLE_SCHEMA='" + dbName + "' WHERE t.TABLE_SCHEMA ='"
                + dbName+ "' AND t.CONSTRAINT_TYPE='PRIMARY KEY'";
        return sql;
    }

}
