package com.funny.combo.codegen.service.impl;

import com.funny.combo.codegen.po.CodeURL;
import com.funny.combo.codegen.po.Table;
import com.funny.combo.codegen.service.AbstractQueryTable;
import com.funny.combo.core.exception.SysException;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

@Service
public class OracleTableHandler extends AbstractQueryTable {

    @Override
    public List<Table> getDatabaseTable(CodeURL codeURL) {

        if (!checkUrl(codeURL.getUrl())) {
            throw new SysException("请检查mysql数据库连接错误");
        }
        ResultSet rs = null;
        List<Table> tables = Lists.newArrayList();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(codeURL.getUrl(), codeURL.getUsr(), codeURL.getPas());
            PreparedStatement st = con.prepareStatement(getQueryTableSQL(codeURL.getUsr()));
            // 查询，得出结果集
            rs = st.executeQuery();
            while (rs.next()) {
                Table table =
                        new Table(rs.getString("TABLE_NAME"), rs.getString("COMMENTS"), rs.getString("COLUMN_NAME"));
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
    protected String getQueryTableSQL(String user) {
        String sql =
                "SELECT T.TABLE_NAME, nvl(T.COMMENTS,' ') COMMENTS, C.CONSTRAINT_NAME, nvl(CC.COLUMN_NAME,' ')COLUMN_NAME"
                        + " FROM USER_TAB_COMMENTS T LEFT JOIN USER_CONSTRAINTS C "
                        + " ON T.TABLE_NAME = C.TABLE_NAME AND C.CONSTRAINT_TYPE = 'P' AND C.OWNER = '"
                        + user.toUpperCase() + "'"
                        + " LEFT JOIN USER_CONS_COLUMNS CC ON CC.OWNER = 'SYW' AND CC.CONSTRAINT_NAME = C.CONSTRAINT_NAME"
                        + " WHERE T.TABLE_TYPE = 'TABLE'";
        return sql;
    }

}
